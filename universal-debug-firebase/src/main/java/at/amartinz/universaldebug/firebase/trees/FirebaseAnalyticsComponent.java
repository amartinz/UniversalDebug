/*
 * The MIT License
 *
 * Copyright (c) 2016 Alexander Martinz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package at.amartinz.universaldebug.firebase.trees;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import at.amartinz.universaldebug.analytics.Analytics;
import at.amartinz.universaldebug.analytics.AnalyticsComponent;

/**
 * Analytics implementation for <a href="https://firebase.google.com/docs/analytics/android/start/">Firebase Analytics</a>.
 */
public class FirebaseAnalyticsComponent extends AnalyticsComponent {
    private final FirebaseAnalytics firebaseAnalytics;

    public FirebaseAnalyticsComponent(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
    }

    @Override public void logCustom(@NonNull String eventName, @Nullable Map<Object, Object> attributes) {
        final Bundle bundle = new Bundle();
        if (attributes != null && !attributes.isEmpty()) {
            final Set<Map.Entry<Object, Object>> entrySet = attributes.entrySet();
            for (final Map.Entry<Object, Object> entry : entrySet) {
                final String key = String.valueOf(entry.getKey());
                final Object valueObject = entry.getValue();
                bundle.putString(key, String.valueOf(valueObject));
            }
        }
        firebaseAnalytics.logEvent(eventName, bundle);
    }

    @Override public void logAppOpened() {
        logCustom(Analytics.Constants.EVENT_APP_OPENED, null);
    }

    @Override public void logClickGeneric(@NonNull String name) {
        final Map<Object, Object> objectMap = new HashMap<>(1);
        objectMap.put("name", name);
        logCustom(Analytics.Constants.EVENT_CLICKED_GENERIC, objectMap);
    }

    @Override public void logClickButton(@NonNull String name) {
        final Map<Object, Object> objectMap = new HashMap<>(1);
        objectMap.put("name", name);
        logCustom(Analytics.Constants.EVENT_CLICKED_BUTTON, objectMap);
    }
}
