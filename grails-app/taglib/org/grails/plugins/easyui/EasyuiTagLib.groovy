package org.grails.plugins.easyui

class EasyuiTagLib {

	private static final String ATTR_TAG = "tag"
	private static final String ATTR_EVENTS = "events"
	private static final String ATTR_STYLE = "style"

	static namespace = "e"

	private static Set styleAttrs = ["width", "height"]

	void doTag(attrs, body, String tag, String className = null, Boolean ignoreStyleAttrs = false){

		def cls = (className)? " class=\"easyui-$className\"" : ""
		def result = body.call()

		if (attrs[ATTR_TAG])
			tag = attrs.remove(ATTR_TAG)

		def style = (!ignoreStyleAttrs) ? attrs.findAll{k,v -> styleAttrs.contains(k) } : []
		style = HtmlUtil.cssEncode(style)

		if (attrs[ATTR_STYLE])
			style += attrs.remove(ATTR_STYLE)

		def attributes = attrs.findAll {k,v->  HtmlUtil.isHtmlAttribute(k) }
		attributes = HtmlUtil.tagEncode(attributes)

		def options = attrs.findAll {k,v-> (ignoreStyleAttrs || !styleAttrs.contains(k)) && !HtmlUtil.isHtmlAttribute(k) }
		options = HtmlUtil.jsEncode(options)

		def events = request.getAttribute(ATTR_EVENTS)
		if (events) {
			def js = HtmlUtil.jsEncode( events.collectEntries{ [(it.name) : it.toString()] } )
			options = (options) ? "$options,$js" : js
			events.clear()
		}

		style = (style) ? " style=\"$style\"" : ""
		options = (options)? " data-options=\"$options\"" : ""
		attributes = (attributes)? " $attributes" : ""

		out << "<$tag$attributes$cls$options$style>"
		out << result
		out << "</$tag>"
	}

	/**
	 * @attr id
	 * @attr total
	 * @attr pageSize
	 * @attr pageNumber
	 * @attr pageList
	 * @attr loading
	 * @attr showPageList
	 * @attr showRefresh
	 * @attr beforePageText
	 * @attr afterPageText
	 * @attr displayMsg
	 */
	def pagination = { attrs, body ->
		doTag(attrs, body, "div", "pagination")
	}

	/**
	 * @attr id
	 * @attr width
	 * @attr prompt
	 * @attr value
	 * @attr menu
	 */
	def searchbox = { attrs, body ->
		doTag(attrs, body, "input", "searchbox")
	}

	/**
	 * @attr id
	 * @attr width
	 * @attr value
	 * @attr text
	 */
	def progressbar = { attrs, body ->
		doTag(attrs, body, "div", "progressbar")
	}

	/**
	 * @attr id
	 * @attr title
	 * @attr width
	 * @attr height
	 * @attr left
	 * @attr top
	 * @attr cls
	 * @attr headerCls
	 * @attr bodyCls
	 * @attr border
	 * @attr doSize
	 * @attr noheader
	 * @attr content
	 * @attr collapsible
	 * @attr minimizable
	 * @attr maximizable
	 * @attr closable
	 * @attr tools
	 * @attr collapsed
	 * @attr minimized
	 * @attr maximized
	 * @attr closed
	 * @attr href
	 * @attr cache
	 * @attr loadingMessage
	 * @attr extractor
	 */
	def panel = { attrs, body ->
		doTag(attrs, body, "div", "panel")
	}


	/**
	 * @attr id
	 * @attr width
	 * @attr height
	 * @attr plain
	 * @attr fit
	 * @attr border
	 * @attr scrollIncrement
	 * @attr scrollDuration
	 * @attr tools
	 */
	def tabs = { attrs, body ->
		doTag(attrs, body, "div", "tabs")
	}

	/**
	 * @attr id
	 * @attr title
	 * @attr content
	 * @attr href
	 * @attr cache
	 * @attr width
	 * @attr height
	 */
	def tabpanel = { attrs, body ->
		doTag(attrs, body, "div")
	}

