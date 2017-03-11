package org.nww.modules.users.orm;

import java.util.List;

import org.nww.core.data.ExtensiblePersistentObject;
import org.nww.modules.profiles.orm.Profile;

/**
 * Defines getters and setters for the user pojo.
 * @author mga
 *
 */
public interface User extends ExtensiblePersistentObject {

	public static final String FIELD_PROFILE_UUID = "profileUUID";
	
	/**
	 * @return the users username
	 */
	public String getUsername();

	/**
	 * Set the users username used for login.
	 * @param username
	 */
	public void setUsername(String username);

	/**
	 * @return the users password
	 */
	public String getPassword();

	/**
	 * Set the users password.
	 * @param password
	 */
	public void setPassword(String password);

	/**
	 * @return true if the user is enabled
	 */
	public int isEnabled();

	/**
	 * Set the user enabled or not.
	 * @param enabled
	 */
	public void setEnabled(int enabled);
	
	/**
	 * @return the roles
	 */
	public List<String> getRoles();
	
	/**
	 * Set the users roles.
	 * @param roles the roles
	 */
	public void setRoles(List<String> roles);
	
	/**
	 * @param role the role to be found
	 * @return true if the user has the passed role
	 */
	public boolean hasRole(String role);
	
	/**
	 * @return the users profile
	 */
	public Profile getProfile();

	/**
	 * Set the users profile
	 * @param profile the profile
	 */
	public void setProfile(Profile profile);

	/**
	 * @return true if the user has a profile attached
	 */
	public boolean hasProfile();
	
	/**
	 * @return the display name for the user
	 */
	public String getDisplayName();
	
	/**
	 * @return true if the user has admin privileges
	 */
	public boolean isAdmin();
}