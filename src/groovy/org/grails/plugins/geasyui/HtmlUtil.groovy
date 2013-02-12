package org.grails.plugins.geasyui

import javax.swing.text.html.HTML

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
			def value = (v == "true" || v == "false" || v.startsWith("function")) ? v : "'$v'"
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
