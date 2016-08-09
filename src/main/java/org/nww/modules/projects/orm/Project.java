/**
 * 
 */
package org.nww.modules.projects.orm;

import java.util.Date;
import java.util.List;

import org.nww.core.data.ExtensiblePersistentObject;
import org.nww.modules.files.orm.FileInformation;
import org.nww.modules.suppliers.orm.Supplier;
import org.nww.modules.users.orm.User;

/**
 * Defines methods for a project data POJO.
 * 
 * @author mga
 */
public interface Project extends ExtensiblePersistentObject {
	/**
	 * @return the name of the project
	 */
	public String getName();
	
	/**
	 * Set the name of the project.
	 * @param name the name
	 */
	public void setName(String name);
	
	/**
	 * @return the description of the project
	 */
	public String getDescription();
	
	/**
	 * Set the description of the project.
	 * @param description the description
	 */
	public void setDescription(String description);

	/**
	 * @return the project owner UUIDs
	 */
	public String getOwnerUUID();
	
	/**
	 * Set the owner UUID of the project.
	 * @param uuid the owner UUID
	 */
	public void setOwnerUUID(String uuid);

	/**
	 * @return the owner
	 */
	public User getOwner();
	
	/**
	 * Set the owner of this project.
	 * @param u the owner
	 */
	public void setOwner(User u);
	
	/**
	 * @return the list of participant user data
	 */
	public List<ProjectParticipantData> getParticipants();
	
	/**
	 * Set the list of participants.
	 * @param participants list of participant user data
	 */
	public void setParticipants(List<ProjectParticipantData> participants);
	
	/**
	 * Add a new project participant to the list.
	 * @param participantData the participant data
	 */
	public void addParticipant(ProjectParticipantData participantData);
	
	/**
	 * Remove a participating user from the project.
	 * @param u the user to remove
	 */
	public void removeParticipant(User u);
	
	/**
	 * @return the project start date
	 */
	public Date getStart();
	
	/**
	 * Set the project start date.
	 * @param start the project start date
	 */
	public void setStart(Date start);
	
	/**
	 * @return the project end date
	 */
	public Date getEnd();
	
	/**
	 * Set the project end date.
	 * @param end the end date
	 */
	public void setEnd(Date end);
	
	/**
	 * @return the project location
	 */
	public String getLocation();
	
	/**
	 * Set the project location.
	 * @param location the location
	 */
	public void setLocation(String location);
	
	/**
	 * @return the projects customer name
	 */
	public String getCustomer();
	
	/**
	 * Set the projects customer name.
	 * @param customer the customer name
	 */
	public void setCustomer(String customer);
	
	/**
	 * @return list of supplier data
	 */
	public List<ProjectSupplierData> getSuppliers();
	
	/**
	 * Set the list of supplier data.
	 * @param suppliers list of supplier data
	 */
	public void setSuppliers(List<ProjectSupplierData> suppliers);
	
	/**
	 * Add a new supplier data object to the internal list.
	 * @param supplier the object to be added
	 */
	public void addSupplier(ProjectSupplierData supplier);
	
	/**
	 * Remove a supplier data object from the internal list.
	 * @param supplier the object to be removed
	 */
	public void removeSupplier(Supplier s);
	
	/**
	 * @return list of gallery files
	 */
	public List<ProjectFileData> getImages();
	
	/**
	 * Set the list of image files.
	 * @param files list of file information objects
	 */
	public void setImages(List<ProjectFileData> files);
	
	/**
	 * Add a new file information object to the image list.
	 * @param file the file information object to be added
	 */
	public void addImageFile(ProjectFileData file);
	
	/**
	 * Remove a file information object from the image list.
	 * @param file the file information object to be removed
	 */
	public void removeImageFile(FileInformation file);

	/**
	 * @return true if there is a description given
	 */
	public boolean hasDescription();
	
	/**
	 * @return true if there is a customer information given
	 */
	public boolean hasCustomer();
	
	/**
	 * @return true if there is a location information given
	 */
	public boolean hasLocation();
	
	/**
	 * @return true if there is an end date configured
	 */
	public boolean hasEndDate();
	
	/**
	 * @return true if the project has at least one additional participant
	 */
	public boolean hasParticipants();
	
	/**
	 * @return true if the project has at least one supplier
	 */
	public boolean hasSuppliers();
	
	/**
	 * @return true if the project has at least one image
	 */
	public boolean hasImages();
}
