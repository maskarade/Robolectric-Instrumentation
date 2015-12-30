package com.github.gfx.android.robolectricinstrumentation;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Non-UI tests work both on Robolectric and Android Instrumentation.
 */
@RunWith(AndroidJUnit4.class)
public class BasicTest {

    @Test
    public void testGetContext() throws Exception {
        assertThat(InstrumentationRegistry.getTargetContext(), is(instanceOf(Context.class)));
    }

    @Test
    public void testGetString() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        assertThat(context.getString(R.string.app_name), is("RobolectricInstrumentation"));
    }

    @Test
    public void testAssets() throws Exception {
        Context context = InstrumentationRegistry.getContext();
        assertThat(context.getAssets().open("test.json").available(), is(greaterThan(0)));
    }
}
