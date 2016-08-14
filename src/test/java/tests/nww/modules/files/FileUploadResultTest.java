/**
 * 
 */
package tests.nww.modules.files;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.nww.modules.files.FileUploadResult;

/**
 *
 * @author mga
 */
public class FileUploadResultTest {
	private static final String B_URL = "bURL";
	private static final String A_URL = "aURL";

	@Test
	public void getterSetter() {
		FileUploadResult r = new FileUploadResult(true, A_URL);
		
		assertThat(r.getSuccess()).isTrue();
		assertThat(r.getUrl()).isEqualTo(A_URL);
		
		r.setSuccess(false);
		r.setUrl(B_URL);
		
		assertThat(r.getSuccess()).isFalse();
		assertThat(r.getUrl()).isEqualTo(B_URL);
	}
}
