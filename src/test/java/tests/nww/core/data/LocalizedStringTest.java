/**
 * 
 */
package tests.nww.core.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.nww.core.data.LocalizedString;

/**
 *
 * @author mga
 */
public class LocalizedStringTest {
	private static final String LOCALE_DE_DE = "de_DE";
	private static final String LOCALE_EN_US = "en_US";
	private static final String DE_VAL = "de_val";
	private static final String EN_VAL = "en_val";

	@Test
	public void addAndGetData() {
		LocalizedString ls = new LocalizedString();
		
		ls.setValue(DE_VAL, LOCALE_DE_DE);
		ls.setValue(EN_VAL, LOCALE_EN_US);
		
		assertThat(ls.getValues()).isNotNull();
		assertThat(ls.getValues().size()).isEqualTo(2);
		
		assertThat(ls.getValue(LOCALE_DE_DE)).isEqualTo(DE_VAL);
		assertThat(ls.getValue(LOCALE_EN_US)).isEqualTo(EN_VAL);
		
		Set<String> localeIds = ls.getLocaleIDs();
		assertThat(localeIds).isNotNull();
		assertThat(localeIds.size()).isEqualTo(2);
		assertThat(localeIds.contains(LOCALE_DE_DE)).isTrue();
		assertThat(localeIds.contains(LOCALE_EN_US)).isTrue();
		
		Map<String, String> mappedValues = ls.getMappedValues();
		assertThat(mappedValues).isNotNull();
		assertThat(mappedValues.containsKey(LOCALE_DE_DE)).isTrue();
		assertThat(mappedValues.containsKey(LOCALE_EN_US)).isTrue();
		assertThat(mappedValues.get(LOCALE_DE_DE)).isEqualTo(DE_VAL);
		assertThat(mappedValues.get(LOCALE_EN_US)).isEqualTo(EN_VAL);
	}
}
