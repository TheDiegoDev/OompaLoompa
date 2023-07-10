package com.example.oompaloompa.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "oompa_loompa_table")
data class OompaLoompaEntity(
    @ColumnInfo(name = "first_name") val first_name: String?,
    @ColumnInfo(name = "last_name") val last_name: String?,
    @ColumnInfo(name = "gender") val gender: String?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "profession") val profession: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "age") val age: Int?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "height") val height: Int?,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int?
)
