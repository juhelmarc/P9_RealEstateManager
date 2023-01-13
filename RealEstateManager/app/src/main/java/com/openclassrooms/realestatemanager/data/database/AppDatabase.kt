package com.openclassrooms.realestatemanager.data.database

import androidx.room.*
import com.openclassrooms.realestatemanager.data.Converters
import com.openclassrooms.realestatemanager.data.dao.*
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyPictureEntity

@Database(
    entities = [
        PropertyEntity::class,
        PropertyPictureEntity::class,
        AgentEntity::class,
//        PoiEntity::class
    ],
    version = 1,
    exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun agentDao(): AgentDao
    abstract fun pictureDao(): PictureDao
    abstract fun propertyDao(): PropertyDao

//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getDatabase(
//            context: Context,
//            scope: CoroutineScope
//        ): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                "app_database"
//                )
//                    // Wipes and rebuilds instead of migrating if no Migration object.
//                    // Migration is not part of this codelab.
//                    .fallbackToDestructiveMigration()
//                    .addCallback(AppDatabaseCallback(scope))
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//        private class AppDatabaseCallback(
//            private val scope: CoroutineScope
//        ) : RoomDatabase.Callback() {
//            /**
//             * Override the onCreate method to populate the database.
//             */
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//                // If you want to keep the data through app restarts,
//                // comment out the following line.
//                INSTANCE?.let { database ->
//                    scope.launch(Dispatchers.IO) {
//                        populateDatabase(database.roomDao())
//                    }
//                }
//            }
//            /**
//             * Populate the database in a new coroutine.
//             * If you want to start with more words, just add them.
//             */
//            suspend fun populateDatabase(roomDao: RoomDao) {
//                // Start the app with a clean database every time.
//                // Not needed if you only populate on creation.
//                //roomDao.deleteAll()
//
//                roomDao.insertAgent(AgentEntity(name = "007"))
//                roomDao.insertAgent(AgentEntity(name = "Chuck Norris"))
//
//            }
//        }
//    }
}