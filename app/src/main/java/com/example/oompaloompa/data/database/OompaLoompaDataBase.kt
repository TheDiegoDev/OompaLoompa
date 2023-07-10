package com.example.oompaloompa.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.oompaloompa.data.database.dao.OompaLoompaDao
import com.example.oompaloompa.data.database.entity.OompaLoompaEntity

@Database(entities = [OompaLoompaEntity::class], version = 1, exportSchema = false)
abstract class OompaLoompaDataBase: RoomDatabase() {

    abstract fun getOompaLoompaDao(): OompaLoompaDao

}