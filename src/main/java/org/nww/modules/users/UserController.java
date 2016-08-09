package org.nww.modules.users;

import static org.nww.app.Constants.REDIRECT_TO_DASHBOARD;
import static org.nww.app.Constants.SELECTED_MENU_ITEM;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.nww.app.AbstractApplicationController;
import org.nww.core.system.OperationResult.State;
import org.nww.modules.files.orm.FileInformation;
import org.nww.modules.files.orm.FileManager;
import org.nww.modules.pinboard.orm.PinboardManager;
import org.nww.modules.profiles.ProfileForm;
import org.nww.modules.profiles.ProfileFormDataMapper;
import org.nww.modules.profiles.SkillsForm;
import org.nww.modules.profiles.SkillsFormDataMapper;
import org.nww.modules.profiles.orm.Profile;
import org.nww.modules.profiles.orm.ProfileManager;
import org.nww.modules.users.orm.User;
import org.nww.services.web.HTMLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author MGA
 *
 */
@Controller
@RequestMapping(value = "/network/users")
public class UserController extends AbstractApplicationController {
	
	private static final String REDIRECT_TO_USERLIST = "redirect:/network/users/";

	private static final String PASSWORD_CONFIRM_ERROR_MSG = "Passwort und Wiederholung m&uuml;ssen &uuml;berein stimmen.";

	private static final String CREDENTIALS_FORM = "users/credentialsForm";

	private static final String PROFILE_IMAGE_UUID = "ProfileImageUUID";
	
	@Resource(name = "ProfileManager")
	private ProfileManager profileMgr;
	
	@Resource(name = "FileManager")
	private FileManager fileMgr;

	@Resource(name = "PinboardManager")
	private PinboardManager pinboardMgr;
	
	@Resource(name = "CredentialsFormDataMapper")
	private CredentialsFormDataMapper credentialsFormDataMapper;
	
	@Resource(name = "ProfileFormDataMapper")
	private ProfileFormDataMapper profileFormDataMapper;
	
	@Resource(name = "SkillsFormDataMapper")
	private SkillsFormDataMapper skillsFormDataMapper;
	
	@Resource(name = "HTMLService")
	private HTMLService htmlService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String userList(Model model) {
		model.addAttribute("Users", getUserManager().findAll(new PageRequest(0, 16, new Sort(Direction.ASC, "username"))));
		
		model.addAttribute(SELECTED_MENU_ITEM, "userList");
		
		return "users/userList";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET, params = "p")
	public String userList(@RequestParam(name = "p", required = true) Integer page, Model model) {
		model.addAttribute("Users", getUserManager().findAll(new PageRequest(page, 16, new Sort(Direction.ASC, "username"))));
		
		return "users/userList :: pagingArea";
	}
	
	@RequestMapping(value = "/{name}/", method = RequestMethod.GET)
	public String showProfile(@PathVariable("name") String username, RedirectAttributes redirectAttributes, Model model) {
		User u = getUserManager().findByUsername(username);
		
		if(null == u) {
			redirectAttributes.addAttribute("e", "UNF");
			return REDIRECT_TO_NETWORK;
		}

		if(populateCurrentUser().getUUID().equals(u.getUUID())) {
			model.addAttribute(SELECTED_MENU_ITEM, "profile");
		}
		
		model.addAttribute("User", u);
		model.addAttribute("PinboardEntries", pinboardMgr.findAllByUser(u));
		
		return "users/profile"; 
	}
	
