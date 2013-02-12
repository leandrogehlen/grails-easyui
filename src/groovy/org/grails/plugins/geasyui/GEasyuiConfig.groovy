package org.grails.plugins.geasyui

import grails.util.Holders

class GEasyuiConfig {
		
	static ConfigObject getConfig() {
		Holders.grailsApplication.config.grails.plugin.geasyui
	}

	static String getLocale() {
		getConfig().locale ?: 'en'
	}
	
	static String getTheme() {
		getConfig().theme ?: 'default'
	}
	
	static String getJqueryVersion() {
		getConfig().jquery.version ?: '1.8.0'
	}

}
