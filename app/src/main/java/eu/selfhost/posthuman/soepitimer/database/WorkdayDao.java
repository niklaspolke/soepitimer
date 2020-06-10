package eu.selfhost.posthuman.soepitimer.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
@Dao
public interface WorkdayDao {
    @Query("SELECT * FROM workday WHERE date = :date")
    WorkdayEntity findByDate(String date);

    @Insert
    long insert(WorkdayEntity workday);

    @Update
    void update(WorkdayEntity workday);

    @Delete
    void delete(WorkdayEntity workday);
}
