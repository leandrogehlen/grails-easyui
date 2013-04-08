package org.grails.plugins.easyui

import grails.util.Holders

class EasyuiConfig {

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
