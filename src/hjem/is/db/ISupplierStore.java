package hjem.is.db;

import hjem.is.model.Supplier;

public interface ISupplierStore {
    void add(Supplier supplier) throws DataAccessException;
}