	/**
	 * @attr id
	 * @attr selected
	 * @attr height
	 * @attr plain
	 * @attr fit
	 * @attr border
	 * @attr scrollIncrement
	 * @attr scrollDuration
	 * @attr tools
	 */
	def accordion = { attrs, body ->
		doTag(attrs, body, "div", "accordion")
	}

	/**
	 * @attr id
	 * @attr title
	 * @attr width
	 * @attr height
	 * @attr fit
	 * @attr border
	 * @attr animate
	 * @attr icconCls
	 */
	def div = { attrs, body ->
		doTag(attrs, body, "div")
	}

	/**
	 * @attr id
	 * @attr fit
	 * @attr tag
	 */
	def layout = { attrs, body ->
		doTag(attrs, body, "body", "layout")
	}

	/**
	 * @attr id
	 * @attr title
	 * @attr region
	 * @attr border
	 * @attr split
	 * @attr iconCls
	 * @attr href
	 */
	def north = { attrs, body ->
		attrs["region"] = "north"
		doTag(attrs, body, "div")
	}

	/**
	 * @attr id
	 * @attr title
	 * @attr region
	 * @attr border
	 * @attr split
	 * @attr iconCls
	 * @attr href
	 */
	def west = { attrs, body ->
		attrs["region"] = "west"
		doTag(attrs, body, "div")
	}

	/**
	 * @attr id
	 * @attr title
	 * @attr region
	 * @attr border
	 * @attr split
	 * @attr iconCls
	 * @attr href
	 */
	def south = { attrs, body ->
		attrs["region"] = "south"
		doTag(attrs, body, "div")
	}

	/**
	 * @attr id
	 * @attr title
	 * @attr region
	 * @attr border
	 * @attr split
	 * @attr iconCls
	 * @attr href
	 */
	def east = { attrs, body ->
		attrs["region"] = "east"
		doTag(attrs, body, "div")
	}

	/**
	 * @attr id
	 * @attr title
	 * @attr region
	 * @attr border
	 * @attr split
	 * @attr iconCls
	 * @attr href
	 */
	def center = { attrs, body ->
		attrs["region"] = "center"
		doTag(attrs, body, "div")
	}

	/**
	 * @attr name
	 * @attr params
	 */
	def event = { attrs, body ->
		def events = request.getAttribute(ATTR_EVENTS)
		if(!events) {
			events = new LinkedList<JsEvent>()
			request.setAttribute(ATTR_EVENTS, events)
		}
		def content = body()
		events.push(new JsEvent(attrs["name"], content))
	}

	/**
	 * @attr id
	 * @attr disabled
	 * @attr plain
	 * @attr text
	 * @attr iconCls
	 */
	def linkbutton = { attrs, body ->
		doTag(attrs, body, "a", "linkbutton")
	}
	
	/**
	 * @attr id
	 * @attr zIndex
	 * @attr left
	 * @attr top
	 * @attr minWidth
	 */
	def menu = { attrs, body ->
		doTag(attrs, body, "div", "menu")
	}

	/**
	 * @attr id
	 * @attr plain
	 * @attr menu
	 * @attr duration
	 */
	def menubutton = { attrs, body ->
		doTag(attrs, body, "a", "menubutton")
	}

	/**
	 * @attr id
	 * @attr plain
	 * @attr menu
	 * @attr duration
	 */
	def splitbutton = { attrs, body ->
		doTag(attrs, body, "a", "splitbutton")
	}

	/**
	 * @attr id
	 * @attr url
	 */
	def form = { attrs, body ->
		doTag(attrs, body, "form")
	}


	/**
	 * @attr id
	 * @attr required
	 * @attr validType
	 * @attr missingMessage
	 * @attr invalidMessage
	 * @attr deltaX
	 */
	def validatebox = { attrs, body ->
		doTag(attrs, body, "input", "validatebox")
	}
	
