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

package at.amartinz.universaldebug.trees;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import timber.log.Timber;

/**
 * Base tree for use with Timber.<br>
 * Allows to add and remove {@link BaseTreeComponent BaseTreeComponents} dynamically
 */
public class BaseTree extends Timber.DebugTree {
    private final Context applicationContext;
    private final HashSet<Integer> priorityFilterSet;

    private final List<BaseTreeComponent> baseTreeComponents;

    /**
     * Creates a {@link BaseTree} to be used with {@link Timber.Tree#plant(Timber.Tree) }.
     *
     * @param applicationContext An application context
     * @param priorityFilterList A Set of priorities to filter, provide an empty Set to allow any priority
     */
    public BaseTree(@NonNull Context applicationContext, @NonNull Set<Integer> priorityFilterList) {
        this.applicationContext = applicationContext;
        this.priorityFilterSet = new HashSet<>(priorityFilterList);

        this.baseTreeComponents = new ArrayList<>();
    }

    @NonNull public Context getApplicationContext() {
        return applicationContext;
    }

    /**
     * @return A list of added {@link BaseTreeComponent BaseTreeComponents}.
     */
    @NonNull public List<BaseTreeComponent> getComponents() {
        return baseTreeComponents;
    }

    /**
     * Adds a {@link BaseTreeComponent} which will receive events and be able to react with custom logic.
     *
     * @param baseTreeComponent The {@link BaseTreeComponent} to add
     * @return The same {@link BaseTree} instance allow chained calls
     */
    public BaseTree addComponent(BaseTreeComponent baseTreeComponent) {
        baseTreeComponents.add(baseTreeComponent);
        return this;
    }

    /**
     * Removes a {@link BaseTreeComponent} which then will not receive any events anymore.
     *
     * @param baseTreeComponent The {@link BaseTreeComponent} to remove
     * @return The same {@link BaseTree} instance allow chained calls
     */
    public BaseTree removeComponent(BaseTreeComponent baseTreeComponent) {
        baseTreeComponents.remove(baseTreeComponent);
        return this;
    }

    /**
     * @see #removeComponent(BaseTreeComponent)
     */
    public BaseTree removeComponent(Class clazz) {
        final Iterator<BaseTreeComponent> iterator = baseTreeComponents.iterator();
        while (iterator.hasNext()) {
            final BaseTreeComponent component = iterator.next();
            if (clazz.isInstance(component)) {
                iterator.remove();
            }
        }
        return this;
    }

    /**
     * DOES NOT ACTUALLY LOG!<br>
     * All log calls are getting forwarded to the added {@link BaseTreeComponent BaseTreeComponents}.<br>
     *
     * @see timber.log.Timber.Tree#log(int, String, String, Throwable)
     */
    @Override protected void log(int priority, String tag, String message, Throwable t) {
        for (final BaseTreeComponent baseTreeComponent : baseTreeComponents) {
            baseTreeComponent.log(priority, tag, message, t);
        }
    }

    /**
     * Calls the {@link timber.log.Timber.DebugTree DebugTree's} log method.<br>
     *
     * @see timber.log.Timber.DebugTree#log(int, String, String, Throwable)
     */
    public void reallyDoLog(int priority, String tag, String message, Throwable t) {
        super.log(priority, tag, message, t);
    }

    /**
     * @param priority The priority, which should be logged
     * @return True, if we should log
     */
    public boolean shouldLog(int priority) {
        if (!priorityFilterSet.isEmpty()) {
            for (final int priorityFilter : priorityFilterSet) {
                // if our priority is filtered, get out of here
                if (priority == priorityFilter) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Maps a {@link Log} priority such as {@link Log#INFO} to a string.
     *
     * @param priority The log priority to map
     * @return A string representation of the priority
     */
    public static String mapPriorityToString(int priority) {
        switch (priority) {
            default:
            case Log.ASSERT: {
                return "WTF";
            }
            case Log.DEBUG: {
                return "D";
            }
            case Log.ERROR: {
                return "E";
            }
            case Log.INFO: {
                return "I";
            }
            case Log.VERBOSE: {
                return "V";
            }
            case Log.WARN: {
                return "W";
            }
        }
    }
}
