/*
 * Copyright (C) 2023 The Android Open Source Project
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

package com.google.android.samples.socialite

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.samples.socialite.ui.Main
import com.google.android.samples.socialite.ui.ShortcutParams
import com.google.android.samples.socialite.widget.SociaLiteAppWidgetReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            // setSystemBarAppearance can be removed after calling enableEdgeToEdge()
            setSystemBarAppearance(isSystemInDarkTheme())

            Main(
                shortcutParams = extractShortcutParams(intent),
            )
            LaunchedEffect(Unit) {
                delay(4000)
                requestToPinWidget()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestToPinWidget(){
        Log.d("TAG", "requestToPinWidget: ")
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val myProvider = ComponentName(this, SociaLiteAppWidgetReceiver::class.java)

        if (appWidgetManager.isRequestPinAppWidgetSupported) {
            Log.d("TAG", "requestToPinWidget: Supporte")
            // Create the PendingIntent object only if your app needs to be notified
            // when the user chooses to pin the widget. Note that if the pinning
            // operation fails, your app isn't notified. This callback receives the ID
            // of the newly pinned widget (EXTRA_APPWIDGET_ID).
            val successCallback = PendingIntent.getBroadcast(
                /* context = */ this,
                /* requestCode = */ 0,
                /* intent = */ Intent(this, SociaLiteAppWidgetReceiver::class.java),
            /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

            appWidgetManager.requestPinAppWidget(myProvider, null, successCallback)
        }
    }

    private fun extractShortcutParams(intent: Intent?): ShortcutParams? {
        if (intent == null || intent.action != Intent.ACTION_SEND) return null
        val shortcutId = intent.getStringExtra(
            ShortcutManagerCompat.EXTRA_SHORTCUT_ID,
        ) ?: return null
        val text = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return null
        return ShortcutParams(shortcutId, text)
    }

    private fun setSystemBarAppearance(isSystemInDarkTheme: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (isSystemInDarkTheme) {
                window?.insetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                )
            } else {
                window?.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                )
            }
        }
    }
}