	/**
	 * @attr id
	 * @attr required
	 * @attr validType
	 * @attr missingMessage
	 * @attr invalidMessage
	 * @attr deltaX
	 */
	def textarea = { attrs, body ->
		doTag(attrs, body, "textarea", "validatebox")
	}

	/**
	 * @attr id
	 * @attr width
	 * @attr panelWidth
	 * @attr panelHeight
	 * @attr multiple
	 * @attr separator
	 * @attr editable
	 * @attr disabled
	 * @attr hasDownArrow
	 * @attr value
	 * @attr delay
	 * @attr keyHandler
	 */
	def combo = { attrs, body ->
		doTag(attrs, body, "input", "combo")
	}

	/**
	 * @attr id
	 * @attr width
	 * @attr panelWidth
	 * @attr panelHeight
	 * @attr multiple
	 * @attr separator
	 * @attr editable
	 * @attr disabled
	 * @attr hasDownArrow
	 * @attr value
	 * @attr delay
	 * @attr keyHandler
	 * @attr valueField
	 * @attr textField
	 * @attr mode
	 * @attr url
	 * @attr method
	 * @attr data
	 * @attr filter
	 * @attr formatter
	 * @attr loader
	 */
	def combobox = { attrs, body ->
		doTag(attrs, body, "select", "combobox")
	}

	/**
	 * @attr id
	 * @attr width
	 * @attr panelWidth
	 * @attr panelHeight
	 * @attr multiple
	 * @attr separator
	 * @attr editable
	 * @attr disabled
	 * @attr hasDownArrow
	 * @attr value
	 * @attr delay
	 * @attr keyHandler
	 * @attr editable
	 */
	def combotree = { attrs, body ->
		doTag(attrs, body, "select", "combotree")
	}

	/**
	 * @attr id
	 * @attr width
	 * @attr panelWidth
	 * @attr panelHeight
	 * @attr multiple
	 * @attr separator
	 * @attr editable
	 * @attr disabled
	 * @attr hasDownArrow
	 * @attr value
	 * @attr delay
	 * @attr keyHandler
	 * @attr idField
	 * @attr textField
	 * @attr mode
	 * @attr filter
	 */
	def combogrid = { attrs, body ->
		doTag(attrs, body, "select", "combogrid")
	}


	/**
	 * @attr id
	 * @attr required
	 * @attr validType
	 * @attr disabled
	 * @attr value
	 * @attr min
	 * @attr max
	 * @attr precision
	 * @attr decimalSeparator
	 * @attr groupSeparator
	 * @attr prefix
	 * @attr suffix
	 * @attr formatter
	 * @attr parser
	 */
	def numberbox = { attrs, body ->
		doTag(attrs, body, "input", "numberbox")
	}

	/**
	 * @attr id
	 * @attr width
	 * @attr panelWidth
	 * @attr panelHeight
	 * @attr currentText
	 * @attr closeText
	 * @attr okText
	 * @attr disabled
	 * @attr formatter
	 * @attr parser
	 */
	def datebox = { attrs, body ->
		doTag(attrs, body, "input", "datebox")
	}

	/**
	 * @attr id
	 * @attr width
	 * @attr panelWidth
	 * @attr panelHeight
	 * @attr currentText
	 * @attr closeText
	 * @attr okText
	 * @attr disabled
	 * @attr formatter
	 * @attr parser
	 * @attr showSeconds
	 * @attr timeSeparator
	 */
	def datetimebox = { attrs, body ->
		doTag(attrs, body, "input", "datetimebox")
	}

	/**
	 * @attr id
	 * @attr width
	 * @attr height
	 * @attr fit
	 * @attr border
	 * @attr firstDay
	 * @attr weeks
	 * @attr months
	 * @attr year
	 * @attr month
	 * @attr current
	 */
	def calendar = { attrs, body ->
		doTag(attrs, body, "div", "calendar")
	}


	/**
	 * @attr id
	 * @attr required
	 * @attr validType
	 * @attr missingMessage
	 * @attr invalidMessage
	 * @attr width
	 * @attr value
	 * @attr min
	 * @attr max
	 * @attr increment
	 * @attr editable
	 * @attr disabled
	 * @attr spin
	 */
	def spinner = { attrs, body ->
		doTag(attrs, body, "input", "spinner")
	}

