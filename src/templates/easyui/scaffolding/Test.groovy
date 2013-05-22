<%=packageName ? "package ${packageName}\n\n" : ''%>
import org.junit.*
import grails.test.mixin.*

@TestFor(${className}Controller)
@Mock(${className})
class ${className}ControllerTests {

    void testSomething() {
       fail "Implement me"
    }
}
