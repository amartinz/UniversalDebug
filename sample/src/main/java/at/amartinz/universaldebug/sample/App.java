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

package at.amartinz.universaldebug.sample;

import android.app.Application;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;

import at.amartinz.universaldebug.UniversalDebug;
import at.amartinz.universaldebug.fabric.FabricConfig;
import at.amartinz.universaldebug.fabric.trees.CrashlyticsComponent;
import at.amartinz.universaldebug.trees.BaseTree;
import at.amartinz.universaldebug.trees.LogComponent;
import at.amartinz.universaldebug.trees.VibrationComponent;

/**
 * Created by amartinz on 18.04.16.
 */
public class App extends Application {
    @Override public void onCreate() {
        super.onCreate();

        final UniversalDebug universalDebug = new UniversalDebug(this)
                .withDebug(BuildConfig.DEBUG)
                .withTimber(true)
                .withDebugTree(buildDebugTree())
                .withProductionTree(buildProductionTree());

        final FabricConfig fabricConfig = new FabricConfig(universalDebug)
                .withAnswers()
                .withCrashlytics();
        universalDebug.withExtension(fabricConfig);

        universalDebug.install();
    }

    private BaseTree buildDebugTree() {
        return buildTree(true);
    }

    private BaseTree buildProductionTree() {
        return buildTree(false);
    }

    private BaseTree buildTree(boolean isDebug) {
        final HashSet<Integer> priorityFilter = new HashSet<>();
        // if we are in release mode, remove all log calls except ERROR and WARN
        if (!isDebug) {
            priorityFilter.addAll(Arrays.asList(Log.ASSERT, Log.DEBUG, Log.INFO, Log.VERBOSE));
        }
        final BaseTree baseTree = new BaseTree(this, priorityFilter);

        final LogComponent logComponent = new LogComponent(baseTree);
        baseTree.addComponent(logComponent);

        if (isDebug) {
            final VibrationComponent vibrationComponent = new VibrationComponent(baseTree);
            // only vibrate on error logs
            final HashSet<Integer> vibrationFilter = new HashSet<>(
                    Arrays.asList(Log.ASSERT, Log.DEBUG, Log.INFO, Log.VERBOSE, Log.WARN));
            vibrationComponent.setPriorityFilterSet(vibrationFilter);
            baseTree.addComponent(vibrationComponent);
        }

        final CrashlyticsComponent crashlyticsComponent = new CrashlyticsComponent(baseTree);
        baseTree.addComponent(crashlyticsComponent);

        return baseTree;
    }
}
