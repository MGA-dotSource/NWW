/**
 * 
 */
package tests.nww.modules.messaging;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.nww.modules.messaging.MailForm;

public class MailFormTest {
	private static final String UUID = "uuid";
	private static final String CONTENT = "content";
	private static final String SUBJECT = "subject";
	private static final String RECIPIENTS = "rec1|rec2";

	@Test
	public void getterSetter() {
		MailForm mf = new MailForm();
		
		mf.setUUID(UUID);
		mf.setContent(CONTENT);
		mf.setSubject(SUBJECT);
		mf.setRecipients(RECIPIENTS);
		
		assertThat(mf.getUUID()).isEqualTo(UUID);
		assertThat(mf.getContent()).isEqualTo(CONTENT);
		assertThat(mf.getSubject()).isEqualTo(SUBJECT);
		assertThat(mf.getRecipients()).isEqualTo(RECIPIENTS);
	}
}
