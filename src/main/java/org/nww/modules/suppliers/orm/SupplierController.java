/**
 * 
 */
package org.nww.modules.suppliers.orm;

import static org.nww.app.Constants.REDIRECT_PARAM_NAME_ERROR;
import static org.nww.app.Constants.REDIRECT_PARAM_NAME_MESSAGE;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.nww.app.AbstractApplicationController;
import org.nww.core.system.OperationResult.State;
import org.nww.modules.files.orm.FileInformation;
import org.nww.modules.files.orm.FileManager;
import org.nww.modules.profiles.orm.Profile;
import org.nww.modules.profiles.orm.ProfileManager;
import org.nww.modules.search.orm.suppliers.ES_Supplier;
import org.nww.modules.search.orm.suppliers.ES_SupplierRepository;
import org.nww.modules.suppliers.SupplierAutoCompleteWrapper;
import org.nww.modules.suppliers.SupplierForm;
import org.nww.modules.suppliers.SupplierFormDataMapper;
import org.nww.modules.suppliers.UserSuppliersForm;
import org.nww.modules.suppliers.UserSuppliersFormDataMapper;
import org.nww.modules.suppliers.queries.FindProfilesBySupplierQuery;
import org.nww.modules.users.orm.User;
import org.nww.services.web.URLUtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles all requests for suppliers.
 * @author mga
 *
 */
@Controller
@RequestMapping("/network/suppliers")
public class SupplierController extends AbstractApplicationController {
	private static final String REDIRECT_TO_SUPPLIERS = "redirect:/network/suppliers/";

	@Resource(name = "SupplierManager")
	private SupplierManager supplierMgr;
	
	@Resource(name = "ProfileManager")
	private ProfileManager profileMgr;
	
	@Autowired
	private ES_SupplierRepository supplierRepo;
	
	@Resource(name = "SupplierFormDataMapper")
	private SupplierFormDataMapper mapper;
	
	@Resource(name = "UserSuppliersFormDataMapper")
	private UserSuppliersFormDataMapper userSuppliersMapper;
	
	@Resource(name = "URLUtilsService")
	private URLUtilsService urlUtilsService;
	
	@Autowired
	private FileManager fileMgr;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * Show the supplier list page.
	 * @param page
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showListPage(
			@RequestParam(name = "p", required = false, defaultValue = "1") Integer page, 
			@RequestParam(name = "ps", required = false, defaultValue = "100") Integer pageSize, 
			Model model) {
		
		Pageable pagingInfo = new PageRequest(--page, pageSize, Direction.ASC, "name");
		
		Page<? extends Supplier> suppliers = populateCurrentUser().isAdmin() 
				? supplierMgr.findAll(pagingInfo)
				: supplierMgr.findByApprovalState(Supplier.STATUS_APPROVED, pagingInfo);
				
		model.addAttribute("Suppliers", suppliers);
		
		return "suppliers/supplierList";
	}
	
