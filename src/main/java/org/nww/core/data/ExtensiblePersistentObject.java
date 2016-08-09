/**
 *
 */
package org.nww.core.data;

import java.util.List;
import java.util.Locale;

/**
 * Extensible persistent object allows to store dynamically generated named
 * information directly at the object without recreating the database structure
 * or by using full database features like mongos document approach.
 *
 * @author mga
 */
public interface ExtensiblePersistentObject extends PersistentObject {

	/**
	 * @return list of all available extensions
	 */
	List<? extends ObjectExtension> getExtensions();
	
    /**
     * Returns a named dynamic string attribute that is stored unlocalized.
     *
     * @param name The unique name of the attribute.
     * @return The String value of the attribute or null if it could not be
     * found.
     */
    public String getString(String name);

    /**
     * Returns a named and localized dynamic string attribute.
     *
     * @param name The unique name of the attribute.
     * @param localeID The locale id of the attribute (e.g. "en_US")
     * @return The localized String value of the attribute or null if it could
     * not be found.
     */
    public String getString(String name, String localeID);

    /**
     * Returns a named and localized dynamic string attribute.
     *
     * @param name The unique name of the attribute.
     * @param locale The locale object of the attribute.
     * @return The localized String value of the attribute or null if it could
     * not be found.
     */
    public String getString(String name, Locale locale);

    /**
     * Create or update a named dynamic string attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The string value of the attribute. The value is limited to
     * 16384 characters.
     */
    public void setString(String name, String value);

    /**
     * Create or update a named and localized dynamic string attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The string value of the attribute. The value is limited to
     * 16384 characters.
     * @param localeID The locale id of the attribute (e.g. "en_US").
     */
    public void setString(String name, String value, String localeID);

    /**
     * Create or update a named and localized dynamic string attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The string value of the attribute. The value is limited to
     * 16384 characters.
     * @param locale The locale object of the attribute.
     */
    public void setString(String name, String value, Locale locale);

    /**
     * Returns a named dynamic double attribute that is stored unlocalized.
     *
     * @param name The unique name of the attribute.
     * @return The Double value of the attribute or null if it could not be
     * found.
     */
    public Double getDouble(String name);

    /**
     * Returns a named and localized dynamic double attribute.
     *
     * @param name The unique name of the attribute.
     * @param localeID The locale id of the attribute (e.g. "en_US")
     * @return The localized Double value of the attribute or null if it could
     * not be found.
     */
    public Double getDouble(String name, String localeID);

    /**
     * Returns a named and localized dynamic double attribute.
     *
     * @param name The unique name of the attribute.
     * @param locale The locale object of the attribute.
     * @return The localized Double value of the attribute or null if it could
     * not be found.
     */
    public Double getDouble(String name, Locale locale);

    /**
     * Create or update a named dynamic double attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The double value of the attribute.
     */
    public void setDouble(String name, Double value);

    /**
     * Create or update a named and localized dynamic double attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The double value of the attribute.
     * @param localeID The locale id of the attribute (e.g. "en_US").
     */
    public void setDouble(String name, Double value, String localeID);

    /**
     * Create or update a named and localized dynamic double attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The string value of the attribute.
     * @param locale The locale object of the attribute.
     */
    public void setDouble(String name, Double value, Locale locale);

    /**
     * Returns a named dynamic Integer attribute that is stored unlocalized.
     *
     * @param name The unique name of the attribute.
     * @return The Integer value of the attribute or null if it could not be
     * found.
     */
    public Integer getInteger(String name);

    /**
     * Returns a named and localized dynamic Integer attribute.
     *
     * @param name The unique name of the attribute.
     * @param localeID The locale id of the attribute (e.g. "en_US")
     * @return The localized Integer value of the attribute or null if it could
     * not be found.
     */
    public Integer getInteger(String name, String localeID);

    /**
     * Returns a named and localized dynamic Integer attribute.
     *
     * @param name The unique name of the attribute.
     * @param locale The locale object of the attribute.
     * @return The localized Integer value of the attribute or null if it could
     * not be found.
     */
    public Integer getInteger(String name, Locale locale);

    /**
     * Create or update a named dynamic Integer attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The Integer value of the attribute.
     */
    public void setInteger(String name, Integer value);

    /**
     * Create or update a named and localized dynamic Integer attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The Integer value of the attribute.
     * @param localeID The locale id of the attribute (e.g. "en_US").
     */
    public void setInteger(String name, Integer value, String localeID);

    /**
     * Create or update a named and localized dynamic Integer attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The string value of the attribute.
     * @param locale The locale object of the attribute.
     */
    public void setInteger(String name, Integer value, Locale locale);

    /**
     * Returns a named dynamic multiple String attribute that is stored
     * unlocalized.
     *
     * @param name The unique name of the attribute.
     * @return The multiple String value of the attribute or null if it could
     * not be found.
     */
    public List<String> getMultipleString(String name);

