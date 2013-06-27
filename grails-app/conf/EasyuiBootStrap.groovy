import grails.converters.JSON

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.grails.plugins.easyui.EasyuiConfig;
import org.grails.plugins.easyui.EasyuiDomainClassMarshaller

class EasyuiBootStrap {
	
	GrailsApplication grailsApplication

    def init = { servletContext ->		
		if (EasyuiConfig.registerMarshaller) {			
			JSON.registerObjectMarshaller(new EasyuiDomainClassMarshaller(true, grailsApplication))	
			JSON.createNamedConfig("easyui-load-domain", {
				it.registerObjectMarshaller(new EasyuiDomainClassMarshaller(true, true, grailsApplication))
			})
		}									
    }
	
    def destroy = {
    }
}
