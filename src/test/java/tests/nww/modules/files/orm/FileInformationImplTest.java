/**
 * 
 */
package tests.nww.modules.files.orm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.nww.modules.files.orm.FileInformationImpl;

import tests.nww.core.data.AbstractExtensiblePersistentObjectTest;



public class FileInformationImplTest extends AbstractExtensiblePersistentObjectTest {

	/**
	 * 
	 */
	private static final String ORIGINAL_NAME = "ORIGINAL_NAME";
	/**
	 * 
	 */
	private static final String NAME = "NAME";
	private static final String LOCAL_PATH = "LocalPath";
	private static final long SIZE = 4l;
	private static final String DESCRIPTION = "aDescription";
	private static final String CONTENT_TYPE = "image/json";
	private FileInformationImpl file = new FileInformationImpl();
	
	/* (non-Javadoc)
	 * @see tests.nww.core.data.AbstractExtensiblePersistentObjectTest#getTestUnit()
	 */
	@Override
	protected FileInformationImpl getTestUnit() {
		return file;
	}
	
	/* (non-Javadoc)
	 * @see tests.nww.core.data.AbstractPersistentObjectTest#getNewTestUnit()
	 */
	@Override
	protected FileInformationImpl getNewTestUnit() {
		return new FileInformationImpl();
	}
	

	/* (non-Javadoc)
	 * @see tests.nww.core.data.AbstractPersistentObjectTest#beforeTestDoCleanupAndRecreation()
	 */
	@Override
	public void beforeTestDoCleanupAndRecreation() {
		file = new FileInformationImpl();
	}
	
	@Test
	public void getterSetter() {
		FileInformationImpl testUnit = getTestUnit();
		
		testUnit.setName(NAME);
		testUnit.setOriginalName(ORIGINAL_NAME);
		testUnit.setLocalPath(LOCAL_PATH);
		testUnit.setSize(SIZE);
		testUnit.setDescription(DESCRIPTION);
		testUnit.setContentType(CONTENT_TYPE);
		
		assertThat(testUnit.getName()).isEqualTo(NAME);
		assertThat(testUnit.getOriginalName()).isEqualTo(ORIGINAL_NAME);
		assertThat(testUnit.getLocalPath()).isEqualTo(LOCAL_PATH);
		assertThat(testUnit.getSize()).isEqualTo(SIZE);
		assertThat(testUnit.getDescription()).isEqualTo(DESCRIPTION);
		assertThat(testUnit.getContentType()).isEqualTo(CONTENT_TYPE);
	}
}
