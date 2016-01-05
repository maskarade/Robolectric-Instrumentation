# Robolectric Instrumentation [![Circle CI](https://circleci.com/gh/gfx/Robolectric-Instrumentation.svg?style=svg)](https://circleci.com/gh/gfx/Robolectric-Instrumentation) [ ![Download](https://api.bintray.com/packages/gfx/maven/robolectric-instrumentation/images/download.svg) ](https://bintray.com/gfx/maven/robolectric-instrumentation/)


This is a wrapper for [Robolectric](http://robolectric.org/)
to provide the interface of Android Instrumentation Testing Framework.

## Synopsis

Let it work on **Robolectric**, as well as Android Instrumentation Testing:

```java
package com.github.gfx.android.robolectricinstrumentation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExampleTest {

    boolean runOnAndroid() {
        return System.getProperty("java.vm.name").equals("Dalvik");
    }

    @Test
    public void testGetContext() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        assertThat(context, is(instanceOf(Context.class)));
    }

    @Test
    public void testGetString() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        assertThat(context.getString(R.string.app_name), is("RobolectricInstrumentation"));
    }

    // Because Robolectric-Instrumentation is limited, you can skip tests
    // that only work with Android Instrumentation.
    public void testPerformClickWithEspresso() throws Exception {
        assumeTrue("Robolectric-Instrumentation does not support Espresso", runOnAndroid());

        onView(withId(R.id.button))
                .perform(click());
    }
}
```

## How It Works

This library implements part of `com.android.support.test:runner:0.4.1`,
which provides Android Instrumentation Framework, i.e. JUnit4 runners and `InstrumentationRegistry`.

### Android Instrumentation Framework

* `@RunWith(AndroidJUnit4.class)`
* `InstrumentationRegistry.getTargetContext()`
* `InstrumentationRegistry.getInstrumentation()`

### Espresso

Not supported.

## Dependencies

Depend on Robolectric-Instrumentation as `testCompile`, and `com.android.support.test:runner` as `androidTestCompile`.

Both `testCompile ...` and `androidTestCompile ...` are required.

```gradle
dependencies {
    testCompile 'com.github.gfx.android.robolectricinstrumentation:robolectric-instrumentation:3.0.6'
    testCompile 'junit:junit:4.12'
    androidTestCompile 'com.android.support.test:runner:0.4.1'
    androidTestCompile 'junit:junit:4.12'
}
```

## Getting Started

Suppose your have an Android application project with `app` sub-project, which
already have a test suite with Robolectric.

First, you have to make a Robolectric configuration file as
`app/src/test/resources/roblectric.properties` with the following contents:

```properties
# this is robolectric-instrumentation specific configuration:
project=app
# and other Robolectric configurations
constants=com.example.app.BuildConfig
sdk=16
```

Second, set `testInstrumentationRunner` to `AndroidJUnitRunner` to run Android Instrumentation Tests with JUnit4.

```gradle
// app/build.gradle
android {
  defaultConfig {
    testInstrumentationRunner 'android.support.test.runner.AndroidJUnitRunner'
  }
}
```

Then, rewrite your test cases to use Android Instrumentation Framework, instead of raw Robolectric API.

```diff
+ @RunWith(AndroidJUnit4.class)
- @RunWith(RobolectricGradleTestRunner.class)
- @Config(constants = BuildConfig.class, sdk = 16)
```

```diff
+ Context context = InstrumentationRegistry.getContext();
- Context context = RuntimeEnvironment.application;
```

Finally, make a symlink to `androidTest`.

```sh
(cd app/src && ln -s test androidTest)
```

Now you can run `./gradlew connectedAndroidTest`, as well as `./gradlew test`

## Practical Examples

* [Migration of the test suite to use Robolectric-Instrumentation by gfx · Pull Request #35 · gfx/Android-Helium](https://github.com/gfx/Android-Helium/pull/35)

## Versioning

This library does not adopt semantic versioning. Rather, the first two
parts indicate the compatible version of Robolectric. That is,
this version is currently compatible with Robolectric v3.0.

The third part of the version is the revision of this library.

## See Also

* [Testing Fundamentals | Android Developers](http://developer.android.com/intl/ja/tools/testing/testing_android.html)
* [android.support.test | Android Developers](http://developer.android.com/reference/android/support/test/package-summary.html)

## Author

FUJI Goro ([gfx](https://github.com/gfx)).

## License

Copyright (c) 2015 FUJI Goro (gfx).

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
