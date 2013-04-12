<%=packageName ? "package ${packageName}\n\n" : ''%>import grails.converters.JSON

import org.springframework.dao.DataIntegrityViolationException

class ${className}Controller {
    
	static allowedMethods = [save: "POST", delete: "POST"]
		
	def index() {
		render (view: "list")
	}

	def list() {
		def page = params.page as Integer ?: 1
		def rows = params.rows as Integer ?: 10
		<% if (pluginManager.hasGrailsPlugin('search-fields')) {%>
		def search = ${className}.createSearch()

		if (params.sort)
			search.setSort(params.sort, params.order != 'desc')
			
		if (params.value)
			search = search.execute(params.field, params.value)
						
		def count = ${className}.executeQuery(search.count)
		def list = ${className}.findAll(search.query, [max: rows, offset: (page-1) * rows])
		<%} else {%>
		params.max = Math.min(rows ?: 10, 100)
		params.offset = (page-1) * rows
		
		//TODO: Create filter with "params.field" and "param.value"
		
		def list = Pais.list(params)
		def count = Pais.count()
		<%}%>
		render([total: count, rows: list] as JSON)
	}

	def show(Long id) {
		def ${propertyName} = ${className}.get(id)
		render ${propertyName} as JSON
	}

	def save() {
		def ${propertyName} = (params.id) ? ${className}.get(params.id) : new ${className}()
		${propertyName}.properties = params
		
		if (!${propertyName}.save(flush: true)) {
			render([success: false] as JSON)
			return
		}
						
		render([success: true] as JSON)
	}

	def delete(Long id) {
		def ${propertyName} = ${className}.get(id)
		if (!${propertyName})
			render([success: false] as JSON)
		else {
			try {
				${propertyName}.delete(flush: true)				
				render([success: true] as JSON)
			}catch (DataIntegrityViolationException e) {
				render([success: false, erro: e.message] as JSON)
			}
		}
	}

}
