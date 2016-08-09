/**
 * 
 */
package org.nww.config;

import org.nww.core.utils.ApplicationContextHelper;
import org.nww.core.utils.DefaultLocaleHelper;
import org.nww.core.utils.LocaleHelper;
import org.nww.modules.files.orm.FileInformation;
import org.nww.modules.files.orm.FileInformationRepository;
import org.nww.modules.files.orm.FileManager;
import org.nww.modules.files.orm.FileManagerImpl;
import org.nww.modules.files.orm.MongoFileInformationRepository;
import org.nww.modules.folders.orm.Folder;
import org.nww.modules.folders.orm.FolderRepository;
import org.nww.modules.folders.orm.MongoFolderRepository;
import org.nww.modules.messaging.MailFormDataMapper;
import org.nww.modules.messaging.orm.Mail;
import org.nww.modules.messaging.orm.MailManager;
import org.nww.modules.messaging.orm.MailManagerImpl;
import org.nww.modules.messaging.orm.MailRepository;
import org.nww.modules.messaging.orm.MongoMailRepository;
import org.nww.modules.pinboard.PinboardEntryFormDataMapper;
import org.nww.modules.pinboard.orm.MongoPinboardEntryRepository;
import org.nww.modules.pinboard.orm.PinboardEntry;
import org.nww.modules.pinboard.orm.PinboardEntryRepository;
import org.nww.modules.pinboard.orm.PinboardManager;
import org.nww.modules.pinboard.orm.PinboardManagerImpl;
import org.nww.modules.preferences.orm.MongoPreferenceValueRepository;
import org.nww.modules.preferences.orm.PreferenceManager;
import org.nww.modules.preferences.orm.PreferenceManagerImpl;
import org.nww.modules.preferences.orm.PreferenceValue;
import org.nww.modules.preferences.orm.PreferenceValueRepository;
import org.nww.modules.profiles.ProfileFormDataMapper;
import org.nww.modules.profiles.SkillsFormDataMapper;
import org.nww.modules.profiles.orm.MongoProfileRepository;
import org.nww.modules.profiles.orm.Profile;
import org.nww.modules.profiles.orm.ProfileManager;
import org.nww.modules.profiles.orm.ProfileManagerImpl;
import org.nww.modules.profiles.orm.ProfileRepository;
import org.nww.modules.projects.ProjectFormDataMapper;
import org.nww.modules.projects.orm.MongoProjectRepository;
import org.nww.modules.projects.orm.Project;
import org.nww.modules.projects.orm.ProjectManager;
import org.nww.modules.projects.orm.ProjectManagerImpl;
import org.nww.modules.projects.orm.ProjectRepository;
import org.nww.modules.projects.validation.UniqueNameValidator;
import org.nww.modules.suppliers.SupplierFormDataMapper;
import org.nww.modules.suppliers.UserSuppliersFormDataMapper;
import org.nww.modules.suppliers.orm.MongoSupplierRepository;
import org.nww.modules.suppliers.orm.Supplier;
import org.nww.modules.suppliers.orm.SupplierManager;
import org.nww.modules.suppliers.orm.SupplierManagerImpl;
import org.nww.modules.suppliers.orm.SupplierRepository;
import org.nww.modules.users.CredentialsFormDataMapper;
import org.nww.modules.users.orm.MongoUserRepository;
import org.nww.modules.users.orm.User;
import org.nww.modules.users.orm.UserManager;
import org.nww.modules.users.orm.UserManagerImpl;
import org.nww.modules.users.orm.UserRepository;
import org.nww.services.TemplateRenderService;
import org.nww.services.TemplateRenderServiceImpl;
import org.nww.services.files.ImageFileService;
import org.nww.services.files.ImageFileServiceImpl;
import org.nww.services.web.EmailService;
import org.nww.services.web.EmailServiceImpl;
import org.nww.services.web.HTMLService;
import org.nww.services.web.JSoupHTMLServiceImpl;
import org.nww.services.web.URLUtilsService;
import org.nww.services.web.URLUtilsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * The bean setup class for this application.
 * 
 * @author MGA
 *
 */
@Configuration
public class ApplicationBeanConfig {
	
	@Value("${nww.resources.messages.cachetime}")
	private int resourceBundleCacheTime;
	
