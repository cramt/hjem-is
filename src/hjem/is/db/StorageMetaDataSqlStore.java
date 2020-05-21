package hjem.is.db;

import hjem.is.model.StorageMetaData;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StorageMetaDataSqlStore implements IStorageMetaDataStore{

    @Override
    public StorageMetaData get() throws DataAccessException {
        try {
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement("SELECT percent_inventory_cost, id FROM storage_meta_data");
            NullableResultSet result = new NullableResultSet(stmt.executeQuery());
            if(result.next()){
                return new StorageMetaData(result.getFloat("percent_inventory_cost"), result.getInt("id"));
            }
            else{
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
