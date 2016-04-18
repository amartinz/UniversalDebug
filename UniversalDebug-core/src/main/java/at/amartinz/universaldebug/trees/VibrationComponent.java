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

import android.Manifest;
import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;

/**
 * A {@link BaseTreeComponent} which vibrates every time {@link BaseTree#log(int, String, String, Throwable)} gets called.<br>
 * <br>
 * <a href="https://youtu.be/otCpCn0l4Wo?t=14s">https://youtu.be/otCpCn0l4Wo?t=14s</a>
 */
public final class VibrationComponent extends BaseTreeComponent {
    public static final long DEFAULT_DURATION = 75;

    private final Vibrator vibrator;
    private final long duration;

    @RequiresPermission(Manifest.permission.VIBRATE)
    public VibrationComponent(@NonNull BaseTree baseTree) {
        this(baseTree, DEFAULT_DURATION);
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    public VibrationComponent(@NonNull BaseTree baseTree, long duration) {
        this(baseTree, duration, null);
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    public VibrationComponent(@NonNull BaseTree baseTree, long duration, @Nullable Vibrator vibrator) {
        super(baseTree);
        this.duration = duration;

        if (vibrator == null) {
            final Context context = baseTree.getApplicationContext();
            final Object vibratorObject = context.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibratorObject instanceof Vibrator && ((Vibrator) vibratorObject).hasVibrator()) {
                this.vibrator = ((Vibrator) vibratorObject);
            } else {
                this.vibrator = null;
            }
        } else {
            this.vibrator = null;
        }
    }

    @Override protected void doLog(int priority, String tag, String message, Throwable t) {
        if (vibrator != null) {
            vibrator.cancel();
            vibrator.vibrate(duration);
        }
    }
}
