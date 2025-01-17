// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package org.jetbrains.kotlin.idea.k2.codeinsight

import com.intellij.openapi.util.registry.Registry
import com.intellij.psi.PsiConstantEvaluationHelper.AuxEvaluator
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.ConstantExpressionEvaluator
import org.jetbrains.kotlin.analysis.api.KtAllowAnalysisFromWriteAction
import org.jetbrains.kotlin.analysis.api.KtAllowAnalysisOnEdt
import org.jetbrains.kotlin.analysis.api.analyze
import org.jetbrains.kotlin.analysis.api.base.KtConstantValue
import org.jetbrains.kotlin.analysis.api.components.KtConstantEvaluationMode
import org.jetbrains.kotlin.analysis.api.lifetime.allowAnalysisFromWriteAction
import org.jetbrains.kotlin.analysis.api.lifetime.allowAnalysisOnEdt
import org.jetbrains.kotlin.asJava.unwrapped
import org.jetbrains.kotlin.psi.KtExpression

class KotlinFirConstantExpressionEvaluator : ConstantExpressionEvaluator {
    override fun computeConstantExpression(
        expression: PsiElement,
        throwExceptionOnOverflow: Boolean
    ): Any? {
        return computeExpression(expression, throwExceptionOnOverflow, null)
    }

    override fun computeExpression(
        expression: PsiElement,
        throwExceptionOnOverflow: Boolean,
        auxEvaluator: AuxEvaluator?
    ): Any? {
        val ktExpression = expression.unwrapped as? KtExpression ?: return null
        val analyze = {
            analyze(ktExpression) {
                ktExpression.evaluate(KtConstantEvaluationMode.CONSTANT_LIKE_EXPRESSION_EVALUATION)
                    ?.takeUnless { it is KtConstantValue.KtErrorConstantValue }?.value
            }
        }

        if (!Registry.`is`("kotlin.fir.allow.constant.computation.on.EDT")) {
            return analyze()
        }

        @OptIn(KtAllowAnalysisOnEdt::class, KtAllowAnalysisFromWriteAction::class)
        return allowAnalysisFromWriteAction {
            allowAnalysisOnEdt {
                analyze()
            }
        }
    }
}