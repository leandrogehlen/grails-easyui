package org.grails.plugins.easyui.scaffolding

import java.io.File;
import java.io.IOException;

import groovy.text.SimpleTemplateEngine
import groovy.text.Template

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty
import org.codehaus.groovy.grails.plugins.GrailsPluginInfo;
import org.codehaus.groovy.grails.plugins.GrailsPluginManager
import org.codehaus.groovy.grails.plugins.GrailsPluginUtils;
import org.codehaus.groovy.grails.scaffolding.AbstractGrailsTemplateGenerator

class EasyuiGrailsTemplateGenerator extends AbstractGrailsTemplateGenerator {
	
	protected static final Log log = LogFactory.getLog(EasyuiGrailsTemplateGenerator.class);
	
	EasyuiGrailsTemplateGenerator(ClassLoader classLoader) {
		super(classLoader)
	}	

	def renderEditor = { GrailsDomainClassProperty property ->
		def domainClass = property.domainClass
		def cp
		boolean hasHibernate = pluginManager?.hasGrailsPlugin('hibernate') || pluginManager?.hasGrailsPlugin('hibernate4')
		if (hasHibernate) {
			cp = domainClass.constrainedProperties[property.name]
		}

		if (!renderEditorTemplate) {
			// create template once for performance
			renderEditorTemplate = engine.createTemplate(getTemplateText('renderEditor.template'))
		}

		def binding = [
			pluginManager: pluginManager,
			property: property,
			domainClass: domainClass,
			cp: cp,
			domainInstance:getPropertyName(domainClass)]
		return renderEditorTemplate.make(binding).toString()
	}
	
	protected File getPluginDir() throws IOException {
		GrailsPluginInfo info = GrailsPluginUtils.getPluginBuildSettings().getPluginInfoForName("easyui");
		return info.getDescriptor().getFile().getParentFile();
	}

}
