includeTargets << grailsScript("_GrailsInit")

target('default': "Installs the artifact and scaffolding templates") {
    depends(checkVersion, parseArguments)

    targetDir = "${basedir}/src/templates/easyui/scaffolding"
    overwrite = false

    // only if template dir already exists in, ask to overwrite templates
    if (new File(targetDir).exists()) {
        if (!isInteractive || confirmInput("Overwrite existing templates? [y/n]", "overwrite.templates")) {
            overwrite = true
        }else{
            return
        }
    }
    else {
        ant.mkdir(dir: targetDir)
    }

    ant.copy(todir: "$targetDir") {
        fileset(dir: "${easyuiPluginDir}/src/templates/easyui/scaffolding")
    }
    event("StatusUpdate", ["Templates installed successfully"])

}