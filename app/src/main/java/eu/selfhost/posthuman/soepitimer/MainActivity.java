package eu.selfhost.posthuman.soepitimer;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import eu.selfhost.posthuman.soepitimer.model.workday.Workday;
import eu.selfhost.posthuman.soepitimer.model.workday.WorkdayCollection;
import eu.selfhost.posthuman.soepitimer.services.DatabaseSyncService;

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
public class MainActivity extends AppCompatActivity {

    public static final int SYNC_JOB_ID = 17;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MMMM.yyyy", Locale.GERMANY);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final String TIME_INITIAL_VALUE = "--:--";


    private JobScheduler jobScheduler;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseBroadcastReceiver();
        jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        WorkdayCollection.workday = new Workday(LocalDate.now());
        syncDataInBackground();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
        syncDataInBackground();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jobScheduler.cancel(SYNC_JOB_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                broadcastReceiver);
    }

    public void actionStart(final View view) {
        WorkdayCollection.workday.setTimeStart(LocalTime.now());
        WorkdayCollection.workday.setTimeStop(null);
        updateView();
        syncDataInBackground();
    }

    public void actionStop(final View view) {
        WorkdayCollection.workday.setTimeStop(LocalTime.now());
        updateView();
        syncDataInBackground();
    }

    private void updateView() {
        if (WorkdayCollection.workday == null || !LocalDate.now().equals(WorkdayCollection.workday.getDate())) {
            WorkdayCollection.workday = new Workday(LocalDate.now());
        }

        TextView textView = findViewById(R.id.workdayDate);
        final Workday workday = WorkdayCollection.workday;
        final LocalDate date = workday.getDate();
        textView.setText(date.format(DATE_FORMATTER));

        textView = findViewById(R.id.startTime);
        if (workday.getTimeStart() != null) {
            textView.setText(workday.getTimeStart().format(TIME_FORMATTER));
        } else {
            textView.setText(TIME_INITIAL_VALUE);
        }

        textView = findViewById(R.id.stopTime);
        if (workday.getTimeStop() != null) {
            textView.setText(workday.getTimeStop().format(TIME_FORMATTER));
        } else {
            textView.setText(TIME_INITIAL_VALUE);
        }

        textView = findViewById(R.id.workTime);
        if (workday.getTimeStart() != null && workday.getTimeStop() != null) {
            textView.setText(workday.getTimeStop().minusHours(workday.getTimeStart().getHour()).minusMinutes(workday.getTimeStart().getMinute()).format(TIME_FORMATTER));
        } else {
            textView.setText(TIME_INITIAL_VALUE);
        }

        if (workday.getTimeStart() != null && workday.getTimeStop() == null) {
            findViewById(R.id.buttonStart).setEnabled(false);
            findViewById(R.id.buttonStop).setEnabled(true);
        } else {
            findViewById(R.id.buttonStart).setEnabled(true);
            findViewById(R.id.buttonStop).setEnabled(false);
        }
    }

    private void initialiseBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (DatabaseSyncService.BROADCAST_ACTION_UPDATE.equals(intent.getAction())) {
                    updateView();
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(DatabaseSyncService.BROADCAST_ACTION_UPDATE));
    }

    private void syncDataInBackground() {
        if (jobScheduler.getPendingJob(SYNC_JOB_ID) == null) {
            final JobInfo syncJobInfo = new JobInfo.Builder(SYNC_JOB_ID, new ComponentName(this, DatabaseSyncService.class))
                    .setMinimumLatency(0)
                    .build();

            jobScheduler.schedule(syncJobInfo);
        }
    }
}