package org.grails.plugins.easyui;

import grails.converters.JSON;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.BooleanUtils;
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.commons.GrailsClassUtils;
import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty;
import org.codehaus.groovy.grails.support.proxy.DefaultProxyHandler;
import org.codehaus.groovy.grails.support.proxy.ProxyHandler;
import org.codehaus.groovy.grails.web.converters.ConverterUtil;
import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException;
import org.codehaus.groovy.grails.web.converters.marshaller.ObjectMarshaller;
import org.codehaus.groovy.grails.web.json.JSONWriter;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class EasyuiDomainClassMarshaller implements ObjectMarshaller<JSON> {
	
	private boolean includeVersion;
    private ProxyHandler proxyHandler;
    private GrailsApplication application;

    public EasyuiDomainClassMarshaller(boolean includeVersion, GrailsApplication application) {
        this(includeVersion, ((ProxyHandler) (new DefaultProxyHandler())), application);
    }

    public EasyuiDomainClassMarshaller(boolean includeVersion, ProxyHandler proxyHandler, GrailsApplication application){
        this.includeVersion = false;
        this.includeVersion = includeVersion;
        this.proxyHandler = proxyHandler;
        this.application = application;
    }
    
    public boolean isIncludeVersion(){
        return includeVersion;
    }

    public void setIncludeVersion(boolean includeVersion){
        this.includeVersion = includeVersion;
    }
    
	@Override
	public boolean supports(Object object) {
		String name = ConverterUtil.trimProxySuffix(object.getClass().getName());
		return application.isArtefactOfType(DomainClassArtefactHandler.TYPE, name);
	}
	
	protected String concatPropertyName(String parentName, String propertyName)
    {
		return parentName == null ? propertyName : parentName.concat("_").concat(propertyName);        
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
    protected void writeProperties(Object obj, JSON json, String parentName) {
        JSONWriter writer = json.getWriter();
        obj = proxyHandler.unwrapIfProxy(obj);
        Class<?> clazz = obj.getClass();
        GrailsDomainClass domainClass = (GrailsDomainClass)application.getArtefact("Domain", ConverterUtil.trimProxySuffix(clazz.getName()));
        GrailsDomainClassProperty id = domainClass.getIdentifier();
        Object idValue = extractValue(obj, id);
        json.property(concatPropertyName(parentName, "id"), idValue);
        
        if(parentName == null && isIncludeVersion()) {
            GrailsDomainClassProperty versionProperty = domainClass.getVersion();
            Object version = extractValue(obj, versionProperty);
            if(version != null) {
                json.property("version", version);
            }
        }
        BeanWrapper beanWrapper = new BeanWrapperImpl(obj);        
        GrailsDomainClassProperty[] properties = domainClass.getPersistentProperties();
        Collection<String> transients = (Collection<String>) GrailsClassUtils.getStaticPropertyValue(obj.getClass(), GrailsDomainClassProperty.TRANSIENT);                
        Set<GrailsDomainClassProperty> exportProperties = new HashSet<GrailsDomainClassProperty>();
        
        for (GrailsDomainClassProperty property : properties)
        	exportProperties.add(property);
        
        if (transients != null) {
        	for (String propName : transients) 
        		exportProperties.add(domainClass.getPropertyByName(propName));
        }
                
        for (GrailsDomainClassProperty property : exportProperties) {
        
            if(!property.isAssociation() ) {
                writer.key(concatPropertyName(parentName, property.getName()));
                Object val = beanWrapper.getPropertyValue(property.getName());
                if (val instanceof Boolean)
                	val = BooleanUtils.toStringTrueFalse((Boolean) val);
                json.convertAnother(val);
            } 
            else {
                Object referenceObject = beanWrapper.getPropertyValue(property.getName());
                if(isRenderDomainClassRelations()) {
                    if(referenceObject == null)                    
                        writer.value(null);
                    else {
                    	referenceObject = proxyHandler.unwrapIfProxy(referenceObject);
                        if(referenceObject instanceof SortedMap){
                            referenceObject = new TreeMap((SortedMap)referenceObject);
                        } 
                        else if(referenceObject instanceof SortedSet) {
                            referenceObject = new TreeSet((SortedSet)referenceObject);
                        } 
                        else if(referenceObject instanceof Set) {
                            referenceObject = new HashSet((Set)referenceObject);
                        } else
                        if(referenceObject instanceof Map) {
                            referenceObject = new HashMap((Map)referenceObject);
                        } else if(referenceObject instanceof Collection) {
                            referenceObject = new ArrayList((Collection)referenceObject);
                        }
                        json.convertAnother(referenceObject);
                    }
                } 
                if (property.isCircular()) {                	
            		writer.key(concatPropertyName(parentName, property.getName() + "_id"));
            		Object val = (referenceObject != null) ? new BeanWrapperImpl(referenceObject).getPropertyValue("id") : null;            		
            		writer.value(val);
            	}                
                else if(referenceObject == null) {
                	writer.key(concatPropertyName(parentName, property.getName()));
                    json.value(null);
                } 
                else {
                	GrailsDomainClass referencedDomainClass = property.getReferencedDomainClass();                	
                	                	                	
                	if(referencedDomainClass == null || property.isEmbedded() || GrailsClassUtils.isJdk5Enum(property.getType())) {
                		json.convertAnother(referenceObject);
                	} 
                	else if(property.isOneToOne() || property.isManyToOne() || property.isEmbedded()) {                		
                		writeProperties(referenceObject, json, concatPropertyName(parentName, property.getName()));
                	}
                }
            }
        }
    }

    protected Object extractValue(Object domainObject, GrailsDomainClassProperty property) {
        return (new BeanWrapperImpl(domainObject)).getPropertyValue(property.getName());
    }

    protected boolean isRenderDomainClassRelations() {
        return false;
    }

	@Override
	public void marshalObject(Object value, JSON json) throws ConverterException {
		 JSONWriter writer = json.getWriter();
	     writer.object();
	     writeProperties(value, json, null);
	     writer.endObject();		
	}

}