	/*********************************
	 * FIX for not trimmed supplier names runs only once after deployment
	 */
	//@Bean
	//public SupplierNamesUpdateScheduler setupSupplierNamesUpdateScheduler() {
	//	return new SupplierNamesUpdateScheduler();
	//}
	
	/*************************************************\
	 * HELPER
	\*************************************************/
	
	/**
	 * @return the application context helper
	 */
	@Bean(name = "ApplicationContextHelper")
	public ApplicationContextHelper setupApplicationContextHelper() {
		return new ApplicationContextHelper();
	}
	
	/***
	 * @return the locale helper
	 */
	@Bean(name = "LocaleHelper")
	public LocaleHelper setupLocalHelper() {
		return new DefaultLocaleHelper();
	}
	
	@Bean(name = "messageSource")
	public ReloadableResourceBundleMessageSource setupLocalizationResourceBundle() {
		ReloadableResourceBundleMessageSource r = new ReloadableResourceBundleMessageSource();
		r.setBasename("classpath:localization/folderNames");
		r.setCacheSeconds(resourceBundleCacheTime);
		
		return r;
	}
	
	/*************************************************\
	 * EOF HELPER
	\*************************************************/

	/*************************************************\
	 * VALIDATION
	\*************************************************/
	
	/**
	 * @return the unique name validator for the project models
	 */
	@Bean
	public UniqueNameValidator uniqueNameValidator() {
		return new UniqueNameValidator();
	}
	
	/*************************************************\
	 * EOF VALIDATION
	\*************************************************/
	
	/*************************************************\
	 * REPOSITORIES
	\*************************************************/
	
	/**
	 * @return the user repository
	 */
	@Bean(name = "UserRepository")
	public UserRepository<? extends User> setupUserRepository() {
		return new MongoUserRepository();
	}
	/**
	 * @return the profile repository
	 */
	@Bean(name = "ProfileRepository")
	public ProfileRepository<? extends Profile> setupProfileRepository() {
		return new MongoProfileRepository();
	}
	
	/**
	 * @return the file information repository
	 */
	@Bean(name = "FileInformationRepository")
	public FileInformationRepository<? extends FileInformation> setupFileInformationRepository() {
		return new MongoFileInformationRepository();
	}
	
	/**
	 * @return the mail repository
	 */
	@Bean(name = "MailRepository")
	public MailRepository<? extends Mail> setupMailRepository() {
		return new MongoMailRepository();
	}
	
	/**
	 * @return the folder repository
	 */
	@Bean(name = "FolderRepository")
	public FolderRepository<? extends Folder> setupFolderRepository() {
		return new MongoFolderRepository();
	}
	
	/**
	 * @return the pinboard entry repository
	 */
	@Bean(name = "PinboardEntryRepository")
	public PinboardEntryRepository<? extends PinboardEntry> setupPinboardEntryRepository() {
		return new MongoPinboardEntryRepository();
	}
	
	/**
	 * @return the preference value repository
	 */
	@Bean(name = "PreferenceValueRepository")
	public PreferenceValueRepository<? extends PreferenceValue> setupPreferenceValueRepository() {
		return new MongoPreferenceValueRepository();
	}
	
	/**
	 * @return the supplier repository
	 */
	@Bean(name = "SupplierRepository")
	public SupplierRepository<? extends Supplier> setupSupplierRepository() {
		return new MongoSupplierRepository();
	}
	
	/**
	 * @return the project repository
	 */
	@Bean(name = "ProjectRepository")
	public ProjectRepository<? extends Project> setupProjectRepository() {
		return new MongoProjectRepository();
	}
	
	/*************************************************\
	 * EOF REPOSITORIES
	\*************************************************/
	
	/*************************************************\
	 * MANAGERS
	\*************************************************/
	
	/**
	 * @return the user manager
	 */
	@Bean(name = "UserManager")
	public UserManager setupUserManager() {
		return new UserManagerImpl();
	}
	
	/**
	 * @return the profile manager
	 */
	@Bean(name = "ProfileManager")
	public ProfileManager setupProfileManager() {
		return new ProfileManagerImpl();
	}
	
	/**
	 * @return the file manager
	 */
	@Bean(name = "FileManager")
	public FileManager setupFileManager() {
		return new FileManagerImpl();
	}
	
