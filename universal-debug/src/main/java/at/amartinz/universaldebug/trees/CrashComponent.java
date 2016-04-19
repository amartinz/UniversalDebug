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
import android.util.Log;

/**
 * Log with {@link timber.log.Timber#e(Throwable, String, Object...)} with the specified crashPrefix
 * to send a crash report to the specified service.
 */
public abstract class CrashComponent extends BaseTreeComponent {
    public static final String DEFAULT_PREFIX_CRASH = "CRASH: ";

    protected String crashPrefix;

    public CrashComponent(@NonNull BaseTree baseTree) {
        this(baseTree, CrashComponent.DEFAULT_PREFIX_CRASH);
    }

    public CrashComponent(@NonNull BaseTree baseTree, @NonNull String crashPrefix) {
        super(baseTree);
        this.crashPrefix = crashPrefix;
    }

    public CrashComponent setCrashPrefix(@NonNull String crashPrefix) {
        this.crashPrefix = crashPrefix;
        return this;
    }

    @Override protected boolean shouldLog(int priority) {
        return (priority == Log.ERROR);
    }

    protected boolean matchMessage(@Nullable String message) {
        return (message != null && message.startsWith(crashPrefix));
    }

    @NonNull protected String extractFromMessage(@NonNull String message) {
        return message.replaceFirst(crashPrefix, "");
    }

    protected abstract void reportCrash(int priority, String tag, String message, Throwable t);

    @Override protected void doLog(int priority, String tag, String message, Throwable t) {
        if (!matchMessage(message)) {
            return;
        }

        message = extractFromMessage(message);

        reportCrash(priority, tag, message, t);
    }
}
