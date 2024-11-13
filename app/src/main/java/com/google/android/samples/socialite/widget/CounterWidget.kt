/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.samples.socialite.widget

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

class CounterWidget : GlanceAppWidget() {

    override val sizeMode: SizeMode
        get() = super.sizeMode

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val count = currentState(key = countKey) ?: 0

            GlanceTheme {
                Column(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(GlanceTheme.colors.surface),
                    verticalAlignment = Alignment.Vertical.CenterVertically,
                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
                ) {
                    Text(
                        text = count.toString(),
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
//                            color = ColorProvider(Color.White),
                            fontSize = 26.sp,
                        ),
                    )
                    Button(
                        text = "Increment Counter",
                        onClick = actionRunCallback(CounterIncrementActionCallback::class.java),
                        /*onClick = actionStartActivity<MainActivity>(parameters = actionParametersOf(
                            ActionParameters.Key<String>(AppWidgetManager.EXTRA_APPWIDGET_ID) to id.toString()
                        )*/
                    )
                }
            }
        }
    }
}
