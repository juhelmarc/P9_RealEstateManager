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
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun agentDao(): AgentDao
    abstract fun pictureDao(): PictureDao
    abstract fun propertyDao(): PropertyDao

}