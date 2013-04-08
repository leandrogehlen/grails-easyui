package org.grails.plugins.easyui

class JsEvent {
	
	String name
	String params
	String script

	JsEvent(String name, String script) {
		def m = name =~ /(\w+)\s*\((.*?)\)/
		if (!m.matches())
			throwTagError "Invalid attribute [name] of tag [event]"

		this.name = m[0][1]
		this.params = m[0][2]
		this.script = script
	}

	String toString() {
		return "function(${params}){ ${script} }"
	}

}