    /**
     * Returns a named and localized dynamic multiple String attribute.
     *
     * @param name The unique name of the attribute.
     * @param localeID The locale id of the attribute (e.g. "en_US")
     * @return The localized multiple String value of the attribute or null if
     * it could not be found.
     */
    public List<String> getMultipleString(String name, String localeID);

    /**
     * Returns a named and localized dynamic multiple String attribute.
     *
     * @param name The unique name of the attribute.
     * @param locale The locale object of the attribute.
     * @return The localized multiple String value of the attribute or null if
     * it could not be found.
     */
    public List<String> getMultipleString(String name, Locale locale);

    /**
     * Create or update a named dynamic multiple String attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The list value of the attribute.
     */
    public void setMultipleString(String name, List<String> value);

    /**
     * Create or update a named and localized dynamic multiple String attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The list value of the attribute.
     * @param localeID The locale id of the attribute (e.g. "en_US").
     */
    public void setMultipleString(String name, List<String> value, String localeID);

    /**
     * Create or update a named and localized dynamic multiple String attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The list value of the attribute.
     * @param locale The locale object of the attribute.
     */
    public void setMultipleString(String name, List<String> value, Locale locale);

    /**
     * Returns a named dynamic multiple Double attribute that is stored
     * unlocalized.
     *
     * @param name The unique name of the attribute.
     * @return The multiple Double value of the attribute or null if it could
     * not be found.
     */
    public List<Double> getMultipleDouble(String name);

    /**
     * Returns a named and localized dynamic multiple Double attribute.
     *
     * @param name The unique name of the attribute.
     * @param localeID The locale id of the attribute (e.g. "en_US")
     * @return The localized multiple Double value of the attribute or null if
     * it could not be found.
     */
    public List<Double> getMultipleDouble(String name, String localeID);

    /**
     * Returns a named and localized dynamic multiple Double attribute.
     *
     * @param name The unique name of the attribute.
     * @param locale The locale object of the attribute.
     * @return The localized multiple Double value of the attribute or null if
     * it could not be found.
     */
    public List<Double> getMultipleDouble(String name, Locale locale);

    /**
     * Create or update a named dynamic multiple Double attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The list value of the attribute.
     */
    public void setMultipleDouble(String name, List<Double> value);

    /**
     * Create or update a named and localized dynamic multiple Double attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The list value of the attribute.
     * @param localeID The locale id of the attribute (e.g. "en_US").
     */
    public void setMultipleDouble(String name, List<Double> value, String localeID);

    /**
     * Create or update a named and localized dynamic multiple Double attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The list value of the attribute.
     * @param locale The locale object of the attribute.
     */
    public void setMultipleDouble(String name, List<Double> value, Locale locale);

    /**
     * Returns a named dynamic multiple Integer attribute that is stored
     * unlocalized.
     *
     * @param name The unique name of the attribute.
     * @return The multiple Integer value of the attribute or null if it could
     * not be found.
     */
    public List<Integer> getMultipleInteger(String name);

    /**
     * Returns a named and localized dynamic multiple Integer attribute.
     *
     * @param name The unique name of the attribute.
     * @param localeID The locale id of the attribute (e.g. "en_US")
     * @return The localized multiple Integer value of the attribute or null if
     * it could not be found.
     */
    public List<Integer> getMultipleInteger(String name, String localeID);

    /**
     * Returns a named and localized dynamic multiple Integer attribute.
     *
     * @param name The unique name of the attribute.
     * @param locale The locale object of the attribute.
     * @return The localized multiple Integer value of the attribute or null if
     * it could not be found.
     */
    public List<Integer> getMultipleInteger(String name, Locale locale);

    /**
     * Create or update a named dynamic multiple Integer attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The list value of the attribute.
     */
    public void setMultipleInteger(String name, List<Integer> value);

    /**
     * Create or update a named and localized dynamic multiple Integer
     * attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The list value of the attribute.
     * @param localeID The locale id of the attribute (e.g. "en_US").
     */
    public void setMultipleInteger(String name, List<Integer> value, String localeID);

    /**
     * Create or update a named and localized dynamic multiple Integer
     * attribute.
     *
     * @param name The unique name of the attribute. The name is limited to 256
     * characters.
     * @param value The list value of the attribute.
     * @param locale The locale object of the attribute.
     */
    public void setMultipleInteger(String name, List<Integer> value, Locale locale);

    /**
     * Remove the extension object with the passed name.
     * @param name the name of the extension
     */
    public void removeAttribute(String name);
    
    /**
     * Check if an attribute with the passed name exists.
     * @param name the attribute name
     * @return true if the attribute exists (does not have to have a value)
     */
    public boolean hasAttribute(String name);
    
    /**
     * Check if an localized attribute with the passed name exists.
     * @param name the attribute name
     * @param localeID the locale id
     * @return true if the attribute exists (does not have to have a value)
     */
    public boolean hasAttribute(String name, String localeID);
}
