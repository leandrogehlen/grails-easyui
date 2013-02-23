package org.grails.plugins.geasyui

import grails.test.mixin.TestFor

@TestFor(EasyUITagLib)
class EasyUITagLibTests {

    void testBasicTag() {
		assert '<div class="easyui-panel"></div>' == applyTemplate('<e:panel/>')

		def expected = "<div class=\"easyui-panel\" data-options=\"title:'test',collapsible:true\"></div>"
		assert expected == applyTemplate('<e:panel title="test" collapsible="true"/>')

		expected = "<div class=\"easyui-panel\" data-options=\"title:'test',collapsible:true\" "+
				   "style=\"width:10px;height:10px;\"></div>"

		assert expected == applyTemplate('<e:panel title="test" collapsible="true" width="10px" height="10px"/>')

		assert '<div class="easyui-layout"></div>' == applyTemplate('<e:layout tag="div"/>')

		assert '<div id="tt" class="easyui-tabs"></div>' == applyTemplate('<e:tabs id="tt"/>')

		assert '<div class="easyui-tabs" style="width:10px;overflow:true"></div>' == applyTemplate('<e:tabs width="10px" style="overflow:true"/>')
    }

	void testEventTag() {

		def expected = "<input class=\"easyui-searchbox\" data-options=\"prompt:'find',"+
					   "searcher:function(name,value){ alert('a'); }\"></input>"

		def result = '<e:searchbox prompt="find"><e:event name="searcher(name,value)">'+
					 'alert(\'a\');'+
					 '</e:event></e:searchbox>'

		assert expected == applyTemplate(result)
	}
}
