# robolectric-instrumentation [ ![Download](https://api.bintray.com/packages/gfx/maven/robolectric-instrumentation/images/download.svg) ](https://bintray.com/gfx/maven/robolectric-instrumentation/)


This is a wrapper for [Robolectric](http://robolectric.org/)
to provide the interface of Android Instrumentation Testing Framework.

## Synopsis

Let it work on **Robolectric**, as well as Android Instrumentation:

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
}
```

## Android Instrumentation and Runners

* `@RunWith(AndroidJUnit4.class)`
* `InstrumentationRegistry.getTargetContext()`
* `InstrumentationRegistry.getInstrumentation()`

## Espresso

Not supported.

## Install

```gradle
dependencies {
    testCompile 'com.github.gfx.android.robolectricinstrumentation:robolectric-instrumentation:3.0.2'
}
```

## Versioning

This library does not adopt semantic versioning. Rather, the first two
parts indicate the compatible version of Robolectric. That is,
this version is currently compatible with Robolectric v3.0.

The third part of the version is the revision of this library.

## See Also

* [Testing Fundamentals | Android Developers](http://developer.android.com/intl/ja/tools/testing/testing_android.html)
*

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
