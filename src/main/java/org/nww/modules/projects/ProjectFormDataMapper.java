/**
 * 
 */
package org.nww.modules.projects;

import javax.annotation.Resource;

import org.nww.core.data.form.FormDataMapper;
import org.nww.modules.projects.orm.Project;
import org.nww.modules.projects.orm.ProjectManager;

/**
 * The form data mapping object for project POJOs.
 * @author mga
 */
public class ProjectFormDataMapper implements FormDataMapper<ProjectForm, Project> {

	@Resource(name = "ProjectManager")
	private ProjectManager projectMgr;
	
	/* (non-Javadoc)
	 * @see org.nww.core.data.form.FormDataMapper#mapToForm(org.nww.core.data.PersistentObject)
	 */
	@Override
	public ProjectForm mapToForm(Project p) {
		ProjectForm f = new ProjectForm();
		
		f.setUUID(p.getUUID());
		f.setName(p.getName());
		f.setDescription(p.getDescription());
		f.setOwnerUUID(p.getOwnerUUID());
		f.setStart(p.getStart());
		f.setEnd(p.getEnd());
		f.setCustomer(p.getCustomer());
		f.setLocation(p.getLocation());
		f.setParticipants(p.getParticipants());
		f.setSuppliers(p.getSuppliers());
		f.setImages(p.getImages());
		
		return f;
	}

	/* (non-Javadoc)
	 * @see org.nww.core.data.form.FormDataMapper#mapToPersistentObject(org.nww.core.data.form.Form)
	 */
	@Override
	public Project mapToPersistentObject(ProjectForm form) {
		Project p = mapToExistingPersistentObject(form, projectMgr.createNew());
		
		// force UUID creation by the database
		p.setUUID(null);
		
		return p;
	}

	/* (non-Javadoc)
	 * @see org.nww.core.data.form.FormDataMapper#mapToExistingPersistentObject(org.nww.core.data.form.Form, org.nww.core.data.PersistentObject)
	 */
	@Override
	public Project mapToExistingPersistentObject(ProjectForm f, Project p) {
		p.setUUID(f.getUUID());
		p.setName(f.getName());
		p.setDescription(f.getDescription());
		p.setOwnerUUID(f.getOwnerUUID());
		p.setStart(f.getStart());
		p.setEnd(f.getEnd());
		p.setCustomer(f.getCustomer());
		p.setLocation(f.getLocation());
		p.setParticipants(f.getParticipants());
		p.setSuppliers(f.getSuppliers());
		p.setImages(f.getImages());
		
		return p;
	}
}
