Grails EasyUI
=============

Provides jQuery EasyUI framework resource files and taglibs.

Including the resources
------------------------

You must use the Grails resources framework to make use of this plugin.

Download http://www.jeasyui.com/download/, extracting content in webapp/js/jquery-easyui

    jquery-easyui - jQuery EasyUI framework (version 1.3.2)

Usage
-----

```xml
<r:require modules="easyui_core"/>
<r:layoutResources />
``` 
    
Hello World!
------------
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
