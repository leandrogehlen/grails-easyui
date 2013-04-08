import org.grails.plugins.easyui.EasyuiConfig

def theme = EasyuiConfig.theme
def locale = EasyuiConfig.locale
def jqver = EasyuiConfig.jqueryVersion

modules = {
	easyui {
		resource url: "js/jquery-easyui/themes/icon.css", disposition: 'head'
		resource url: "js/jquery-easyui/themes/${theme}/easyui.css", disposition: 'head'
		resource url: "js/jquery-easyui/jquery-${jqver}.min.js", disposition: 'head'
		resource url: "js/jquery-easyui/jquery.easyui.min.js", disposition: 'head'
		resource url: "js/jquery-easyui/locale/easyui-lang-${locale}.js", disposition: 'head'
	}
}
