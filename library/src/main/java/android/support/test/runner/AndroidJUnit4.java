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

import org.apache.commons.io.FileUtils;
import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.internal.GradleManifestFactory;
import org.robolectric.internal.ManifestFactory;
import org.robolectric.internal.ManifestIdentifier;
import org.robolectric.res.FileFsFile;
import org.robolectric.util.ReflectionHelpers;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * <p>An {@link AndroidJUnit4} implementation with {@link RobolectricTestRunner}.</p>
 * <p>The {@code project} configuration must be set in {@code robolectric.properties}</p>
 *
 * <p>Usage: {@code @RunWith(AndroidJUnit4.class)}</p>
 */
public class AndroidJUnit4 extends RobolectricTestRunner {

    private static final String BUILD_OUTPUT = "build/intermediates";

    private Properties configProperties;

    public AndroidJUnit4(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected ManifestFactory getManifestFactory(Config config) {
        if (config.constants().equals(Void.class)) {
            return super.getManifestFactory(config);
        }

        return new MyManifestFactory(config);
    }

    @Override
    protected synchronized Properties getConfigProperties() {
        if (configProperties == null) {
            configProperties = super.getConfigProperties();
        }
        return configProperties;
    }

    private class MyManifestFactory extends GradleManifestFactory {

        private final Config config;

        MyManifestFactory(Config config) {
            this.config = config;
        }

        String guessProjectDir() {
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

        private String getType(Config config) {
            try {
                return ReflectionHelpers.getStaticField(config.constants(), "BUILD_TYPE");
            } catch (Throwable e) {
                return null;
            }
        }

        private String getFlavor(Config config) {
            try {
                return ReflectionHelpers.getStaticField(config.constants(), "FLAVOR");
            } catch (Throwable e) {
                return null;
            }
        }

        private String getPackageName(Config config) {
            try {
                final String packageName = config.packageName();
                if (!packageName.isEmpty()) {
                    return packageName;
                } else {
                    return config.constants().getPackage().getName();
                }
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public ManifestIdentifier identify(Config config) {
            String projectDir = guessProjectDir();
            String buildOutputDir = projectDir + File.separator + BUILD_OUTPUT;

            // Same as RobolectricGradleRunner#getAppManifest() except for this version can handle the "project" directry

            final String type = getType(config);
            final String flavor = getFlavor(config);
            final String packageName = getPackageName(config);

            final FileFsFile res;
            final FileFsFile assets;
            final FileFsFile manifest;

            if (FileFsFile.from(buildOutputDir, "res", "merged").exists()) {
                res = FileFsFile.from(buildOutputDir, "res", "merged", flavor, type);
            } else if (FileFsFile.from(buildOutputDir, "res").exists()) {
                res = FileFsFile.from(buildOutputDir, "res", flavor, type);
            } else {
                res = FileFsFile.from(buildOutputDir, "bundles", flavor, type, "res");
            }

            if (FileFsFile.from(buildOutputDir, "assets").exists()) {
                assets = FileFsFile.from(buildOutputDir, "assets", flavor, type);
            } else {
                assets = FileFsFile.from(buildOutputDir, "bundles", flavor, type, "assets");
            }

            if (FileFsFile.from(buildOutputDir, "manifests", "full").exists()) {
                manifest = FileFsFile.from(buildOutputDir, "manifests", "full", flavor, type, "AndroidManifest.xml");
            } else if (FileFsFile.from(buildOutputDir, "manifests", "aapt").exists()) {
                manifest = FileFsFile.from(buildOutputDir, "manifests", "aapt", flavor, type, "AndroidManifest.xml");
            } else {
                manifest = FileFsFile.from(buildOutputDir, "bundles", flavor, type, "AndroidManifest.xml");
            }

            // Merges test assets into BUILD_OUTPUT
            // because Android JVM unit testing does not handle test assets.
            if (FileFsFile.from(projectDir, "src", "test", "assets").exists()) {
                FileFsFile testAssets = FileFsFile.from(projectDir, "src", "test", "assets");
                try {
                    FileUtils.copyDirectory(testAssets.getFile(), assets.getFile());
                } catch (IOException e) {
                    // ignore
                }
            }

            // Merges AAR's assets int BUILD_OUTPUT
            File explodedAar = FileFsFile.from(buildOutputDir, "exploded-aar").getFile();
            if (explodedAar.exists()) {
                forEachFile(explodedAar, new Action1<File>() {
                    @Override
                    public void call(File file) {
                        if (file.isDirectory() && file.getName().equals("assets")) {
                            try {
                                FileUtils.copyDirectory(file, assets.getFile());
                            } catch (IOException e) {
                                // ignore
                            }
                        }
                    }
                });
            }
            return new ManifestIdentifier(manifest, res, assets, packageName, null);
        }
    }

    private static void forEachFile(File dir, Action1<File> action) {
        File[] list = dir.listFiles();
        if (list == null) {
            return;
        }
        for (File file : list) {
            action.call(file);

            if (file.isDirectory()) {
                forEachFile(file, action);
            }
        }
    }

    interface Action1<T> {

        void call(T t);
    }
}
