/**
 * 
 */
package tests.nww.core.data;

import org.nww.core.data.AbstractExtensiblePersistentObject;

/**
 *
 * @author mga
 */
public abstract class AbstractExtensiblePersistentObjectTest extends AbstractPersistentObjectTest {

	@Override
	protected abstract AbstractExtensiblePersistentObject getTestUnit();
	
	@Override
	protected abstract AbstractExtensiblePersistentObject getNewTestUnit();
}
