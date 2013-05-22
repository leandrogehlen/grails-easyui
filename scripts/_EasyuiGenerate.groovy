import grails.util.GrailsNameUtils


includeTargets << grailsScript("_GrailsBootstrap")


generateForName = null
generateViews = true
generateController = true

target(generateForOne: "Generates controllers and views for only one domain class.") {
	depends(loadApp)

	def name = generateForName
	name = name.indexOf('.') > -1 ? name : GrailsNameUtils.getClassNameRepresentation(name)
	def domainClass = grailsApp.getDomainClass(name)

	if (!domainClass) {
		println "Domain class not found in grails-app/domain, trying hibernate mapped classes..."
		bootstrap()
		domainClass = grailsApp.getDomainClass(name)
	}

	if (domainClass) {
		generateForDomainClass(domainClass)
		event("StatusFinal", ["Finished generation for domain class ${domainClass.fullName}"])
	}
	else {
		event("StatusFinal", ["No domain class found for name ${name}. Please try again and enter a valid domain class name"])
		exit(1)
	}
}

target(uberGenerate: "Generates controllers and views for all domain classes.") {
	depends(loadApp)

	def domainClasses = grailsApp.domainClasses

	if (!domainClasses) {
		println "No domain classes found in grails-app/domain, trying hibernate mapped classes..."
		bootstrap()
		domainClasses = grailsApp.domainClasses
	}

	if (domainClasses) {
		domainClasses.each { domainClass -> generateForDomainClass(domainClass) }
		event("StatusFinal", ["Finished generation for domain classes"])
	}
	else {
		event("StatusFinal", ["No domain classes found"])
	}
}

def generateForDomainClass(domainClass) {
	def templateGenerator = classLoader.loadClass("org.grails.plugins.easyui.scaffolding.EasyuiGrailsTemplateGenerator").newInstance([classLoader] as Object[])
	templateGenerator.grailsApplication = grailsApp
	templateGenerator.pluginManager = pluginManager
	templateGenerator.easyuiPluginDir = easyuiPluginDir
	if (generateViews) {
		event("StatusUpdate", ["Generating views for domain class ${domainClass.fullName}"])
		templateGenerator.generateViews(domainClass, basedir)
		event("GenerateViewsEnd", [domainClass.fullName])			
	}

	if (generateController) {
		event("StatusUpdate", ["Generating controller for domain class ${domainClass.fullName}"])
		templateGenerator.generateController(domainClass, basedir)
		templateGenerator.generateTest(domainClass, "${basedir}/test/unit")		
		event("GenerateControllerEnd", [domainClass.fullName])
	}
}
