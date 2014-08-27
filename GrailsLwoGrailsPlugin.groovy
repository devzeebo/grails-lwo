import com.zeebo.lwo.BeanHelper

class GrailsLwoGrailsPlugin {
    // the plugin version
    def version = "alpha-0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.4 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Grails Lwo Plugin" // Headline display name of the plugin
    def author = "Eric Siebeneich"
    def authorEmail = "eric.siebeneich@gmail.com"
    def description = '''\
The Grails LWO plugin allows quick conversion of domain objects from database representations to lightweight map objects\
that contain only the fields relevant to the client. It also supports finding, updating, and creating domain objects\
from sparsely populated maps.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/grails-lwo"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
//    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->

	    application.domainClasses.each {
		    def domainClass = it.clazz
		    BeanHelper.beanBuilderMap[domainClass] = BeanHelper.populateBuilderMap(domainClass)
		    setMetaClassMethods(domainClass)
	    }
    }

    def doWithApplicationContext = { ctx ->
        // TODO Implement post initialization spring config (optional)
    }

	def watchedResources = "file:./grails-app/domain/*.groovy"

    def onChange = { event ->
	    if (event.source) {
		    def domainClass = event.source.clazz
		    BeanHelper.beanBuilderMap[domainClass] = BeanHelper.populateBuilderMap(domainClass)
		    setMetaClassMethods(domainClass)
	    }
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }

	def setMetaClassMethods = { domainClass ->
		domainClass.metaClass.toBean = BeanHelper.toBean
		domainClass.metaClass.static.fromBean = BeanHelper.fromBean
	}
}
