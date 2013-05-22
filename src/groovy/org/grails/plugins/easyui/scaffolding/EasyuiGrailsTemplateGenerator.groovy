package org.grails.plugins.easyui.scaffolding

import groovy.text.SimpleTemplateEngine
import groovy.text.Template

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.cli.CommandLineHelper
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.plugins.GrailsPluginManager
import org.codehaus.groovy.grails.scaffolding.DomainClassPropertyComparator
import org.codehaus.groovy.grails.scaffolding.GrailsTemplateGenerator
import org.codehaus.groovy.grails.scaffolding.SimpleDomainClassPropertyComparator
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.util.Assert

class EasyuiGrailsTemplateGenerator implements GrailsTemplateGenerator {
	
	static final Log LOG = LogFactory.getLog(EasyuiGrailsTemplateGenerator)
		
	private helper = new CommandLineHelper()
	
	String basedir = "."
	String domainSuffix = 'Instance'
	boolean overwrite = false
	def engine = new SimpleTemplateEngine()
	File easyuiPluginDir	
	GrailsPluginManager pluginManager
	Template renderEditorTemplate
	ResourceLoader resourceLoader
	GrailsApplication grailsApplication	
	
	EasyuiGrailsTemplateGenerator(ClassLoader classLoader) {
		engine = new SimpleTemplateEngine(classLoader)
	}
	
	EasyuiGrailsTemplateGenerator() {}
	
	private canWrite(testFile) {
		if (!overwrite && testFile.exists()) {
			try {
				def response = helper.userInput("File ${testFile} already exists. Overwrite?", ['y', 'n', 'a'] as String[])
				overwrite = overwrite || response == "a"
				return overwrite || response == "y"
			}
			catch (Exception e) {
				// failure to read from standard in means we're probably running from an automation tool like a build server
				return true
			}
		}
		return true
	}
	
	private getTemplateText(String template) {
		def application = grailsApplication
		// first check for presence of template in application
		if (resourceLoader && application?.warDeployed) {
			return resourceLoader.getResource("/WEB-INF/templates/easyui/scaffolding/${template}").inputStream.text
		}

		def templateFile = new FileSystemResource("${basedir}/src/templates/easyui/scaffolding/${template}")
		if (!templateFile.exists()) {
			templateFile = new FileSystemResource("${easyuiPluginDir}/src/templates/easyui/scaffolding/${template}")
		}
		return templateFile.inputStream.getText()
	}
	
	private String getPropertyName(GrailsDomainClass domainClass) {
		"${domainClass.propertyName}${domainSuffix}"
	}

	@Override
	void setResourceLoader(ResourceLoader resourceLoader) {
		LOG.info "Scaffolding template generator set to use resource loader ${resourceLoader}"
		this.resourceLoader = resourceLoader		
	}
	
	void generateTest(GrailsDomainClass domainClass, String destDir) {
		File destFile = new File("$destDir/${domainClass.packageName.replace('.','/')}/${domainClass.shortName}ControllerTests.groovy")
		def templateText = getTemplateText("Test.groovy")
		def t = engine.createTemplate(templateText)

		def binding = [pluginManager: pluginManager,
					   packageName: domainClass.packageName,
					   domainClass: domainClass,
					   className: domainClass.shortName,
					   propertyName: domainClass.logicalPropertyName]

		if (canWrite(destFile)) {
			destFile.parentFile.mkdirs()
			destFile.withWriter {
				t.make(binding).writeTo(it)
			}
		}
	}

	@Override
	void generateViews(GrailsDomainClass domainClass, String destDir) {
		 Assert.hasText destDir, "Argument [destdir] not specified"

        def viewsDir = new File("${destDir}/grails-app/views/${domainClass.propertyName}")
        if (!viewsDir.exists()) {
            viewsDir.mkdirs()
        }

        for (t in getTemplateNames()) {
            LOG.info "Generating $t view for domain class [${domainClass.fullName}]"
            generateView domainClass, t, viewsDir.absolutePath
        }		
	}

	@Override
	void generateController(GrailsDomainClass domainClass, String destDir) {
		Assert.hasText destDir, "Argument [destdir] not specified"

		if (domainClass) {
			def fullName = domainClass.fullName
			def pkg = ""
			def pos = fullName.lastIndexOf('.')
			if (pos != -1) {
				// Package name with trailing '.'
				pkg = fullName[0..pos]
			}

			def destFile = new File("${destDir}/grails-app/controllers/${pkg.replace('.' as char, '/' as char)}${domainClass.shortName}Controller.groovy")
			if (canWrite(destFile)) {
				destFile.parentFile.mkdirs()

				destFile.withWriter { w ->
					generateController(domainClass, w)
				}

				LOG.info("Controller generated at ${destDir}")
			}
		}
	}
	
