package eu.selfhost.posthuman.soepitimer.model.workday;

import org.junit.Test;

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
public class WorkdayBreakTest {
    private static final LocalTime SOME_TIME = LocalTime.of(19, 10);
    private static final LocalTime SOME_TIME_WITH_SECONDS = SOME_TIME.withSecond(12).withNano(739);

    private WorkdayBreak myBreak = new WorkdayBreak();

    @Test
    public void getId_initial() {
        assertEquals(Workday.NO_ID, myBreak.getId());
    }

    @Test
    public void setId() {
        myBreak.setId(4);
        assertEquals(4, myBreak.getId());
    }

    @Test
    public void isPersisted_false() {
        assertFalse(myBreak.isPersisted());
    }

    @Test
    public void isPersisted_true() {
        myBreak.setId(4);
        assertTrue(myBreak.isPersisted());
    }

    @Test
    public void getTimeStart_initial() {
        assertNull(myBreak.getTimeStart());
    }

    @Test
    public void setTimeStart_normal() {
        myBreak.setTimeStart(SOME_TIME);
        assertEquals(SOME_TIME, myBreak.getTimeStart());
    }

    @Test
    public void setTimeStart_withSeconds() {
        myBreak.setTimeStart(SOME_TIME_WITH_SECONDS);
        assertEquals(SOME_TIME, myBreak.getTimeStart());
    }

    @Test
    public void getTimeStop_initial() {
        assertNull(myBreak.getTimeStop());
    }

    @Test
    public void setTimeStop_normal() {
        myBreak.setTimeStop(SOME_TIME);
        assertEquals(SOME_TIME, myBreak.getTimeStop());
    }

    @Test
    public void setTimeStop_withSeconds() {
        myBreak.setTimeStop(SOME_TIME_WITH_SECONDS);
        assertEquals(SOME_TIME, myBreak.getTimeStop());
    }
}
