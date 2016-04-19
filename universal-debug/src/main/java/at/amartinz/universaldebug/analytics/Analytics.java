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

package at.amartinz.universaldebug.analytics;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * Singleton interface for interacting with analytics services.<br>
 * Analytics service implementation is abstract and should be extended from {@link AnalyticsComponent} and
 * added to the Analytics via {@link Analytics#addComponent(AnalyticsComponent)}.<br>
 * <br>
 * Override this class for your own needs!
 */
public class Analytics {
    private static Analytics sInstance;

    private final HashSet<AnalyticsComponent> analyticsComponents;

    public static class Constants {
        public static final String EVENT_TEST = "test";

        public static final String EVENT_APP_OPENED = "app_opened";

        public static final String EVENT_CLICKED_GENERIC = "click_generic";
        public static final String EVENT_CLICKED_BUTTON = "click_button";
    }

    private Analytics() {
        analyticsComponents = new HashSet<>();
    }

    public static Analytics get() {
        if (sInstance == null) {
            sInstance = new Analytics();
        }
        return sInstance;
    }

    public Analytics addComponent(AnalyticsComponent analyticsComponent) {
        analyticsComponents.add(analyticsComponent);
        return this;
    }

    public Analytics removeComponent(AnalyticsComponent analyticsComponent) {
        analyticsComponents.remove(analyticsComponent);
        return this;
    }

    public Analytics removeComponent(Class clazz) {
        final Iterator<AnalyticsComponent> iterator = analyticsComponents.iterator();
        while (iterator.hasNext()) {
            final AnalyticsComponent component = iterator.next();
            if (clazz.isInstance(component)) {
                iterator.remove();
            }
        }
        return this;
    }

    public Analytics logCustom(@NonNull String eventName, @Nullable Map<Object, Object> attributes) {
        for (final AnalyticsComponent analyticsComponent : analyticsComponents) {
            analyticsComponent.logCustom(eventName, attributes);
        }
        return this;
    }

    public Analytics logAppOpened() {
        for (final AnalyticsComponent analyticsComponent : analyticsComponents) {
            analyticsComponent.logAppOpened();
        }
        return this;
    }

    public Analytics logClickGeneric(@NonNull String name) {
        for (final AnalyticsComponent analyticsComponent : analyticsComponents) {
            analyticsComponent.logClickGeneric(name);
        }
        return this;
    }

    public Analytics logClickButton(@NonNull String name) {
        for (final AnalyticsComponent analyticsComponent : analyticsComponents) {
            analyticsComponent.logClickButton(name);
        }
        return this;
    }
}
