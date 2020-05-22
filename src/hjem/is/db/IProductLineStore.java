package hjem.is.db;

import hjem.is.model.OrderProductLine;

import java.util.List;

public interface IProductLineStore {
    List<OrderProductLine> getCurrentlyStored() throws DataAccessException;
}
