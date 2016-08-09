/**
 * 
 */
package org.nww.modules.projects.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.nww.modules.projects.ProjectForm;
import org.nww.modules.projects.orm.Project;
import org.nww.modules.projects.orm.ProjectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Validates a given ProjectForm and checks whether the given project name is allowed as project names
 * have to be unique per user.
 * @author mga
 */
public class UniqueNameValidator implements ConstraintValidator<UniqueName, ProjectForm> {

	@Autowired
	private ProjectManager projectMgr;
	
	/* (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
	 */
	@Override
	public void initialize(UniqueName validation) {
	}

	/* (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
	 */
	@Override
	public boolean isValid(ProjectForm form, ConstraintValidatorContext context) {
		// check mode
		if(!StringUtils.isEmpty(form.getUUID())) {
			// edit mode
			Project possibleDup = projectMgr.findByNameAndOwnerUUID(form.getName(), form.getOwnerUUID());
			
			if(null != possibleDup) {
				return possibleDup.getUUID().equals(form.getUUID());
			}
		}
		else {
			// create mode
			return null == projectMgr.findByNameAndOwnerUUID(form.getName(), form.getOwnerUUID());
		}
		return true;
	}
}