	/**
	 * @attr id
	 * @attr required
	 * @attr validType
	 * @attr missingMessage
	 * @attr invalidMessage
	 * @attr width
	 * @attr value
	 * @attr min
	 * @attr max
	 * @attr increment
	 * @attr editable
	 * @attr disabled
	 * @attr spin
	 */
	def numberspinner = { attrs, body ->
		doTag(attrs, body, "input", "numberspinner")
	}

	/**
	 * @attr id
	 * @attr required
	 * @attr validType
	 * @attr missingMessage
	 * @attr invalidMessage
	 * @attr width
	 * @attr value
	 * @attr min
	 * @attr max
	 * @attr increment
	 * @attr editable
	 * @attr disabled
	 * @attr spin
	 * @attr separator
	 * @attr showSeconds
	 * @attr highlight
	 */
	def timespinner = { attrs, body ->
		doTag(attrs, body, "input", "timespinner")
	}

	/**
	 * @attr id
	 * @attr width
	 * @attr height
	 * @attr mode
	 * @attr showTip
	 * @attr disabled
	 * @attr value
	 * @attr min
	 * @attr max
	 * @attr step
	 * @attr rule
	 * @attr tipFormatter
	 */
	def slider = { attrs, body ->
		doTag(attrs, body, "input", "slider")
	}

	/**
	 * @attr id
	 * @attr title
	 * @attr width
	 * @attr height
	 * @attr left
	 * @attr top
	 * @attr cls
	 * @attr headerCls
	 * @attr bodyCls
	 * @attr border
	 * @attr doSize
	 * @attr noheader
	 * @attr content
	 * @attr collapsible
	 * @attr minimizable
	 * @attr maximizable
	 * @attr closable
	 * @attr tools
	 * @attr collapsed
	 * @attr minimized
	 * @attr maximized
	 * @attr closed
	 * @attr href
	 * @attr cache
	 * @attr loadingMessage
	 * @attr extractor
	 * @attr draggable
	 * @attr draggable
	 * @attr shadow
	 * @attr inline
	 * @attr modal
	 *
	 */
	def window = { attrs, body ->
		doTag(attrs, body, "div", "window")
	}

	/**
	 * @attr id
	 * @attr title
	 * @attr width
	 * @attr height
	 * @attr left
	 * @attr top
	 * @attr cls
	 * @attr headerCls
	 * @attr bodyCls
	 * @attr border
	 * @attr doSize
	 * @attr noheader
	 * @attr content
	 * @attr collapsible
	 * @attr minimizable
	 * @attr maximizable
	 * @attr closable
	 * @attr tools
	 * @attr collapsed
	 * @attr minimized
	 * @attr maximized
	 * @attr closed
	 * @attr href
	 * @attr cache
	 * @attr loadingMessage
	 * @attr extractor
	 * @attr draggable
	 * @attr draggable
	 * @attr shadow
	 * @attr inline
	 * @attr modal
	 * @attr toolbar
	 * @attr buttons
	 */
	def dialog = { attrs, body ->
		doTag(attrs, body, "div", "dialog")
	}

	/**
	 * @attr id
	 * @attr title
	 * @attr width
	 * @attr height
	 * @attr left
	 * @attr top
	 * @attr cls
	 * @attr columns
	 * @attr frozenColumns
	 * @attr fitColumns
	 * @attr autoRowHeight
	 * @attr toolbar
	 * @attr striped
	 * @attr method
	 * @attr nowrap
	 * @attr idField
	 * @attr url
	 * @attr loadMsg
	 * @attr pagination
	 * @attr rownumbers
	 * @attr singleSelect
	 * @attr checkOnSelect
	 * @attr selectOnCheck
	 * @attr pagePosition
	 * @attr pageNumber
	 * @attr pageSize
	 * @attr pageList
	 * @attr queryParams
	 * @attr sortName
	 * @attr sortOrder
	 * @attr showHeader
	 * @attr showFooter
	 * @attr scrollbarSize
	 * @attr rowStyler
	 * @attr loader
	 * @attr loadFilter
	 * @attr editors
	 * @attr view
	 */
	def datagrid = { attrs, body ->
		doTag(attrs, body, "table", "datagrid")
	}


