/**
 * 
 */
package org.nww.modules.projects.orm;

import org.nww.core.utils.ApplicationContextHelper;
import org.nww.modules.files.orm.FileInformation;
import org.nww.modules.files.orm.FileManager;
import org.springframework.data.annotation.Transient;

/**
 * POJO to store files related to a project.
 * @author mga
 */
public class ProjectFileData {
	private String fileInformationUUID;
	private String description;

	@Transient
	private FileInformation fileInformation;
	@Transient
	private FileManager fileMgr = ApplicationContextHelper.getBeanByClass(FileManager.class);
	
	/**
	 * Default constructor.
	 */
	public ProjectFileData() {
		super();
	}
	
	/**
	 * Create a new {@link ProjectFileData} object using the passed data.
	 * @param fileInformationUUID the file information UUID
	 * @param description the description
	 */
	public ProjectFileData(String fileInformationUUID, String description) {
		this.fileInformationUUID = fileInformationUUID;
		this.description = description;
	}
	
	/**
	 * @return the file information UUID
	 */
	public String getFileInformationUUID() {
		return fileInformationUUID;
	}
	
	/**
	 * @param fileInformationUUID the fileInformationUUID to set
	 */
	public void setFileInformationUUID(String fileInformationUUID) {
		this.fileInformationUUID = fileInformationUUID;
	}
	
	/**
	 * @return the file information
	 */
	public FileInformation getFileInformation() {
		if(null == this.fileInformation) {
			this.fileInformation = fileMgr.findOne(this.fileInformationUUID);
		}
		return this.fileInformation;
	}
	
	/**
	 * @param file information the file information to set
	 */
	public void setSupplier(FileInformation fileInformation) {
		this.fileInformation = fileInformation;
		this.fileInformationUUID = fileInformation.getUUID();
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
	 * @return true if there is a valid file information objectd attached
	 */
	public boolean hasFileInformation() {
		return null != getFileInformation();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if(null == this.fileInformationUUID || null == this.description) {
			return super.hashCode();
		}
		
		return this.fileInformationUUID.hashCode() ^ this.description.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof ProjectSupplierData) {
			ProjectFileData other = (ProjectFileData) o;
			return other.getFileInformation().equals(this.getFileInformation()) && other.getDescription().equals(this.getDescription());
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getFileInformation().getName() + " - " + getDescription();
	}
}
