/**
 * 
 */
package org.nww.modules.projects.orm;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.nww.core.system.AbstractPersistentObjectManager;
import org.nww.modules.suppliers.orm.Supplier;
import org.nww.modules.users.orm.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

/**
 * Default implementation of the {@link ProjectManager} interface.
 * @author mga
 */
public class ProjectManagerImpl extends AbstractPersistentObjectManager<Project, ProjectRepository<Project>>
		implements ProjectManager {

	@Resource(name = "ProjectRepository")
	private ProjectRepository<Project> projectRepo;
	
	/* (non-Javadoc)
	 * @see org.nww.core.system.PersistentObjectManager#getRepository()
	 */
	@Override
	public ProjectRepository<Project> getRepository() {
		return this.projectRepo;
	}

	/* (non-Javadoc)
	 * @see org.nww.core.system.PersistentObjectManager#setRepository(org.nww.core.data.PersistentObjectRepository)
	 */
	@Override
	public void setRepository(ProjectRepository<Project> repository) {
		this.projectRepo = repository;
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.projects.orm.ProjectManager#findByNameAndOwnerUUID(java.lang.String, java.lang.String)
	 */
	@Override
	public Project findByNameAndOwnerUUID(String name, String ownerUUID) {
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(ownerUUID)) {
			return null;
		}
		
		return getRepository().findByNameAndOwner(name, ownerUUID);
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.projects.orm.ProjectManager#findByNameAndOwner(java.lang.String, org.nww.modules.users.orm.User)
	 */
	@Override
	public Project findByNameAndOwner(String name, User owner) {
		if(StringUtils.isEmpty(name) || null == owner) {
			return null;
		}
		
		return getRepository().findByNameAndOwner(name, owner.getUUID());
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.projects.orm.ProjectManager#findByOwner(org.nww.modules.users.orm.User)
	 */
	@Override
	public List<? extends Project> findByOwner(User owner) {
		if(null == owner) {
			return Collections.emptyList();
		}
		
		return getRepository().findAllByOwnerUUID(owner.getUUID());
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.projects.orm.ProjectManager#findByOwner(org.nww.modules.users.orm.User, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<? extends Project> findByOwner(User owner, Pageable p) {
		if(null == owner) {
			return null;
		}
		
		return getRepository().findAllByOwnerUUID(owner.getUUID(), p);
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.projects.orm.ProjectManager#findByOwnerUUID(java.lang.String)
	 */
	@Override
	public List<? extends Project> findByOwnerUUID(String ownerUUID) {
		if(StringUtils.isEmpty(ownerUUID)) {
			return Collections.emptyList();
		}
		
		return getRepository().findAllByOwnerUUID(ownerUUID);
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.projects.orm.ProjectManager#findByOwnerUUID(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<? extends Project> findByOwnerUUID(String ownerUUID, Pageable p) {
		if(StringUtils.isEmpty(ownerUUID)) {
			return new PageImpl<>(Collections.emptyList());
		}
		
		return getRepository().findAllByOwnerUUID(ownerUUID, p);
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.projects.orm.ProjectManager#findBySupplier(org.nww.modules.suppliers.orm.Supplier)
	 */
	@Override
	public List<? extends Project> findBySupplier(Supplier s) {
		if(null == s) {
			return Collections.emptyList();
		}
		
		return getRepository().findAllBySupplierUUID(s.getUUID());
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.projects.orm.ProjectManager#findBySupplier(org.nww.modules.suppliers.orm.Supplier, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<? extends Project> findBySupplier(Supplier s, Pageable p) {
		if(null == s) {
			return new PageImpl<>(Collections.emptyList());
		}
		
		return getRepository().findAllBySupplierUUID(s.getUUID(), p);
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.projects.orm.ProjectManager#findBySupplierUUID(java.lang.String)
	 */
	@Override
	public List<? extends Project> findBySupplierUUID(String supplierUUID) {
		if(StringUtils.isEmpty(supplierUUID)) {
			return Collections.emptyList();
		}
		
		return getRepository().findAllBySupplierUUID(supplierUUID);
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.projects.orm.ProjectManager#findBySupplierUUID(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<? extends Project> findBySupplierUUID(String supplierUUID, Pageable p) {
		if(StringUtils.isEmpty(supplierUUID)) {
			return new PageImpl<>(Collections.emptyList());
		}
		
		return getRepository().findAllBySupplierUUID(supplierUUID, p);
	}
}
