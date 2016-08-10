/**
 * 
 */
package tests.nww.core.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

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

		assertThat(lh.getLocaleID(Locale.GERMANY), equalTo("de_DE"));
	}
	
	@Test
	public void getLocaleByID() {
		LocaleHelper lh = new DefaultLocaleHelper();

		assertThat(lh.getLocaleByID("de_DE"), equalTo(Locale.GERMANY));
	}
	
	@Test
	public void getLocalesByIDs() {
		LocaleHelper lh = new DefaultLocaleHelper();
		List<Locale> expectedLocales = new ArrayList<>();
		expectedLocales.add(Locale.GERMANY);
		expectedLocales.add(Locale.US);
		
		List<Locale> createdLocales = lh.getLocalesByIDs("de_DE", "en_US");
		assertNotNull(createdLocales);
		assertThat(createdLocales.size(), is(expectedLocales.size()));
		expectedLocales.forEach(l -> {
			assertThat(createdLocales.contains(l), is(true));
		});
	}
}
