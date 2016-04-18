package at.amartinz.universaldebug.sample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import at.amartinz.universaldebug.analytics.Analytics;
import at.amartinz.universaldebug.trees.CrashComponent;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    @Override protected void onCreate(Bundle savedInstanceState) {
        Timber.d("OnCreate got called!");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override protected void onResume() {
        super.onResume();
        HANDLER.postDelayed(errorLogRunnable, 2500);

        Analytics.get().logAppOpened();
    }

    private static final Runnable errorLogRunnable = new Runnable() {
        @Override public void run() {
            Timber.e("%sAny error occurred, i guess...", CrashComponent.DEFAULT_PREFIX_CRASH);
        }
    };
}
