class GeasyuiGrailsPlugin {

    def version = "0.1"	
    def grailsVersion = "2.1 > *"
    def dependsOn = [:]

    def pluginExcludes = [
        "web-app/css",
		"web-app/images",
		"web-app/js/prototype",
		"web-app/js/application.js"
    ]

    def title = "JQuery EasyUI form Grails"
    def author = "Leandro Guindani Gehlen"
    def authorEmail = "leandrogehlen@gmail.com"
    def description = "Supplies EasyUI resources, depends on jQuery plugin. See http://www.jeasyui.com/"

    def documentation = "http://grails.org/plugin/geasy-ui"
}
