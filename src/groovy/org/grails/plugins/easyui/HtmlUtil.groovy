package org.grails.plugins.easyui

import javax.swing.text.html.HTML

import org.apache.commons.lang.ObjectUtils
import org.apache.commons.lang.StringUtils

class HtmlUtil {

	private static ignoreAttrs = ["prompt", "title", "height", "width"] as Set

	private static Set eventsAttrs = [
		"onblur",
		"onchange",
		"onclick",
		"ondblclick",
		"onfocus",
		"onkeydown",
		"onkeypress",
		"onkeyup",
		"onload",
		"onmousedown",
		"onmousemove",
		"onmouseout",
		"onmouseover",
		"onmouseup",
		"onreset",
		"onselect",
		"onsubmit",
		"onunload"
	]

	static String cssEncode(attrs){
		def css = attrs.collect{k,v -> "$k:$v"}.join(";")
		(css) ? "${css};" : css
	}

	static String jsEncode(attrs){
		attrs.collect{k,v->
			String[] js = ["function", "js:"]
			boolean nonQuoted = (!(v instanceof String) || v == "true" || v == "false" || StringUtils.startsWithAny(v, js))
			
			if (v instanceof GString)
				v = ObjectUtils.toString(v)
			
			if (v instanceof String && v.startsWith("js:"))
				v = v.substring(3)
				
			def value = (nonQuoted) ? v : "'$v'"
			"$k:$value"
		}.join(",")
	}

	static String tagEncode(attrs) {
		attrs.collect{k,v->
			"$k=\"$v\""
		}.join(" ")
	}

	static boolean isHtmlAttribute(String attr) {
		def key = attr.toLowerCase()
		return eventsAttrs.contains(key) || (HTML.getAttributeKey(key) != null && !ignoreAttrs.contains(key))
	}

}
