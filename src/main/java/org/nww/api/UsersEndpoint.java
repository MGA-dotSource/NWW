/**
 * 
 */
package org.nww.api;

import org.nww.modules.users.orm.User;
import org.nww.modules.users.orm.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mga
 */
@RestController
@RequestMapping("/api/users")
public class UsersEndpoint {
	
	@Autowired
	private UserManager userManager;
	
	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public User findByUsername(@PathVariable String username) {
		
		User user = userManager.findByUsername(username);
		return user;
	}
}
