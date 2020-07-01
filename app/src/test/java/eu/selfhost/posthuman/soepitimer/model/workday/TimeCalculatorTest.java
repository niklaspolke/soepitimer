package eu.selfhost.posthuman.soepitimer.model.workday;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
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

    @Test
    public void calcBreaktime_noBreaks() {
        day = new Workday(SOME_DATE);

        assertNull(TimeCalculator.calcBreaktime(day));
    }

    @Test
    public void calcBreaktime_breakWithoutTimes() {
        day = new Workday(SOME_DATE);
        WorkdayBreak break1 = new WorkdayBreak();
        day.getWorkdayBreaks().add(break1);

        assertNull(TimeCalculator.calcBreaktime(day));
    }

    @Test
    public void calcBreaktime_breakWithoutStop() {
        day = new Workday(SOME_DATE);
        WorkdayBreak break1 = new WorkdayBreak();
        break1.setTimeStart(SOME_TIME);
        day.getWorkdayBreaks().add(break1);

        assertNull(TimeCalculator.calcBreaktime(day));
    }

    @Test
    public void calcBreaktime_breakWithoutStart() {
        day = new Workday(SOME_DATE);
        WorkdayBreak break1 = new WorkdayBreak();
        break1.setTimeStop(SOME_TIME);
        day.getWorkdayBreaks().add(break1);

        assertNull(TimeCalculator.calcBreaktime(day));
    }

    @Test
    public void calcBreaktime_breakWithTimesEqual() {
        day = new Workday(SOME_DATE);
        WorkdayBreak break1 = new WorkdayBreak();
        break1.setTimeStart(SOME_TIME);
        break1.setTimeStop(SOME_TIME);
        day.getWorkdayBreaks().add(break1);

        final LocalTime result = TimeCalculator.calcBreaktime(day);
        assertEquals(0, result.getHour());
        assertEquals(0, result.getMinute());
        assertEquals(0, result.getSecond());
        assertEquals(0, result.getNano());
    }

    @Test
    public void calcBreaktime_break() {
        day = new Workday(SOME_DATE);
        WorkdayBreak break1 = new WorkdayBreak();
        break1.setTimeStart(SOME_TIME);
        break1.setTimeStop(SOME_TIME.plusHours(1).plusMinutes(2));
        day.getWorkdayBreaks().add(break1);

        final LocalTime result = TimeCalculator.calcBreaktime(day);
        assertEquals(1, result.getHour());
        assertEquals(2, result.getMinute());
        assertEquals(0, result.getSecond());
        assertEquals(0, result.getNano());
    }

    @Test
    public void calcBreaktime_twoBreaks() {
        day = new Workday(SOME_DATE);
        WorkdayBreak break1 = new WorkdayBreak();
        break1.setTimeStart(SOME_TIME);
        break1.setTimeStop(SOME_TIME.plusHours(1).plusMinutes(2));
        day.getWorkdayBreaks().add(break1);
        WorkdayBreak break2 = new WorkdayBreak();
        break2.setTimeStart(SOME_TIME.plusHours(2));
        break2.setTimeStop(SOME_TIME.plusHours(3).plusMinutes(3));
        day.getWorkdayBreaks().add(break2);

        final LocalTime result = TimeCalculator.calcBreaktime(day);
        assertEquals(2, result.getHour());
        assertEquals(5, result.getMinute());
        assertEquals(0, result.getSecond());
        assertEquals(0, result.getNano());
    }

    @Test
    public void calcWorktime_withBreaks() {
        day = new Workday(SOME_DATE);
        day.setTimeStart(SOME_TIME);
        day.setTimeStop(SOME_TIME.plusHours(1).plusMinutes(2));
        WorkdayBreak break1 = new WorkdayBreak();
        break1.setTimeStart(SOME_TIME);
        break1.setTimeStop(SOME_TIME.plusHours(0).plusMinutes(2));
        day.getWorkdayBreaks().add(break1);
        WorkdayBreak break2 = new WorkdayBreak();
        break2.setTimeStart(SOME_TIME.plusHours(2));
        break2.setTimeStop(SOME_TIME.plusHours(2).plusMinutes(3));
        day.getWorkdayBreaks().add(break2);

        final LocalTime result = TimeCalculator.calcWorktime(day);
        assertEquals(0, result.getHour());
        assertEquals(57, result.getMinute());
        assertEquals(0, result.getSecond());
        assertEquals(0, result.getNano());
    }
}
