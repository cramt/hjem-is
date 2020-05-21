package hjem.is.controller;

import hjem.is.db.DataAccessException;
import hjem.is.db.StorageMetaDataSqlStore;
import hjem.is.model.StorageMetaData;

public class StorageMetaDataController {
    public StorageMetaData get(){
        StorageMetaData metaData = null;
        try {
            metaData = new StorageMetaDataSqlStore().get();
        } catch (DataAccessException ignored) {

        }
        if(metaData == null){
            metaData = new StorageMetaData(5);
        }
        return metaData;
    }
}
