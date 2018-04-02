package com.example.user.eventest;

import android.support.v7.app.AppCompatDelegate
import dagger.Component
import javax.inject.Singleton

/**
 * Created by DR
 * on 31.03.2018.
 */
@Singleton
@Component(modules = [(MainAppModule::class)])
interface AppCompatComponent {
    fun provideAppCompatDelegate(): AppCompatDelegate
}
