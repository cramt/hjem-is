package hjem.is.db;

import hjem.is.model.StorageMetaData;

public interface IStorageMetaDataStore {
    public StorageMetaData get() throws DataAccessException;
}
