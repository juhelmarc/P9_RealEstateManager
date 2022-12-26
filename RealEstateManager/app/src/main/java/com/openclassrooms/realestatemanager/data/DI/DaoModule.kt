package com.openclassrooms.realestatemanager.data.DI

import android.content.ContentValues
import android.content.Context
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.data.dao.RoomDao
import com.openclassrooms.realestatemanager.data.database.AppDatabase
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
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
        roomDaoProvider: Provider<RoomDao>
    ): AppDatabase {
        return Room.databaseBuilder(appContext,
            AppDatabase::class.java,
            "AppDatabase.db")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
                        val roomDao = roomDaoProvider.get()

                        roomDao.insertAgent(AgentEntity(name = "007"))
                        roomDao.insertAgent(AgentEntity(name = "Chuck Norris"))
                        roomDao.insertAgent(AgentEntity(name = "Agent Smith"))

                    }
                }
            })
            .build()
    }
    @Provides
    fun provideRoomDao(database: AppDatabase): RoomDao {
        return database.roomDao()
    }




}