/**
 * 
 */
package tests.nww.core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.nww.core.utils.DefaultLocaleHelper;
import org.nww.core.utils.LocaleHelper;

/**
 *
 * @author mga
 */
public class DefaultLocaleHelperTest {
	
	
	@Test
	public void getLocaleID() throws Exception {
		LocaleHelper lh = new DefaultLocaleHelper();

		assertThat(lh.getLocaleID(Locale.GERMANY)).isEqualTo("de_DE");
	}
	
	@Test
	public void getLocaleByID() {
		LocaleHelper lh = new DefaultLocaleHelper();

		assertThat(lh.getLocaleByID("de_DE")).isEqualTo(Locale.GERMANY);
	}
	
	@Test
	public void getLocalesByIDs() {
		LocaleHelper lh = new DefaultLocaleHelper();
		List<Locale> expectedLocales = new ArrayList<>();
		expectedLocales.add(Locale.GERMANY);
		expectedLocales.add(Locale.US);
		
		List<Locale> createdLocales = lh.getLocalesByIDs("de_DE", "en_US");
		assertThat(createdLocales).isNotNull();
		assertThat(createdLocales.size()).isEqualTo(expectedLocales.size());
		expectedLocales.forEach(l -> {
			assertThat(createdLocales.contains(l)).isTrue();
		});
	}
	
	@Test
	public void nullCheck() {
		LocaleHelper lh = new DefaultLocaleHelper();
		assertThat(lh.getLocaleByID("")).isNull();
		assertThat(lh.getLocaleByID(null)).isNull();
	}
}
