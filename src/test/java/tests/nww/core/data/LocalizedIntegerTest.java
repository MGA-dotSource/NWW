/**
 * 
 */
package tests.nww.core.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.Test;
import org.nww.core.data.LocalizedInteger;

/**
 *
 * @author mga
 */
public class LocalizedIntegerTest {
	@Test
	public void addAndGetData() {
		LocalizedInteger ls = new LocalizedInteger();
		
		ls.setValue(1, "de_DE");
		ls.setValue(2, "en_US");
		
		assertThat(ls.getValues()).isNotNull();
		assertThat(ls.getValues().size()).isEqualTo(2);
		
		assertThat(ls.getValue("de_DE")).isEqualTo(1);
		assertThat(ls.getValue("en_US")).isEqualTo(2);
		
		Set<String> localeIds = ls.getLocaleIDs();
		assertThat(localeIds).isNotNull();
		assertThat(localeIds.size()).isEqualTo(2);
		assertThat(localeIds.contains("de_DE")).isTrue();
		assertThat(localeIds.contains("en_US")).isTrue();
	}
}