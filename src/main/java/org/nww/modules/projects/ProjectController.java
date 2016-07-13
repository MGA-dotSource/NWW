/**
 * 
 */
package org.nww.modules.projects;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.nww.app.AbstractApplicationController;
import org.nww.app.Constants;
import org.nww.modules.files.orm.FileInformation;
import org.nww.modules.files.orm.FileManager;
import org.nww.modules.projects.orm.Project;
import org.nww.modules.projects.orm.ProjectFileData;
import org.nww.modules.projects.orm.ProjectManager;
import org.nww.modules.projects.orm.ProjectParticipantData;
import org.nww.modules.projects.orm.ProjectSupplierData;
import org.nww.modules.users.orm.User;
import org.nww.services.web.URLUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * The project controller is responsible for handling all servlet request related to
 * the project management feature.
 * @author mga
 */
@Controller
@RequestMapping(value = "/network/projects")
public class ProjectController extends AbstractApplicationController {
	
	/**
	 * 
	 */
	private static final String FILE_TYPE_IMAGE = "image";
	private static final String PROJECT_NOT_FOUND = "PNF";
	private static final String TEMPLATE_CREATE_PROJECT = "projects/createProject";
	private static final String TEMPLATE_EDIT_PROJECT = "projects/editProject";
	private static final String MODE_CREATE = "create";
	private static final String MODE_EDIT = "edit";
	private static final String REDIRECT_TO_PROJECT_LIST = "redirect:/network/projects/";
	
	@Resource(name = "ProjectManager")
	private ProjectManager projectMgr;
	
	@Autowired
	private FileManager fileMgr;
	
	@Resource(name = "ProjectFormDataMapper")
	private ProjectFormDataMapper mapper;
	
	@Resource(name = "URLUtilsService")
	private URLUtilsService urlUtils;
	
	@ModelAttribute("Users")
	public List<? extends User> populateUsers() {
		return getUserManager().findAll();
	}
	
