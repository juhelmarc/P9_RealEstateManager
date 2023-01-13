package com.openclassrooms.realestatemanager.data

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter


object Converters {

    @TypeConverter
    fun stringToList(value: String?): List<Int> {
        val poiList = mutableListOf<Int>()
        val poiListString = value?.split("\\s*,\\s*".toRegex())?.toTypedArray()
        poiListString?.forEach {
            if(it != "") {
                poiList.add(it.toInt())
            }

        }
        return poiList
    }

    @TypeConverter
    fun listToString(value: List<Int>?): String {
        var poiString = ""
        value?.forEach {
            poiString = "$poiString$it,"
            //poiString.append(it.toString()).append(",")
        }
        return poiString
    }


}