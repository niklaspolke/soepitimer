package eu.selfhost.posthuman.soepitimer.services;

import android.app.job.JobParameters;
import android.content.Intent;

import eu.selfhost.posthuman.soepitimer.database.AppDatabase;
import eu.selfhost.posthuman.soepitimer.database.WorkdayDao;
import eu.selfhost.posthuman.soepitimer.model.WorkdayEntity;
import eu.selfhost.posthuman.soepitimer.model.workday.Workday;
import eu.selfhost.posthuman.soepitimer.model.workday.WorkdayCollection;
import eu.selfhost.posthuman.soepitimer.model.workday.WorkdayEntityWorkdayMapper;
import eu.selfhost.posthuman.soepitimer.shared.MockableLocalBroadcastManager;

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
public class DatabaseSyncService extends SingleThreadPoolService {

    public static final String BROADCAST_ACTION_UPDATE = "DatabaseSyncService.BroadcastActionUpdate";

    WorkdayDao workdayDao;
    MockableLocalBroadcastManager localBroadcastManager = new MockableLocalBroadcastManager();

    @Override
    public boolean runJob(JobParameters params) {
        final Workday workday = WorkdayCollection.workday;
        if (workday != null && workday.getDate() != null) {
            if (workday.getId() == Workday.NO_ID) {
                // insert or findByDate
                final WorkdayEntity dbEntity = lazilyGetDao().findByDate(workday.getDate().toString());

                if (dbEntity == null) {
                    // insert new WorkdayEntity for date
                    final WorkdayEntity newEntity = new WorkdayEntity();
                    newEntity.date = workday.getDate().toString();
                    lazilyGetDao().insert(newEntity);
                }
            } else if (workday.isDirty()) {
                // update WorkdayEntity
                lazilyGetDao().update(WorkdayEntityWorkdayMapper.exportWorkday(workday));
            }
            // overwrite local changes with values from database
            WorkdayCollection.workday = WorkdayEntityWorkdayMapper.importWorkdayEntity(lazilyGetDao().findByDate(workday.getDate().toString()));
            WorkdayCollection.workday.resetDirty();
            localBroadcastManager.sendBroadcast(this, new Intent(BROADCAST_ACTION_UPDATE));
        }
        return true;
    }

    private WorkdayDao lazilyGetDao() {
        if (workdayDao == null) {
            // delayed creation to be mockable
            workdayDao = AppDatabase.get().workdayDao();
        }
        return workdayDao;
    }
}
