/**
 * 
 */
package org.nww.modules.files;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nww.modules.files.orm.FileInformation;
import org.nww.modules.files.orm.FileManager;
import org.nww.services.files.ImageFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author mga
 *
 */
@Controller
@RequestMapping(value = "/files")
public class FileController extends AbstractController {
	
	@Resource(name = "FileManager")
	private FileManager fileMgr;
	
	@Resource(name = "ImageFileService")
	private ImageFileService imageFileService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * @return the current file manager instance
	 */
	public FileManager getFileMgr() {
		return fileMgr;
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.error("Method called but shouldn't!");
		return null;
	}

	@RequestMapping(value = "/**", method = { RequestMethod.GET })
	@ResponseBody
	public FileSystemResource handleFileRequest(HttpServletRequest request, Model model) throws MalformedURLException, FileNotFoundException {
		String path = request.getRequestURI();
		String localPath = path.substring(0, path.lastIndexOf("/")).replaceAll("/files/", "");
		
		String filename = path.substring(path.lastIndexOf("/") + 1);
		FileRequestInformation fri = new FileRequestInformation(filename);
		
		FileInformation fi = getFileMgr().findByLocalPath(localPath, fri.getPurName());
		
		if(null != fi) {
			// get possible resized image
			fi = getOrCreateResizedImage(fri, fi);
			File f = getFileMgr().getFile(fi);
			
			return new FileSystemResource(f);
		}
		
		log.error("Could not find file for URL '" + path + "'");
		
		return null;
	}
	
	@RequestMapping(value = "/upload.do", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public FileUploadResult handleAJAXFileUpload(@RequestParam(name = "file", required = true) MultipartFile file, Model model) {
		if(!file.isEmpty()) {
			FileInformation fi = null;
			
			try {
				fi = fileMgr.saveFile(fileMgr.getShared(), file.getBytes(), file.getContentType());

				return new FileUploadResult(true, fileMgr.createAbsoluteDownloadUrl(fi));
			} catch (IOException e) {
				log.error("Could not save user profile image: ", e);
			}
		}
		
		return new FileUploadResult(false, null);
	}

	private FileInformation getOrCreateResizedImage(FileRequestInformation fri, FileInformation purFileInfo) {
		
		// files in temp will never be resized
		if(fileMgr.isFileInTemp(purFileInfo)) {
			return purFileInfo;
		}
		
		if(imageFileService.isImageFile(purFileInfo) && fri.getAttributeCount() >= 1) {
			
			// check resized image already exists
			String resizedFileAttributeName = createSizedImageAttributeName(fri);
			if(purFileInfo.hasAttribute(resizedFileAttributeName)) {
				return fileMgr.findOne(purFileInfo.getString(resizedFileAttributeName));
			}
			else {
				// create resized image
				if(fri.getAttributeCount() == 1) {
					return createResizedImage(Integer.parseInt(fri.getAttributes()[0]), 0, purFileInfo, fri);
				}
				else {
					return createResizedImage(Integer.parseInt(fri.getAttributes()[0]), Integer.parseInt(fri.getAttributes()[1]), purFileInfo, fri);
				}
			}
		}
		
		return purFileInfo;
	}

	private String createSizedImageAttributeName(FileRequestInformation fri) {
		String resizedFileAttributeName = fri.getAttributeCount() > 1 ? 
				"resized_" + fri.getAttributes()[0] + "_" + fri.getAttributes()[1]
						: "resized_" + fri.getAttributes()[0];
		return resizedFileAttributeName;
	}
	
	/**
	 * Create a resized image using either both width and height or width only (set height to zero) for a scaled image.
	 * @param width the max width of the resized image
	 * @param height the max height of the resized image, pass 0 (zero) to force image scaling by dimension
	 * @param orig the origial file information object
	 * @param fri the file request information object
	 * @return the resized files {@link FileInformation} object or the same as the original to handle possibly failed resize actions
	 */
	private FileInformation createResizedImage(int width, int height, FileInformation orig, FileRequestInformation fri) {
		FileInformation resizedOrig = orig; // just for safety if something goes wrong 
		
		try {
			// resize either using width and height or width only as a total max dimension value
			BufferedImage resizedImage = height > 0 ? 
					imageFileService.resizeImage(imageFileService.getImageFromFile(getFileMgr().getFile(orig)), 
							width, height)
					: imageFileService.resizeImage(imageFileService.getImageFromFile(getFileMgr().getFile(orig)), width);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(resizedImage, ImageIO.getImageReadersByMIMEType(orig.getContentType()).next().getFormatName() , baos);
			resizedOrig = getFileMgr().saveFile(orig.getLocalPath(), baos.toByteArray(), orig.getContentType());
			
			// update original file info
			orig.setString(createSizedImageAttributeName(fri), resizedOrig.getUUID());
			getFileMgr().save(orig);
		} catch (IOException e) {
			log.error("Could not load and resize image from file: " + orig.getLocalPath());
		}
		
		return resizedOrig;
	}
}
