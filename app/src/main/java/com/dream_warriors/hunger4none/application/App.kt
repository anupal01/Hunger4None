package com.dream_warriors.hunger4none.application

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application()
{
    override fun onCreate()
    {
        super.onCreate()

        Realm.init(this@App)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder().name("database.realm").deleteRealmIfMigrationNeeded().build())

        startKoin {
            androidContext(this@App)
            androidLogger(Level.INFO)
            modules(koinModule)
        }
    }
}