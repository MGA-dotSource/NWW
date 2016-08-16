/**
 * 
 */
package tests.nww.core.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.nww.core.data.LocalizedInteger;

/**
 *
 * @author mga
 */
public class LocalizedIntegerTest {
	private static final String LOCALE_DE_DE = "de_DE";
	private static final String LOCALE_EN_US = "en_US";
	private static final Integer DE_VAL = 1;
	private static final Integer EN_VAL = 2;
	
	@Test
	public void addAndGetData() {
		LocalizedInteger ls = new LocalizedInteger();
		
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
		
		Map<String, Integer> mappedValues = ls.getMappedValues();
		assertThat(mappedValues).isNotNull();
		assertThat(mappedValues.containsKey(LOCALE_DE_DE)).isTrue();
		assertThat(mappedValues.containsKey(LOCALE_EN_US)).isTrue();
		assertThat(mappedValues.get(LOCALE_DE_DE)).isEqualTo(DE_VAL);
		assertThat(mappedValues.get(LOCALE_EN_US)).isEqualTo(EN_VAL);
	}
}
