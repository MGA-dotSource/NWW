/**
 * 
 */
package tests.nww.modules.folders.orm;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.nww.core.data.mongo.AbstractMongoPersistentObjectRepository;
import org.nww.modules.folders.orm.FolderImpl;
import org.nww.modules.folders.orm.MongoFolderRepository;

import tests.nww.core.data.mongo.AbstractMongoPersistentObjectRepsitoryTest;

/**
 *
 * @author mga
 */
public class MongoFolderRepositoryTest extends AbstractMongoPersistentObjectRepsitoryTest<FolderImpl> {

	/* (non-Javadoc)
	 * @see tests.nww.core.data.mongo.AbstractMongoPersistentObjectRepsitoryTest#getSingleMockItem()
	 */
	@Override
	protected FolderImpl getSingleMockItem() {
		FolderImpl f = new FolderImpl();
		f.setUUID("uuid");
		
		return f;
	}

	/* (non-Javadoc)
	 * @see tests.nww.core.data.mongo.AbstractMongoPersistentObjectRepsitoryTest#getMultipleMockItems()
	 */
	@Override
	protected List<FolderImpl> getMultipleMockItems() {
		FolderImpl f1 = new FolderImpl();
		FolderImpl f2 = new FolderImpl();
		f1.setUUID("uuid1");
		f2.setUUID("uuid2");
		
		return Stream.of(f1, f2).collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see tests.nww.core.data.mongo.AbstractMongoPersistentObjectRepsitoryTest#getTestUnit()
	 */
	@Override
	protected AbstractMongoPersistentObjectRepository<FolderImpl> getTestUnit() {
		MongoFolderRepository mongoFolderRepository = new MongoFolderRepository();
		mongoFolderRepository.setMongoOperations(mongoOperationsMock);
		return mongoFolderRepository;
	}
	
}
