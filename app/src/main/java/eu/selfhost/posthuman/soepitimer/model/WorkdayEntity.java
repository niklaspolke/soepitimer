package eu.selfhost.posthuman.soepitimer.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
@Entity(tableName = "workday")
public class WorkdayEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @NonNull
    @ColumnInfo(name = "date", index = true)
    public String date;

    @ColumnInfo(name = "time_start")
    public String workdayStart;

    @ColumnInfo(name = "time_stop")
    public String workdayEnd;
}
