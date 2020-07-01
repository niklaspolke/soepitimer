package eu.selfhost.posthuman.soepitimer.model.workday;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
public class WorkdayTest {

    private static final LocalDate SOME_DATE = LocalDate.of(2020, 6, 9);
    private static final LocalTime SOME_TIME = LocalTime.of(17, 39);
    private static final LocalTime SOME_TIME_WITH_SECONDS = SOME_TIME.withSecond(12).withNano(739);

    private Workday day = new Workday(SOME_DATE);

    @Test
    public void getDate_null() {
        day = new Workday(null);

        assertNull(day.getDate());
    }

    @Test
    public void getDate_date() {
        assertEquals(SOME_DATE, day.getDate());
    }

    @Test
    public void getId_initial() {
        assertEquals(Workday.NO_ID, day.getId());
    }

    @Test
    public void setId() {
        day.setId(4);
        assertEquals(4, day.getId());
    }

    @Test
    public void isPersisted_false() {
        assertFalse(day.isPersisted());
    }

    @Test
    public void isPersisted_true() {
        day.setId(4);
        assertTrue(day.isPersisted());
    }

    @Test
    public void getTimeStart_initial() {
        assertNull(day.getTimeStart());
    }

    @Test
    public void setTimeStart_normal() {
        day.setTimeStart(SOME_TIME);
        assertEquals(SOME_TIME, day.getTimeStart());
    }

    @Test
    public void setTimeStart_withSeconds() {
        day.setTimeStart(SOME_TIME_WITH_SECONDS);
        assertEquals(SOME_TIME, day.getTimeStart());
    }

    @Test
    public void getTimeStop_initial() {
        assertNull(day.getTimeStop());
    }

    @Test
    public void setTimeStop_normal() {
        day.setTimeStop(SOME_TIME);
        assertEquals(SOME_TIME, day.getTimeStop());
    }

    @Test
    public void setTimeStop_withSeconds() {
        day.setTimeStop(SOME_TIME_WITH_SECONDS);
        assertEquals(SOME_TIME, day.getTimeStop());
    }

    @Test
    public void isDirty_initial() {
        assertFalse(day.isDirty());
    }

    @Test
    public void isDirty_setTimeStart() {
        day.setTimeStart(SOME_TIME);

        assertTrue(day.isDirty());
    }

    @Test
    public void isDirty_setTimeStartSame() {
        day.setTimeStart(SOME_TIME);
        resetDirty();
        day.setTimeStart(SOME_TIME);

        assertFalse(day.isDirty());
    }

    @Test
    public void isDirty_setTimeStartEqual() {
        day.setTimeStart(SOME_TIME);
        resetDirty();
        day.setTimeStart(SOME_TIME_WITH_SECONDS);

        assertFalse(day.isDirty());
    }

    @Test
    public void isDirty_setTimeStartToNull() {
        day.setTimeStart(SOME_TIME);
        resetDirty();
        day.setTimeStart(null);

        assertTrue(day.isDirty());
    }

    @Test
    public void isDirty_setTimeStop() {
        day.setTimeStop(SOME_TIME);

        assertTrue(day.isDirty());
    }

    @Test
    public void isDirty_setTimeStopSame() {
        day.setTimeStop(SOME_TIME);
        resetDirty();
        day.setTimeStop(SOME_TIME);

        assertFalse(day.isDirty());
    }

    @Test
    public void isDirty_setTimeStopEqual() {
        day.setTimeStop(SOME_TIME);
        resetDirty();
        day.setTimeStop(SOME_TIME_WITH_SECONDS);

        assertFalse(day.isDirty());
    }

    @Test
    public void isDirty_setTimeStopToNull() {
        day.setTimeStop(SOME_TIME);
        resetDirty();
        day.setTimeStop(null);

        assertTrue(day.isDirty());
    }

    @Test
    public void resetDirty() {
        day.setTimeStart(SOME_TIME);

        day.resetDirty();

        assertFalse(day.isDirty());
    }

    @Test
    public void getWorkdayBreaks_initial() {
        assertNotNull(day.getWorkdayBreaks());
        assertEquals(0, day.getWorkdayBreaks().size());
    }

    @Test
    public void toString_initial() {
        assertEquals("{ \"id\": -1, \"date\": \"2020-06-09\", \"timeStart\": null, \"timeStop\": null, \"breaks\": [ ] }", day.toString());
    }

    @Test
    public void toString_withTimesAndId() {
        day.setId(4);
        day.setTimeStart(SOME_TIME.minusHours(10));
        day.setTimeStop(SOME_TIME);
        assertEquals("{ \"id\": 4, \"date\": \"2020-06-09\", \"timeStart\": \"07:39\", \"timeStop\": \"17:39\", \"breaks\": [ ] }", day.toString());
    }

    @Test
    public void toString_withTimesAndTwoBreaks() {
        day.setId(4);
        day.setTimeStart(SOME_TIME.minusHours(10));
        day.setTimeStop(SOME_TIME);
        WorkdayBreak break1 = new WorkdayBreak();
        break1.setId(14);
        break1.setTimeStart(SOME_TIME.minusHours(9));
        break1.setTimeStop(SOME_TIME.minusHours(8));
        WorkdayBreak break2 = new WorkdayBreak();
        break2.setTimeStart(SOME_TIME.minusHours(7));
        day.getWorkdayBreaks().add(break1);
        day.getWorkdayBreaks().add(break2);
        assertEquals("{ \"id\": 4, \"date\": \"2020-06-09\", \"timeStart\": \"07:39\", \"timeStop\": \"17:39\", \"breaks\": [ { \"id\": 14, \"timeStart\": \"08:39\", \"timeStop\": \"09:39\" }, { \"id\": -1, \"timeStart\": \"10:39\", \"timeStop\": null } ] }", day.toString());
    }
}
