/**
 * 
 */
package org.nww.modules.files.orm;

import org.nww.core.data.AbstractExtensiblePersistentObject;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mga
 *
 */
@Document(collection = "files")
public class FileInformationImpl extends AbstractExtensiblePersistentObject implements FileInformation {
	private String name;
	private String originalName;
	private String localPath;
	private long size;
	private String description;
	private String contentType;
	
	/* (non-Javadoc)
	 * @see org.nww.orm.files.File#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.File#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.File#getOriginalName()
	 */
	@Override
	public String getOriginalName() {
		return this.originalName;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.File#setOriginalName(java.lang.String)
	 */
	@Override
	public void setOriginalName(String name) {
		this.originalName = name;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.File#getLocalPath()
	 */
	@Override
	public String getLocalPath() {
		return this.localPath;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.File#setLocalPath(java.lang.String)
	 */
	@Override
	public void setLocalPath(String path) {
		this.localPath = path;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.File#getSize()
	 */
	@Override
	public long getSize() {
		return this.size;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.File#setSize(long)
	 */
	@Override
	public void setSize(long sizeInByte) {
		this.size = sizeInByte;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.File#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.File#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * @see org.nww.orm.files.FileInformation#getContentType()
	 */
	@Override
	public String getContentType() {
		return this.contentType;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.orm.files.FileInformation#setContentType(java.lang.String)
	 */
	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