	/**
	 * Show the supplier creation form.
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Model model) {
		SupplierForm form = new SupplierForm();
		
		model.addAttribute("SupplierForm", form);
		
		return "suppliers/editSupplier";
	}
	
	/**
	 * Validate and persiste a new supplier.
	 * @param form
	 * @param bindingResult
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/create.do", method = RequestMethod.POST)
	public String createDo(
			@RequestParam(name = "file", required = true) MultipartFile file,
			@Valid @ModelAttribute("SupplierForm") SupplierForm form, 
			BindingResult bindingResult, 
			RedirectAttributes redirectAttributes,
			Model model) {
		
		// check company name to be unique
		if(supplierMgr.findByName(form.getName()) != null) {
			bindingResult.addError(new FieldError("SupplierForm", 
					"name", 
					form.getName(), false, new String[0], new Object[0],
					"Ein Lieferant mit diesem Namen ist bereits registriert."));
			model.addAttribute("BindingResult", bindingResult);
		}
		
		if(!bindingResult.hasErrors()) {
			// handle file upload
			FileInformation logoImage = null;
			if(null != file) {
				// first parameter is file name that should be set automatically
				try {
					logoImage = fileMgr.saveFile(null, file.getOriginalFilename() ,fileMgr.getShared(), file.getBytes(), file.getContentType());
				} catch (IOException e) {
					logger.error("Could not upload file " + e.getMessage());
				}
			}
			
			Supplier s = mapper.mapToPersistentObject(form);
			
			if(null != logoImage) {
				s.setLogoFileInformation(logoImage);
			}
			
			if(supplierMgr.save(s).getResultState() == State.SUCCESSFULL) {
				redirectAttributes.addAttribute(REDIRECT_PARAM_NAME_MESSAGE, "SSC");
				return REDIRECT_TO_SUPPLIERS;
			}
		}
		
		return "suppliers/editSupplier";
	}
	
	/**
	 * Show the supplier detail page.
	 * @param name
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{name}/", method = RequestMethod.GET)
	public String showDetails(@PathVariable("name") String name, RedirectAttributes redirectAttributes, Model model) {
		Supplier s = supplierMgr.findByName(urlUtilsService.decodeURLSegments(name));
		
		if(null == s) {
			redirectAttributes.addAttribute(REDIRECT_PARAM_NAME_ERROR, "SNF");
			return REDIRECT_TO_SUPPLIERS;
		}
		
		// find users related to this supplier
		List<? extends Profile> profiles = profileMgr.findAllByQuery(new FindProfilesBySupplierQuery(s));
		List<? extends User> users = getUserManager().findByProfileID(profiles.stream().map(p -> p.getUUID()).collect(Collectors.toList()));
		
		model.addAttribute("Supplier", s);
		model.addAttribute("Users", users);
		
		return "suppliers/details";
	}
	
	/**
	 * Show the supplier edit form.
	 * @param name
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{name}/edit", method = RequestMethod.GET)
	public CompletionStage<String> edit(@PathVariable("name") String name, RedirectAttributes redirectAttributes, Model model) {
		return CompletableFuture.supplyAsync(() -> {
			Supplier s = supplierMgr.findByName(urlUtilsService.decodeURLSegments(name));
			
			if(null == s) {
				redirectAttributes.addAttribute(REDIRECT_PARAM_NAME_ERROR, "SNF");
				return REDIRECT_TO_SUPPLIERS;
			}
			
			SupplierForm form = mapper.mapToForm(s);
			
			model.addAttribute("Supplier", s);
			model.addAttribute("SupplierForm", form);
			
			return "suppliers/editSupplier";
		});
	}
	
	/**
	 * Validate and persist edited supplier information.
	 * @param form
	 * @param bindingResult
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{name}/edit.do", method = RequestMethod.POST)
	public CompletionStage<String> editDo(
			@RequestParam(name = "file", required = true) MultipartFile file,
			@RequestParam(name = "removeImage", required = false) boolean removeImage,
			@Valid @ModelAttribute("SupplierForm") SupplierForm form, 
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {
		
		return CompletableFuture.supplyAsync(() -> {
			
			Supplier s = supplierMgr.findOne(form.getUUID());
			if(null == s) {
				redirectAttributes.addAttribute(REDIRECT_PARAM_NAME_ERROR, "SNF");
				return REDIRECT_TO_SUPPLIERS;
			}
			
			// check company name to be unique
			Supplier possibleDuplicate = supplierMgr.findByName(form.getName()); 
			if(possibleDuplicate != null && !possibleDuplicate.getUUID().equals(form.getUUID())) {
				bindingResult.addError(new FieldError("SupplierForm", 
						"name", 
						form.getName(), false, new String[0], new Object[0],
						"Ein Lieferant mit diesem Namen ist bereits registriert."));
				model.addAttribute("BindingResult", bindingResult);
			}
			
			if(!bindingResult.hasErrors()) {
				// handle file upload
				FileInformation logoImage = null;
				if(null != file) {
					// first parameter is file name that should be set automatically
					try {
						logoImage = fileMgr.saveFile(null, file.getOriginalFilename() ,fileMgr.getShared(), file.getBytes(), file.getContentType());
					} catch (IOException e) {
						logger.error("Could not upload file " + e.getMessage());
					}
				}
				if(s.hasLogo()) {
					// delete old logo before storing the new one
					if(null != logoImage || removeImage) {
						fileMgr.deleteFile(s.getLogoFileInformation());
					}
					s.setLogoFileInformation(logoImage);
				}
				
				s = mapper.mapToExistingPersistentObject(form, s);
				
				if(supplierMgr.save(s).getResultState() == State.SUCCESSFULL) {
					redirectAttributes.addAttribute(REDIRECT_PARAM_NAME_MESSAGE, "SSE");
					return REDIRECT_TO_SUPPLIERS;
				}
			}
			
			model.addAttribute("Supplier", s);
			return "suppliers/editSupplier";
		});
	}
	
	/**
	 * Prepare the delete confirmation modal content.
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{name}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable("name") String name, Model model) {
		Supplier s = supplierMgr.findByName(urlUtilsService.decodeURLSegments(name));
		
		if(null == s) {
			logger.error("Could not find supplier with name \"" + name + "\" for delete confirmation layer.");
			return "common/empty";
		}
		
		model.addAttribute("Supplier", s);
		
		return "suppliers/modals/deleteConfirmation";
	}
	
	/**
	 * Delete a supplier and redirect to the supplier list page.
	 * @param name
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{name}/delete.do", method = RequestMethod.POST)
	public String deleteDo(@PathVariable("name") String name, RedirectAttributes redirectAttributes, Model model) {
		Supplier s = supplierMgr.findByName(urlUtilsService.decodeURLSegments(name));
		
		if(null == s) {
			redirectAttributes.addAttribute(REDIRECT_PARAM_NAME_ERROR, "SNF");
		}
		else {
			if(s.hasLogo()) {
				fileMgr.deleteFile(s.getLogoFileInformation());
			}
			supplierMgr.delete(s);
			redirectAttributes.addAttribute(REDIRECT_PARAM_NAME_MESSAGE, "SDS");
		}
		
		return REDIRECT_TO_SUPPLIERS;
	}
	
	/**
	 * Return a page fragment including a short paged list of the users suppliers.
	 * Doesn't support error processing, will always return at least an empty template.
	 * @param uName
	 * @param page
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/byuser/{uname}/shortlist/", method = RequestMethod.GET)
	public String showUsersSupplierShortlist(
			@PathVariable("uname") String uName, 
			@RequestParam(name = "p", required = false, defaultValue = "1") Integer page, 
			@RequestParam(name = "ps", required = false, defaultValue = "10") Integer pageSize,
			Model model) {
		
		User u = getUserManager().findByUsername(uName);
		if(null == u) {
			log.error("Could not find user \"" + uName + "\" to lookup supplier shortlist for!");
			return "common/empty";
		}
		
		if(null != u.getProfile()) {
			// find users suppliers (from extended object)
			List<Supplier> suppliers = supplierMgr.getSuppliersFromExtensibleObject(u.getProfile());
			
			Page<? extends Supplier> supplierPage = null;
			
			// filter by approval state if user is not the current user or current user is not admin
			if(populateCurrentUser().getUUID().equals(u.getUUID()) || populateCurrentUser().isAdmin()) {
				supplierPage = new PageImpl<>(suppliers, new PageRequest(page, pageSize), suppliers.size());
			}
			else {
				// viewing a foreign profile -> see only approved suppliers
				supplierPage = supplierMgr.createFilteredList(suppliers, 
						Supplier.STATUS_APPROVED, page, pageSize);
				// add remark, whether there are more suppliers not beeing approved
				model.addAttribute("HasNotApprovedSuppliers", supplierPage.getTotalElements() < suppliers.size());
			}
			
			model.addAttribute("Suppliers", supplierPage);
			
		}
		else {
			model.addAttribute("Suppliers", new PageImpl<>(Collections.emptyList()));
		}
		
		model.addAttribute("User", u);
		
		return "suppliers/inc/usersSuppliersShortList";
	}
	
	/**
	 * Show the edit form for suppliers of a passed user.
	 * @param uName the username to identify the user
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/byuser/{uname}/edit", method = RequestMethod.GET)
	public String editUsersSuppliers(@PathVariable("uname") String uName, RedirectAttributes redirectAttributes, Model model) {
		User u = getUserManager().findByUsername(uName);
		
		if(null == u) {
			redirectAttributes.addAttribute(REDIRECT_PARAM_NAME_ERROR, "UNF");
			return REDIRECT_TO_NETWORK;
		}
		
		User currentUser = populateCurrentUser();
		if(!currentUser.getUUID().equals(u.getUUID()) && !currentUser.isAdmin()) {
			// user is not allowed to edit the requested profile
			log.error("Access denied! " + currentUser.getDisplayName() + " tried illegal access to " + u.getDisplayName() + "!");
			return REDIRECT_TO_NETWORK;
		}
		
		// mapper & manager are aware of profile beeing null!!
		UserSuppliersForm form = userSuppliersMapper.mapToForm(u.getProfile());
		
		List<Supplier> suppliers = supplierMgr.getSuppliersFromExtensibleObject(u.getProfile());
		
		model.addAttribute("User", u);
		model.addAttribute("Suppliers", suppliers);
		model.addAttribute("UserSuppliersForm", form);
				
		return "suppliers/editUsersSuppliers";
	}
	
	/**
	 * Handles the saving of users supplier selection / input.
	 * @param uName
	 * @param form
	 * @param bindingResult
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/byuser/{uname}/edit.do", method = RequestMethod.POST)
	public String editUserSuppliersDo(@PathVariable("uname") String uName, 
			@Valid @ModelAttribute("UserSupplierForm") UserSuppliersForm form, BindingResult bindingResult, 
			RedirectAttributes redirectAttributes, Model model) {
		
		User u = getUserManager().findByUsername(uName);
		
		if(null == u) {
			redirectAttributes.addAttribute(REDIRECT_PARAM_NAME_ERROR, "UNF");
			return REDIRECT_TO_NETWORK;
		}
		
		User currentUser = populateCurrentUser();
		if(!currentUser.getUUID().equals(u.getUUID()) && !currentUser.isAdmin()) {
			// user is not allowed to edit the requested profile
			log.error("Access denied! " + currentUser.getDisplayName() + " tried illegal access to " + u.getDisplayName() + "!");
			return REDIRECT_TO_NETWORK;
		}
		
		if(!bindingResult.hasErrors()) {
			List<Supplier> suppliers = form.getSupplierNames().stream().map(name -> {
				Supplier s = supplierMgr.findByName(name);
				if(null == s) {
					// create new supplier with name and approval status == not approved
					return supplierMgr.createNew(name, null, null, null, null);
				}
				else {
					return s;
				}
			}).collect(Collectors.toList());
			
			if(null == u.getProfile()) {
				getUserManager().createNewUserProfileWithCheck(u);
			}
			
			Profile p = supplierMgr.setSuppliersForExtensibleObject(u.getProfile(), suppliers);
			
			profileMgr.save(p);
			redirectAttributes.addAttribute(REDIRECT_PARAM_NAME_MESSAGE, "SUUS"); // successfully updated user suppliers
			return "redirect:/network/users/" + u.getUsername() + "/";
		}

		// error case -> return to edit page
		List<Supplier> suppliers = supplierMgr.getSuppliersFromExtensibleObject(u.getProfile());
		
		model.addAttribute("User", u);
		model.addAttribute("Suppliers", suppliers);
		model.addAttribute("UserSuppliersForm", form);
		
		return "suppliers/editUsersSuppliers";
	}
	
	/**
	 * Find autocomplete suggestions for a supplier name search query using elastic search
	 * @param q the query term
	 * @return JSON formatted {@link SupplierAutoCompleteWrapper} object
	 */
	@RequestMapping(value = "/autocomplete", method = RequestMethod.GET)
	public @ResponseBody SupplierAutoCompleteWrapper queryForAutoComplete(@RequestParam("q") String q) {
		SupplierAutoCompleteWrapper result = new SupplierAutoCompleteWrapper();
		result.setQuery(q);
		
		List<ES_Supplier> searchResult = supplierRepo.findByNameLike(q);
		searchResult.forEach(sr -> {
			Supplier s = supplierMgr.findOne(sr.getUUID());
			if(null != s) {
				result.addSuggestion(s.getName(), s);
			}
		});
		
		return result;
	}
	
	/**
	 * Returns a HTML snippet to be used for supplier list inside user supplier area.
	 * @param name the name of the supplier (or intended for newly created ones)
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/boxes/usershort", method = RequestMethod.GET)
	public String createUserSupplierDisplayFragment(
			@RequestParam(name = "n", required = true) String name, 
			Model model) {
		Supplier s = supplierMgr.findByName(urlUtilsService.decodeURLSegments(name));
		
		if(null != s) {
			model.addAttribute("Supplier", s);
		}
		
		model.addAttribute("Name", name);
		
		return "suppliers/inc/supplierBoxes :: userShort";
	}
}
