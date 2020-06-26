package eu.selfhost.posthuman.soepitimer.model.workday;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import eu.selfhost.posthuman.soepitimer.model.BreakEntity;
import eu.selfhost.posthuman.soepitimer.model.WorkdayEntityWithBreaks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
public class WorkdayEntityWorkdayMapperTest {

    private static final long SOME_ID = 14;
    private static final LocalDate SOME_DATE = LocalDate.of(2020, 6, 10);
    private static final LocalTime SOME_TIME = LocalTime.of(19,26);

    private WorkdayEntityWithBreaks entity = new WorkdayEntityWithBreaks();
    private Workday day = new Workday(SOME_DATE);

    @Test
    public void import_null() {
        assertNull(WorkdayEntityWorkdayMapper.importWorkdayEntity(null));
    }

    @Test
    public void import_noId() {
        entity.id = Workday.NO_ID;
        entity.date = SOME_DATE.toString();

        assertNull(WorkdayEntityWorkdayMapper.importWorkdayEntity(entity));
    }

    @Test
    public void import_noDate() {
        entity.id = SOME_ID;
        entity.date = null;

        assertNull(WorkdayEntityWorkdayMapper.importWorkdayEntity(entity));
    }

    @Test
    public void import_illegalDate() {
        entity.id = SOME_ID;
        entity.date = "thisIsNoDate";

        assertNull(WorkdayEntityWorkdayMapper.importWorkdayEntity(entity));
    }

    @Test
    public void import_illegalStart() {
        entity.id = SOME_ID;
        entity.date = SOME_DATE.toString();
        entity.workdayStart = "illegalTime";
        entity.workdayEnd = SOME_TIME.toString();

        final Workday result = WorkdayEntityWorkdayMapper.importWorkdayEntity(entity);

        assertNotNull(result);
        assertEquals(SOME_ID, result.getId());
        assertEquals(SOME_DATE, result.getDate());
        assertNull(result.getTimeStart());
        assertEquals(SOME_TIME, result.getTimeStop());
    }

    @Test
    public void import_illegalStop() {
        entity.id = SOME_ID;
        entity.date = SOME_DATE.toString();
        entity.workdayStart = SOME_TIME.toString();
        entity.workdayEnd = "illegalTime";

        final Workday result = WorkdayEntityWorkdayMapper.importWorkdayEntity(entity);

        assertNotNull(result);
        assertEquals(SOME_ID, result.getId());
        assertEquals(SOME_DATE, result.getDate());
        assertEquals(SOME_TIME, result.getTimeStart());
        assertNull(result.getTimeStop());
    }

    @Test
    public void import_noTimes() {
        entity.id = SOME_ID;
        entity.date = SOME_DATE.toString();

        final Workday result = WorkdayEntityWorkdayMapper.importWorkdayEntity(entity);

        assertNotNull(result);
        assertEquals(SOME_ID, result.getId());
        assertEquals(SOME_DATE, result.getDate());
        assertNull(result.getTimeStart());
        assertNull(result.getTimeStop());
    }

    @Test
    public void import_full() {
        entity.id = SOME_ID;
        entity.date = SOME_DATE.toString();
        entity.workdayStart = SOME_TIME.toString();
        entity.workdayEnd = SOME_TIME.plusHours(1).toString();

        final Workday result = WorkdayEntityWorkdayMapper.importWorkdayEntity(entity);

        assertNotNull(result);
        assertEquals(SOME_ID, result.getId());
        assertEquals(SOME_DATE, result.getDate());
        assertEquals(SOME_TIME, result.getTimeStart());
        assertEquals(SOME_TIME.plusHours(1), result.getTimeStop());
    }

    @Test
    public void import_full_withBreak() {
        entity.id = SOME_ID;
        entity.date = SOME_DATE.toString();
        entity.workdayStart = SOME_TIME.toString();
        entity.workdayEnd = SOME_TIME.plusHours(1).toString();
        entity.breakEntityList = new ArrayList<BreakEntity>();
        BreakEntity breakEntity = new BreakEntity();
        breakEntity.id = SOME_ID;
        breakEntity.breakStart = SOME_TIME.toString();
        breakEntity.breakEnd = SOME_TIME.plusHours(1).toString();
        entity.breakEntityList.add(breakEntity);

        final Workday result = WorkdayEntityWorkdayMapper.importWorkdayEntity(entity);

        assertNotNull(result);
        assertNotNull(result.getWorkdayBreaks());
        assertEquals(1, result.getWorkdayBreaks().size());
        assertEquals(SOME_ID, result.getWorkdayBreaks().get(0).getId());
        assertEquals(SOME_TIME, result.getWorkdayBreaks().get(0).getTimeStart());
        assertEquals(SOME_TIME.plusHours(1), result.getWorkdayBreaks().get(0).getTimeStop());
    }

