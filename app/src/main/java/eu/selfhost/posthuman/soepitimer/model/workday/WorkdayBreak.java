package eu.selfhost.posthuman.soepitimer.model.workday;

import java.time.LocalTime;

import static eu.selfhost.posthuman.soepitimer.model.workday.Workday.NO_ID;

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
public class WorkdayBreak {

    private long id = NO_ID;

    private LocalTime timeStart;

    private LocalTime timeStop;

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

    private static LocalTime cleanSeconds(final LocalTime time) {
        if (time == null) {
            return null;
        } else {
            return time.withSecond(0).withNano(0);
        }
    }
}
