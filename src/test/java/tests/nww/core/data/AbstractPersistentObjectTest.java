/**
 * 
 */
package tests.nww.core.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.nww.core.data.AbstractPersistentObject;

/**
 *
 * @author mga
 */
public abstract class AbstractPersistentObjectTest {
	
	/**
	 * 
	 */
	private static final String UUID = "uuid";
	private static final Date LM_DATE = new Date();

	protected abstract AbstractPersistentObject getTestUnit();

	protected abstract AbstractPersistentObject getNewTestUnit();
	
	@Before
	public abstract void beforeTestDoCleanupAndRecreation();
	
	@Test
	public void getSetUUID() {
		AbstractPersistentObject testUnit = getTestUnit();
		
		testUnit.setUUID(UUID);
		assertThat(testUnit.getUUID()).isEqualTo(UUID);
	}
	
	@Test
	public void getSetLastModified() {
		AbstractPersistentObject testUnit = getTestUnit();
		
		testUnit.setLastModified(LM_DATE);
		assertThat(testUnit.getLastModified()).isEqualTo(LM_DATE);
	}
	
	/**
	 * The basic implementation will return the UUIDs hashcode as its own
	 * cause it is unique enough.
	 */
	@Test
	public void generateHashCode() {
		AbstractPersistentObject testUnit = getTestUnit();
		
		testUnit.setUUID(UUID);
		assertThat(testUnit.hashCode()).isEqualTo(UUID.hashCode());
	}
	
	@Test
	public void equality() throws InstantiationException, IllegalAccessException {
		AbstractPersistentObject testUnit = getTestUnit();
		testUnit.setUUID(UUID);
		
		Object notSameInstance = new Object();
		AbstractPersistentObject anotherUnit = getNewTestUnit();
		anotherUnit.setUUID("another");

		assertThat(testUnit.equals(notSameInstance)).isFalse();
		assertThat(testUnit.equals(anotherUnit)).isFalse();
		
		anotherUnit.setUUID(UUID);
		assertThat(testUnit.equals(anotherUnit)).isTrue();
	}
	
	@Test
	public void stringify() {
		AbstractPersistentObject testUnit = getTestUnit();
		
		testUnit.setUUID(UUID);
		testUnit.setLastModified(LM_DATE);
		
		String expected = UUID + LM_DATE.toString();
		
		assertThat(testUnit.toString()).isEqualTo(expected);
	}
}
