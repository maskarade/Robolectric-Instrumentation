/*
 * Copyright (c) 2015 FUJI Goro (gfx).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.test;

import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowLooper;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.test.internal.runner.lifecycle.ActivityLifecycleMonitorImpl;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

/**
 * {@link InstrumentationRegistry} compatibility wrapper for Robolectric.
 *
 * @see <a href="http://developer.android.com/reference/android/support/test/InstrumentationRegistry.html">InstrumentationRegistry</a>
 */
public class InstrumentationRegistry {

    static Instrumentation instrumentation;

    @NonNull
    public static Context getTargetContext() {
        return getInstrumentation().getTargetContext();
    }

    @NonNull
    public static Context getContext() {
        return getInstrumentation().getTargetContext();
    }

    @NonNull
    public static Instrumentation getInstrumentation() {
        if (instrumentation == null) {
            instrumentation = new InstrumentationImpl();
        }
        return instrumentation;
    }

    static class InstrumentationImpl extends Instrumentation {

        final ActivityLifecycleMonitorImpl activityLifecycleMonitor = new ActivityLifecycleMonitorImpl();

        public InstrumentationImpl() {
        }

        @NonNull
        @Override
        public Context getTargetContext() {
            if (RuntimeEnvironment.application == null) {
                throw new RuntimeException("RuntimeEnvironment.application is null. Missing `@RunWith(AndroidJUnit4.class)`?");
            }

            return RuntimeEnvironment.application;
        }

        @NonNull
        @Override
        public Context getContext() {
            return getTargetContext();
        }

        @SuppressWarnings("unchecked")
        @Override
        public Activity startActivitySync(@NonNull Intent intent) {
            ComponentName componentName = intent.getComponent();
            Class<? extends Activity> cls;
            try {
                cls = (Class<? extends Activity>) Class.forName(componentName.getClassName());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            Activity activity = Robolectric.setupActivity(cls);
            ActivityLifecycleMonitorRegistry.registerInstance(activityLifecycleMonitor);
            activityLifecycleMonitor.signalLifecycleChange(Stage.CREATED, activity);
            activityLifecycleMonitor.signalLifecycleChange(Stage.STARTED, activity);
            activityLifecycleMonitor.signalLifecycleChange(Stage.RESUMED, activity);
            return activity;
        }

        @Override
        public void runOnMainSync(@NonNull Runnable task) {
            new Handler(Looper.getMainLooper()).post(task);
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        @Override
        public void waitForIdleSync() {
          ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        }

        @Override
        public void setInTouchMode(boolean inTouch) {
            // nop
        }
    }
}
