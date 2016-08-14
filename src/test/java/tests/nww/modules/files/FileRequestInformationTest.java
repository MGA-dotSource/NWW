/**
 * 
 */
package tests.nww.modules.files;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.nww.modules.files.FileRequestInformation;

/**
 *
 * @author mga
 */
public class FileRequestInformationTest {
	private static final String FILE_NAME = "aFile";
	private static final String SEPARATOR = "_";
	private static final String FIRST_ATTR = "64";
	private static final String SECOND_ATTR = "102";
	private static final String THRID_ATTR = "max";
	
	@Test
	public void noAttributes() {
		FileRequestInformation fri = new FileRequestInformation(FILE_NAME);
		
		assertThat(fri.getPurName()).isEqualTo(FILE_NAME);
		assertThat(fri.hasAttributes()).isFalse();
		assertThat(fri.getAttributeCount()).isEqualTo(0);
		assertThat(fri.getAttributes()).isNull();
	}
	
	@Test
	public void oneAttribute() {
		FileRequestInformation fri = new FileRequestInformation(FILE_NAME 
				+ SEPARATOR + FIRST_ATTR);
		
		assertThat(fri.getPurName()).isEqualTo(FILE_NAME);
		assertThat(fri.hasAttributes()).isTrue();
		assertThat(fri.getAttributeCount()).isEqualTo(1);
		assertThat(fri.getAttributes()).isNotNull();
		assertThat(fri.getAttributes().length).isEqualTo(1);
		assertThat(fri.getAttributes()[0]).isEqualTo(FIRST_ATTR);
	}
	
	@Test
	public void twoAttributes() {
		FileRequestInformation fri = new FileRequestInformation(FILE_NAME 
				+ SEPARATOR + FIRST_ATTR 
				+ SEPARATOR + SECOND_ATTR);
		
		assertThat(fri.getPurName()).isEqualTo(FILE_NAME);
		assertThat(fri.hasAttributes()).isTrue();
		assertThat(fri.getAttributeCount()).isEqualTo(2);
		assertThat(fri.getAttributes()).isNotNull();
		assertThat(fri.getAttributes().length).isEqualTo(2);
		assertThat(fri.getAttributes()[0]).isEqualTo(FIRST_ATTR);
		assertThat(fri.getAttributes()[1]).isEqualTo(SECOND_ATTR);
	}
	
	@Test
	public void threeAttributes() {
		FileRequestInformation fri = new FileRequestInformation(
				FILE_NAME 
				+ SEPARATOR + FIRST_ATTR 
				+ SEPARATOR + SECOND_ATTR
				+ SEPARATOR + THRID_ATTR);
		
		assertThat(fri.getPurName()).isEqualTo(FILE_NAME);
		assertThat(fri.hasAttributes()).isTrue();
		assertThat(fri.getAttributeCount()).isEqualTo(3);
		assertThat(fri.getAttributes()).isNotNull();
		assertThat(fri.getAttributes().length).isEqualTo(3);
		assertThat(fri.getAttributes()[0]).isEqualTo(FIRST_ATTR);
		assertThat(fri.getAttributes()[1]).isEqualTo(SECOND_ATTR);
		assertThat(fri.getAttributes()[2]).isEqualTo(THRID_ATTR);
	}
}
