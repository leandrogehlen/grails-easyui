<%=packageName ? "package ${packageName}\n\n" : ''%>import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ${className}Controller {

	static allowedMethods = [create: "POST", update: "POST", delete: "POST"]	

	def index() {
		render (view: "list")
	}

	def list() {
		params.max = params.rows as Integer
		params.offset = ((params.page as Integer) -1) * params.max

		//TODO: Create filter with "params.field" and "params.value"

		respond( [rows: ${className}.list(params), count: ${className}.count()] )
	}

	def show(${className} ${propertyName}) {
		respond ${propertyName}
	}

	@Transactional
	def create() {
		def ${propertyName} = new ${className}(params)
		def errors = save(${propertyName})

		if (errors){
			respond errors, [status: NOT_ACCEPTABLE]
			return
		}

		respond ${propertyName}, [status: CREATED]
	}

	@Transactional
	def update(${className} ${propertyName}) {

		if (${propertyName} == null) {
			notFound()
			return
		}

		def errors = save(${propertyName})
		if (errors){
			respond errors, [status: NOT_ACCEPTABLE]
			return
		}

		respond ${propertyName}, [status: OK]
	}
		
	@Transactional
	def delete(${className} ${propertyName}) {

		if (${propertyName} == null) {
			notFound()
			return
		}
		
		${propertyName}.delete flush:true
		render status: OK
	}
		
	protected def save(${className} ${propertyName}) {
		
		if (!${propertyName}.validate()) {
			return ${propertyName}.errors
		}

		${propertyName}.save flush:true
		return null
	}
	
	protected void notFound() {
		render status: NOT_FOUND, text: message(code: 'default.not.found.message')
	}
	
}
