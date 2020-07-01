package eu.selfhost.posthuman.soepitimer.model.workday;

import java.time.LocalTime;

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
public class TimeCalculator {

    public static LocalTime calcWorktime(final Workday workday) {
        LocalTime result = null;
        if (workday != null
                && workday.getTimeStart() != null && workday.getTimeStop() != null
                && !workday.getTimeStart().isAfter(workday.getTimeStop())) {
            result = minus(workday.getTimeStop(), workday.getTimeStart());
            result = minus(result, calcBreaktime(workday));
        }
        return result;
    }

    public static LocalTime calcBreaktime(final Workday workday) {
        LocalTime result = null;
        if (workday != null && workday.getWorkdayBreaks() != null && workday.getWorkdayBreaks().size() > 0) {
            for (WorkdayBreak singleBreak : workday.getWorkdayBreaks()) {
                if (singleBreak.getTimeStart() != null && singleBreak.getTimeStop() != null
                        && !singleBreak.getTimeStart().isAfter(singleBreak.getTimeStop())) {
                    final LocalTime difference = minus(singleBreak.getTimeStop(), singleBreak.getTimeStart());
                    result = result == null ? difference : plus(result, difference);
                }
            }
        }
        return result;
    }

    private static LocalTime plus(final LocalTime time1, final LocalTime time2) {
        return time1.plusHours(time2.getHour()).plusMinutes(time2.getMinute());
    }

    private static LocalTime minus(final LocalTime time1, final LocalTime time2) {
        if (time2 == null) {
            return time1;
        } else {
            return time1.minusHours(time2.getHour()).minusMinutes(time2.getMinute());
        }
    }
}
