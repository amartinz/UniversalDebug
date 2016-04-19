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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashSet;

/**
 * A TreeComponent can be attached and removed to the tree at runtime.
 */
public abstract class BaseTreeComponent {
    protected BaseTree baseTree;

    protected HashSet<Integer> priorityFilterSet;

    public BaseTreeComponent(@NonNull BaseTree baseTree) {
        this.baseTree = baseTree;
    }

    /**
     * Called whenever the {@link BaseTree BaseTree's} log method gets called.<br>
     *
     * @see timber.log.Timber.Tree#log(int, String, String, Throwable)
     */
    protected abstract void doLog(int priority, String tag, String message, Throwable t);

    protected void log(int priority, String tag, String message, Throwable t) {
        if (shouldLog(priority)) {
            doLog(priority, tag, message, t);
        }
    }

    /**
     * If a priority filter set is set, it will be used to decide whether {@link #doLog(int, String, String, Throwable)} gets
     * called.<br>
     * If no filter is set, it will call {@link BaseTree#shouldLog(int)}.
     *
     * @see BaseTree#shouldLog(int)
     */
    protected boolean shouldLog(int priority) {
        if (priorityFilterSet == null) {
            return baseTree.shouldLog(priority);
        }

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

    public void setPriorityFilterSet(@Nullable HashSet<Integer> priorityFilterSet) {
        this.priorityFilterSet = priorityFilterSet;
    }
}
