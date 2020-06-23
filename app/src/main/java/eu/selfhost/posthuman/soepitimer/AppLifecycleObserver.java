package eu.selfhost.posthuman.soepitimer;

import android.app.Application;

import androidx.lifecycle.LifecycleObserver;

import eu.selfhost.posthuman.soepitimer.database.AppDatabase;

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
public class AppLifecycleObserver extends Application implements LifecycleObserver {

    @Override
    public void onCreate()
    {
        super.onCreate();
        AppDatabase.initializeDatabase(getApplicationContext());
    }
}