	@Override
	void generateController(GrailsDomainClass domainClass, Writer out) {
		def templateText = getTemplateText("Controller.groovy")

		boolean hasHibernate = pluginManager.hasGrailsPlugin('hibernate')
		def binding = [
			pluginManager: pluginManager,
			packageName: domainClass.packageName,
			domainClass: domainClass,
			className: domainClass.shortName,
			propertyName: getPropertyName(domainClass),
			comparator: hasHibernate ? DomainClassPropertyComparator : SimpleDomainClassPropertyComparator]

		def t = engine.createTemplate(templateText)
		t.make(binding).writeTo(out)
		
	}

	@Override
	void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite		
	}
	
	@Override
	public void generateView(GrailsDomainClass domainClass, String viewName, String destDir) {
		File destFile = new File("$destDir/${viewName}.gsp")
        if (canWrite(destFile)) {
            destFile.withWriter { Writer writer ->
                generateView domainClass, viewName, writer
            }
        }	
	}
	
	@Override
	void generateView(GrailsDomainClass domainClass, String viewName, Writer out) {
		def templateText = getTemplateText("${viewName}.gsp")

		def t = engine.createTemplate(templateText)
		def multiPart = domainClass.properties.find {it.type == ([] as Byte[]).class || it.type == ([] as byte[]).class}

		boolean hasHibernate = pluginManager.hasGrailsPlugin('hibernate')
		def packageName = domainClass.packageName ? "<%@ page import=\"${domainClass.fullName}\" %>" : ""
		def binding = [packageName: packageName,
				domainClass: domainClass,
				multiPart: multiPart,
				className: domainClass.shortName,				
				renderEditor: renderEditor,
				comparator: hasHibernate ? DomainClassPropertyComparator : SimpleDomainClassPropertyComparator]

		t.make(binding).writeTo(out)
	}	
	
	void setGrailsApplication(GrailsApplication ga) {
		this.grailsApplication = ga
		if (ga != null) {
			def suffix = ga.config?.grails?.scaffolding?.templates?.domainSuffix
			if (suffix != [:]) {
				domainSuffix = suffix
			}
		}
	}
	
	def renderEditor = { property ->
		def domainClass = property.domainClass
		def cp
		if (pluginManager.hasGrailsPlugin('hibernate')) {
			cp = domainClass.constrainedProperties[property.name]
		}

		if (!renderEditorTemplate) {
			// create template once for performance
			def templateText = getTemplateText("renderEditor.template")
			renderEditorTemplate = engine.createTemplate(templateText)
		}

		def binding = [property: property,
				domainClass: domainClass,
				cp: cp,
				domainInstance: getPropertyName(domainClass)]
		return renderEditorTemplate.make(binding).toString()
	}	
		
	def getTemplateNames() {
		Closure filter = { it[0..-5] }
		if (resourceLoader && application?.isWarDeployed()) {
			def resolver = new PathMatchingResourcePatternResolver(resourceLoader)
			try {
				return resolver.getResources("/WEB-INF/templates/scaffolding/easyui/*.gsp").filename.collect(filter)
			}
			catch (e) {
				return []
			}
		}

		def resources = []
		def resolver = new PathMatchingResourcePatternResolver()
		String templatesDirPath = "${basedir}/src/templates/easyui/scaffolding"
		def templatesDir = new FileSystemResource(templatesDirPath)
		if (templatesDir.exists()) {
			try {
				resources = resolver.getResources("file:$templatesDirPath/*.gsp").filename.collect(filter)
			}
			catch (e) {
				LOG.info("Error while loading views from grails-app scaffolding folder", e)
			}
		}

		try {
			def pluginsHomeTemplates = resolver.getResources("file:${easyuiPluginDir}/src/templates/easyui/scaffolding/*.gsp").filename.collect(filter)
			resources.addAll(pluginsHomeTemplates)
		}
		catch (e) {
			// ignore
			LOG.debug("Error locating templates from pluginsHome: ${e.message}", e)
		}
		return resources
	}

}
