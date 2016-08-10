/**
 * 
 */
package tests.nww.core.data;


import static org.assertj.core.api.Assertions.assertThat;

import org.nww.core.data.DefaultObjectExtension;
import org.nww.core.data.ObjectExtension;

/**
 *
 * @author mga
 */
public class DefaultObjectExtensionTest {
	public void unlocalizedExtensionSingleValues() {
		ObjectExtension ext = new DefaultObjectExtension();
		ext.setName("ext");
		ext.setString("aString");
		ext.setInteger(1);
		ext.setDouble(2.0);
		
		assertThat(ext.getString()).isEqualTo("aString");
		assertThat(ext.getInteger()).isEqualTo(1);
		assertThat(ext.getDouble()).isEqualTo(2.0);
	}
}
