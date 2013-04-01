class GeasyuiGrailsPlugin {

    def version = "0.8"
    def grailsVersion = "2.0 > *"    
    def dependsOn = [:]

    def pluginExcludes = [
        "web-app/css",
	"web-app/images",
	"web-app/js/prototype",
	"web-app/js/application.js"
    ]

    def title = "JQuery EasyUI from Grails"
    def author = "Leandro Guindani Gehlen"
    def authorEmail = "leandrogehlen@gmail.com"
    def description = "Supplies jQuery EasyUI resources and taglibs. Depends on jQuery EasyUI plugin. See http://www.jeasyui.com/"
    def documentation = "http://grails.org/plugin/geasyui"
        

    def license = "LGPL"    
    def issueManagement = [system: "github", url: "https://github.com/leandrogehlen/geasyui/issues"]
    def scm = [ url: "https://github.com/leandrogehlen/geasyui" ]
}
