package com.github.gfx.android.robolectricinstrumentation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ExampleTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class);

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
    public void testCreateActivity() throws Exception {
        MainActivity mainActivity = mainActivityRule.getActivity();
        assertThat(mainActivity, is(instanceOf(MainActivity.class)));
    }

    @Test
    public void testPerformClick() throws Exception {
        onView(withId(R.id.button))
                .perform(click());

    }
}
