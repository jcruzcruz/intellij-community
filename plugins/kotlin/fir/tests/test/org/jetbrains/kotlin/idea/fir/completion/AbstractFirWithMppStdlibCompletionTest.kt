// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.

package org.jetbrains.kotlin.idea.fir.completion

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.util.ThrowableRunnable
import org.jetbrains.kotlin.idea.completion.test.KotlinFixtureCompletionBaseTestCase
import org.jetbrains.kotlin.idea.completion.test.firFileName
import org.jetbrains.kotlin.idea.fir.invalidateCaches
import org.jetbrains.kotlin.idea.test.KotlinJdkAndMultiplatformStdlibDescriptor
import org.jetbrains.kotlin.idea.test.KotlinLightProjectDescriptor
import org.jetbrains.kotlin.idea.test.runAll
import org.jetbrains.kotlin.platform.jvm.JvmPlatforms
import org.jetbrains.kotlin.idea.base.test.IgnoreTests

abstract class AbstractFirWithMppStdlibCompletionTest : KotlinFixtureCompletionBaseTestCase() {
    override fun isFirPlugin(): Boolean = true

    override fun getDefaultProjectDescriptor(): KotlinLightProjectDescriptor {
        return KotlinJdkAndMultiplatformStdlibDescriptor.JDK_AND_MULTIPLATFORM_STDLIB_WITH_SOURCES
    }

    override fun tearDown() {
        runAll(
            ThrowableRunnable { project.invalidateCaches() },
            ThrowableRunnable { super.tearDown() }
        )
    }

    override fun fileName(): String = firFileName(super.fileName(), testDataDirectory)

    override fun executeTest(test: () -> Unit) {
        IgnoreTests.runTestIfNotDisabledByFileDirective(dataFile().toPath(), IgnoreTests.DIRECTIVES.IGNORE_K2) {
            super.executeTest(test)
            IgnoreTests.cleanUpIdenticalFirTestFile(dataFile())
        }
    }

    override fun getPlatform() = JvmPlatforms.unspecifiedJvmPlatform
    override fun defaultCompletionType() = CompletionType.BASIC
}