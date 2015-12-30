package com.github.gfx.android.robolectricinstrumentation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

/**
 * Activity testing is limited because Robolectric API is much different from Android Instrumentation API.
 */
@RunWith(AndroidJUnit4.class)
public class ActivityTest {

    boolean runOnAndroid() {
        return System.getProperty("java.vm.name").equals("Dalvik");
    }

    boolean runOnRobolectric() {
        return !runOnAndroid();
    }

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testCreateActivity() throws Exception {
        MainActivity mainActivity = mainActivityRule.getActivity();
        assertThat(mainActivity, is(instanceOf(MainActivity.class)));
    }


    @Test
    public void testPerformClick() throws Exception {
        // NOTE: android.support.test.espresso.base.QueueInterrogator calls MessageQueue#next() via reflection,
        // NOTE: which is not implemented in Robolectric ShadowMessageQueue.
        assumeTrue("Robolectric-Instrumentation does not support Espresso", runOnAndroid());

        onView(withId(R.id.button))
                .perform(click());
    }

    @Test
    public void testListView() throws Exception {
        Activity activity = mainActivityRule.getActivity();
        final ListView listView = (ListView) activity.findViewById(R.id.list);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                adapter.add("foo");
                adapter.add("bar");
                listView.setAdapter(adapter);
            }
        });
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        populateItems(listView);
        assertThat(listView.getChildCount(), is(2));
    }

    void populateItems(final ListView listView) {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                // Robolectric does not populate items unless explicitly call layout()
                listView.measure(0, 0);
                listView.layout(0, 0, 320, 480);
            }
        });
    }
}
