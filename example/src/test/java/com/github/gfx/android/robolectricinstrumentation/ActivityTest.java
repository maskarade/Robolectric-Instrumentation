package com.github.gfx.android.robolectricinstrumentation;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testCreateActivity() throws Exception {
        MainActivity mainActivity = mainActivityRule.getActivity();
        assertThat(mainActivity, is(instanceOf(MainActivity.class)));
    }

    // NOTE: android.support.test.espresso.base.QueueInterrogator calls MessageQueue#next() via reflection,
    // which is not implemented in robolectric ShadowMessageQueue.
    @Ignore("ShadowMessageQueue#nativePollOnce() raises AssertionError, which is called in perform()")
    @Test
    public void testPerformClick() throws Exception {
        onView(withId(R.id.button))
                .perform(click());

    }
}
