package hjem.is.test;

import hjem.is.controller.PeriodicPlanController;
import hjem.is.controller.StoragePlanController;
import hjem.is.controller.regression.ModelFinder;
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

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StoragePlanControllerTest {

    @Mock IStoragePlanStore spsMock;
    @Mock PeriodicPlanController ppcMock;
    @Mock ModelFinder mfMock;

    @InjectMocks StoragePlanController spc = new StoragePlanController();

    @Test
    public void generatesNewPlan() {
        fail("Not yet implemented");
    }

    @Test
    public void getsPeriodicPlanControllerWithIndex() {
        fail("Not yet implemented");
    }

    @Test
    public void tellsDatabaseToSaveNewStoragePlan() {
        StoragePlan sp = new StoragePlan("June", false, new StorageMetaData(100f), new ArrayList<PeriodicPlan>());
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
    public void tellsDataBaseToUpdateExistingStoragePlan() {
        StoragePlan sp = new StoragePlan("July", false, new StorageMetaData(100f), new ArrayList<PeriodicPlan>(), 3);
        try {
            when(spsMock.getByName("July")).thenReturn(sp);
        } catch (DataAccessException ignored) {
        }
        spc.select("July");
        spc.save();

        try {
            verify(spsMock, times(1)).update(sp);
            verify(spsMock, never()).add(sp);
        } catch (DataAccessException ignored) {
        }
    }

    @AfterEach
    public void tearDown() {
        reset(spsMock);
        reset(ppcMock);
    }
}
