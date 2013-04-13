<% import grails.persistence.Event %>
<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" href="\${resource(dir: 'css', file: 'main.css')}" type="text/css">
		<link rel="stylesheet" href="\${resource(dir: 'css', file: 'mobile.css')}" type="text/css">	
		<r:require module="easyui-scaffold"/>				
		<r:layoutResources />
				
		<script type="text/javascript">								
			\$(document).ready(function() {
				var scaffold = new Scaffold({
					window: \$('#win'), 
				 	grid: \$('#grid'), 
				 	route: '\${g.createLink()}'
				});
				
												
				\$('#btnAdd').click(function(){
					scaffold.add();
				});

				\$('#btnEdit').click(function(){
					scaffold.edit();
				});

				\$('#btnDelete').click(function(){
					scaffold.remove();
				});

				\$('#btnRefresh').click(function(){
					scaffold.refresh();
				});

				\$('#btnSave').click(function(){
					scaffold.save();
				});

				\$('#txtSearch').searchbox({
					searcher: function(value, name){
						scaffold.search(name, value);
					}
				});			
			});															
		</script>								
	<body>		
		<div id="tb">	
			<table width="100%">
				<tr valign="middle">
					<td>
						<e:linkbutton id="btnAdd" plain="true" iconCls="icon-add"><g:message code="default.button.create.label"/></e:linkbutton>
						<e:linkbutton id="btnEdit" plain="true" iconCls="icon-edit"><g:message code="default.button.edit.label"/></e:linkbutton>  
						<e:linkbutton id="btnDelete" plain="true" iconCls="icon-remove"><g:message code="default.button.delete.label"/></e:linkbutton>
						<e:linkbutton id="btnRefresh" plain="true" iconCls="icon-reload"><g:message code="default.button.refresh.label"/></e:linkbutton>
					</td>
					<td align="right">
						<e:searchbox id="txtSearch" menu="#mm"/>												
						<div id="mm" ><%
							excludedProps = Event.allEvents.toList() << 'version'
							allowedNames = domainClass.persistentProperties*.name << 'id' << 'dateCreated' << 'lastUpdated'
							props = domainClass.properties.findAll { allowedNames.contains(it.name) && !excludedProps.contains(it.name) && it.type != null && !Collection.isAssignableFrom(it.type) }
							Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
							
							props.eachWithIndex { p, i ->
							if (p.name != 'id' && i < 6) { %>
							<div data-options="name:'${p.name}'"><g:message code="${p.name}"/></div><%}}%>       					 
						</div>
					</td>
				</tr>
			</table>
		</div>

		<e:datagrid id="grid" idField="id" fit="true" fitColumns="true" width="500px" height="300px" 
			pagination="true" url="\${createLink(action:'list')}" toolbar="#tb">
			<e:columns>
				<e:column field="ck" checkbox="true" /><% 												
				width = (props.size > 0) ? (100 / props.size) as Integer : ""
				width = (width) ? "width=\"$width\"" : ""
				
				props.eachWithIndex { p, i ->
				if (i < 6) { 							
					attrs = (p.name == 'id') ? 'hidden="true"' : 'sortable="true"'%>  						
				<e:column field="${p.name}" $attrs $width><g:message code="${domainClass.propertyName}.${p.name}"/></e:column><%} }%>																				  												
			</e:columns>			
		</e:datagrid>
				
		<g:render template="form"/>	
	</body>	
</html>