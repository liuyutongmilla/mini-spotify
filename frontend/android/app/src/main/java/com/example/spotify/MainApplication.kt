package com.example.spotify

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/** Application class annotated for Hilt's dependency graph to attach to. */
@HiltAndroidApp
class MainApplication : Application()
