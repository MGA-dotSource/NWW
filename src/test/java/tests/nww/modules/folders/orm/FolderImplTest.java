/**
 * 
 */
package tests.nww.modules.folders.orm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		assertThat(f.hasParentFolderUUID()).isTrue();
		assertThat(f.getParentFolderUUID()).isEqualTo(PARENT_UUID);
	}
	
	@Test
	public void subFolderUUIDs() {
		FolderImpl f = getTestUnit();
		
		assertThat(f.hasSubFolderUUIDs()).isFalse();
		
		List<String> subFolderUUIDs = Stream.of("uuid1", "uuid2").collect(Collectors.toList());
		f.setSubFolderUUIDs(subFolderUUIDs);
		
		assertThat(f.hasSubFolderUUIDs()).isTrue();
		assertThat(f.getSubFolderUUIDs()).hasSameElementsAs(subFolderUUIDs);
	}
	
	@Test
	public void contentUUIDs() {
		FolderImpl f = getTestUnit();
		
		assertThat(f.hasContentUUIDs()).isFalse();
		
		List<String> contentUUIDs = Stream.of("uuid1", "uuid2").collect(Collectors.toList());
		f.setContentUUIDs(contentUUIDs);
		
		assertThat(f.hasContentUUIDs()).isTrue();
		assertThat(f.getContentUUIDs()).hasSameElementsAs(contentUUIDs);
	}
}
