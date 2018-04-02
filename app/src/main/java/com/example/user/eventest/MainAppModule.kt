package com.example.user.eventest

import android.app.Activity
import android.support.v7.app.AppCompatDelegate

import dagger.Module
import dagger.Provides

/**
 * Created by DR
 * on 31.03.2018.
 */
@Module
class MainAppModule(private val activity: Activity) {

    @Provides
    fun provideAppComp(): AppCompatDelegate {
        return AppCompatDelegate.create(activity, null)
    }
}
