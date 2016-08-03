/**
 * 
 */
package org.nww.modules.suppliers;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.nww.core.data.form.AbstractPersistentObjectForm;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author mga
 *
 */
public class SupplierForm extends AbstractPersistentObjectForm {
	@NotNull
	private Integer status = 0;
	@Length(max = 50)
	private String name;
	@Length(max = 250)
	private String description;
	@Length(max = 250)
	@URL
	private String url;

	private MultipartFile file;
	
	/**
	 * Set the status.
	 * @param status the status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * @return the file
	 */
	public MultipartFile getFile() {
		return file;
	}
	
	/**
	 * @param file the file to set
	 */
	public void setFile(MultipartFile file) {
		this.file = file;
	}
}