	@RequestMapping(value = "/{name}/editProfile", method = RequestMethod.GET)
	public String editProfile(@PathVariable("name") String username, RedirectAttributes redirectAttributes, Model model) {
		User u = getUserManager().findByUsername(username);
		
		if(null == u) {
			redirectAttributes.addAttribute("e", "UNF");
			return REDIRECT_TO_NETWORK;
		}
		
		// check user is allowed to be edited -> current = user to edit OR current = admin
		User currentUser = populateCurrentUser();
		if(currentUser.getUUID().equals(u.getUUID())) {
			model.addAttribute(SELECTED_MENU_ITEM, "profile");
		}
		else if(!currentUser.isAdmin()) {
			// user is not allowed to edit the requested profile
			log.error("Access denied! " + currentUser.getDisplayName() + " tried illegal access to " + u.getDisplayName() + "!");
			return REDIRECT_TO_NETWORK;
		}
		
		model.addAttribute("User", u);
		ProfileForm form = profileFormDataMapper.mapToForm(u.getProfile() != null ? u.getProfile() : profileMgr.createNew());
		
		// #62: temporarily fixes HTML markup within company description
		form.setCompanyDescription(htmlService.removeHTMLTags(form.getCompanyDescription()));
		
		// handle possible empty email within form
		if(StringUtils.isEmpty(form.getEmail())) {
			form.setEmail(u.getUsername());			
		}
		
		model.addAttribute("ProfileForm", form);
		
		return "users/editProfile";
	}
	
	@RequestMapping(value = "/{name}/editProfile.do", method = RequestMethod.POST)
	public String editProfileDo(@Valid @ModelAttribute("ProfileForm") ProfileForm form, BindingResult bindingResult, @PathVariable("name") String username, RedirectAttributes redirectAttributes, Model model) {
		User u = getUserManager().findByUsername(username);

		if(null == u) {
			redirectAttributes.addAttribute("e", "UNF");
			return REDIRECT_TO_NETWORK;
		}
		
		User currentUser = populateCurrentUser();
		if(!currentUser.getUUID().equals(u.getUUID()) && !currentUser.isAdmin()) {
			// user is not allowed to edit the requested profile
			log.error("Access denied! " + currentUser.getDisplayName() + " tried illegal access to " + u.getDisplayName() + "!");
			return REDIRECT_TO_NETWORK;
		}
		
		if(!bindingResult.hasErrors()) {
			form.setCompanyDescription(htmlService.convertNewlinesToHTML(form.getCompanyDescription()));
			
			Profile existingProfile = u.getProfile() != null
					? profileFormDataMapper.mapToExistingPersistentObject(form, u.getProfile()) 
							: profileFormDataMapper.mapToPersistentObject(form);
			
			if(profileMgr.save(existingProfile).getResultState() == State.SUCCESSFULL) {
				u.setProfile(existingProfile);
				getUserManager().save(u);
				
				redirectAttributes.addAttribute("m", "PUS");
			}
			else {
				redirectAttributes.addAttribute("e", "PUE");
			}
			return REDIRECT_TO_USERLIST + u.getUsername() + "/";
		}

		model.addAttribute("User", u);
		
		return "users/editProfile";
	}
	
	@RequestMapping(value = "/{name}/editSkills", method = RequestMethod.GET)
	public String editSkills(@PathVariable("name") String username, RedirectAttributes redirectAttributes, 
			Model model) {
		User u = getUserManager().findByUsername(username);
		
		if(null == u) {
			redirectAttributes.addAttribute("e", "UNF");
			return REDIRECT_TO_NETWORK;
		}
		
		User currentUser = populateCurrentUser();
		if(!currentUser.getUUID().equals(u.getUUID()) && !currentUser.isAdmin()) {
			// user is not allowed to edit the requested profile
			log.error("Access denied! " + currentUser.getDisplayName() + " tried illegal access to " + u.getDisplayName() + "!");
			return REDIRECT_TO_NETWORK;
		}
		
		model.addAttribute("User", u);
		SkillsForm form = skillsFormDataMapper.mapToForm(u.getProfile() != null ? u.getProfile() : profileMgr.createNew());
		
		model.addAttribute("SkillsForm", form);
		
		return "users/editSkills";
	}
	
	@RequestMapping(value = "/{name}/newSkillTemplate", method = RequestMethod.GET)
	public String newSkillTemplate() {
		return "users/inc/skills :: formPart";
	}
	
