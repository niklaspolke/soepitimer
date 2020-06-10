package eu.selfhost.posthuman.soepitimer.model.workday;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import eu.selfhost.posthuman.soepitimer.model.WorkdayEntity;

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
public class WorkdayEntityWorkdayMapper {

    public static Workday importWorkdayEntity(final WorkdayEntity entity) {
        Workday result = null;
        if (entity != null && entity.id != Workday.NO_ID) {
            final LocalDate date = parseDateNullSafe(entity.date);
            if (date != null) {
                result = new Workday(date);
                result.setId(entity.id);
                result.setTimeStart(parseTimeNullSafe(entity.workdayStart));
                result.setTimeStop(parseTimeNullSafe(entity.workdayEnd));
            }
        }
        return result;
    }

    public static WorkdayEntity exportWorkday(final Workday workday) {
        WorkdayEntity result = null;
        if (workday != null && workday.getDate() != null) {
            result = new WorkdayEntity();
            result.date = workday.getDate().toString();
            result.id = workday.getId();
            result.workdayStart = toStringNullSafe(workday.getTimeStart());
            result.workdayEnd = toStringNullSafe(workday.getTimeStop());
        }
        return result;
    }

    private static LocalDate parseDateNullSafe(final String dateAsString) {
        LocalDate parsedDate = null;
        if (dateAsString != null) {
            try {
                parsedDate = LocalDate.parse(dateAsString);
            } catch (DateTimeParseException e) {
                parsedDate = null;
            }
        }
        return parsedDate;
    }

    private static LocalTime parseTimeNullSafe(final String timeAsString) {
        LocalTime parsedTime = null;
        if (timeAsString != null) {
            try {
                parsedTime = LocalTime.parse(timeAsString);
            } catch (DateTimeParseException e) {
                parsedTime = null;
            }
        }
        return parsedTime;
    }

    private static String toStringNullSafe(final LocalTime time) {
        String result = null;
        if (time != null) {
            result = time.toString();
        }
        return result;
    }
}
