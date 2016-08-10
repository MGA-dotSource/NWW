/**
 * 
 */
package tests.nww.core.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.Test;
import org.nww.core.data.LocalizedString;

/**
 *
 * @author mga
 */
public class LocalizedStringTest {
	@Test
	public void addAndGetData() {
		LocalizedString ls = new LocalizedString();
		
		ls.setValue("de_val", "de_DE");
		ls.setValue("en_val", "en_US");
		
		assertThat(ls.getValues()).isNotNull();
		assertThat(ls.getValues().size()).isEqualTo(2);
		
		assertThat(ls.getValue("de_DE")).isEqualTo("de_val");
		assertThat(ls.getValue("en_US")).isEqualTo("en_val");
		
		Set<String> localeIds = ls.getLocaleIDs();
		assertThat(localeIds).isNotNull();
		assertThat(localeIds.size()).isEqualTo(2);
		assertThat(localeIds.contains("de_DE")).isTrue();
		assertThat(localeIds.contains("en_US")).isTrue();
	}
}
