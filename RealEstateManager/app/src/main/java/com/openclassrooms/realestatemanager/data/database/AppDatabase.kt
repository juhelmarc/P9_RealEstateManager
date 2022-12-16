package com.openclassrooms.realestatemanager.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.realestatemanager.data.dao.RoomDao
import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyEntity
import com.openclassrooms.realestatemanager.data.models.entities.PropertyPicturesEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        PropertyEntity::class,
        PropertyPicturesEntity::class,
        AgentEntity::class
    ],
    version = 1,
    exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun roomDao(): RoomDao

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