	/**
	 * @return the mail manager
	 */
	@Bean(name = "MailManager")
	public MailManager setupMailManager() {
		return new MailManagerImpl();
	}
	
	/**
	 * @return the pinboard entry manager
	 */
	@Bean(name = "PinboardManager")
	public PinboardManager setupPinboardEntryManager() {
		return new PinboardManagerImpl();
	}
	
	/**
	 * @return the preference manager
	 */
	@Bean(name = "PreferenceManager")
	public PreferenceManager setupPreferenceManager() {
		return new PreferenceManagerImpl();
	}
	
	/**
	 * @return the supplier manager
	 */
	@Bean(name = "SupplierManager")
	public SupplierManager setupSupplierManager() {
		return new SupplierManagerImpl();
	}
	
	/**
	 * @return the project manager
	 */
	@Bean(name = "ProjectManager")
	public ProjectManager setupProjectManager() {
		return new ProjectManagerImpl();
	}
	
	/*************************************************\
	 * EOF MANAGERS
	\*************************************************/
	
	/*************************************************\
	 * SERVICES
	\*************************************************/
	
	/**
	 * @return the email service
	 */
	@Bean(name = "EmailService")
	public EmailService setupEmailService() {
		return new EmailServiceImpl();
	}
	
	/**
	 * @return the template render service
	 */
	@Bean(name = "TemplateRenderService")
	public TemplateRenderService setupTemplateRenderService() {
		return new TemplateRenderServiceImpl();
	}
	
	/**
	 * @return the image file service
	 */
	@Bean(name = "ImageFileService")
	public ImageFileService setupImageFileService() {
		return new ImageFileServiceImpl();
	}
	
	/**
	 * @return the HTML service
	 */
	@Bean(name = "HTMLService")
	public HTMLService setupHTMLService() {
		return new JSoupHTMLServiceImpl();
	}
	
	/**
	 * @return the URL utils service
	 */
	@Bean(name = "URLUtilsService")
	public URLUtilsService setupURLService() {
		return new URLUtilsServiceImpl();
	}
	
	/*************************************************\
	 * EOF SERVICES
	\*************************************************/
	
	/*************************************************\
	 * FORM DATA MAPPERS
	\*************************************************/
	/**
	 * @return the credentials form data mapper
	 */
	@Bean(name = "CredentialsFormDataMapper")
	public CredentialsFormDataMapper setupCrededentialsFormDataMapper() {
		return new CredentialsFormDataMapper();
	}
	
	/**
	 * @return the profile form data mapper
	 */
	@Bean(name = "ProfileFormDataMapper")
	public ProfileFormDataMapper setupProfileFormDataMapper() {
		return new ProfileFormDataMapper();
	}
	
	/**
	 * @return the skills form data mapper
	 */
	@Bean(name = "SkillsFormDataMapper")
	public SkillsFormDataMapper setupSkillsFormDataMapper() {
		return new SkillsFormDataMapper();
	}
	
	/**
	 * @return the mail form data mapper
	 */
	@Bean(name = "MailFormDataMapper")
	public MailFormDataMapper setupMailFormDataMapper() {
		return new MailFormDataMapper();
	}
	
	/**
	 * @return the pinboard entry form data mapper
	 */
	@Bean(name = "PinboardEntryFormDataMapper")
	public PinboardEntryFormDataMapper setupPinboardEntryFormDataMapper() {
		return new PinboardEntryFormDataMapper();
	}
	
	/**
	 * @return the supplier form data mapper
	 */
	@Bean(name = "SupplierFormDataMapper")
	public SupplierFormDataMapper setupSupplierFormDataMapper() {
		return new SupplierFormDataMapper();
	}
	
	/**
	 * @return the user supplier form data mapper
	 */
	@Bean(name = "UserSuppliersFormDataMapper")
	public UserSuppliersFormDataMapper setupUserSuppliersFormDataMapper() {
		return new UserSuppliersFormDataMapper();
	}
	
	/**
	 * @return the project form data mapper
	 */
	@Bean(name = "ProjectFormDataMapper")
	public ProjectFormDataMapper setupProjectFormDataMapper() {
		return new ProjectFormDataMapper();
	}
	
	/*************************************************\
	 * EOF FORM DATA MAPPERS
	\*************************************************/
}
