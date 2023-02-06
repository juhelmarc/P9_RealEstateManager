package com.openclassrooms.realestatemanager.data.DI

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.data.dao.*
import com.openclassrooms.realestatemanager.data.database.AppDatabase
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DoaModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context,
        roomDaoProviderAgent: Provider<AgentDao>,
    ): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "AppDatabase.db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
                        val agentDao = roomDaoProviderAgent.get()
                        agentDao.insertAgent(AgentEntity(name = "007"))
                        agentDao.insertAgent(AgentEntity(name = "Chuck Norris"))
                        agentDao.insertAgent(AgentEntity(name = "Agent Smith"))
                    }
                }
            }).build()
    }

    @Provides
    fun provideAgentDao(database: AppDatabase): AgentDao {
        return database.agentDao()
    }

    @Provides
    fun providePictureDao(database: AppDatabase): PictureDao {
        return database.pictureDao()
    }

    @Provides
    fun providePropertyDao(database: AppDatabase): PropertyDao {
        return database.propertyDao()
    }
}