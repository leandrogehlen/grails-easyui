<%=packageName ? "package ${packageName}\n\n" : ''%>import grails.converters.JSON

import org.springframework.dao.DataIntegrityViolationException

import br.com.virtualcode.search.Search
import br.com.virtualcode.search.SearchEngine
import br.com.virtualcode.util.SearchUtil
 
class ${className}Controller {
    
	static allowedMethods = [save: "POST", delete: "POST"]
		
	def index() {
		render (view: 'index')
	}

	def list() {
		def page = request.getParameter('page') as Integer ?: 1
		def rows = request.getParameter('rows') as Integer ?: 10
		def sort = request.getParameter('sort')

		def searchEngine = new SearchEngine(SearchUtil.extractConfig(${className}))
		def search = searchEngine.createSearch()

		if (sort)
			search.setSort(sort, request.getParameter('order') != 'desc')

		def count = ${className}.executeQuery(search.queryCount)
		def list = ${className}.findAll(search.query, [max: rows, offset: (page-1) * rows])

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
