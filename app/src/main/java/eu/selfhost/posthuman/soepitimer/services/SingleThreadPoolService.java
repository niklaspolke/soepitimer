package eu.selfhost.posthuman.soepitimer.services;

import android.app.job.JobParameters;
import android.app.job.JobService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
public abstract class SingleThreadPoolService extends JobService {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public boolean onStartJob(JobParameters params) {
        executor.execute(() -> run(params));
        return true; // yes, service will continue running in another thread
    }

    private void run(JobParameters params) {
        final boolean reschedule = !runJob(params);
        jobFinished(params, reschedule);
    }

    /**
     * @return success of job, false may order reschedule of job
     */
    public abstract boolean runJob(JobParameters params);

    @Override
    public boolean onStopJob(JobParameters params) {
        executor.shutdown();
        return true; // yes, reschedule job (after this interruption of the job)
    }
}