    @Test
    public void import_full_withBreak_illegalStart() {
        entity.id = SOME_ID;
        entity.date = SOME_DATE.toString();
        entity.workdayStart = SOME_TIME.toString();
        entity.workdayEnd = SOME_TIME.plusHours(1).toString();
        entity.breakEntityList = new ArrayList<BreakEntity>();
        BreakEntity breakEntity = new BreakEntity();
        breakEntity.id = SOME_ID;
        breakEntity.breakStart = "illegalTime";
        breakEntity.breakEnd = SOME_TIME.plusHours(1).toString();
        entity.breakEntityList.add(breakEntity);

        final Workday result = WorkdayEntityWorkdayMapper.importWorkdayEntity(entity);

        assertNotNull(result);
        assertNotNull(result.getWorkdayBreaks());
        assertEquals(1, result.getWorkdayBreaks().size());
        assertEquals(SOME_ID, result.getWorkdayBreaks().get(0).getId());
        assertNull(result.getWorkdayBreaks().get(0).getTimeStart());
        assertEquals(SOME_TIME.plusHours(1), result.getWorkdayBreaks().get(0).getTimeStop());
    }

    @Test
    public void import_full_withBreak_illegalStop() {
        entity.id = SOME_ID;
        entity.date = SOME_DATE.toString();
        entity.workdayStart = SOME_TIME.toString();
        entity.workdayEnd = SOME_TIME.plusHours(1).toString();
        entity.breakEntityList = new ArrayList<BreakEntity>();
        BreakEntity breakEntity = new BreakEntity();
        breakEntity.id = SOME_ID;
        breakEntity.breakStart = SOME_TIME.toString();
        breakEntity.breakEnd = "illegalTime";
        entity.breakEntityList.add(breakEntity);

        final Workday result = WorkdayEntityWorkdayMapper.importWorkdayEntity(entity);

        assertNotNull(result);
        assertNotNull(result.getWorkdayBreaks());
        assertEquals(1, result.getWorkdayBreaks().size());
        assertEquals(SOME_ID, result.getWorkdayBreaks().get(0).getId());
        assertEquals(SOME_TIME, result.getWorkdayBreaks().get(0).getTimeStart());
        assertNull(result.getWorkdayBreaks().get(0).getTimeStop());
    }

    @Test
    public void import_full_with3Breaks() {
        entity.id = SOME_ID;
        entity.date = SOME_DATE.toString();
        entity.workdayStart = SOME_TIME.toString();
        entity.workdayEnd = SOME_TIME.plusHours(1).toString();
        entity.breakEntityList = new ArrayList<BreakEntity>();
        BreakEntity breakEntity = new BreakEntity();
        breakEntity.id = SOME_ID;
        breakEntity.breakStart = SOME_TIME.toString();
        breakEntity.breakEnd = SOME_TIME.plusHours(1).toString();
        entity.breakEntityList.add(breakEntity);
        breakEntity = new BreakEntity();
        breakEntity.id = SOME_ID + 1;
        breakEntity.breakStart = SOME_TIME.plusHours(2).toString();
        breakEntity.breakEnd = SOME_TIME.plusHours(3).toString();
        entity.breakEntityList.add(breakEntity);
        breakEntity = new BreakEntity();
        breakEntity.id = SOME_ID + 2;
        breakEntity.breakStart = SOME_TIME.plusHours(4).toString();
        breakEntity.breakEnd = SOME_TIME.plusHours(5).toString();
        entity.breakEntityList.add(breakEntity);

        final Workday result = WorkdayEntityWorkdayMapper.importWorkdayEntity(entity);

        assertNotNull(result);
        assertNotNull(result.getWorkdayBreaks());
        assertEquals(3, result.getWorkdayBreaks().size());

        assertEquals(SOME_ID, result.getWorkdayBreaks().get(0).getId());
        assertEquals(SOME_TIME, result.getWorkdayBreaks().get(0).getTimeStart());
        assertEquals(SOME_TIME.plusHours(1), result.getWorkdayBreaks().get(0).getTimeStop());

        assertEquals(SOME_ID + 1, result.getWorkdayBreaks().get(1).getId());
        assertEquals(SOME_TIME.plusHours(2), result.getWorkdayBreaks().get(1).getTimeStart());
        assertEquals(SOME_TIME.plusHours(3), result.getWorkdayBreaks().get(1).getTimeStop());

        assertEquals(SOME_ID + 2, result.getWorkdayBreaks().get(2).getId());
        assertEquals(SOME_TIME.plusHours(4), result.getWorkdayBreaks().get(2).getTimeStart());
        assertEquals(SOME_TIME.plusHours(5), result.getWorkdayBreaks().get(2).getTimeStop());
    }

    @Test
    public void export_null() {
        assertNull(WorkdayEntityWorkdayMapper.exportWorkday(null));
    }

