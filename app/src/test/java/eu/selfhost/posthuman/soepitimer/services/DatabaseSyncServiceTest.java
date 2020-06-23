package eu.selfhost.posthuman.soepitimer.services;

import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.time.LocalDate;
import java.time.LocalTime;

import eu.selfhost.posthuman.soepitimer.database.WorkdayDao;
import eu.selfhost.posthuman.soepitimer.model.WorkdayEntity;
import eu.selfhost.posthuman.soepitimer.model.workday.Workday;
import eu.selfhost.posthuman.soepitimer.model.workday.WorkdayCollection;
import eu.selfhost.posthuman.soepitimer.shared.MockableLocalBroadcastManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Copyright 2020 Niklas Polke
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * @author Niklas Polke
 */
public class DatabaseSyncServiceTest {

    private static final LocalDate SOME_DATE = LocalDate.of(2020,6,11);
    private static final LocalTime SOME_TIME = LocalTime.of(12, 11);
    private static final long SOME_ID = 14;

    private DatabaseSyncService service = new DatabaseSyncService();
    private WorkdayDao daoMock;
    private MockableLocalBroadcastManager broadcastManagerMock;

    @Before
    public void init() {
        daoMock = mock(WorkdayDao.class);
        service.workdayDao = daoMock;
        broadcastManagerMock = mock(MockableLocalBroadcastManager.class);
        service.localBroadcastManager = broadcastManagerMock;
        WorkdayCollection.workday = null;
    }

    @Test
    public void runJob_null() {
        service.runJob(null);

        verifyNoInteractions(daoMock);
    }

    @Test
    public void runJob_noIdNonExisting() {
        // expect insert of new WorkdayEntity
        WorkdayCollection.workday = new Workday(SOME_DATE);

        WorkdayEntity entityToCreate = new WorkdayEntity();
        entityToCreate.date = SOME_DATE.toString();
        entityToCreate.id = SOME_ID;

        when(daoMock.findByDate(SOME_DATE.toString())).thenReturn(null, entityToCreate);

        service.runJob(null);

        final ArgumentCaptor<WorkdayEntity> insertWorkdayCapture = ArgumentCaptor.forClass(WorkdayEntity.class);
        final InOrder inOrder = inOrder(daoMock);
        inOrder.verify(daoMock).findByDate(SOME_DATE.toString());
        inOrder.verify(daoMock).insert(insertWorkdayCapture.capture());
        inOrder.verify(daoMock).findByDate(SOME_DATE.toString());

        assertNotNull(insertWorkdayCapture.getValue());
        assertEquals(SOME_DATE.toString(), insertWorkdayCapture.getValue().date);

        assertNotNull(WorkdayCollection.workday);
        assertEquals(SOME_DATE, WorkdayCollection.workday.getDate());
        assertEquals(SOME_ID, WorkdayCollection.workday.getId());
        assertNull(WorkdayCollection.workday.getTimeStart());
        assertNull(WorkdayCollection.workday.getTimeStop());
        assertFalse(WorkdayCollection.workday.isDirty());

        verify(broadcastManagerMock).sendBroadcast(same(service), any(Intent.class));
    }

    @Test
    public void runJob_noIdExisting() {
        // expect findByDate returns existing WorkdayEntity
        WorkdayCollection.workday = new Workday(SOME_DATE);

        WorkdayEntity existingEntity = new WorkdayEntity();
        existingEntity.date = SOME_DATE.toString();
        existingEntity.id = SOME_ID;
        existingEntity.workdayStart = SOME_TIME.toString();

        when(daoMock.findByDate(SOME_DATE.toString())).thenReturn(existingEntity, existingEntity);

        service.runJob(null);

        assertNotNull(WorkdayCollection.workday);
        assertEquals(SOME_DATE, WorkdayCollection.workday.getDate());
        assertEquals(SOME_ID, WorkdayCollection.workday.getId());
        assertEquals(SOME_TIME, WorkdayCollection.workday.getTimeStart());
        assertNull(WorkdayCollection.workday.getTimeStop());
        assertFalse(WorkdayCollection.workday.isDirty());
        verify(daoMock, never()).insert(any());
        verify(daoMock, never()).update(any());

        verify(broadcastManagerMock).sendBroadcast(same(service), any(Intent.class));
    }

    @Test
    public void runJob_withIdDirty() {
        // expect update of existing WorkdayEntity
        WorkdayCollection.workday = new Workday(SOME_DATE);
        WorkdayCollection.workday.setId(SOME_ID);
        WorkdayCollection.workday.setTimeStop(SOME_TIME);

        WorkdayEntity entityToLoad = new WorkdayEntity();
        entityToLoad.date = SOME_DATE.toString();
        entityToLoad.id = SOME_ID;
        entityToLoad.workdayEnd = SOME_TIME.toString();

        when(daoMock.findByDate(SOME_DATE.toString())).thenReturn(entityToLoad);

        service.runJob(null);

        final ArgumentCaptor<WorkdayEntity> updateWorkdayCapture = ArgumentCaptor.forClass(WorkdayEntity.class);
        final InOrder inOrder = inOrder(daoMock);
        inOrder.verify(daoMock).update(updateWorkdayCapture.capture());
        inOrder.verify(daoMock).findByDate(SOME_DATE.toString());

        assertNotNull(updateWorkdayCapture.getValue());
        assertEquals(SOME_DATE.toString(), updateWorkdayCapture.getValue().date);
        assertEquals(SOME_ID, updateWorkdayCapture.getValue().id);
        assertNull(updateWorkdayCapture.getValue().workdayStart);
        assertEquals(SOME_TIME.toString(), updateWorkdayCapture.getValue().workdayEnd);

        assertNotNull(WorkdayCollection.workday);
        assertEquals(SOME_DATE, WorkdayCollection.workday.getDate());
        assertEquals(SOME_ID, WorkdayCollection.workday.getId());
        assertNull(WorkdayCollection.workday.getTimeStart());
        assertEquals(SOME_TIME, WorkdayCollection.workday.getTimeStop());
        assertFalse(WorkdayCollection.workday.isDirty());

        verify(broadcastManagerMock).sendBroadcast(same(service), any(Intent.class));
    }

    @Test
    public void runJob_withIdNotDirty() {
        // expect no changes
        WorkdayCollection.workday = new Workday(SOME_DATE);
        WorkdayCollection.workday.setId(SOME_ID);
        WorkdayCollection.workday.setTimeStop(SOME_TIME);
        WorkdayCollection.workday.resetDirty();

        WorkdayEntity entityToLoad = new WorkdayEntity();
        entityToLoad.date = SOME_DATE.toString();
        entityToLoad.id = SOME_ID;
        entityToLoad.workdayEnd = SOME_TIME.toString();

        when(daoMock.findByDate(SOME_DATE.toString())).thenReturn(entityToLoad);

        service.runJob(null);

        assertNotNull(WorkdayCollection.workday);
        assertEquals(SOME_DATE, WorkdayCollection.workday.getDate());
        assertEquals(SOME_ID, WorkdayCollection.workday.getId());
        assertNull(WorkdayCollection.workday.getTimeStart());
        assertEquals(SOME_TIME, WorkdayCollection.workday.getTimeStop());
        assertFalse(WorkdayCollection.workday.isDirty());
        verify(daoMock, never()).insert(any());
        verify(daoMock, never()).update(any());

        verify(broadcastManagerMock).sendBroadcast(same(service), any(Intent.class));
    }
}
