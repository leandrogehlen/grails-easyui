<%=packageName ? "package ${packageName}\n\n" : ''%>import grails.converters.JSON

import org.apache.commons.lang.math.NumberUtils
import org.springframework.dao.DataIntegrityViolationException

class ${className}Controller {
    
	static allowedMethods = [save: "POST", delete: "POST"]
		
	def index() {
		render (view: "list")
	}

	def list() {
		def page = params.page as Integer
		def rows = params.rows as Integer
		<% if (pluginManager.hasGrailsPlugin('search-fields')) {%>
		def search = ${className}.createSearch()

		if (params.sort)
			search.setSort(params.sort, params.order != 'desc')
			
		if (params.value)
			search = search.execute(params.field, params.value)
						
		def limit = (page && rows) ? [max: rows, offset: (page-1) * rows] : [:]
		def count = ${className}.executeQuery(search.count)
		def list = ${className}.findAll(search.query, limit)
		<%} else {%>
		if (page && rows) {
			params.max = Math.min(rows ?: 10, 100)
			params.offset = (page-1) * rows
		}
		
		//TODO: Create filter with "params.field" and "params.value"
		
		def list = ${className}.list(params)
		def count = ${className}.count()
		<%}%>
		render([total: count, rows: list] as JSON)
	}

	def show(Long id) {				
		def ${propertyName} = ${className}.get(id)
		if (!${propertyName}) {
			render([success: false, error: message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), id])] as JSON)
			return
		}
		render ${propertyName} as JSON
	}

	def save() {
		def ${propertyName} = (params.id) ? ${className}.get(params.id) : new ${className}()
		
		if (!${propertyName}) {						
			def error = [message: message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), params.id])]
			render([success: false, messages: [errors:[error]] ] as JSON)		
			return
		}
		
		def version = NumberUtils.toLong(params.version)	
		if (version != null) {
			if (${propertyName}.version > version) {<% def lowerCaseName = grails.util.GrailsNameUtils.getPropertyName(className) %>
				${propertyName}.errors.rejectValue("version", "default.optimistic.locking.failure",
						  [message(code: '${domainClass.propertyName}.label', default: '${className}')] as Object[],
						  "Another user has updated this ${className} while you were editing")
				render([success: false, messages: ${propertyName}.errors] as JSON)
				return
			}
		}
		
		${propertyName}.properties = params<% 
		props = domainClass.properties.findAll { it.association }
		props.each { p -> %>
		${propertyName}.${p.name} = ${p.referencedDomainClass.name}.get(params.${p.name}_id)
		<%}%>				
		if (!${propertyName}.save(flush: true)) {
			render([success: false, messages: ${propertyName}.errors] as JSON)
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
