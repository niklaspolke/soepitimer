package eu.selfhost.posthuman.soepitimer.model.workday;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import eu.selfhost.posthuman.soepitimer.model.WorkdayEntity;

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

    private WorkdayEntity entity = new WorkdayEntity();
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
    public void export_null() {
        assertNull(WorkdayEntityWorkdayMapper.exportWorkday(null));
    }

    @Test
    public void export_noId_noTimes() {
        final WorkdayEntity result = WorkdayEntityWorkdayMapper.exportWorkday(day);

        assertNotNull(result);
        assertEquals(SOME_DATE.toString(), result.date);
        assertEquals(Workday.NO_ID, result.id);
        assertNull(result.workdayStart);
        assertNull(result.workdayEnd);
    }

    @Test
    public void export_withId_withTimes() {
        day.setId(SOME_ID);
        day.setTimeStart(SOME_TIME);
        day.setTimeStop(SOME_TIME.plusHours(1));

        final WorkdayEntity result = WorkdayEntityWorkdayMapper.exportWorkday(day);

        assertNotNull(result);
        assertEquals(SOME_DATE.toString(), result.date);
        assertEquals(SOME_ID, result.id);
        assertEquals(SOME_TIME.toString(), result.workdayStart);
        assertEquals(SOME_TIME.plusHours(1).toString(), result.workdayEnd);
    }
}