	/**
	 * Show the project list.
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String projectList(Model model) {
		// workaround for sorting, paging will be implemented later
		Pageable p = new PageRequest(0, 1000, Direction.DESC, "lastModified");
		
		Page<? extends Project> projects = projectMgr.findAll(p);
		
		model.addAttribute("Projects", projects.getContent());
		
		return "projects/projectList";
	}
	
	/**
	 * Show all project details in one view.
	 * @param userName
	 * @param projectName
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{userName}/{projectName}/", method = RequestMethod.GET)
	public String showDetails(@PathVariable("userName") String userName, 
			@PathVariable("projectName") String projectName,
			RedirectAttributes redirectAttributes,
			Model model) {
		Project p = projectMgr.findByNameAndOwner(urlUtils.decodeURLSegments(projectName), getUserManager().findByUsername(userName));
		
		if(null == p) {
			redirectAttributes.addAttribute(Constants.REDIRECT_PARAM_NAME_ERROR, PROJECT_NOT_FOUND);
			return REDIRECT_TO_PROJECT_LIST;
		}
		
		model.addAttribute("Project", p);
		
		// add template, links, etc..
		return "projects/details";
	}
	
	/**
	 * Show the project creation form.
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String create(Model model) {
		ProjectForm form = new ProjectForm();
		
		model.addAttribute("ProjectForm", form);
		
		return TEMPLATE_CREATE_PROJECT;
	}
	
	@RequestMapping(value = "/{userName}/{projectName}/edit", method = RequestMethod.GET)
	public String edit(
			@PathVariable("userName") String userName,
			@PathVariable("projectName") String projectName,
			RedirectAttributes redirectAttributes,
			Model model) {
		
		Project p = projectMgr.findByNameAndOwner(urlUtils.decodeURLSegments(projectName), getUserManager().findByUsername(userName));
		
		// product exists and credentials check credentials
		// TODO: handle failed credentials separatly
		User current = populateCurrentUser();
		if(null == p || !current.isAdmin() || !current.equals(p.getOwner())) {
			redirectAttributes.addAttribute(Constants.REDIRECT_PARAM_NAME_ERROR, PROJECT_NOT_FOUND);
			return REDIRECT_TO_PROJECT_LIST;
		}

		ProjectForm form = mapper.mapToForm(p);
		
		model.addAttribute("ProjectForm", form);
		model.addAttribute("mode", MODE_EDIT);
		
		return TEMPLATE_EDIT_PROJECT;
	}
	
	/**
	 * Adds a new participant template to the passed form.
	 * @param form
	 * @param mode
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/addParticipant.do", method = RequestMethod.POST)
	public String addParticipantDo(
			@ModelAttribute("ProjectForm") ProjectForm form, 
			@RequestParam(required = false, defaultValue = MODE_CREATE) String mode,
			Model model) {
		
		form.getParticipants().add(0, new ProjectParticipantData());
		
		if(MODE_EDIT.equals(mode)) {
			return TEMPLATE_EDIT_PROJECT;
		}
		
		return TEMPLATE_CREATE_PROJECT;
	}
	
	/**
	 * Removes the participant at the removeIndex from the passed form.
	 * @param form
	 * @param removeIndex
	 * @param mode
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/removeParticipant.do", method = RequestMethod.POST)
	public String removeParticipantDo(
			@ModelAttribute("ProjectForm") ProjectForm form,
			@RequestParam(required = true) Integer removeIndex,
			@RequestParam(required = false, defaultValue = MODE_CREATE) String mode,
			Model model) {
		
		form.getParticipants().remove((int)removeIndex);
		
		if(MODE_EDIT.equals(mode)) {
			return TEMPLATE_EDIT_PROJECT;
		}
		
		return TEMPLATE_CREATE_PROJECT;
	}
	
	/**
	 * Adds a new supplier template to the passed form.
	 * @param form
	 * @param mode
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/addSupplier.do", method = RequestMethod.POST)
	public String addSupplierDo(
			@ModelAttribute("ProjectForm") ProjectForm form,
			@RequestParam(required = false, defaultValue = MODE_CREATE) String mode,
			Model model) {
		
		form.getSuppliers().add(0, new ProjectSupplierData());
		
		if(MODE_EDIT.equals(mode)) {
			return TEMPLATE_EDIT_PROJECT;
		}
		
		return TEMPLATE_CREATE_PROJECT;
	}
	
	/**
	 * Removes the supplier with at the removeIndex position from the passed form.
	 * @param form
	 * @param removeIndex
	 * @param mode
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/removeSupplier.do", method = RequestMethod.POST)
	public String removeSupplierDo(
			@ModelAttribute("ProjectForm") ProjectForm form,
			@RequestParam(required = true) Integer removeIndex,
			@RequestParam(required = false, defaultValue = MODE_CREATE) String mode,
			Model model) {
		
		form.getSuppliers().remove((int)removeIndex);
		
		if(MODE_EDIT.equals(mode)) {
			return TEMPLATE_EDIT_PROJECT;
		}
		
		return TEMPLATE_CREATE_PROJECT;
	}
	
	// for file upload search a nice file upload plugin
	// upload via ajax on select the image
	// upload into temp dir
	// return whole FI object as JSON response
	// copy from temp dir to project dir on save / edit -> edit could be difficult as files will already exist
	// how to delete files? -> AJAX? with confirmation?

	@RequestMapping(value = "/uploadFile.do", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ResponseEntity<? extends FileInformation> uploadFileDo(@RequestParam(name = "type", required = false, defaultValue = FILE_TYPE_IMAGE) String type,
			@RequestParam(name = "file", required = true) MultipartFile file,
			@ModelAttribute("ProjectForm") ProjectForm form,
			@RequestParam(required = false, defaultValue = MODE_CREATE) String mode) {
		
		// support only image uploads currently
		if(FILE_TYPE_IMAGE.equals(type)) {
			try {
				return new ResponseEntity<FileInformation>(fileMgr.saveFile(fileMgr.getTemp(), file.getBytes(), file.getContentType()), HttpStatus.OK);
			} catch (IOException e) {
				// TODO: add logging
			}
		}
		
		return ResponseEntity.badRequest().body(null);
	}
	
	@RequestMapping(value = "/newFileDataEntry", params = { "fi" }, method = RequestMethod.GET)
	public String newFileDataEntry(@PathVariable("fi") String fileInformationUUID, Model model) {
		ProjectFileData pfd = new ProjectFileData(fileInformationUUID, null);
		
		model.addAttribute("FileData", pfd);
		
		return "projects/inc/newFileDataEntry";
	}
	
	/**
	 * Save a new project.
	 * @param form
	 * @param bindingResult
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/new.do", method = RequestMethod.POST)
	public String createDo(
			@Valid @ModelAttribute("ProjectForm") ProjectForm form, 
			BindingResult bindingResult, 
			RedirectAttributes redirectAttributes, Model model) {
		
		if(!bindingResult.hasErrors()) {
			projectMgr.save(mapper.mapToPersistentObject(form));
			
			redirectAttributes.addAttribute(Constants.REDIRECT_PARAM_NAME_MESSAGE, "PCS");
			return REDIRECT_TO_PROJECT_LIST;
		}
		
		return TEMPLATE_CREATE_PROJECT;
	}

	/**
	 * Save an edited project.
	 * @param form
	 * @param bindingResult
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/edit.do", method = RequestMethod.POST)
	public String editDo(
			@Valid @ModelAttribute("ProjectForm") ProjectForm form,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			Model model) {
		
		if(!bindingResult.hasErrors()) {
			projectMgr.save(mapper.mapToPersistentObject(form));
			
			redirectAttributes.addAttribute(Constants.REDIRECT_PARAM_NAME_MESSAGE, "PES");
			return REDIRECT_TO_PROJECT_LIST;
		}

		return TEMPLATE_EDIT_PROJECT;
	}
}
