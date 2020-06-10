package eu.selfhost.posthuman.soepitimer.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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
@Database(entities = {WorkdayEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WorkdayDao workdayDao();

    public static final String DB_NAME = "timetrackerDB";

    private static AppDatabase singletonDatabase = null;

    public static void initializeDatabase(final Context context) {
        singletonDatabase = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                .enableMultiInstanceInvalidation()
                .build();
    }

    public static AppDatabase get() {
        return singletonDatabase;
    }
}
