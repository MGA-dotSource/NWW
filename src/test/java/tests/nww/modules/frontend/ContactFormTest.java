/**
 * 
 */
package tests.nww.modules.frontend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.nww.modules.frontend.ContactForm;

public class ContactFormTest {
	private static final String EMAIL = "amail";
	private static final String MESSAGE = "amessage";
	private static final String NAME = "aname";

	@Test
	public void getterSetter() {
		ContactForm cf = new ContactForm();
		
		cf.setEmail(EMAIL);
		cf.setMessage(MESSAGE);
		cf.setName(NAME);
		
		assertThat(cf.getEmail()).isEqualTo(EMAIL);
		assertThat(cf.getMessage()).isEqualTo(MESSAGE);
		assertThat(cf.getName()).isEqualTo(NAME);
	}
}