	@RequestMapping(value = "/{name}/editSkills.do", method = RequestMethod.POST)
	public String editSkillsDo(@Valid @ModelAttribute("SkillsForm") SkillsForm form,
			@PathVariable("name") String username, 
			RedirectAttributes redirectAttributes, Model model) {
		
		User u = getUserManager().findByUsername(username);
		
		if(null == u) {
			redirectAttributes.addAttribute("e", "UNF");
			return REDIRECT_TO_NETWORK;
		}
		
		User currentUser = populateCurrentUser();
		if(!currentUser.getUUID().equals(u.getUUID()) && !currentUser.isAdmin()) {
			// user is not allowed to edit the requested profile
			log.error("Access denied! " + currentUser.getDisplayName() + " tried illegal access to " + u.getDisplayName() + "!");
			return REDIRECT_TO_NETWORK;
		}
		
		Profile p = u.getProfile() != null ? skillsFormDataMapper.mapToExistingPersistentObject(form, u.getProfile())
				: skillsFormDataMapper.mapToPersistentObject(form);
		
		if(profileMgr.save(p).getResultState() == State.SUCCESSFULL) {
			u.setProfile(p);
			getUserManager().save(u);
			
			redirectAttributes.addAttribute("m", "SUS");
		}
		else {
			redirectAttributes.addAttribute("e", "SUE");
		}
		return REDIRECT_TO_USERLIST + u.getUsername() + "/";
	}
	
	@RequestMapping(value = "/{name}/editProfileImage", method = RequestMethod.GET)
	public String editProfileImage(@PathVariable("name") String username, Model model) {
		User u = getUserManager().findByUsername(username);
		
		if(null == u) {
			return "common/empty"; // should not happend - bad request status accepted for a while
		}
		
		User currentUser = populateCurrentUser();
		if(!currentUser.getUUID().equals(u.getUUID()) && !currentUser.isAdmin()) {
			// user is not allowed to edit the requested profile
			log.error("Access denied! " + currentUser.getDisplayName() + " tried illegal access to " + u.getDisplayName() + "!");
			return "common/empty";
		}
		
		model.addAttribute("User", u);
		
		return "users/modals/profileImageSelector";
	}
	
	@RequestMapping(value = "/{name}/editProfileImage.do", method = RequestMethod.POST)
	public ResponseEntity<String> editProfileImageDo(@PathVariable("name") String username, 
			@RequestParam(name = "file", required = true) MultipartFile file,
			Model model) {
		
		User u = getUserManager().findByUsername(username);
		
		if(null == u) {
			// user does not have a profile currently
			return ResponseEntity.badRequest().body("");
		}
		
		User currentUser = populateCurrentUser();
		if(!currentUser.getUUID().equals(u.getUUID()) && !currentUser.isAdmin()) {
			// user is not allowed to edit the requested profile
			log.error("Access denied! " + currentUser.getDisplayName() + " tried illegal access to " + u.getDisplayName() + "!");
			return ResponseEntity.badRequest().body("");
		}
		
		if(!u.hasProfile()) {
			Profile p = profileMgr.createNew();
			p.setEmail(u.getUsername());
			profileMgr.save(p);
			u.setProfile(p);
			getUserManager().save(u);
		}
		
		if(!StringUtils.isEmpty(u.getProfile().getString(PROFILE_IMAGE_UUID))) {
			// user already has a profile image attached
			// delete image and image data
			FileInformation toDelete = fileMgr.findOne(u.getProfile().getString(PROFILE_IMAGE_UUID));
			fileMgr.deleteFile(toDelete);
		}
		
		if(!file.isEmpty()) {
			FileInformation fi = null;
			
			try {
				fi = fileMgr.saveFile("profiles", file.getBytes(), file.getContentType());
			} catch (IOException e) {
				log.error("Could not save user profile image: ", e);
			}
			
			if(null != fi) {
				u.getProfile().setString(PROFILE_IMAGE_UUID, fi.getUUID());
				profileMgr.save(u.getProfile());

				return ResponseEntity.ok("");
			}
		}
		
		return ResponseEntity.badRequest().body("");
	}
	
