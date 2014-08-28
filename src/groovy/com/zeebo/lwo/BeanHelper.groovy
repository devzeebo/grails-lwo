package com.zeebo.lwo

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
/**
 * User: Eric Siebeneich
 * Date: 6/6/14
 */
class BeanHelper {

	static def beanBuilderMap = [:].withDefault populateBuilderMap

	static def populateBuilderMap = { Class k ->
		def fields = k.declaredFields.findAll { it.annotations.find { it.annotationType() == BeanProperty } }
		def keys = fields.findAll { it.annotations.find { it.annotationType() == BeanProperty }.value() }
		[
				keys: keys*.name,
				copy: (fields - keys)*.name
		]
	}

	static def fromBean = { def bean ->

		def clazz = delegate
		def obj = clazz."findBy${beanBuilderMap[clazz].keys.collect {String s -> "${s[0].toUpperCase()}${s.substring(1)}" }.join('And') }"(
			beanBuilderMap[clazz].keys.collect { bean[it] }
		)

		if (!obj) {
			obj = clazz.newInstance()
			beanBuilderMap[clazz].keys.each { obj."${it}" = bean[it] }
		}

		beanBuilderMap[clazz].copy.each {
			if (bean[it]) {
				obj."${it}" = bean[it]
			}
		}

		return obj
	}

	static def toBean = {

		def bean = [:]

		(beanBuilderMap[delegate.class].keys + beanBuilderMap[delegate.class].copy).each {
			bean[it] = delegate."${it}"
		}

		return bean
	}
}

@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.FIELD])
@interface BeanProperty {
	boolean value() default false
}