package eu.selfhost.posthuman.soepitimer.model.workday;

import java.time.LocalDate;
import java.time.LocalTime;

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
public class Workday {
    public static final long NO_ID = -1;

    private final LocalDate date;

    private long id = NO_ID;

    private LocalTime timeStart;

    private LocalTime timeStop;

    public Workday(final LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public boolean isPersisted() {
        return getId() != NO_ID;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(final LocalTime newTime) {
        timeStart = cleanSeconds(newTime);
    }

    public LocalTime getTimeStop() {
        return timeStop;
    }

    public void setTimeStop(final LocalTime newTime) {
        timeStop = cleanSeconds(newTime);
    }

    @Override
    public String toString() {
        final StringBuffer str = new StringBuffer();
        str.append("{ \"id\": ").append(getId());
        str.append(", \"date\": \"").append(getDate().toString()).append("\"");
        str.append(", \"timeStart\": ").append(localTimeToString(timeStart));
        str.append(", \"timeStop\": ").append(localTimeToString(timeStop));
        str.append(" }");
        return str.toString();
    }

    private static LocalTime cleanSeconds(final LocalTime time) {
        if (time == null) {
            return null;
        } else {
            return time.withSecond(0).withNano(0);
        }
    }

    private static StringBuffer localTimeToString(final LocalTime time) {
        final StringBuffer str = new StringBuffer();
        if (time == null) {
            str.append("null");
        } else {
            str.append("\"").append(time.toString()).append("\"");
        }
        return str;
    }
}
