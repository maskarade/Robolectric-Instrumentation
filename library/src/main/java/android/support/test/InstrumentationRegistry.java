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

import org.robolectric.RuntimeEnvironment;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * {@link InstrumentationRegistry} compatibility wrapper for Robolectric.
 *
 * @see <a href="http://developer.android.com/reference/android/support/test/InstrumentationRegistry.html">InstrumentationRegistry</a>
 */
public class InstrumentationRegistry {

    @NonNull
    public static Context getTargetContext() {
        if (RuntimeEnvironment.application == null) {
            throw new RuntimeException("RuntimeEnvironment.applicatin is null. Missing `@RunWith(AndroidJUnit4.class)`?");
        }
        return RuntimeEnvironment.application;
    }
}