	def columns = { attrs, body ->
		out << "<thead>"
		out << "<tr>"		
		out << body.call()
		out << "</tr>"
		out << "</thead>"
	}

	/**
	 * @attr id
	 * @attr title
	 * @attr field
	 * @attr width
	 * @attr height
	 * @attr rowspan
	 * @attr colspan
	 * @attr align
	 * @attr sortable
	 * @attr resizable
	 * @attr hidden
	 * @attr checkbox
	 * @attr toolbar
	 * @attr styler
	 * @attr sorter
	 * @attr editor
	 */
	def column = { attrs, body ->
		if (attrs.width)
			attrs.width = attrs.width as Integer 
		
		if (attrs.height)
			attrs.height = attrs.height as Integer
		
		doTag(attrs, body, "th", null, true)
	}

	/**
	 * @attr id
	 * @attr title
	 * @attr width
	 * @attr height
	 * @attr left
	 * @attr top
	 * @attr cls
	 * @attr columns
	 * @attr frozenColumns
	 * @attr fitColumns
	 * @attr autoRowHeight
	 * @attr toolbar
	 * @attr striped
	 * @attr method
	 * @attr nowrap
	 * @attr idField
	 * @attr url
	 * @attr loadMsg
	 * @attr pagination
	 * @attr rownumbers
	 * @attr singleSelect
	 * @attr checkOnSelect
	 * @attr selectOnCheck
	 * @attr pagePosition
	 * @attr pageNumber
	 * @attr pageSize
	 * @attr pageList
	 * @attr queryParams
	 * @attr sortName
	 * @attr sortOrder
	 * @attr showHeader
	 * @attr showFooter
	 * @attr scrollbarSize
	 * @attr rowStyler
	 * @attr loader
	 * @attr loadFilter
	 * @attr editors
	 * @attr showGroup
	 * @attr groupField
	 * @attr groupFormatter
	 */
	def propertygrid = { attrs, body ->
		doTag(attrs, body, "div", "propertygrid")
	}


	/**
	 * @attr id
	 * @attr url
	 * @attr method
	 * @attr animate
	 * @attr checkbox
	 * @attr cascadeCheck
	 * @attr onlyLeafCheck
	 * @attr lines
	 * @attr dnd
	 * @attr data
	 * @attr loader
	 * @attr loadFilter
	 */
	def tree = { attrs, body ->
		doTag(attrs, body, "div", "tree")
	}

	/**
	 * @attr id
	 * @attr title
	 * @attr width
	 * @attr height
	 * @attr left
	 * @attr top
	 * @attr cls
	 * @attr columns
	 * @attr frozenColumns
	 * @attr fitColumns
	 * @attr autoRowHeight
	 * @attr toolbar
	 * @attr striped
	 * @attr method
	 * @attr nowrap
	 * @attr idField
	 * @attr url
	 * @attr loadMsg
	 * @attr pagination
	 * @attr rownumbers
	 * @attr singleSelect
	 * @attr checkOnSelect
	 * @attr selectOnCheck
	 * @attr pagePosition
	 * @attr pageNumber
	 * @attr pageSize
	 * @attr pageList
	 * @attr queryParams
	 * @attr sortName
	 * @attr sortOrder
	 * @attr showHeader
	 * @attr showFooter
	 * @attr scrollbarSize
	 * @attr rowStyler
	 * @attr loader
	 * @attr loadFilter
	 * @attr editors
	 * @attr view
	 * @attr treeField
	 * @attr animate
	 * @attr loader
	 * @attr loadFilter
	 */
	def treegrid = { attrs, body ->
		doTag(attrs, body, "div", "treegrid")
	}	

}
