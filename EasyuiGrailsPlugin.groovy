class EasyuiGrailsPlugin {

    def version = "1.5"
    def grailsVersion = "2.0 > *"    
    def dependsOn = [:]

    def pluginExcludes = [        
		"web-app/images"		
    ]

    def title = "JQuery EasyUI from Grails"
    def author = "Leandro Guindani Gehlen"
    def authorEmail = "leandrogehlen@gmail.com"
    def description = "Supplies jQuery EasyUI resources and taglibs. Depends on jQuery EasyUI plugin. See http://www.jeasyui.com/"
    def documentation = "http://grails.org/plugin/easyui"
        
    def license = "LGPL"    
    def issueManagement = [system: "github", url: "https://github.com/leandrogehlen/grails-easyui/issues"]
    def scm = [ url: "https://github.com/leandrogehlen/grails-easyui" ]
}
