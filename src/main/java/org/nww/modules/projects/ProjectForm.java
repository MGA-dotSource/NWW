/**
 * 
 */
package org.nww.modules.projects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.nww.core.data.form.AbstractPersistentObjectForm;
import org.nww.modules.projects.orm.Project;
import org.nww.modules.projects.orm.ProjectFileData;
import org.nww.modules.projects.orm.ProjectParticipantData;
import org.nww.modules.projects.orm.ProjectSupplierData;
import org.nww.modules.projects.validation.UniqueName;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Provides form validation fields for {@link Project} instances.
 * @author mga
 */
@UniqueName
public class ProjectForm extends AbstractPersistentObjectForm {
	@Length(min = 1, max = 100)
	private String name;
	@Length(max = 5000)
	private String description;
	@NotNull
	@Length(max = 50)
	private String ownerUUID;
	@NotNull
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date start;
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date end;
	@Length(max = 100)
	private String location;
	@Length(max = 100)
	private String customer;

	private List<ProjectParticipantData> participants = new ArrayList<>();
	private List<ProjectSupplierData> suppliers = new ArrayList<>();
	private List<ProjectFileData> images = new ArrayList<>();
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the ownerUUID
	 */
	public String getOwnerUUID() {
		return ownerUUID;
	}
	
	/**
	 * @param ownerUUID the ownerUUID to set
	 */
	public void setOwnerUUID(String ownerUUID) {
		this.ownerUUID = ownerUUID;
	}
	
	/**
	 * @return the participants
	 */
	public List<ProjectParticipantData> getParticipants() {
		return participants;
	}
	
	/**
	 * @param participants the participants to set
	 */
	public void setParticipants(List<ProjectParticipantData> participants) {
		this.participants = participants;
	}
	
	/**
	 * @return the start
	 */
	public Date getStart() {
		return start;
	}
	
	/**
	 * @param start the start to set
	 */
	public void setStart(Date start) {
		this.start = start;
	}
	
	/**
	 * @return the end
	 */
	public Date getEnd() {
		return end;
	}
	
	/**
	 * @param end the end to set
	 */
	public void setEnd(Date end) {
		this.end = end;
	}
	
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
	/**
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
	}
	
	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	/**
	 * @return the suppliers
	 */
	public List<ProjectSupplierData> getSuppliers() {
		return suppliers;
	}
	
	/**
	 * @param suppliers the suppliers to set
	 */
	public void setSuppliers(List<ProjectSupplierData> suppliers) {
		this.suppliers = suppliers;
	}
	
	/**
	 * @return the images
	 */
	public List<ProjectFileData> getImages() {
		return images;
	}

	/**
	 * @param images images to set
	 */
	public void setImages(List<ProjectFileData> images) {
		this.images = images;
	}
}
