package com.example.oompaloompa.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.oompaloompa.data.database.entity.OompaLoompaEntity

@Dao
interface OompaLoompaDao {

    @Query("SELECT * FROM oompa_loompa_table")
    suspend fun getAllDataDao(): List<OompaLoompaEntity>

    @Query("SELECT * FROM oompa_loompa_table WHERE id = :id")
    suspend fun getCharacterDaoByID(id: String): OompaLoompaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quotes: List<OompaLoompaEntity>)
}