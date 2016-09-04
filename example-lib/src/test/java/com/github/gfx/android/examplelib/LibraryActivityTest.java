package com.github.gfx.android.examplelib;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class LibraryActivityTest {

    @Rule
    public final ActivityTestRule<LibraryActivity> mainActivityRule = new ActivityTestRule<>(LibraryActivity.class);

    @Test
    public void testCreateActivity() throws Exception {
        LibraryActivity mainActivity = mainActivityRule.getActivity();
        assertThat(mainActivity, is(instanceOf(LibraryActivity.class)));
    }
}