	@RequestMapping(value = "/{name}/removeProfileImage.do", method = RequestMethod.POST)
	public String removeProfileImageDo(@PathVariable("name") String username, RedirectAttributes redirectAttributes, 
			Model model) {
		User u = getUserManager().findByUsername(username);
		
		if(null == u) {
			redirectAttributes.addAttribute("e", "UNF");
			return REDIRECT_TO_NETWORK;
		}
		
		User currentUser = populateCurrentUser();
		if(!currentUser.getUUID().equals(u.getUUID()) && !currentUser.isAdmin()) {
			// user is not allowed to edit the requested profile
			log.error("Access denied! " + currentUser.getDisplayName() + " tried illegal access to " + u.getDisplayName() + "!");
			return REDIRECT_TO_NETWORK;
		}
		
		if(u.hasProfile() && u.getProfile().hasAttribute(PROFILE_IMAGE_UUID)) {
			FileInformation fi = fileMgr.findOne(u.getProfile().getString(PROFILE_IMAGE_UUID));
			
			u.getProfile().removeAttribute(PROFILE_IMAGE_UUID);
			
			if(null != fi && fileMgr.deleteFile(fi) && profileMgr.save(u.getProfile()).getResultState() == State.SUCCESSFULL) {
				redirectAttributes.addAttribute("m", "PIR");
			}
			else {
				redirectAttributes.addAttribute("e", "PIR");
			}
		}
		
		return REDIRECT_TO_USERLIST + u.getUsername()  + "/";
	}
	
