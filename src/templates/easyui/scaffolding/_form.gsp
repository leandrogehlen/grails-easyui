<% import grails.persistence.Event %>
<e:window id="win" title="\${g.message(code: '${domainClass.propertyName}.label')}" width="400px" closed="true"
	maximizable="false" minimizable="false" collapsible="false" modal="true">
	<form id="frm" method='POST' autocomplete="off" ><%
		excludedProps = Event.allEvents.toList() << 'version'
		allowedNames = domainClass.persistentProperties*.name << 'id' << 'dateCreated' << 'lastUpdated'
		props = domainClass.properties.findAll { allowedNames.contains(it.name) && !excludedProps.contains(it.name) && it.type != null && !Collection.isAssignableFrom(it.type) }
		Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
		props.each {p ->					
			if (p.name == 'id') {%>
		<g:hiddenField name="id" name="id" />
		  <%} else { %>						
		<div class="row-input">
			<label for="${p.name}"><g:message code="${domainClass.propertyName}.${p.name}"/></label>
			${renderEditor(p)}
		</div><%}}%>
		
		<div class="row-errors">
		</div>
														
		<div class="row-buttons">
			<e:linkbutton id="btnSave" iconCls="icon-ok"><g:message code="default.button.save.label" /></e:linkbutton>  			
		</div>			
	</form>		
</e:window>		
