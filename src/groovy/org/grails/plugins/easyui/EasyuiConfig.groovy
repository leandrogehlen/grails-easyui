package org.grails.plugins.easyui

import grails.util.Holders

class EasyuiConfig {

	static ConfigObject getConfig() {
		Holders.grailsApplication.config.grails.plugin.easyui
	}

	static String getLocale() {
		getConfig().locale ?: "en"
	}

	static String getTheme() {
		getConfig().theme ?: "default"
	}

	static String getJqueryVersion() {
		getConfig().jquery.version ? "-${getConfig().jquery.version}": ""
	}
	
	static Boolean getRegisterMarshaller() {			
		getConfig().register.marshaller != null ? getConfig().register.marshaller as Boolean : true
	}
}
