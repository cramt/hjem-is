package hjem.is.test;

import hjem.is.controller.StoragePlanController;
import hjem.is.db.DataAccessException;
import hjem.is.db.IStoragePlanStore;
import hjem.is.db.StoragePlanSqlStore;
import hjem.is.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StoragePlanControllerTest {

    @Mock
    IStoragePlanStore spsMock;

    @InjectMocks
    StoragePlanController spc = new StoragePlanController();

    @Test
    public void generatesNewPlan() {

    }

    @Test
    public void getsPeriodicPlanControllerWithIndex() {

    }

    @Test
    public void tellsDatabaseToSaveNewStoragePlan() {
        StoragePlan sp = new StoragePlan("June", false,new StorageMetaData(100f), new ArrayList<PeriodicPlan>());
        try {
            when(spsMock.getByName("June")).thenReturn(sp);
        } catch (DataAccessException ignored) {
        }
        spc.select("June");
        spc.save();

        try {
            verify(spsMock, times(1)).add(sp);
            verify(spsMock, never()).update(sp);
        } catch (DataAccessException ignored) {
        }

    }

    @Test
    public void tellsDataBaseToSaveExistingStoragePlan() {

    }

    @AfterEach
    public void tearDown() {
        reset(spsMock);
    }
}