	@RequestMapping(value = "/createCredentials", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String createCredentials(Model model) {
		CredentialsForm cf = new CredentialsForm();
		
		model.addAttribute("CredentialsForm", cf);
		model.addAttribute("Mode", "create");
		
		return CREDENTIALS_FORM;
	}
	
	@RequestMapping(value = "/createCredentials.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String createCredentialsDo(@Valid @ModelAttribute("CredentialsForm") CredentialsForm form,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			Model model) {
		// validate passwords
		if(!StringUtils.isEmpty(form.getPassword())) {
			if(!form.getPassword().equals(form.getPasswordConfirmation())) {
				bindingResult.addError(new FieldError("CredentialsForm", "passwordConfirmation", PASSWORD_CONFIRM_ERROR_MSG));
			}
		}
		
		// check username (email) to be unique
		if(getUserManager().findByUsername(form.getEmail()) != null) {
			bindingResult.addError(new FieldError("CredentialsForm", "email", "Diese Email-Adresse wird bereits verwendet."));
		}
		
		if(!bindingResult.hasErrors()) {
			
			User u = credentialsFormDataMapper.mapToPersistentObject(form);
			Profile p = profileMgr.createNew();
			p.setEmail(u.getUsername());
			
			profileMgr.save(p);
			u.setProfile(p);
			
			if(getUserManager().save(u).getResultState() == State.SUCCESSFULL) {
				redirectAttributes.addAttribute("m", "CCS");
				return REDIRECT_TO_USERLIST + u.getUsername() + "/";
			}
		}

		model.addAttribute("Mode", "create");
		
		return CREDENTIALS_FORM;
	}

	@RequestMapping(value = "/{name}/editCredentials", method = RequestMethod.GET)
	public String editCredentials(@PathVariable("name") String username, RedirectAttributes redirectAttributes, Model model) {
		User u = getUserManager().findByUsername(username);
		
		if(null == u) {
			redirectAttributes.addAttribute("e", "UNF");
			return REDIRECT_TO_NETWORK;
		}
		
		User currentUser = populateCurrentUser();
		if(!currentUser.getUUID().equals(u.getUUID()) && !currentUser.isAdmin()) {
			// user is not allowed to edit the requested profile
			log.error("Access denied! " + currentUser.getDisplayName() + " tried illegal access to " + u.getDisplayName() + "!");
			return REDIRECT_TO_NETWORK;
		}
		
		CredentialsForm cf = credentialsFormDataMapper.mapToForm(u);
		
		model.addAttribute("User", u);
		model.addAttribute("CredentialsForm", cf);
		
		return CREDENTIALS_FORM;
	}

	@RequestMapping(value = "/{name}/editCredentials.do", method = RequestMethod.POST)
	public String editCredentialsDo(@Valid @ModelAttribute("CredentialsForm") CredentialsForm form, 
			BindingResult bindingResult, @PathVariable("name") String username, 
			RedirectAttributes redirectAttributes, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
		User u = getUserManager().findByUsername(username);
		
		if(null == u) {
			redirectAttributes.addAttribute("e", "UNF");
			return REDIRECT_TO_NETWORK;
		}
		
		User currentUser = populateCurrentUser();
		if(!currentUser.getUUID().equals(u.getUUID()) && !currentUser.isAdmin()) {
			// user is not allowed to edit the requested profile
			log.error("Access denied! " + currentUser.getDisplayName() + " tried illegal access to " + u.getDisplayName() + "!");
			return REDIRECT_TO_NETWORK;
		}
		
		// validate passwords
		if(!StringUtils.isEmpty(form.getPassword())) {
			if(!form.getPassword().equals(form.getPasswordConfirmation())) {
				bindingResult.addError(new FieldError("CredentialsForm", "passwordConfirmation", PASSWORD_CONFIRM_ERROR_MSG));
			}
		}
		else {
			// no new password provided -> take from existing data to ensure default save method to work correct
			form.setPassword(u.getPassword());
		}
		
		// check username (email) to be unique
		if(!u.getUsername().equals(form.getEmail()) && getUserManager().findByUsername(form.getEmail()) != null) {
			bindingResult.addError(new FieldError("CredentialsForm", "email", "Diese Email-Adresse wird bereits verwendet."));
		}
		
		if(!bindingResult.hasErrors()) {
			boolean isLogoutRequired = isLoginAfterUpdateNeeded(u, form);
			
			u = credentialsFormDataMapper.mapToExistingPersistentObject(form, u);
			Profile p = getUserManager().createNewUserProfileWithCheck(u);
			
			if(null == p) {
				redirectAttributes.addAttribute("e", "SE");
				return REDIRECT_TO_DASHBOARD;
			}
			
			p.setEmail(u.getUsername());
			
			// save profile and user
			if(getUserManager().save(u).getResultState() == State.SUCCESSFULL 
					&& profileMgr.save(u.getProfile()).getResultState() == State.SUCCESSFULL) {
				redirectAttributes.addAttribute("m", "CUS");

				if(isLogoutRequired) {
					new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
					return "redirect:/login";
				}
				
				return REDIRECT_TO_USERLIST + u.getUsername() + "/";
			}
		}

		model.addAttribute("User", u);
		
		return CREDENTIALS_FORM;
	}
	
	@RequestMapping(value = "/{name}/remove", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String removeUser(@PathVariable("name") String username, Model model) {
		User u = getUserManager().findByUsername(username);
		
		if(null == u) {
			return "";
		}
		
		model.addAttribute("User", u);
		
		return "users/modals/userRemoveConfirmation";
	}
	
	@RequestMapping(value = "/{name}/remove.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String removeUserDo(@PathVariable("name") String username, 
			RedirectAttributes redirectAttributes,
			Model model) {
		User u = getUserManager().findByUsername(username);
		
		if(null == u) {
			redirectAttributes.addAttribute("e", "UNF");
			return REDIRECT_TO_DASHBOARD;
		}
		
		if(getUserManager().delete(u).getResultState() == State.SUCCESSFULL) {
			redirectAttributes.addAttribute("m", "DUS"); // delete user successful
		}
		else {
			redirectAttributes.addAttribute("e", "DUE"); // delete user error
		}
		
		return REDIRECT_TO_USERLIST;
	}
	
	/**
	 * Checks the passed data for changes that will require a login after they got successfully changed.
	 * @param u the original user data
	 * @param f the credentials form 
	 * @return
	 */
	private boolean isLoginAfterUpdateNeeded(User u, CredentialsForm f) {
		// users only need to logout if they change their own username
		return !u.getUsername().equals(f.getEmail()) && populateCurrentUser().getUUID().equals(u.getUUID());
	}
}
