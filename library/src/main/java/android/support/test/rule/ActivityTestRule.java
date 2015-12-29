package android.support.test.rule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.robolectric.Robolectric;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;

public class ActivityTestRule<T extends Activity> implements TestRule {

    protected final Class<T> activityClass;

    protected final boolean initialTouchMode;

    protected final boolean launchActivity;

    protected T activity;

    public ActivityTestRule(Class<T> activityClass) {
        this(activityClass, false);
    }

    public ActivityTestRule(Class<T> activityClass, boolean initialTouchMode) {
        this(activityClass, initialTouchMode, true);
    }

    public ActivityTestRule(Class<T> activityClass, boolean initialTouchMode, boolean launchActivity) {
        this.activityClass = activityClass;
        this.initialTouchMode = initialTouchMode;
        this.launchActivity = launchActivity;
    }

    @NonNull
    public T launchActivity(@Nullable Intent startIntent) {
        final String targetPackage = InstrumentationRegistry.getTargetContext().getPackageName();
        if (null == startIntent) {
            startIntent = getActivityIntent();
        }
        startIntent.setClassName(targetPackage, activityClass.getName());
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        beforeActivityLaunched();
        activity = activityClass.cast(Robolectric.setupActivity(activityClass));

        afterActivityLaunched();
        return activity;
    }

    @Nullable
    public T getActivity() {
        return activity;
    }

    void finishActivity() {
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    @NonNull
    protected Intent getActivityIntent() {
        return new Intent(Intent.ACTION_MAIN);
    }

    protected void beforeActivityLaunched() {
        // empty by default
    }
    protected void afterActivityLaunched() {
        // empty by default
    }
    protected void afterActivityFinished() {
        // empty by default
    }


    @Override
    public Statement apply(Statement base, Description description) {
        return new ActivityStatement(base);
    }

    private class ActivityStatement extends Statement {

        private final Statement base;

        public ActivityStatement(Statement base) {
            this.base = base;
        }

        @Override
        public void evaluate() throws Throwable {
            try {
                if (launchActivity) {
                    activity = launchActivity(getActivityIntent());
                }
                base.evaluate();
            } finally {
                finishActivity();
                afterActivityFinished();
            }
        }
    }

}
