/**
 * 
 */
package tests.nww.core.data;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.nww.core.data.DefaultObjectExtension;
import org.nww.core.data.ObjectExtension;

/**
 *
 * @author mga
 */
public class DefaultObjectExtensionTest {

	private static final double A_DOUBLE = 2.0;
	private static final int A_INT = 1;
	private static final String A_STRING = "aString";
	private static final String NAME = "ext";
	private static final String LOCALE_ID = "de_DE";

	@Test
	public void constructors() {
		ObjectExtension ext = new DefaultObjectExtension(NAME);
		
		ext.setString(A_STRING);
		ext.setInteger(A_INT);
		ext.setDouble(A_DOUBLE);
		
		assertSingleValues(ext);
		
		ext = new DefaultObjectExtension(NAME, LOCALE_ID);
		ext.setString(A_STRING);
		ext.setInteger(A_INT);
		ext.setDouble(A_DOUBLE);
		
		assertSingleValues(ext);
		assertThat(ext.getLocaleID()).isEqualTo(LOCALE_ID);
	}

	@Test
	public void singleValues() {
		ObjectExtension ext = new DefaultObjectExtension();
		ext.setName(NAME);
		ext.setString(A_STRING);
		ext.setInteger(A_INT);
		ext.setDouble(A_DOUBLE);
		
		assertSingleValues(ext);
	}
	
	@Test
	public void multipleValues() {
		List<String> stringList = Stream.of("a1", "a2").collect(Collectors.toList());
		List<Integer> integerList = Stream.of(1, 2).collect(Collectors.toList());
		List<Double> doubleList = Stream.of(1.0, 2.0).collect(Collectors.toList());
		
		ObjectExtension ext = new DefaultObjectExtension(NAME);
		ext.setMultipleString(stringList);
		ext.setMultipleInteger(integerList);
		ext.setMultipleDouble(doubleList);
		
		assertThat(ext.getMultipleString()).isEqualTo(stringList);
		assertThat(ext.getMultipleInteger()).isEqualTo(integerList);
		assertThat(ext.getMultipleDouble()).isEqualTo(doubleList);
	}
	
	private void assertSingleValues(ObjectExtension ext) {
		assertThat(ext.getName()).isEqualTo(NAME);
		assertThat(ext.getString()).isEqualTo(A_STRING);
		assertThat(ext.getInteger()).isEqualTo(1);
		assertThat(ext.getDouble()).isEqualTo(2.0);
	}
	
	
}
