/**
 *
 */
package org.nww.core.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.data.annotation.Transient;
import org.springframework.util.StringUtils;

/**
 * Basically implements all methods of the ExtensiblePersistentObject interface.
 *
 * @author MGA
 *
 */ 
public abstract class AbstractExtensiblePersistentObject extends
        AbstractPersistentObject implements ExtensiblePersistentObject {

	/**
     * This list is used to map the database entities.
     */
    private List<DefaultObjectExtension> extensions = new ArrayList<>();
    /**
     * The cache will be updated after a request for any object name. To speed
     * things up do cache fails too.
     */
    @Transient
    private Map<String, DefaultObjectExtension> cache = new HashMap<>();

    /**
     * Tries to find an extension object. Queries an internal cache first that
     * possibly speeds up the lookup mechanism.
     *
     * @param name The name of the extension object.
     * @param localeID The locale id to find localized extensions. Null is
     * allowed.
     * @return The object extension or null if nothing was found.
     */
    private DefaultObjectExtension find(String name, String localeID) {
        // try cache before querying the database mapped list
        String cacheKey = createCacheKey(name, localeID);

        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        DefaultObjectExtension oe = extensions
                .stream()
                .filter(ext -> ext.getName().equals(name)
                		&& compareExtensionLocaleID(ext.getLocaleID(), localeID)).findFirst()
                .orElse(null);

        // update cache for every request
        // requests without a result value are cached too to prevent multiple
        // senseless iterations
        cache.put(cacheKey, oe);

        return oe;
    }

    private boolean compareExtensionLocaleID(String extensionsLocaleID, String passedLocaleID) {
		if(StringUtils.isEmpty(extensionsLocaleID)) {
			return StringUtils.isEmpty(passedLocaleID);
		}
		
		return extensionsLocaleID.equals(passedLocaleID);
	}

	/**
     * Creates a string value that could be used to store key value pairs inside
     * the internal cache.
     *
     * @param name The name of the extension object.
     * @param localeID
     * @return
     */
    private String createCacheKey(String name, String localeID) {
        // prevent null values a keys
        String key = name != null ? name : "";
        if (!StringUtils.isEmpty(localeID)) {
            key = localeID + "-" + key;
        }

        return key;
    }
    
    /**
     * Create a new extension object and update the underlying cache.
     * @param name the name of the extension
     * @param localeID the locale id
     * @return the newly created object instance
     */
    private DefaultObjectExtension createNewExtension(String name, String localeID) {
    	DefaultObjectExtension oe = new DefaultObjectExtension(name, localeID);
    	
    	this.extensions.add(oe);
    	this.cache.put(createCacheKey(name, localeID), oe);
    	
    	return oe;
    }

    /* (non-Javadoc)
     * @see org.nww.core.data.ExtensiblePersistentObject#getExtensions()
     */
    @Override
    public List<? extends ObjectExtension> getExtensions() {
    	return this.extensions;
    }
    
    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getString(java.lang.String)
     */
    @Override
    public String getString(String name) {
        return getString(name, "");
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getString(java.lang.String, java.lang.String)
     */
    @Override
    public String getString(String name, String localeID) {
        ObjectExtension oe = find(name, localeID);

        return oe != null ? oe.getString() : null;
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getString(java.lang.String, java.util.Locale)
     */
    @Override
    public String getString(String name, Locale locale) {
        return getString(name, localeHelper.getLocaleID(locale));
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setString(java.lang.String, java.lang.String)
     */
    @Override
    public void setString(String name, String value) {
        setString(name, value, "");
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setString(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void setString(String name, String value, String localeID) {
        DefaultObjectExtension oe = find(name, localeID);

        if (null != oe) {
            // update existing object
            oe.setString(value);
        } else {
            // create a new object
            oe = new DefaultObjectExtension(name);
            if (!StringUtils.isEmpty(localeID)) {
                oe.setLocaleID(localeID);
            }
            oe.setString(value);

            // trigger database storing
            this.extensions.add(oe);
        }
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setString(java.lang.String, java.lang.String, java.util.Locale)
     */
    @Override
    public void setString(String name, String value, Locale locale) {
        setString(name, value, localeHelper.getLocaleID(locale));
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getDouble(java.lang.String)
     */
    @Override
    public Double getDouble(String name) {
        return getDouble(name, "");
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getDouble(java.lang.String, java.lang.String)
     */
    @Override
    public Double getDouble(String name, String localeID) {
        ObjectExtension oe = find(name, localeID);

        return oe != null ? oe.getDouble() : null;
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getDouble(java.lang.String, java.util.Locale)
     */
    @Override
    public Double getDouble(String name, Locale locale) {
        return getDouble(name, localeHelper.getLocaleID(locale));
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setDouble(java.lang.String, java.lang.Double)
     */
    @Override
    public void setDouble(String name, Double value) {
        setDouble(name, value, "");
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setDouble(java.lang.String, java.lang.Double, java.lang.String)
     */
    @Override
    public void setDouble(String name, Double value, String localeID) {
        DefaultObjectExtension oe = find(name, localeID);

        if (null != oe) {
            // update existing object
            oe.setDouble(value);
        } else {
            // create a new object
            oe = new DefaultObjectExtension(name);
            if (!StringUtils.isEmpty(localeID)) {
                oe.setLocaleID(localeID);
            }
            oe.setDouble(value);

            // trigger database storing
            this.extensions.add(oe);
        }
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setDouble(java.lang.String, java.lang.Double, java.util.Locale)
     */
    @Override
    public void setDouble(String name, Double value, Locale locale) {
        setDouble(name, value, localeHelper.getLocaleID(locale));
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getInteger(java.lang.String)
     */
    @Override
    public Integer getInteger(String name) {
        return getInteger(name, "");
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getInteger(java.lang.String, java.lang.String)
     */
    @Override
    public Integer getInteger(String name, String localeID) {
        ObjectExtension oe = find(name, localeID);

        return oe != null ? oe.getInteger() : null;
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getInteger(java.lang.String, java.util.Locale)
     */
    @Override
    public Integer getInteger(String name, Locale locale) {
        return getInteger(name, localeHelper.getLocaleID(locale));
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setInteger(java.lang.String, java.lang.Integer)
     */
    @Override
    public void setInteger(String name, Integer value) {
        setInteger(name, value, "");
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setInteger(java.lang.String, java.lang.Integer, java.lang.String)
     */
    @Override
    public void setInteger(String name, Integer value, String localeID) {
        DefaultObjectExtension oe = find(name, localeID);

        if (null != oe) {
            // update existing object
            oe.setInteger(value);
        } else {
            // create a new object
            oe = new DefaultObjectExtension(name);
            if (!StringUtils.isEmpty(localeID)) {
                oe.setLocaleID(localeID);
            }
            oe.setInteger(value);

            // trigger database storing
            this.extensions.add(oe);
        }
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setInteger(java.lang.String, java.lang.Integer, java.util.Locale)
     */
    @Override
    public void setInteger(String name, Integer value, Locale locale) {
        setInteger(name, value, localeHelper.getLocaleID(locale));
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getMultipleString(java.lang.String)
     */
    @Override
    public List<String> getMultipleString(String name) {
        return getMultipleString(name, "");
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getMultipleString(java.lang.String, java.lang.String)
     */
    @Override
    public List<String> getMultipleString(String name, String localeID) {
        ObjectExtension oe = find(name, localeID);

        if (oe != null) {
        	return oe.getMultipleString();
        }

        return new ArrayList<>();
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getMultipleString(java.lang.String, java.util.Locale)
     */
    @Override
    public List<String> getMultipleString(String name, Locale locale) {
        return getMultipleString(name, localeHelper.getLocaleID(locale));
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setMultipleString(java.lang.String, java.util.List)
     */
    @Override
    public void setMultipleString(String name, List<String> value) {
        setMultipleString(name, value, "");
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setMultipleString(java.lang.String, java.util.List, java.lang.String)
     */
    @Override
    public void setMultipleString(String name, List<String> value,
            String localeID) {
    	ObjectExtension oe = find(name, localeID);
    	if(null == oe) {
    		oe = createNewExtension(name, localeID);
    	}
    	
    	oe.setMultipleString(value);
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setMultipleString(java.lang.String, java.util.List, java.util.Locale)
     */
    @Override
    public void setMultipleString(String name, List<String> value, Locale locale) {
        setMultipleString(name, value, localeHelper.getLocaleID(locale));
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getMultipleDouble(java.lang.String)
     */
    @Override
    public List<Double> getMultipleDouble(String name) {
        return getMultipleDouble(name, "");
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getMultipleDouble(java.lang.String, java.lang.String)
     */
    @Override
    public List<Double> getMultipleDouble(String name, String localeID) {
        ObjectExtension oe = find(name, localeID);

        if (oe != null) {
            return oe.getMultipleDouble();
        }

        return new ArrayList<>();
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getMultipleDouble(java.lang.String, java.util.Locale)
     */
    @Override
    public List<Double> getMultipleDouble(String name, Locale locale) {
        return getMultipleDouble(name, localeHelper.getLocaleID(locale));
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setMultipleDouble(java.lang.String, java.util.List)
     */
    @Override
    public void setMultipleDouble(String name, List<Double> value) {
        setMultipleDouble(name, value, "");
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setMultipleDouble(java.lang.String, java.util.List, java.lang.String)
     */
    @Override
    public void setMultipleDouble(String name, List<Double> value,
            String localeID) {
    	ObjectExtension oe = find(name, localeID);
    	if(null == oe) {
    		oe = createNewExtension(name, localeID);
    	}
    	
    	oe.setMultipleDouble(value);
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setMultipleDouble(java.lang.String, java.util.List, java.util.Locale)
     */
    @Override
    public void setMultipleDouble(String name, List<Double> value, Locale locale) {
        setMultipleDouble(name, value, localeHelper.getLocaleID(locale));
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getMultipleInteger(java.lang.String)
     */
    @Override
    public List<Integer> getMultipleInteger(String name) {
        return getMultipleInteger(name, "");
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getMultipleInteger(java.lang.String, java.lang.String)
     */
    @Override
    public List<Integer> getMultipleInteger(String name, String localeID) {
        ObjectExtension oe = find(name, localeID);

        if (oe != null) {
            return oe.getMultipleInteger();
        }

        return new ArrayList<>();
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#getMultipleInteger(java.lang.String, java.util.Locale)
     */
    @Override
    public List<Integer> getMultipleInteger(String name, Locale locale) {
        return getMultipleInteger(name, localeHelper.getLocaleID(locale));
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setMultipleInteger(java.lang.String, java.util.List)
     */
    @Override
    public void setMultipleInteger(String name, List<Integer> value) {
        setMultipleInteger(name, value, "");
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setMultipleInteger(java.lang.String, java.util.List, java.lang.String)
     */
    @Override
    public void setMultipleInteger(String name, List<Integer> value,
            String localeID) {
    	ObjectExtension oe = find(name, localeID);
    	if(null == oe) {
    		oe = createNewExtension(name, localeID);
    	}
    	
    	oe.setMultipleInteger(value);
    }

    /* (non-Javadoc)
     * @see org.sunrise.core.data.ExtensiblePersistentObject#setMultipleInteger(java.lang.String, java.util.List, java.util.Locale)
     */
    @Override
    public void setMultipleInteger(String name, List<Integer> value,
            Locale locale) {
        setMultipleInteger(name, value, localeHelper.getLocaleID(locale));
    }
    
    /*
     * (non-Javadoc)
     * @see org.nww.core.data.ExtensiblePersistentObject#removeAttribute(java.lang.String)
     */
    @Override
    public void removeAttribute(String name) {
    	int toRemoveIndex = -1;
    	
    	for (int i = 0; i < extensions.size(); i++) {
			if(extensions.get(i).getName().equals(name)) {
				toRemoveIndex = i;
				break;
			}
		}
    	
    	if(toRemoveIndex > -1) {
    		extensions.remove(toRemoveIndex);
    	}
    }
    
    /*
     * (non-Javadoc)
     * @see org.nww.core.data.ExtensiblePersistentObject#hasAttribute(java.lang.String)
     */
    @Override
    public boolean hasAttribute(String name) {
    	return find(name, "") != null;
    }
    
    /*
     * (non-Javadoc)
     * @see org.nww.core.data.ExtensiblePersistentObject#hasAttribute(java.lang.String, java.lang.String)
     */
    @Override
	public boolean hasAttribute(String name, String localeID) {
    	return find(name, localeID) != null;
    }
}
