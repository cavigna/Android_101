package com.cavigna.mmotd.utils

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.cavigna.mmotd.model.models.details.Screenshot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@TypeConverters
class Converters {

    @TypeConverter
    fun fromScreenShot(spokenLanguage: List<Screenshot>):String{
        val type = object : TypeToken<List<Screenshot>>(){}.type

        return Gson().toJson(spokenLanguage, type)?: "[]"
    }


    @TypeConverter
    fun toScreenShot(json:String):List<Screenshot>{
        val type=object : TypeToken<List<Screenshot>>(){}.type
        return Gson().fromJson(json,type)
    }
}