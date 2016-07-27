/**
 * 
 */
package org.nww.app.errors;

import java.util.concurrent.CompletionStage;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * This additional component configures the embedded tomcat to pass 404 errors to the application
 * to be handled.
 * 
 * The error path handler inside the application has some limitations: e.g. returning a {@link CompletionStage} is not possible as the tomcat cannot handle it correctly.
 *  
 * @author mga
 */
@Component
public class ErrorConfiguration implements EmbeddedServletContainerCustomizer {

	/* (non-Javadoc)
	 * @see org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer#customize(org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer)
	 */
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, NetzwerkWohnenErrorController.ERROR_PATH_404));
	}
}
