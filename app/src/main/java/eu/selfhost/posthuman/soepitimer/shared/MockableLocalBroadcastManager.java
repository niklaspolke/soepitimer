package eu.selfhost.posthuman.soepitimer.shared;

import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
public class MockableLocalBroadcastManager {

    private LocalBroadcastManager localBroadcastManager;

    private LocalBroadcastManager lazilyGetBroadcastManager(final Context context) {
        if (localBroadcastManager == null) {
            localBroadcastManager = LocalBroadcastManager.getInstance(context);
        }
        return localBroadcastManager;
    }

    public boolean sendBroadcast(Context context, Intent intent) {
        return lazilyGetBroadcastManager(context).sendBroadcast(intent);
    }
}
