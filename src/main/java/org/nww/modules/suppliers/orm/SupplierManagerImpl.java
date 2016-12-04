/**
 * 
 */
package org.nww.modules.suppliers.orm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.nww.core.data.ExtensiblePersistentObject;
import org.nww.core.system.AbstractPersistentObjectManager;
import org.nww.services.web.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @author mga
 *
 */
public class SupplierManagerImpl extends AbstractPersistentObjectManager<Supplier, SupplierRepository<Supplier>>
		implements SupplierManager {

	public static final String EXTENSION_OBJECT_KEY_SUPPLIER_IDS = "SupplierIDs";
	
	@Resource(name = "SupplierRepository")
	private SupplierRepository<Supplier> supplierRepo;
	
	@Resource(name = "EmailService")
	private EmailService emailService;
	
	/* (non-Javadoc)
	 * @see org.nww.core.system.PersistentObjectManager#getRepository()
	 */
	@Override
	public SupplierRepository<Supplier> getRepository() {
		return this.supplierRepo;
	}

	/* (non-Javadoc)
	 * @see org.nww.core.system.PersistentObjectManager#setRepository(org.nww.core.data.PersistentObjectRepository)
	 */
	@Override
	public void setRepository(SupplierRepository<Supplier> repository) {
		this.supplierRepo = repository;
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.suppliers.orm.SupplierManager#findAllByApprovalState(java.lang.Integer)
	 */
	@Override
	public List<? extends Supplier> findAllByApprovalState(Integer state) {
		return getRepository().findAllByApprovalState(state);
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.suppliers.orm.SupplierManager#findByApprovalState(java.lang.Integer, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<? extends Supplier> findByApprovalState(Integer state, Pageable paging) {
		return getRepository().findByApprovalState(state, paging);
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.suppliers.orm.SupplierManager#findPossibleDuplicates(org.nww.modules.suppliers.orm.Supplier)
	 */
	@Override
	public List<? extends Supplier> findPossibleDuplicates(Supplier ref) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.suppliers.orm.SupplierManager#mergeDuplicates(org.nww.modules.suppliers.orm.Supplier, java.util.List)
	 */
	@Override
	public void mergeDuplicates(Supplier ref, List<? extends Supplier> dups) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.nww.modules.suppliers.orm.SupplierManager#approve(org.nww.modules.suppliers.orm.Supplier)
	 */
	@Override
	public Supplier approve(Supplier toApprove) {
		toApprove.setApprovalStatus(Supplier.STATUS_APPROVED);
		
		return getRepository().save(toApprove);
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.suppliers.orm.SupplierManager#decline(org.nww.modules.suppliers.orm.Supplier)
	 */
	@Override
	public Supplier decline(Supplier toDecline) {
		toDecline.setApprovalStatus(Supplier.STATUS_NOT_APPROVED);
		return getRepository().save(toDecline);
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.suppliers.orm.SupplierManager#approve(java.util.List)
	 */
	@Override
	public List<Supplier> approve(List<? extends Supplier> toApprove) {
		List<Supplier> approved = new ArrayList<>();
		toApprove.stream().forEach(s -> approved.add(approve(s)));
		
		return approved;
	}

	/* (non-Javadoc)
	 * @see org.nww.modules.suppliers.orm.SupplierManager#decline(java.util.List)
	 */
	@Override
	public List<? extends Supplier> decline(List<? extends Supplier> toDecline) {
		List<Supplier> declined = new ArrayList<>();
		toDecline.stream().forEach(s -> declined.add(decline(s)));
		
		return declined;
	}

	/*
	 * (non-Javadoc)
	 * @see org.nww.modules.suppliers.orm.SupplierManager#findByName(java.lang.String)
	 */
	@Override
	public Supplier findByName(String name) {
		return getRepository().findByName(name.trim());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.modules.suppliers.orm.SupplierManager#createFilteredList(java.util.List, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page<? extends Supplier> createFilteredList(Iterable<? extends Supplier> unfilteredSuppliers,
			Integer approvalState, Integer page, Integer pageSize) {
		
		List<Supplier> filtered = new ArrayList<>();
		unfilteredSuppliers.forEach(s -> {
			if(s.getApprovalStatus().equals(approvalState)) {
				filtered.add(s);
			}
		});

		return new PageImpl<>(filtered, new PageRequest(page, pageSize), filtered.size());
	}

	/*
	 * (non-Javadoc)
	 * @see org.nww.modules.suppliers.orm.SupplierManager#getSuppliersFromUser(org.nww.modules.users.orm.User)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Supplier> getSuppliersFromExtensibleObject(ExtensiblePersistentObject o) {
		if(null == o) {
			return Collections.emptyList();
		}
		
		List<String> supplierIDs = o.getMultipleString(EXTENSION_OBJECT_KEY_SUPPLIER_IDS);
		
		if(null == supplierIDs) {
			return Collections.emptyList();
		}
		
		// null save implementation needed cause suppliers stored at a profile are currently not cleaned up when a supplier is beeing deleted
		List<Supplier> suppliers = new ArrayList<>();
		for (String id : supplierIDs) {
			Supplier s = findOne(id);
			if(null != s) {
				suppliers.add(s);
			}
		}
		
		return suppliers;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.modules.suppliers.orm.SupplierManager#setSuppliersForExtensibleObject(org.nww.core.data.ExtensiblePersistentObject, java.util.List)
	 */
	@Override
	public <T extends ExtensiblePersistentObject> T setSuppliersForExtensibleObject(T o,
			List<Supplier> suppliers) {
		
		o.setMultipleString(EXTENSION_OBJECT_KEY_SUPPLIER_IDS, suppliers.stream().map(s -> s.getUUID()).collect(Collectors.toList()));
		
		return o;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.modules.suppliers.orm.SupplierManager#createNew(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public Supplier createNew(String name, Integer approvalStatus, String description, String url, String imageUUID) {
		Supplier s = createNew();
		
		s.setName(name);
		s.setApprovalStatus(approvalStatus == null ? Supplier.STATUS_NOT_APPROVED : approvalStatus);
		s.setDescription(description);
		s.setHomepage(url);
		s.setLogoFileInformationUUID(imageUUID);
		
		save(s);

		if(s.getApprovalStatus() == Supplier.STATUS_NOT_APPROVED) {
			Map<String, Object> data = new HashMap<>();
			data.put("Supplier", s);
			
			emailService.sendMail(new String[] { "mirco.gatz@gmail.com" }, "info@netzwerkwohnen.org", "Neuer Lieferant", "suppliers/mail/newSupplierCreatedMail", data);
		}
		
		return s;
	}
}
