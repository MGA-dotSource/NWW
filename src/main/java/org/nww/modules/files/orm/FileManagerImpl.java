/**
 * 
 */
package org.nww.modules.files.orm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.nww.core.system.AbstractPersistentObjectManager;
import org.nww.core.system.OperationResult.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

/**
 * @author mga
 *
 */
public class FileManagerImpl
		extends AbstractPersistentObjectManager<FileInformation, FileInformationRepository<FileInformation>>
		implements FileManager {

	@Resource(name = "FileInformationRepository")
	private FileInformationRepository<FileInformation> fileInformationRepository;
	
	@Value("${nww.vfs.root}")
	private String nwwVfsRootPropertyValue;
	
	@Value("${nww.host}")
	private String hostNamePropertyValue;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@PostConstruct
	private void checkRootPathExists() {
		Path root = Paths.get(getRoot());
		
		if(!Files.exists(root)) {
			log.debug("VFS root path does not exist - trying to create!");
			try {
				Files.createDirectories(root);
			} catch (IOException e) {
				log.error("Could not create VFS root path '" + root + "'", e);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.nww.core.system.PersistentObjectManager#getRepository()
	 */
	@Override
	public FileInformationRepository<FileInformation> getRepository() {
		return this.fileInformationRepository;
	}

	/* (non-Javadoc)
	 * @see org.nww.core.system.PersistentObjectManager#setRepository(org.nww.core.data.PersistentObjectRepository)
	 */
	@Override
	public void setRepository(FileInformationRepository<FileInformation> repository) {
		this.fileInformationRepository = repository;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#getRoot()
	 */
	@Override
	public String getRoot() {
		return this.nwwVfsRootPropertyValue;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#findByLocalPath(java.lang.String)
	 */
	@Override
	public FileInformation findByLocalPath(String localPath, String name) {
		return getRepository().findByLocalPathAndName(localPath, name);
	};

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#getFilePath(org.nww.orm.files.FileInformation)
	 */
	@Override
	public Path getFilePath(FileInformation fi) {
		return fi != null ? Paths.get(getRoot(), fi.getLocalPath(), fi.getName()) : null;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#getFilePathByLocalPath(java.lang.String)
	 */
	@Override
	public Path getFilePath(String localPath, String name) {
		FileInformation fi = findByLocalPath(localPath, name);
		
		return null != fi ? Paths.get(getRoot(), fi.getLocalPath()) : null;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#getFile(org.nww.orm.files.FileInformation)
	 */
	@Override
	public File getFile(FileInformation fi) {
		Path p = getFilePath(fi);
		
		return null != p ? p.toFile() : null;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#getFileByLocalPath(java.lang.String)
	 */
	@Override
	public File getFile(String localPath, String name) {
		FileInformation fi = findByLocalPath(localPath, name);
		
		return null != fi ? getFile(fi) : null;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#existsFile(org.nww.orm.files.FileInformation)
	 */
	@Override
	public boolean existsFile(FileInformation fi) {
		return Files.exists(getFilePath(fi));
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#existsFileByLocalPath(java.lang.String)
	 */
	@Override
	public boolean existsFile(String localPath, String name) {
		FileInformation fi = findByLocalPath(localPath, name);
		
		return null != fi ? existsFile(fi) : false;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#saveFile(byte[], java.lang.String)
	 */
	@Override
	public FileInformation saveFile(byte[] fileContent, String contentType) {
		return saveFile("", "", "", fileContent, contentType);
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#saveFile(java.lang.String, byte[], java.lang.String)
	 */
	@Override
	public FileInformation saveFile(String folderPath, byte[] fileContent, String contentType) {
		return saveFile("", "", folderPath, fileContent, contentType);
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#saveFile(java.lang.String, java.lang.String, byte[], java.lang.String)
	 */
	@Override
	public FileInformation saveFile(String name, String folderPath, byte[] fileContent, String contentType) {
		return saveFile(name, "", folderPath, fileContent, contentType);
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#saveFile(java.lang.String, java.lang.String, java.lang.String, byte[], java.lang.String)
	 */
	@Override
	public FileInformation saveFile(String name, String originalName, String folderPath, byte[] fileContent, String contentType) {
		// prevent file overriding
		if(existsFile(folderPath, name)) {
			return null;
		}
		
		// first generate empty FileInformation object to gather a UUID
		FileInformation fi = (FileInformation) save(createNew()).getAffectedObject();
		
		// now set values / or fallbacks
		fi.setName(!StringUtils.isEmpty(name) ? name : fi.getUUID());
		fi.setOriginalName(originalName);
		fi.setLocalPath(!StringUtils.isEmpty(folderPath) ? folderPath : "/");
		fi.setSize(fileContent.length);
		fi.setContentType(contentType);
		
		if(save(fi).getResultState() == State.SUCCESSFULL) {
			Path absoluteDirectory = Paths.get(getRoot(), fi.getLocalPath());
			Path absoluteFilePath = Paths.get(getRoot(), fi.getLocalPath(), fi.getUUID());
			try {
				Files.createDirectories(absoluteDirectory);
				Files.write(absoluteFilePath, fileContent);
				return fi;
			} catch (IOException e) {
				log.error("Could not write file '" + absoluteFilePath + "'", e);
			}
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#copyFile(org.nww.orm.files.FileInformation, java.lang.String)
	 */
	@Override
	public FileInformation copyFile(FileInformation source, String targetFolderPath) {
		return copyFile(source, source.getName(), targetFolderPath);
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#copyFile(org.nww.orm.files.FileInformation, java.lang.String, java.lang.String)
	 */
	@Override
	public FileInformation copyFile(FileInformation source, String targetName, String targetFolderPath) {
		boolean isExisting = existsFile(targetFolderPath, source.getName());
		
		if(!isExisting) {
			FileInformation copiedFi = (FileInformation) save(createNew()).getAffectedObject();
			copiedFi.setName(targetName);
			copiedFi.setLocalPath(targetFolderPath);
			copiedFi.setSize(source.getSize());
			copiedFi.setDescription(source.getDescription());
			copiedFi.setOriginalName(source.getOriginalName());
			
			if(save(copiedFi).getResultState() == State.SUCCESSFULL) {
				try {
					Files.copy(getFilePath(source), getFilePath(copiedFi));
					return copiedFi;
				} catch (IOException e) {
					log.error("Could not copy file!", e);
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#renameFile(org.nww.orm.files.FileInformation, java.lang.String)
	 */
	@Override
	public boolean renameFile(FileInformation source, String targetName) {
		// check whether new name already exists
		boolean isExisting = null != getRepository().findByLocalPathAndName(source.getLocalPath(), targetName);
		
		if(!isExisting) {
			source.setName(targetName);
			
			return save(source).getResultState() == State.SUCCESSFULL;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#moveFile(org.nww.orm.files.FileInformation, java.lang.String)
	 */
	@Override
	public FileInformation moveFile(FileInformation source, String targetFolderPath) {
		return moveFile(source, source.getName(), targetFolderPath);
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#moveFile(org.nww.orm.files.FileInformation, java.lang.String, java.lang.String)
	 */
	@Override
	public FileInformation moveFile(FileInformation source, String targetName, String targetFolderPath) {
		boolean isExisting = null != getRepository().findByLocalPathAndName(targetFolderPath, targetName);
		
		if(!isExisting) {
			source.setLocalPath(targetFolderPath);
			source.setName(targetName);
			if(save(source).getResultState() == State.SUCCESSFULL) {
				return source;
			}
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#deleteFile(org.nww.orm.files.FileInformation)
	 */
	@Override
	public boolean deleteFile(FileInformation fi) {
		Path p = getFilePath(fi);
		
		if(null != p) {
			try {
				delete(fi);
				return Files.deleteIfExists(p);
			} catch (IOException e) {
				log.error("Could not delete file '" + p + "'", e);
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#getFilesByDirectoryPath(java.lang.String)
	 */
	@Override
	public List<? extends FileInformation> getFilesByDirectoryPath(String folderPath) {
		return getRepository().findByLocalPath(folderPath);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#createDownloadUrlSegment(java.lang.String)
	 */
	@Override
	public String createDownloadUrlSegment(String fiUUID) {
		FileInformation fi = findOne(fiUUID);
		
		return null != fi ? createDownloadUrlSegment(fi) : "";
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.orm.files.FileManager#createDownloadUrlSegment(org.nww.orm.files.FileInformation)
	 */
	@Override
	public String createDownloadUrlSegment(FileInformation fi) {
		return "/" + fi.getLocalPath() + "/" + fi.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see org.nww.modules.files.orm.FileManager#createAbsoluteDownloadUrl(java.lang.String)
	 */
	@Override
	public String createAbsoluteDownloadUrl(String fiUUID) {
		FileInformation fi = findOne(fiUUID);
		
		return null != fi ? createAbsoluteDownloadUrl(fi) : "";
	}

	/*
	 * (non-Javadoc)
	 * @see org.nww.modules.files.orm.FileManager#createAbsoluteDownloadUrl(org.nww.modules.files.orm.FileInformation)
	 */
	@Override
	public String createAbsoluteDownloadUrl(FileInformation fi) {
		return hostNamePropertyValue + "/files" + createDownloadUrlSegment(fi);
	}
}
