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

package at.amartinz.universaldebug.fabric;

import android.Manifest;
import android.content.Context;
import android.support.annotation.RequiresPermission;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;

import java.util.HashSet;

import at.amartinz.universaldebug.UniversalDebug;
import at.amartinz.universaldebug.UniversalDebugExtension;
import at.amartinz.universaldebug.analytics.Analytics;
import at.amartinz.universaldebug.fabric.trees.AnswerComponent;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;

/**
 * Created by amartinz on 18.04.16.
 */
public class FabricConfig extends UniversalDebugExtension {
    private final Context applicationContext;

    public final HashSet<Kit> kitHashSet;

    @RequiresPermission(Manifest.permission.INTERNET)
    public FabricConfig(UniversalDebug universalDebug) {
        this.applicationContext = universalDebug.getApplicationContext();
        this.kitHashSet = new HashSet<>();
    }

    public FabricConfig withAnswers() {
        kitHashSet.add(new Answers());
        Analytics.get().addComponent(new AnswerComponent());
        return this;
    }

    public FabricConfig withCrashlytics() {
        kitHashSet.add(new Crashlytics());
        return this;
    }

    public Kit[] getKits() {
        return kitHashSet.toArray(new Kit[kitHashSet.size()]);
    }

    public void install() {
        Fabric.with(applicationContext, getKits());
    }
}
