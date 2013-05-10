Grails EasyUI
=============

Grails EasyUI Plugin helps you to develop rich applications using the jQuery JavaScript library EasyUI framework.

The plugin implements taglibs for writing the compontentes in gsp, plus the scripts to generate scaffold to produce CRUD quick and powerful.

Another feature created is EasyuiDomainClassMarshaller that simplifies the rendering of domain objects to compatible JSON components JQuery EasyUI.

Including the resources
------------------------

You must use the Grails resources framework to make use of this plugin.

Download http://www.jeasyui.com/download/, extracting content in webapp/js/jquery-easyui

    jquery-easyui - jQuery EasyUI framework (version 1.3.2)

TagLib
------

```xml
<r:require modules="easyui_core"/>
<r:layoutResources />
``` 
```xml
<html>
    <head>
        <title>Hello World Demo</title>
        <r:require module="easyui_core"/>    			
        <r:layoutResources />
    </head>
    <body>
        <e:window title="My Window" width="300px" height="100px">
            Some Content.
        </e:window>
    </body>
</html>
``` 
![Hello World](https://jquery-easyui.googlecode.com/svn/trunk/share/tutorial/window/win1_1.png)

Scaffold
--------
We can generate scaffold with commands:

```
grails easyui-generate-controller [domainClass]
grails easyui-generate-view [domainClass]
grails easyui-generate-all [domainClass]
grails easyui-install-templates
```

Registering DomainClassMarshaller
---------------------------------

To integrate the data with the components you need to register the domain class renderer. To register this class it is necessary to change the file BootStrap.groovy as follows:

```groovy
import grails.converters.JSON
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.grails.plugins.easyui.EasyuiDomainClassMarshaller

...

class BootStrap {

    GrailsApplication grailsApplication

    def init = { servletContext ->    	 
        JSON.registerObjectMarshaller(new EasyuiDomainClassMarshaller(true, grailsApplication))
        ...
    }
    ...
}
```
