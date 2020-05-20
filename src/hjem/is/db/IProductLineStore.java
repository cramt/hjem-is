package hjem.is.db;

import hjem.is.model.ProductLine;

import java.util.List;

public interface IProductLineStore {
    List<ProductLine> getCurrentlyStored() throws DataAccessException;
}
