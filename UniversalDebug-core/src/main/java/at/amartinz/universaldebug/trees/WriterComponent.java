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
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

/**
 * Logs and writes the log to a file.
 */
public class WriterComponent extends BaseTreeComponent {
    private final File logDirectory;
    private final File logFile;

    public WriterComponent(@NonNull BaseTree baseTree) {
        this(baseTree, false);
    }

    public WriterComponent(@NonNull BaseTree baseTree, @Nullable File logDirectory) {
        super(baseTree);
        final Context applicationContext = baseTree.getApplicationContext();

        final Pair<File, File> logFilePair = sanitizeFileDirectories(applicationContext, logDirectory);
        this.logDirectory = logFilePair.first;
        this.logFile = logFilePair.second;
    }

    public WriterComponent(@NonNull BaseTree baseTree, boolean saveOnExternalStorage) {
        super(baseTree);
        final Context applicationContext = baseTree.getApplicationContext();

        File logDir = null;
        if (saveOnExternalStorage) {
            logDir = applicationContext.getExternalFilesDir(null);
        }
        // if we did not decide to save on external storage or we do not have external storage,
        // default to internal storage.
        if (logDir == null) {
            logDir = applicationContext.getFilesDir();
        }

        final Pair<File, File> logFilePair = sanitizeFileDirectories(applicationContext, logDir);
        this.logDirectory = logFilePair.first;
        this.logFile = logFilePair.second;
    }

    private String generateFileName(Context applicationContext) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH);
        final Date date = new Date();
        final String currentTime = sdf.format(date);
        return String.format("log_%s_%s.txt", applicationContext.getPackageName(), currentTime);
    }

    private Pair<File, File> sanitizeFileDirectories(Context applicationContext, @Nullable File logDirectory) {
        final File dir;
        if (logDirectory != null && logDirectory.exists() && logDirectory.canRead() && logDirectory.canWrite()) {
            dir = logDirectory;
        } else {
            dir = applicationContext.getFilesDir();
        }

        final File file = new File(dir, generateFileName(applicationContext));
        if (!file.exists()) {
            try {
                Timber.d("Creating %s -> %s", file, file.createNewFile());
            } catch (Exception ignored) { }
        }
        return new Pair<>(dir, file);
    }

    public File getLogDirectory() {
        return logDirectory;
    }

    public File getLogFile() {
        return logFile;
    }

    @Override protected void doLog(final int priority, final String tag, final String message, final Throwable t) {
        // never do that on the main thread!
        AsyncTask.execute(new Runnable() {
            @Override public void run() {
                writeLogToFile(priority, tag, message, t);
            }
        });
    }

    private void writeLogToFile(int priority, String tag, String message, Throwable t) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(logFile, true);
            fileWriter.write(String.format("%s/%s: %s\n", BaseTree.mapPriorityToString(priority), tag, message));
            fileWriter.flush();
        } catch (IOException ioe) {
            baseTree.reallyDoLog(priority, tag, "Could not write log to file!", ioe);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException ignored) { }
            }
        }
    }
}
