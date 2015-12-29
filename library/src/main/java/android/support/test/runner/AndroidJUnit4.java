/*
 * Copyright (c) 2015 FUJI Goro (gfx).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.test.runner;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.FileFsFile;
import org.robolectric.util.Logger;
import org.robolectric.util.ReflectionHelpers;

import java.io.File;
import java.util.Properties;

/**
 * <p>An {@link AndroidJUnit4} implementation with {@link RobolectricGradleTestRunner}.</p>
 * <p>The {@code project} configuration must be set in {@code robolectric.properties}</p>
 *
 * <p>Usage: {@code @RunWith(AndroidJUnit4.class)}</p>
 */
public class AndroidJUnit4 extends RobolectricGradleTestRunner {

    private static final String BUILD_OUTPUT = "build/intermediates";

    Properties configProperties;

    public AndroidJUnit4(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    private static String getType(Config config) {
        try {
            return ReflectionHelpers.getStaticField(config.constants(), "BUILD_TYPE");
        } catch (Throwable e) {
            return null;
        }
    }

    private static String getFlavor(Config config) {
        try {
            return ReflectionHelpers.getStaticField(config.constants(), "FLAVOR");
        } catch (Throwable e) {
            return null;
        }
    }

    private static String getPackageName(Config config) {
        try {
            final String packageName = config.packageName();
            if (packageName != null && !packageName.isEmpty()) {
                return packageName;
            } else {
                return ReflectionHelpers.getStaticField(config.constants(), "APPLICATION_ID");
            }
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        if (config.constants() == null || config.constants().equals(Void.class)) {
            return null;
        }

        String project = guessProjectDir();

        // Same as RobolectricGradleRunner#getAppManifest() except for this version can handle the "project" directry

        final String type = getType(config);
        final String flavor = getFlavor(config);
        final String packageName = getPackageName(config);

        final FileFsFile res;
        final FileFsFile assets;
        final FileFsFile manifest;

        if (FileFsFile.from(project, BUILD_OUTPUT, "res", "merged").exists()) {
            res = FileFsFile.from(project, BUILD_OUTPUT, "res", "merged", flavor, type);
        } else if (FileFsFile.from(project, BUILD_OUTPUT, "res").exists()) {
            res = FileFsFile.from(project, BUILD_OUTPUT, "res", flavor, type);
        } else {
            res = FileFsFile.from(project, BUILD_OUTPUT, "bundles", flavor, type, "res");
        }

        if (FileFsFile.from(project, BUILD_OUTPUT, "assets").exists()) {
            assets = FileFsFile.from(project, BUILD_OUTPUT, "assets", flavor, type);
        } else {
            assets = FileFsFile.from(project, BUILD_OUTPUT, "bundles", flavor, type, "assets");
        }

        if (FileFsFile.from(project, BUILD_OUTPUT, "manifests").exists()) {
            manifest = FileFsFile.from(project, BUILD_OUTPUT, "manifests", "full", flavor, type, "AndroidManifest.xml");
        } else {
            manifest = FileFsFile.from(project, BUILD_OUTPUT, "bundles", flavor, type, "AndroidManifest.xml");
        }

        Logger.debug("Robolectric assets directory: " + assets.getPath());
        Logger.debug("   Robolectric res directory: " + res.getPath());
        Logger.debug("   Robolectric manifest path: " + manifest.getPath());
        Logger.debug("    Robolectric package name: " + packageName);
        return new AndroidManifest(manifest, res, assets, packageName);
    }

    protected String guessProjectDir() {
        String workingDir = System.getProperty("user.dir");
        String project = getConfigProperties().getProperty("project");

        if (project != null) {
            File projectFile = new File(workingDir, project);
            if (projectFile.exists()) {
                return projectFile.getAbsolutePath();
            }
        }

        return workingDir;
    }

    @Override
    protected synchronized Properties getConfigProperties() {
        if (configProperties == null) {
            configProperties = super.getConfigProperties();
        }
        return configProperties;
    }

}
