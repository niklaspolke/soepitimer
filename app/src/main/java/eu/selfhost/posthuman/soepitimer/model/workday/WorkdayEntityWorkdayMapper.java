package eu.selfhost.posthuman.soepitimer.model.workday;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import eu.selfhost.posthuman.soepitimer.model.BreakEntity;
import eu.selfhost.posthuman.soepitimer.model.WorkdayEntityWithBreaks;

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

    public static Workday importWorkdayEntity(final WorkdayEntityWithBreaks entity) {
        Workday result = null;
        if (entity != null && entity.id != Workday.NO_ID) {
            final LocalDate date = parseDateNullSafe(entity.date);
            if (date != null) {
                result = new Workday(date);
                result.setId(entity.id);
                result.setTimeStart(parseTimeNullSafe(entity.workdayStart));
                result.setTimeStop(parseTimeNullSafe(entity.workdayEnd));
                if (entity.breakEntityList != null) {
                    for (BreakEntity breakEntity : entity.breakEntityList) {
                        WorkdayBreak wdBreak = new WorkdayBreak();
                        wdBreak.setId(breakEntity.id);
                        wdBreak.setTimeStart(parseTimeNullSafe(breakEntity.breakStart));
                        wdBreak.setTimeStop(parseTimeNullSafe(breakEntity.breakEnd));
                        result.getWorkdayBreaks().add(wdBreak);
                    }
                }
                result.resetDirty();
            }
        }
        return result;
    }

    public static WorkdayEntityWithBreaks exportWorkday(final Workday workday) {
        WorkdayEntityWithBreaks result = null;
        if (workday != null && workday.getDate() != null) {
            result = new WorkdayEntityWithBreaks();
            result.date = workday.getDate().toString();
            result.id = workday.getId();
            result.workdayStart = toStringNullSafe(workday.getTimeStart());
            result.workdayEnd = toStringNullSafe(workday.getTimeStop());
            result.breakEntityList = new ArrayList<BreakEntity>();
            if (workday.getWorkdayBreaks().size() > 0) {
                for (WorkdayBreak oneBreak : workday.getWorkdayBreaks()) {
                    final BreakEntity breakEntity = new BreakEntity();
                    if (Workday.NO_ID != oneBreak.getId()) {
                        breakEntity.id = oneBreak.getId();
                    }
                    breakEntity.workdayId = result.id;
                    breakEntity.breakStart = toStringNullSafe(oneBreak.getTimeStart());
                    breakEntity.breakEnd = toStringNullSafe(oneBreak.getTimeStop());
                    result.breakEntityList.add(breakEntity);
                }
            }
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
