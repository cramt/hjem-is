package hjem.is.test;

import hjem.is.controller.PeriodicPlanController;
import hjem.is.controller.StoragePlanController;
import hjem.is.db.DataAccessException;
import hjem.is.db.IStoragePlanStore;
import hjem.is.model.PeriodicPlan;
import hjem.is.model.StorageMetaData;
import hjem.is.model.StoragePlan;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StoragePlanControllerTest {

    @Mock IStoragePlanStore spsMock;
    @Mock PeriodicPlanController ppcMock;

    @InjectMocks StoragePlanController spc = new StoragePlanController();

    @Test
    public void generatesNewPlanWithNameJuni() {
        String planName = "Juni";
        spc.generateNew(planName);
        assertEquals(planName,spc.get().getName());
        assertNotNull(spc.get().getPeriodicPlans());
    }

    @Test
    public void generatesNewPlanWithNameJuli() {
        String planName = "Juli";
        spc.generateNew(planName);
        assertEquals(planName, spc.get().getName());
        assertNotNull(spc.get().getPeriodicPlans());
    }

    @Test
    public void generatesNewPlanWithName100() {
        String planName = "100";
        spc.generateNew(planName);
        assertEquals(planName, spc.get().getName());
        assertNotNull(spc.get().getPeriodicPlans());
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

    @Test
    public void setCurrentPlanActive(){
        StoragePlan sp = new StoragePlan("September", false, new StorageMetaData(100f), new ArrayList<PeriodicPlan>(), 1);
        try {
            when(spsMock.getByName("September")).thenReturn(sp);
        } catch (DataAccessException ignored) {
        }
        spc.select("September");
        spc.setActive(true);

        assertTrue(spc.isActive());
    }

    @Test
    public void deletesPlanByName() {
        StoragePlan sp = new StoragePlan("Juni", false, new StorageMetaData(100f), new ArrayList<PeriodicPlan>(), 1);
        spc.select("Juni");
        try {
            when(spsMock.getByName("Juni")).thenReturn(sp);
        } catch (DataAccessException ignored) {
        }
        spc.delete();

        try {
            verify(spsMock, times(1)).delete(sp);
        } catch (DataAccessException ignored) {
        }
    }

    @AfterEach
    public void tearDown() {
        reset(spsMock);
        reset(ppcMock);
    }
}
