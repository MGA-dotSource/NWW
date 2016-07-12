/**
 * 
 */
package org.nww.modules.files.orm;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.nww.core.system.PersistentObjectManager;

/**
 * The file manager provides basic operations to handle files and file informations for the application.
 * @author mga
 *
 */
public interface FileManager extends PersistentObjectManager<FileInformation, FileInformationRepository<FileInformation>> {
	
	/**
	 * @return the file system root path
	 */
	public String getRoot();
	
	/**
	 * @return the file system temporary path
	 */
	public String getTemp();
	
	// TODO: cleanup temp files after a 2 or 3 days
	
	/**
	 * Find the file information for the passed local path.
	 * @param localPath the local path
	 * @param name the virtual file name
	 * @return the {@link FileInformation} object or null if none is found
	 */
	public FileInformation findByLocalPath(String localPath, String name);
	
	/**
	 * Get the file path for the passed file information.
	 * @param fi the file information object
	 * @return the path or null if none is found
	 */
	public Path getFilePath(FileInformation fi);
	
	/**
	 * Get the file path for the passed localPath
	 * @param localPath the local path
	 * @param name the virtual file name
	 * @return the path or null if none is found
	 */
	public Path getFilePath(String localPath, String name);
	
	/**
	 * Get the file for the passed file information.
	 * @param fi the file information object
	 * @return the file or null if none is found
	 */
	public File getFile(FileInformation fi);
	
	/**
	 * Get the file for the passed local path.
	 * @param localPath the local path
	 * @param name the virtual name of the file
	 * @return the file or null if none is found
	 */
	public File getFile(String localPath, String name);
	
	/**
	 * Checks the existence of the file for the passed file information.
	 * @param fi the file information
	 * @return true if the file exists
	 */
	public boolean existsFile(FileInformation fi);
	
	/**
	 * Checks the existence of the file for the passed local path.
	 * @param localPath the local path
	 * @param name the virtual name of the file
	 * @return true if the file exists
	 */
	public boolean existsFile(String localPath, String name);
	
	/**
	 * Save the passed file content to disk. Will save the file into the root of the 
	 * configured file system structure.
	 * @param fileContent the file content to be saved
	 * @param contentType the mime type
	 * @return the created file information object
	 */
	public FileInformation saveFile(byte[] fileContent, String contentType);
	
	/**
	 * Save the passed file content to disk. Will save the fill inside the passed folder path
	 * relative to the root of the configured file system structure.
	 * @param folderPath the folder path to create the file in
	 * @param fileContent the file content to be saved
	 * @param contentType the mime type
	 * @return
	 */
	public FileInformation saveFile(String folderPath, byte[] fileContent, String contentType);
	
	/**
	 * Save the passed file content to disk. Will save the file inside the passed folder path
	 * relative to the root of the configured file system structure.
	 * @param name the name of the file only used for the file information object
	 * @param folderPath the folder path to create the file in
	 * @param fileContent the file content to be saved
	 * @param contentType the mime type
	 * @return the created file information object
	 */
	public FileInformation saveFile(String name, String folderPath, byte[] fileContent, String contentType);
	
	/**
	 * Save the passed file content to disk. Will save the file inside the passed folder path
	 * relative to the root of the configured file system structure.
	 * @param name the name of the file only used for the file information object
	 * @param originalName the original name of the file before uploading / saving it
	 * @param folderPath the folder path to create the file in
	 * @param fileContent the file content to be saved
	 * @param contentType the mime type
	 * @return the created file information object
	 */
	public FileInformation saveFile(String name, String originalName, String folderPath, byte[] fileContent, String contentType);
	
	/**
	 * Will copy the file described by the source object to the target path.
	 * If both describe the same file location nothing will be copied and the original file information will be returned.
	 * @param source the source information object
	 * @param targetFolderPath the target folder path
	 * @return the new created file information
	 */
	public FileInformation copyFile(FileInformation source, String targetFolderPath);
	
	/**
	 * Will copy the file described by the source object to the target path whilst renaming it.
	 * @param source the source information object
	 * @param targetName the target name
	 * @param targetFolderPath the target folder path
	 * @return the new created file information
	 */
	public FileInformation copyFile(FileInformation source, String targetName, String targetFolderPath);
	
	/**
	 * Rename the file described by the source information. Will only update the file information. Will not rename the file on disk.
	 * @param source the source information object
	 * @param targetName the target name
	 * @return the updated file information
	 */
	public boolean renameFile(FileInformation source, String targetName);
	
	/**
	 * Move the file described by the source information to the target path.
	 * @param source the source information object
	 * @param targetFolderPath the target folder path
	 * @return the updated file information
	 */
	public FileInformation moveFile(FileInformation source, String targetFolderPath);
	
	/**
	 * Move the file described by the source information object to the target path whilst renaming it.
	 * @param source the source information object
	 * @param targetName the target name
	 * @param targetFolderPath the target folder path
	 * @return the updated file information
	 */
	public FileInformation moveFile(FileInformation source, String targetName, String targetFolderPath);
	
	/**
	 * Delete the file described by the file information.
	 * @param fi the file information
	 * @return true if the file was deleted
	 */
	public boolean deleteFile(FileInformation fi);
	
	/**
	 * Get a list of paths describing all files of a directory.
	 * @param folderPath the path of the directory to find files in
	 * @return list of file information objects
	 */
	public List<? extends FileInformation> getFilesByDirectoryPath(String folderPath);
	
	/**
	 * Generate the part of the download URL that depends on the file information.
	 * @param fiUUID the uuid of the file information object
	 * @return the part of the download URL that depends on the file information always starting with a slash
	 */
	public String createDownloadUrlSegment(String fiUUID);
	
	/**
	 * Generate the part of the download URL that depends on the file information.
	 * @param fi the file information object
	 * @return the part of the download URL that depends on the file information always starting with a slash
	 */
	public String createDownloadUrlSegment(FileInformation fi);

	/**
	 * Creates an absolute download URL for the file with the passed file information UUID.
	 * Will use the property configured host name.
	 * @param fiUUID the file information UUID.
	 * @return the download URL
	 */
	public String createAbsoluteDownloadUrl(String fiUUID);
	
	/**
	 * Creates an absolute download URL for the passed file information object.
	 * @param fi the file information object
	 * @return the download URL
	 */
	public String createAbsoluteDownloadUrl(FileInformation fi);
}
