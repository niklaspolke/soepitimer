package eu.selfhost.posthuman.soepitimer.model.workday;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    public void toString_initial() {
        assertEquals("{ \"id\": -1, \"date\": \"2020-06-09\", \"timeStart\": null, \"timeStop\": null }", day.toString());
    }

    @Test
    public void toString_withTimesAndId() {
        day.setId(4);
        day.setTimeStart(SOME_TIME.minusHours(10));
        day.setTimeStop(SOME_TIME);
        assertEquals("{ \"id\": 4, \"date\": \"2020-06-09\", \"timeStart\": \"07:39\", \"timeStop\": \"17:39\" }", day.toString());
    }
}
