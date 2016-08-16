/**
 * 
 */
package tests.nww.core.data.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.nww.core.data.PersistentObject;
import org.nww.core.data.mongo.AbstractMongoPersistentObjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 *
 * @author mga
 */
public abstract class AbstractMongoPersistentObjectRepsitoryTest<T extends PersistentObject> {
	
	protected MongoOperations mongoOperationsMock;
	
	protected abstract T getSingleMockItem();
	protected abstract List<T> getMultipleMockItems();
	protected abstract AbstractMongoPersistentObjectRepository<T> getTestUnit();
	
	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		mongoOperationsMock = mock(MongoOperations.class);
		// mock finder methods
		when(mongoOperationsMock.count(Mockito.any(), (Class<T>)Mockito.any())).thenReturn(Long.valueOf(getMultipleMockItems().size()));
		when(mongoOperationsMock.findAll((Class<T>)Mockito.any())).thenReturn(getMultipleMockItems());
		when(mongoOperationsMock.find(Mockito.any(), (Class<T>)Mockito.any())).thenReturn(getMultipleMockItems());
		when(mongoOperationsMock.findOne(Mockito.any(), (Class<T>)Mockito.any())).thenReturn(getSingleMockItem());
		when(mongoOperationsMock.findById(Mockito.anyString(), (Class<T>)Mockito.any())).thenReturn(getSingleMockItem());
		
	}
	
	
	@Test
	public void getEntityClass() {
		assertThat(getTestUnit().getEntityClass()).isNotNull();
	}
	
	@Test
	public void createNew() {
		T aInstance = getTestUnit().createNew();
		
		assertThat(aInstance).isNotNull();
		assertThat(aInstance).isInstanceOf(getTestUnit().getEntityClass());
	}
	
	@Test
	public void findAll() {
		AbstractMongoPersistentObjectRepository<T> testUnit = getTestUnit();
		
		assertThat(testUnit.findAll()).hasSameElementsAs(getMultipleMockItems());
		
		Sort s = new Sort("aProp");
		assertThat(testUnit.findAll(s)).hasSameElementsAs(getMultipleMockItems());
	}
	
	@Test
	public void findAllWithPageable() {
		AbstractMongoPersistentObjectRepository<T> testUnit = getTestUnit();
		
		int expectedPageSize = 100;
		int expectedPageNumber = 1;
		Pageable p = new PageRequest(expectedPageNumber, expectedPageSize);
		Page<T> page = testUnit.findAll(p);
		
		assertThat(page).isNotNull();
		assertThat(page.getNumber()).isEqualTo(expectedPageNumber);
		assertThat(page.getSize()).isEqualTo(expectedPageSize);
		assertThat(page.getContent()).hasSameElementsAs(getMultipleMockItems());
	}
	
	@Test
	public void save() {
		AbstractMongoPersistentObjectRepository<T> testUnit = getTestUnit();
		T item = getSingleMockItem();
		
		assertThat(testUnit.save(item)).isEqualTo(item);
		
		List<T> multipleMockItems = getMultipleMockItems();
		assertThat(testUnit.save(multipleMockItems)).hasSameElementsAs(multipleMockItems);
	}

	@Test
	public void findOne() {
		AbstractMongoPersistentObjectRepository<T> testUnit = getTestUnit();
		String uuid = getSingleMockItem().getUUID();
		
		assertThat(testUnit.findOne(uuid)).isEqualTo(getSingleMockItem());
	}
	
	@Test
	public void exists() {
		AbstractMongoPersistentObjectRepository<T> testUnit = getTestUnit();
		String uuid = getSingleMockItem().getUUID();
		
		assertThat(testUnit.exists(uuid)).isTrue();
	}
	
	@Test
	public void count() {
		AbstractMongoPersistentObjectRepository<T> testUnit = getTestUnit();
		
		assertThat(testUnit.count()).isEqualTo(getMultipleMockItems().size());
	}
}
