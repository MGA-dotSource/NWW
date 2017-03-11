/**
 * 
 */
package org.nww.modules.users.orm;

import java.util.ArrayList;
import java.util.List;

import org.nww.core.data.AbstractExtensiblePersistentObject;
import org.nww.core.utils.ApplicationContextHelper;
import org.nww.modules.profiles.orm.Profile;
import org.nww.modules.profiles.orm.ProfileManager;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

/**
 * Basic implementation of the {@link User} interface.
 * @author mga
 *
 */
@Document(collection = "users")
public class UserImpl extends AbstractExtensiblePersistentObject implements User {

	@Indexed(unique = true)
	private String username;
	private String password;
	private int enabled;
	private List<String> roles = new ArrayList<>();
	private String profileUUID;
	
	@Transient
	private Profile profile;
	
	@Transient
	private ProfileManager profileMgr = ApplicationContextHelper.getBeanByClass(ProfileManager.class);
	
	/* (non-Javadoc)
	 * @see org.nww.orm.dao.users.User#getUsername()
	 */
	@Override
	public String getUsername() {
		return username;
	}
	
	/* (non-Javadoc)
	 * @see org.nww.orm.dao.users.User#setUsername(java.lang.String)
	 */
	@Override
	public void setUsername(String username) {
		this.username = username;
	}
	
	/* (non-Javadoc)
	 * @see org.nww.orm.dao.users.User#getPassword()
	 */
	@Override
	public String getPassword() {
		return password;
	}
	
	/* (non-Javadoc)
	 * @see org.nww.orm.dao.users.User#setPassword(java.lang.String)
	 */
	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	
	/* (non-Javadoc)
	 * @see org.nww.orm.dao.users.User#isEnabled()
	 */
	@Override
	public int isEnabled() {
		return enabled;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.dao.users.User#setEnabled(int)
	 */
	@Override
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.modules.users.orm.User#getRoles()
	 */
	@Override
	public List<String> getRoles() {
		return this.roles;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.modules.users.orm.User#setRoles(java.util.List)
	 */
	@Override
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.modules.users.orm.User#hasRole(java.lang.String)
	 */
	@Override
	public boolean hasRole(String role) {
		if(null == roles) {
			return false;
		}
		
		return roles.contains(role);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.orm.dao.users.User#getProfile()
	 */
	@Override
	public Profile getProfile() {
		if(null == this.profile) {
			this.profile = profileMgr.findOne(this.profileUUID);
		}
		return this.profile;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.orm.dao.users.User#setProfile(org.nww.orm.dao.users.ProfileImpl)
	 */
	@Override
	public void setProfile(Profile profile) {
		this.profile = profile;
		this.profileUUID = profile.getUUID();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.orm.dao.users.User#hasProfile()
	 */
	@Override
	public boolean hasProfile() {
		// this implementation fixes a lot of frontend problems regarding 'having no profile but setting a profile image' etc.
		// I'm not happy with this, but currently there' no better solution
		return getProfile() != null 
				&& ((!StringUtils.isEmpty(getProfile().getSurename()) || !StringUtils.isEmpty(getProfile().getName()))
				|| (getProfile().hasAttribute("ProfileImageUUID")));
	}

	/*
	 * (non-Javadoc)
	 * @see org.nww.orm.users.User#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		if(isDetailedUsernameAvailable()) {
			StringBuilder dn = new StringBuilder();
			Profile p = getProfile();
			
			if(!StringUtils.isEmpty(p.getSurename())) {
				dn.append(p.getSurename());
			}
			
			if(!StringUtils.isEmpty(p.getName())) {
				if(dn.length() > 0) {
					dn.append(" ");
				}
				dn.append(p.getName());
			}
			
			return dn.toString();
		}
		
		return getUsername();
	}
	
	private boolean isDetailedUsernameAvailable() {
		return hasProfile() && (!StringUtils.isEmpty(getProfile().getSurename()) || !StringUtils.isEmpty(getProfile().getName()));
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.modules.users.orm.User#isAdmin()
	 */
	@Override
	public boolean isAdmin() {
		return getRoles().contains("ROLE_ADMIN");
	}
}
