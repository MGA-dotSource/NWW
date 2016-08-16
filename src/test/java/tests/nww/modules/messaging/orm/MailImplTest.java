/**
 * 
 */
package tests.nww.modules.messaging.orm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.nww.modules.messaging.orm.MailImpl;

import tests.nww.core.data.AbstractPersistentObjectTest;

public class MailImplTest extends AbstractPersistentObjectTest {

	private static final String SUBJECT = "subject";
	private static final String CONTENT = "content";
	private MailImpl mi = new MailImpl();
	
	/* (non-Javadoc)
	 * @see tests.nww.core.data.AbstractPersistentObjectTest#getTestUnit()
	 */
	@Override
	protected MailImpl getTestUnit() {
		return mi;
	}

	/* (non-Javadoc)
	 * @see tests.nww.core.data.AbstractPersistentObjectTest#getNewTestUnit()
	 */
	@Override
	protected MailImpl getNewTestUnit() {
		return new MailImpl();
	}

	/* (non-Javadoc)
	 * @see tests.nww.core.data.AbstractPersistentObjectTest#beforeTestDoCleanupAndRecreation()
	 */
	@Override
	public void beforeTestDoCleanupAndRecreation() {
		this.mi = new MailImpl();
	}

	@Test
	public void getterSetter() {
		MailImpl testUnit = getTestUnit();
		
		List<String> recipients = Stream.of("rec1", "rec2").collect(Collectors.toList());
		Date sendDate = new Date();
		
		testUnit.setContent(CONTENT);
		testUnit.setSubject(SUBJECT);
		testUnit.setRecipients(recipients);
		testUnit.setSendDate(sendDate);
		
		assertThat(testUnit.getContent()).isEqualTo(CONTENT);
		assertThat(testUnit.getSubject()).isEqualTo(SUBJECT);
		assertThat(testUnit.getRecipients()).hasSameElementsAs(recipients);
		assertThat(testUnit.getSendDate()).isEqualTo(sendDate);
	}
	
	@Test
	@Override
	public void stringify() {
		MailImpl testUnit = getTestUnit();

		List<String> recipients = Stream.of("rec1", "rec2").collect(Collectors.toList());
		
		testUnit.setSubject(SUBJECT);
		testUnit.setRecipients(recipients);

		
		String expected = 
				"UUID: " + testUnit.getUUID() 
				+ " | Subject: " + testUnit.getSubject() 
				+ " | MailTo: " + testUnit.getRecipients();
		
		assertThat(testUnit.toString()).isEqualTo(expected);
	}
}
