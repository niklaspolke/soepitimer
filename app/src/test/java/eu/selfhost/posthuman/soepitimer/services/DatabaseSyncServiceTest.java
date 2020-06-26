package eu.selfhost.posthuman.soepitimer.services;

import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import eu.selfhost.posthuman.soepitimer.database.WorkdayBreakDao;
import eu.selfhost.posthuman.soepitimer.database.WorkdayDao;
import eu.selfhost.posthuman.soepitimer.model.BreakEntity;
import eu.selfhost.posthuman.soepitimer.model.WorkdayEntity;
import eu.selfhost.posthuman.soepitimer.model.WorkdayEntityWithBreaks;
import eu.selfhost.posthuman.soepitimer.model.workday.Workday;
import eu.selfhost.posthuman.soepitimer.model.workday.WorkdayBreak;
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
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
    private WorkdayDao workdaydaoMock;
    private WorkdayBreakDao breakdaoMock;
    private MockableLocalBroadcastManager broadcastManagerMock;

    @Before
    public void init() {
        workdaydaoMock = mock(WorkdayDao.class);
        breakdaoMock = mock(WorkdayBreakDao.class);
        service.workdayDao = workdaydaoMock;
        service.breakDao = breakdaoMock;
        broadcastManagerMock = mock(MockableLocalBroadcastManager.class);
        service.localBroadcastManager = broadcastManagerMock;
        WorkdayCollection.workday = null;
    }

    @Test
    public void runJob_null() {
        service.runJob(null);

        verifyNoInteractions(workdaydaoMock);
    }

    @Test
    public void runJob_noIdNonExisting() {
        // expect insert of new WorkdayEntity
        WorkdayCollection.workday = new Workday(SOME_DATE);

        WorkdayEntityWithBreaks entityToCreate = new WorkdayEntityWithBreaks();
        entityToCreate.date = SOME_DATE.toString();
        entityToCreate.id = SOME_ID;

        when(workdaydaoMock.findByDate(SOME_DATE.toString())).thenReturn(null, entityToCreate);

        service.runJob(null);

        final ArgumentCaptor<WorkdayEntity> insertWorkdayCapture = ArgumentCaptor.forClass(WorkdayEntity.class);
        final InOrder inOrder = inOrder(workdaydaoMock);
        inOrder.verify(workdaydaoMock).findByDate(SOME_DATE.toString());
        inOrder.verify(workdaydaoMock).insert(insertWorkdayCapture.capture());
        inOrder.verify(workdaydaoMock).findByDate(SOME_DATE.toString());
        verifyNoInteractions(breakdaoMock);

        assertNotNull(insertWorkdayCapture.getValue());
        assertEquals(SOME_DATE.toString(), insertWorkdayCapture.getValue().date);

        assertNotNull(WorkdayCollection.workday);
        assertEquals(SOME_DATE, WorkdayCollection.workday.getDate());
        assertEquals(SOME_ID, WorkdayCollection.workday.getId());
        assertNull(WorkdayCollection.workday.getTimeStart());
        assertNull(WorkdayCollection.workday.getTimeStop());
        assertFalse(WorkdayCollection.workday.isDirty());
        assertEquals(0, WorkdayCollection.workday.getWorkdayBreaks().size());

        verify(broadcastManagerMock).sendBroadcast(same(service), any(Intent.class));
    }

    @Test
    public void runJob_noIdExisting() {
        // expect findByDate returns existing WorkdayEntity
        WorkdayCollection.workday = new Workday(SOME_DATE);

        WorkdayEntityWithBreaks existingEntity = new WorkdayEntityWithBreaks();
        existingEntity.date = SOME_DATE.toString();
        existingEntity.id = SOME_ID;
        existingEntity.workdayStart = SOME_TIME.toString();

        when(workdaydaoMock.findByDate(SOME_DATE.toString())).thenReturn(existingEntity, existingEntity);

        service.runJob(null);

        assertNotNull(WorkdayCollection.workday);
        assertEquals(SOME_DATE, WorkdayCollection.workday.getDate());
        assertEquals(SOME_ID, WorkdayCollection.workday.getId());
        assertEquals(SOME_TIME, WorkdayCollection.workday.getTimeStart());
        assertNull(WorkdayCollection.workday.getTimeStop());
        assertFalse(WorkdayCollection.workday.isDirty());
        verify(workdaydaoMock, never()).insert(any());
        verify(workdaydaoMock, never()).update(any());
        assertEquals(0, WorkdayCollection.workday.getWorkdayBreaks().size());

        verify(broadcastManagerMock).sendBroadcast(same(service), any(Intent.class));
    }

    @Test
    public void runJob_withIdDirty() {
        // expect update of existing WorkdayEntity
        WorkdayCollection.workday = new Workday(SOME_DATE);
        WorkdayCollection.workday.setId(SOME_ID);
        WorkdayCollection.workday.setTimeStop(SOME_TIME);
        WorkdayBreak break_1 = new WorkdayBreak();
        break_1.setId(SOME_ID + 10);
        WorkdayBreak break_2 = new WorkdayBreak();
        break_2.setTimeStart(SOME_TIME);
        WorkdayCollection.workday.getWorkdayBreaks().add(break_1);
        WorkdayCollection.workday.getWorkdayBreaks().add(break_2);

        WorkdayEntityWithBreaks entityToLoad = new WorkdayEntityWithBreaks();
        entityToLoad.date = SOME_DATE.toString();
        entityToLoad.id = SOME_ID;
        entityToLoad.workdayEnd = SOME_TIME.toString();
        BreakEntity break1 = new BreakEntity();
        break1.id = SOME_ID + 10;
        BreakEntity break2 = new BreakEntity();
        break2.id = SOME_ID + 20;
        break2.breakStart = SOME_TIME.toString();
        entityToLoad.breakEntityList = new ArrayList<BreakEntity>();
        entityToLoad.breakEntityList.add(break1);
        entityToLoad.breakEntityList.add(break2);

        when(workdaydaoMock.findByDate(SOME_DATE.toString())).thenReturn(entityToLoad);

        service.runJob(null);

        final ArgumentCaptor<WorkdayEntity> updateWorkdayCapture = ArgumentCaptor.forClass(WorkdayEntity.class);
        final InOrder inOrder = inOrder(workdaydaoMock);
        inOrder.verify(workdaydaoMock).update(updateWorkdayCapture.capture());
        inOrder.verify(workdaydaoMock).findByDate(SOME_DATE.toString());

        final ArgumentCaptor<BreakEntity> insertBreakCapture = ArgumentCaptor.forClass(BreakEntity.class);
        verify(breakdaoMock).insert(insertBreakCapture.capture());
        verifyNoMoreInteractions(breakdaoMock);
        assertNotNull(insertBreakCapture.getValue());
        assertEquals(SOME_TIME.toString(), insertBreakCapture.getValue().breakStart);

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
        assertEquals(2, WorkdayCollection.workday.getWorkdayBreaks().size());
        assertEquals(SOME_ID + 10, WorkdayCollection.workday.getWorkdayBreaks().get(0).getId());
        assertNull(WorkdayCollection.workday.getWorkdayBreaks().get(0).getTimeStart());
        assertEquals(SOME_ID + 20, WorkdayCollection.workday.getWorkdayBreaks().get(1).getId());
        assertEquals(SOME_TIME, WorkdayCollection.workday.getWorkdayBreaks().get(1).getTimeStart());

        verify(broadcastManagerMock).sendBroadcast(same(service), any(Intent.class));
    }

    @Test
    public void runJob_withIdNotDirty() {
        // expect no changes
        WorkdayCollection.workday = new Workday(SOME_DATE);
        WorkdayCollection.workday.setId(SOME_ID);
        WorkdayCollection.workday.setTimeStop(SOME_TIME);
        WorkdayCollection.workday.resetDirty();

        WorkdayEntityWithBreaks entityToLoad = new WorkdayEntityWithBreaks();
        entityToLoad.date = SOME_DATE.toString();
        entityToLoad.id = SOME_ID;
        entityToLoad.workdayEnd = SOME_TIME.toString();

        when(workdaydaoMock.findByDate(SOME_DATE.toString())).thenReturn(entityToLoad);

        service.runJob(null);

        assertNotNull(WorkdayCollection.workday);
        assertEquals(SOME_DATE, WorkdayCollection.workday.getDate());
        assertEquals(SOME_ID, WorkdayCollection.workday.getId());
        assertNull(WorkdayCollection.workday.getTimeStart());
        assertEquals(SOME_TIME, WorkdayCollection.workday.getTimeStop());
        assertFalse(WorkdayCollection.workday.isDirty());
        verify(workdaydaoMock, never()).insert(any());
        verify(workdaydaoMock, never()).update(any());

        verify(broadcastManagerMock).sendBroadcast(same(service), any(Intent.class));
    }
}
