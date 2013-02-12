import org.grails.plugins.geasyui.GEasyuiConfig

def theme = GEasyuiConfig.theme
def locale = GEasyuiConfig.locale
def jqver = GEasyuiConfig.jqueryVersion

modules = {

	easyui {
		resource url: "js/jquery-easyui/themes/icon.css", disposition: 'head'
		resource url: "js/jquery-easyui/themes/${theme}/easyui.css", disposition: 'head'
		resource url: "js/jquery-easyui/jquery-${jqver}.min.js", disposition: 'head'
		resource url: "js/jquery-easyui/jquery.easyui.min.js", disposition: 'head'
		resource url: "js/jquery-easyui/locale/easyui-lang-${locale}.js", disposition: 'head'
	}
}
