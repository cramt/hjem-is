package hjem.is.test;

import hjem.is.controller.PeriodicPlanController;
import hjem.is.controller.StoragePlanController;
import hjem.is.db.*;
import hjem.is.model.*;
import hjem.is.model.time.Period;
import org.apache.poi.hpsf.Array;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PeriodicPlanControllerTest {

    @Mock private StoragePlanController spcMock;
    @Mock private IPeriodicPlanStore ppsMock;
    @Mock private IProductStore psMock;
    @InjectMocks private PeriodicPlanController ppc;

    @Test
    public void setsProductAmountFrom50To40() {
        ppc.setProductAmount("Isbåd", 40);
        assertEquals(40, ppc.getAmountByName("Isbåd"));
    }

    @Test
    public void failsToSetProductAmountToNegativeValue() {
        ppc.init(0);
        assertThrows(IllegalArgumentException.class, () -> ppc.setProductAmount("Isbåd", -2));
    }

    @Test
    public void failsToSetProductAmountToZero() {
        ppc.init(0);
        assertThrows(IllegalArgumentException.class, () -> ppc.setProductAmount("Isbåd", 0));
    }

    @Test
    public void setsProductAmountFrom50To9999() {
        ppc.init(0);
        ppc.setProductAmount("Isbåd", 9999);
        assertEquals(9999, ppc.getAmountByName("Isbåd"));
    }

    @Test
    public void savesPeriodicPlanToDatabase() {
        ppc.init(0);
        try {
            doThrow(new DataAccessException("Stuff", new Exception("more"))).when(ppsMock).update(isA(PeriodicPlan.class));
        } catch (DataAccessException ignored) {
        }
        ppc.save();
    }

    @BeforeEach
    public void setup() {
        HashMap<Product, Integer> productMap = new HashMap<>();
        PeriodicPlan pp;
        productMap.put(new Product(1, "Isbåd", null, 1), 50);
        pp = new PeriodicPlan(productMap,new Period(0, 1),new ArrayList<>());
        ArrayList<PeriodicPlan> ppList = new ArrayList<>();
        ppList.add(pp);
        when(spcMock.get()).thenReturn(new StoragePlan("Juni",true,new StorageMetaData(10f), ppList));
        try {
            when(ppsMock.getByStoragePlan(any())).thenReturn(new ArrayList<>(Arrays.asList(pp)));
        } catch (DataAccessException ignored) {
        }
        ppc.init(0);
    }

    @AfterEach
    public void tearDown() {
        reset(spcMock);
        reset(ppsMock);
        reset(psMock);
    }

}
