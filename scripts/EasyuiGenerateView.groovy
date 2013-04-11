includeTargets << grailsScript("_GrailsCreateArtifacts")
includeTargets << new File("${easyuiPluginDir}/scripts/_EasyuiGenerate.groovy")

generateViews = true
generateController = false

target(main: "Gant script that generates CRUD views for a given domain class") {
    depends(checkVersion, parseArguments, packageApp)
    promptForName(type: "Domain Class")
	
	try {
		def name = argsMap["params"][0]
		if (!name || name == "*") {
			uberGenerate()
		}
		else {
			generateForName = name
			generateForOne()
		}
	}
	catch (Exception e) {
		logError("Error running easyui-generate-view", e)
		exit(1)
	}
}

setDefaultTarget("main")
