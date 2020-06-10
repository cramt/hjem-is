package hjem.is.test;

import hjem.is.controller.PeriodicPlanController;
import hjem.is.controller.StoragePlanController;
import hjem.is.db.DataAccessException;
import hjem.is.db.IPeriodicPlanStore;
import hjem.is.db.IProductStore;
import hjem.is.model.PeriodicPlan;
import hjem.is.model.Product;
import hjem.is.model.StorageMetaData;
import hjem.is.model.StoragePlan;
import hjem.is.model.time.Period;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PeriodicPlanControllerTest {

    @Mock private StoragePlanController spcMock;
    @Mock private IPeriodicPlanStore ppsMock;
    @Mock private IProductStore psMock;
    @InjectMocks private PeriodicPlanController ppc;

    PeriodicPlan pp;
    PeriodicPlan ppLeft;
    PeriodicPlan ppRight;

    Product leftProduct = new Product(1, "Filur", null, 2);
    Product product = new Product(1, "Isbåd", null, 1);
    Product rightProduct = new Product(1, "Islagkage", null, 3);

    @Test
    public void setsProductAmountFrom50To40() {
        ppc.setProductAmount("Isbåd", 40);
        assertEquals(40, (int)ppc.getAmountByName("Isbåd"));
    }

    @Test
    public void failsToSetProductAmountToNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> ppc.setProductAmount("Isbåd", -2));
    }

    @Test
    public void failsToSetProductAmountToZero() {
        assertThrows(IllegalArgumentException.class, () -> ppc.setProductAmount("Isbåd", 0));
    }

    @Test
    public void setsProductAmountFrom50To9999() {
        ppc.setProductAmount("Isbåd", 9999);
        assertEquals(9999, (int)ppc.getAmountByName("Isbåd"));
    }

    @Test
    public void savesPeriodicPlanToDatabase() {
        InOrder inOrder = inOrder(ppsMock);
        ppc.save();
        try {
            inOrder.verify(ppsMock).update(
                    argThat((PeriodicPlan ppArg) ->
                            ppArg.getProductMap().containsKey(leftProduct)),
                    eq(false));
            inOrder.verify(ppsMock).update(argThat((PeriodicPlan ppArg) ->
                    ppArg.getProductMap().containsKey(product)));
            inOrder.verify(ppsMock).update(argThat((PeriodicPlan ppArg) ->
                    ppArg.getProductMap().containsKey(rightProduct)),
                    eq(false));
        } catch (DataAccessException ignored) {
        }
    }

    @BeforeEach
    public void setup() {
        HashMap<Product, Integer> productMapLeft = new HashMap<>();
        HashMap<Product, Integer> productMap = new HashMap<>();
        HashMap<Product, Integer> productMapRight = new HashMap<>();
        productMapLeft.put(leftProduct, 30);
        productMap.put(product, 50);
        productMapRight.put(rightProduct, 20);
        pp = new PeriodicPlan(productMap, new Period(1, 2),new ArrayList<>());
        ppLeft = new PeriodicPlan(productMapLeft, new Period(0, 1), new ArrayList<>());
        ppRight = new PeriodicPlan(productMapRight, new Period(2, 3), new ArrayList<>());
        ArrayList<PeriodicPlan> ppList = new ArrayList<>();
        ppList.add(ppLeft);
        ppList.add(pp);
        ppList.add(ppRight);
        when(spcMock.get()).thenReturn(new StoragePlan("Juni",true,new StorageMetaData(10f), ppList));
        try {
            when(ppsMock.getByStoragePlan(any()))
                    .thenReturn(ppList);
        } catch (DataAccessException ignored) {
        }
        ppc.init(1);
    }

    @AfterEach
    public void tearDown() {
        reset(spcMock);
        reset(ppsMock);
        reset(psMock);
    }

}