    @Test
    public void export_noId_noTimes() {
        final WorkdayEntityWithBreaks result = WorkdayEntityWorkdayMapper.exportWorkday(day);

        assertNotNull(result);
        assertEquals(SOME_DATE.toString(), result.date);
        assertEquals(Workday.NO_ID, result.id);
        assertNull(result.workdayStart);
        assertNull(result.workdayEnd);
        assertNotNull(result.breakEntityList);
        assertEquals(0, result.breakEntityList.size());
    }

    @Test
    public void export_withId_withTimes() {
        day.setId(SOME_ID);
        day.setTimeStart(SOME_TIME);
        day.setTimeStop(SOME_TIME.plusHours(1));

        final WorkdayEntityWithBreaks result = WorkdayEntityWorkdayMapper.exportWorkday(day);

        assertNotNull(result);
        assertEquals(SOME_DATE.toString(), result.date);
        assertEquals(SOME_ID, result.id);
        assertEquals(SOME_TIME.toString(), result.workdayStart);
        assertEquals(SOME_TIME.plusHours(1).toString(), result.workdayEnd);
        assertNotNull(result.breakEntityList);
        assertEquals(0, result.breakEntityList.size());
    }

    @Test
    public void export_withId_withTimes_and1Break() {
        day.setId(SOME_ID);
        day.setTimeStart(SOME_TIME);
        day.setTimeStop(SOME_TIME.plusHours(1));
        WorkdayBreak break1 = new WorkdayBreak();
        break1.setId(SOME_ID + 10);
        break1.setTimeStart(SOME_TIME.plusMinutes(10));
        break1.setTimeStop(SOME_TIME.plusMinutes(20));
        day.getWorkdayBreaks().add(break1);

        final WorkdayEntityWithBreaks result = WorkdayEntityWorkdayMapper.exportWorkday(day);

        assertNotNull(result);
        assertEquals(SOME_DATE.toString(), result.date);
        assertEquals(SOME_ID, result.id);
        assertEquals(SOME_TIME.toString(), result.workdayStart);
        assertEquals(SOME_TIME.plusHours(1).toString(), result.workdayEnd);

        assertNotNull(result.breakEntityList);
        assertEquals(1, result.breakEntityList.size());
        assertEquals(SOME_ID + 10, result.breakEntityList.get(0).id);
        assertEquals(SOME_TIME.plusMinutes(10).toString(), result.breakEntityList.get(0).breakStart);
        assertEquals(SOME_TIME.plusMinutes(20).toString(), result.breakEntityList.get(0).breakEnd);
    }

    @Test
    public void export_withId_withTimes_and3Breaks() {
        day.setId(SOME_ID);
        day.setTimeStart(SOME_TIME);
        day.setTimeStop(SOME_TIME.plusHours(1));
        WorkdayBreak break1 = new WorkdayBreak();
        break1.setId(SOME_ID + 10);
        break1.setTimeStart(SOME_TIME.plusMinutes(10));
        break1.setTimeStop(SOME_TIME.plusMinutes(20));
        day.getWorkdayBreaks().add(break1);
        WorkdayBreak break2 = new WorkdayBreak();
        break2.setId(SOME_ID + 20);
        break2.setTimeStart(SOME_TIME.plusMinutes(30));
        break2.setTimeStop(SOME_TIME.plusMinutes(40));
        day.getWorkdayBreaks().add(break2);
        WorkdayBreak break3 = new WorkdayBreak();
        break3.setId(SOME_ID + 30);
        break3.setTimeStart(SOME_TIME.plusMinutes(45));
        break3.setTimeStop(SOME_TIME.plusMinutes(50));
        day.getWorkdayBreaks().add(break3);

        final WorkdayEntityWithBreaks result = WorkdayEntityWorkdayMapper.exportWorkday(day);

        assertNotNull(result);
        assertEquals(SOME_DATE.toString(), result.date);
        assertEquals(SOME_ID, result.id);
        assertEquals(SOME_TIME.toString(), result.workdayStart);
        assertEquals(SOME_TIME.plusHours(1).toString(), result.workdayEnd);

        assertNotNull(result.breakEntityList);
        assertEquals(3, result.breakEntityList.size());

        assertEquals(SOME_ID + 10, result.breakEntityList.get(0).id);
        assertEquals(SOME_TIME.plusMinutes(10).toString(), result.breakEntityList.get(0).breakStart);
        assertEquals(SOME_TIME.plusMinutes(20).toString(), result.breakEntityList.get(0).breakEnd);

        assertEquals(SOME_ID + 20, result.breakEntityList.get(1).id);
        assertEquals(SOME_TIME.plusMinutes(30).toString(), result.breakEntityList.get(1).breakStart);
        assertEquals(SOME_TIME.plusMinutes(40).toString(), result.breakEntityList.get(1).breakEnd);

        assertEquals(SOME_ID + 30, result.breakEntityList.get(2).id);
        assertEquals(SOME_TIME.plusMinutes(45).toString(), result.breakEntityList.get(2).breakStart);
        assertEquals(SOME_TIME.plusMinutes(50).toString(), result.breakEntityList.get(2).breakEnd);
    }
}
