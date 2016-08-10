/**
 * 
 */
package tests.nww.core.system;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;
import org.nww.core.data.PersistentObject;
import org.nww.core.system.OperationResult;
import org.nww.core.system.OperationResult.State;

/**
 *
 * @author mga
 */
public class OperationResultTest {

	private static final String MESSAGE = "no";
	private static final String EXCEPTION_MESSAGE = "test";
	private static final State SUCCESSFULL = State.SUCCESSFULL;
	private static final Long AFFECTED_ENTITIES_COUNT = 1l;
	private static final Exception EXCEPTION = new Exception(EXCEPTION_MESSAGE);
	private static final String MESSAGE_KEY = "key";
	private static final PersistentObject AFFECTED_OBJECT = new PersistentObject() {
		
		@Override
		public void setUUID(String uuid) { }
		
		@Override
		public void setLastModified(Date lastModified) { }
		
		@Override
		public String getUUID() {
			return "uuid";
		}
		
		@Override
		public Date getLastModified() { return null; }
	};
	private static final String UUID = "uuid";

	@Test
	public void constructor1() {
		OperationResult or = new OperationResult(SUCCESSFULL);
		assertStatus(or);
	}

	@Test
	public void constructor2() {
		OperationResult or = new OperationResult(SUCCESSFULL, EXCEPTION);
		assertStatus(or);
		assertException(or);
	}
	
	@Test
	public void constructor3() {
		OperationResult or = new OperationResult(SUCCESSFULL, 1l);
		assertStatus(or);
		assertAffectedEntitiesCount(or);
	}
	
	@Test
	public void constructor4() {
		OperationResult or = new OperationResult(SUCCESSFULL, MESSAGE);
		assertStatus(or);
		assertMessage(or);
	}
	
	@Test
	public void constructor5() {
		OperationResult or = new OperationResult(SUCCESSFULL, AFFECTED_ENTITIES_COUNT, EXCEPTION);
		assertStatus(or);
		assertAffectedEntitiesCount(or);
		assertException(or);
	}
	
	@Test
	public void constructor6() {
		OperationResult or = new OperationResult(SUCCESSFULL, AFFECTED_ENTITIES_COUNT, MESSAGE);
		assertStatus(or);
		assertAffectedEntitiesCount(or);
		assertMessage(or);
	}
	
	@Test
	public void constructor7() {
		OperationResult or = new OperationResult(SUCCESSFULL, MESSAGE, EXCEPTION);
		assertStatus(or);
		assertMessage(or);
		assertException(or);
	}
	
	@Test
	public void constructor8() {
		OperationResult or = new OperationResult(SUCCESSFULL, MESSAGE, MESSAGE_KEY);
		assertStatus(or);
		assertMessage(or);
		assertMessageKey(or);
	}
	
	@Test
	public void constructor9() {
		OperationResult or = new OperationResult(SUCCESSFULL, AFFECTED_ENTITIES_COUNT, MESSAGE, EXCEPTION);
		assertStatus(or);
		assertAffectedEntitiesCount(or);
		assertMessage(or);
		assertException(or);
	}
	
	@Test
	public void constructor10() {
		OperationResult or = new OperationResult(SUCCESSFULL, AFFECTED_ENTITIES_COUNT, MESSAGE, MESSAGE_KEY);
		assertStatus(or);
		assertAffectedEntitiesCount(or);
		assertMessage(or);
		assertMessageKey(or);
	}
	
	@Test
	public void constructor11() {
		OperationResult or = new OperationResult(SUCCESSFULL, AFFECTED_ENTITIES_COUNT, MESSAGE, MESSAGE_KEY, AFFECTED_OBJECT);
		assertStatus(or);
		assertAffectedEntitiesCount(or);
		assertMessage(or);
		assertMessageKey(or);
		assertAffectedObject(or);
	}	
	
	@Test
	public void consructor12() {
		OperationResult or = new OperationResult(SUCCESSFULL, AFFECTED_ENTITIES_COUNT, MESSAGE, MESSAGE_KEY, AFFECTED_OBJECT, EXCEPTION);
		assertStatus(or);
		assertAffectedEntitiesCount(or);
		assertMessage(or);
		assertMessageKey(or);
		assertAffectedObject(or);
		assertException(or);
	}
	
	private void assertStatus(OperationResult or) {
		assertThat(or.getResultState()).isEqualTo(SUCCESSFULL);
	}

	private void assertMessage(OperationResult or) {
		assertThat(or.getMessage()).isEqualTo(MESSAGE);
	}
	
	private void assertMessageKey(OperationResult or) {
		assertThat(or.getMessageKey()).isEqualTo(MESSAGE_KEY);
	}

	private void assertAffectedEntitiesCount(OperationResult or) {
		assertThat(or.getAffectedEntitiesCount()).isEqualTo(1l);
	}

	private void assertException(OperationResult or) {
		assertThat(or.getException().getMessage()).isEqualTo(EXCEPTION_MESSAGE);
	}
	
	private void assertAffectedObject(OperationResult or) {
		assertThat(or.getAffectedObject().getUUID()).isEqualTo(UUID);
	}
}
