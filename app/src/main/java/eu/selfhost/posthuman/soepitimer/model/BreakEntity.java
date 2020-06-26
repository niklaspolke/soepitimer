package eu.selfhost.posthuman.soepitimer.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
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
@Entity(tableName = "workdaybreak", foreignKeys = @ForeignKey(entity = WorkdayEntity.class,
        parentColumns = "id",
        childColumns = "workdayId",
        onDelete = ForeignKey.CASCADE), indices = {@Index("workdayId")})
public class BreakEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "workdayId")
    public long workdayId;

    @ColumnInfo(name = "break_start")
    public String breakStart;

    @ColumnInfo(name = "break_stop")
    public String breakEnd;
}
