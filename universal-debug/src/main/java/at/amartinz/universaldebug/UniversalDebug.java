package at.amartinz.universaldebug;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import at.amartinz.universaldebug.trees.BaseTree;
import at.amartinz.universaldebug.trees.LogComponent;
import timber.log.Timber;

/**
 * Created by amartinz on 18.04.16.
 */
public class UniversalDebug {
    private final Context applicationContext;
    private final List<UniversalDebugExtension> extensionList;

    private boolean enableDebug;
    private boolean enableTimber;

    private Timber.Tree debugTree;
    private Timber.Tree productionTree;

    @NonNull public Context getApplicationContext() {
        return applicationContext;
    }

    public UniversalDebug(Context applicationContext) {
        this.applicationContext = applicationContext;
        this.extensionList = new ArrayList<>();
    }

    public UniversalDebug withDebug(boolean enableDebug) {
        this.enableDebug = enableDebug;
        return this;
    }

    public UniversalDebug withTimber(boolean enableTimber) {
        this.enableTimber = enableTimber;
        return this;
    }

    public UniversalDebug withDebugTree(Timber.Tree debugTree) {
        this.debugTree = debugTree;
        return this;
    }

    public UniversalDebug withProductionTree(Timber.Tree productionTree) {
        this.productionTree = productionTree;
        return this;
    }

    public UniversalDebug withExtension(UniversalDebugExtension extension) {
        extensionList.add(extension);
        return this;
    }

    public UniversalDebug withExtensions(Collection<? extends UniversalDebugExtension> extensions) {
        extensionList.addAll(extensions);
        return this;
    }

    public void install() {
        if (enableTimber) {
            if (enableDebug) {
                if (debugTree == null) {
                    debugTree = buildDefaultDebugTree(applicationContext);
                }
                Timber.plant(debugTree);
            } else {
                if (productionTree == null) {
                    productionTree = buildDefaultProductionTree(applicationContext);
                }
                Timber.plant(productionTree);
            }
        }

        if (!extensionList.isEmpty()) {
            for (final UniversalDebugExtension extension : extensionList) {
                if (extension.canInstall(enableDebug)) {
                    extension.install();
                }
            }
        }
    }

    public static BaseTree buildDefaultDebugTree(@NonNull Context applicationContext) {
        final BaseTree baseTree = new BaseTree(applicationContext, Collections.EMPTY_SET);
        baseTree.addComponent(new LogComponent(baseTree));
        return baseTree;
    }

    public static BaseTree buildDefaultProductionTree(@NonNull Context applicationContext) {
        final HashSet<Integer> priorityFilter = new HashSet<>(3);
        priorityFilter.add(Log.DEBUG);
        priorityFilter.add(Log.INFO);
        priorityFilter.add(Log.VERBOSE);

        final BaseTree baseTree = new BaseTree(applicationContext, priorityFilter);
        baseTree.addComponent(new LogComponent(baseTree));
        return baseTree;
    }
}
