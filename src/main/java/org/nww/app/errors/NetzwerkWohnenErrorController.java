/**
 * 
 */
package org.nww.app.errors;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles possible application error state pages like 404 and 500 HTTP states.
 * @author mga
 */
@Controller
public class NetzwerkWohnenErrorController implements ErrorController {
	
	static final String ERROR_PATH_404 = "/missing";
	static final String ERROR_PATH_500 = "/error";
	
	/* (non-Javadoc)
	 * @see org.springframework.boot.autoconfigure.web.ErrorController#getErrorPath()
	 */
	@Override
	public String getErrorPath() {
		return ERROR_PATH_500;
	}

	@RequestMapping(value = ERROR_PATH_404)
	public String handleMissing(Model model) {
		return "errors/404";
	}

	@RequestMapping(value = ERROR_PATH_500)
	public String handleError(Model model) {
		return "errors/500";
	}
}
