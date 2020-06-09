package eu.selfhost.posthuman.soepitimer.model.workday;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Copyright 2029 Niklas Polke
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
public class TimeCalculatorTest {

    private static final LocalDate SOME_DATE = LocalDate.of(2020, 6, 9);
    private static final LocalTime SOME_TIME = LocalTime.of(17,58);

    private Workday day;

    @Test
    public void calcWorktime_null() {
        assertNull(TimeCalculator.calcWorktime(null));
    }

    @Test
    public void calcWorktime_timeStartNull() {
        day = new Workday(SOME_DATE);
        day.setTimeStop(SOME_TIME);

        assertNull(TimeCalculator.calcWorktime(day));
    }

    @Test
    public void calcWorktime_timeStopNull() {
        day = new Workday(SOME_DATE);
        day.setTimeStart(SOME_TIME);

        assertNull(TimeCalculator.calcWorktime(day));
    }

    @Test
    public void calcWorktime_timeStartAfterTimeStop() {
        day = new Workday(SOME_DATE);
        day.setTimeStart(SOME_TIME);
        day.setTimeStop(SOME_TIME.minusMinutes(1));

        assertNull(TimeCalculator.calcWorktime(day));
    }

    @Test
    public void calcWorktime_timesEqual() {
        day = new Workday(SOME_DATE);
        day.setTimeStart(SOME_TIME);
        day.setTimeStop(SOME_TIME);

        final LocalTime result = TimeCalculator.calcWorktime(day);
        assertEquals(0, result.getHour());
        assertEquals(0, result.getMinute());
        assertEquals(0, result.getSecond());
        assertEquals(0, result.getNano());
    }

    @Test
    public void calcWorktime_withTimes() {
        day = new Workday(SOME_DATE);
        day.setTimeStart(SOME_TIME);
        day.setTimeStop(SOME_TIME.plusHours(1).plusMinutes(2).plusSeconds(3).plusNanos(4));

        final LocalTime result = TimeCalculator.calcWorktime(day);
        assertEquals(1, result.getHour());
        assertEquals(2, result.getMinute());
        assertEquals(0, result.getSecond());
        assertEquals(0, result.getNano());
    }
}
