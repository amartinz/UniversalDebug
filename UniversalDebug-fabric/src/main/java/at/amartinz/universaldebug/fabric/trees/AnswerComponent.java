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

package at.amartinz.universaldebug.fabric.trees;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.util.Map;
import java.util.Set;

import at.amartinz.universaldebug.analytics.Analytics;
import at.amartinz.universaldebug.analytics.AnalyticsComponent;

/**
 * Analytics implementation for <a href="https://fabric.io/kits/android/answers">Answers from Fabric</a>.
 */
public class AnswerComponent extends AnalyticsComponent {
    @Override public void logCustom(@NonNull String eventName, @Nullable Map<Object, Object> attributes) {
        final CustomEvent customEvent = new CustomEvent(eventName);
        if (attributes != null && !attributes.isEmpty()) {
            final Set<Map.Entry<Object, Object>> entrySet = attributes.entrySet();
            for (final Map.Entry<Object, Object> entry : entrySet) {
                final String key = String.valueOf(entry.getKey());
                final Object valueObject = entry.getValue();
                if (valueObject instanceof Number) {
                    customEvent.putCustomAttribute(key, (Number) valueObject);
                } else {
                    customEvent.putCustomAttribute(key, String.valueOf(valueObject));
                }
            }
        }
        Answers.getInstance().logCustom(customEvent);
    }

    @Override public void logAppOpened() {
        Answers.getInstance().logCustom(new CustomEvent(Analytics.Constants.EVENT_APP_OPENED));
    }

    @Override public void logClickGeneric(@NonNull String name) {
        final CustomEvent customEvent = new CustomEvent(Analytics.Constants.EVENT_CLICKED_GENERIC);
        customEvent.putCustomAttribute("name", name);
        Answers.getInstance().logCustom(customEvent);
    }

    @Override public void logClickButton(@NonNull String name) {
        final CustomEvent customEvent = new CustomEvent(Analytics.Constants.EVENT_CLICKED_BUTTON);
        customEvent.putCustomAttribute("name", name);
        Answers.getInstance().logCustom(customEvent);
    }
}
