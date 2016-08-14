/**
 * 
 */
package tests.nww.modules.folders.orm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.nww.modules.folders.orm.FolderImpl;

import tests.nww.core.data.AbstractExtensiblePersistentObjectTest;

/**
 *
 * @author mga
 */
public class FolderImplTest extends AbstractExtensiblePersistentObjectTest {

	/**
	 * 
	 */
	private static final String PARENT_UUID = "pUUID";
	/**
	 * 
	 */
	private static final String NAME = "aName";
	private FolderImpl f = new FolderImpl();
	
	@Override
	protected FolderImpl getTestUnit() {
		return this.f;
	}

	@Override
	protected FolderImpl getNewTestUnit() {
		return new FolderImpl();
	}

	@Override
	public void beforeTestDoCleanupAndRecreation() {
		this.f = getNewTestUnit();
	}
	
	@Test
	public void basicAttributes() {
		FolderImpl f = getTestUnit();
		
		f.setName(NAME);
		f.setParentFolderUUID(PARENT_UUID);
		
		assertThat(f.getName()).isEqualTo(NAME);
		assertThat(f.getParentFolderUUID()).isEqualTo(PARENT_UUID);
	}
}
