package com.github.gfx.android.robolectricinstrumentation;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RobolectricInstrumentationTest {

    @Test
    public void testWithoutConfiguration() throws Exception {
        assertThat(InstrumentationRegistry.getTargetContext(), is(instanceOf(Context.class)));
    }
}
