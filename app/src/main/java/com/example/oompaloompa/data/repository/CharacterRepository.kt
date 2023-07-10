package com.example.oompaloompa.data.repository

import com.example.oompaloompa.data.database.OompaLoompaDataBase
import com.example.oompaloompa.data.database.entity.OompaLoompaEntity
import com.example.oompaloompa.data.network.OompaLoompaApi
import com.example.oompaloompa.model.ListOompaLoompa
import com.example.oompaloompa.model.OompaLoompaModel

class CharacterRepository(
    private val api: OompaLoompaApi,
    private val oompaLoompaDataBase: OompaLoompaDataBase) {

    suspend fun getCharacters(numPage: String): Result<ListOompaLoompa> {
        return try {
            val listOompaLoompa = api.getCharacters(numPage)
            addUsers(numPage)
            Result.success(listOompaLoompa)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCharacterByID(id: String): Result<OompaLoompaModel> {
        return try {
            val OompaLoompa = api.getCharacterByID(id)
            Result.success(OompaLoompa)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDataByDataBase(): List<OompaLoompaModel> {
        val data = oompaLoompaDataBase.getOompaLoompaDao().getAllDataDao()
         return data.map {
            OompaLoompaModel(
                it.first_name,
                it.last_name,
                null,
                it.gender,
                it.image,
                it.profession,
                it.email,
                it.age,
                it.country,
                it.height,
                it.id
            )
        }
    }

    private suspend fun addUsers(numPage: String) {
         try {
            val listOompaLoompa = api.getCharacters(numPage)
             val daoModel = listOompaLoompa.results?.map {
                 OompaLoompaEntity(
                     it.first_name,
                     it.last_name,
                     it.gender,
                     it.image,
                     it.profession,
                     it.email,
                     it.age,
                     it.country,
                     it.height,
                     it.id
                 )
             }
             daoModel?.let { oompaLoompaDataBase.getOompaLoompaDao().insertAll(it) }
        } catch (e: Exception) {

        }
    }

    suspend fun getCharacterByDataBase(id: String) : OompaLoompaModel? {
        val character = oompaLoompaDataBase.getOompaLoompaDao().getCharacterDaoByID(id)
        val characterModel = character?.let {
            OompaLoompaModel(
                character.first_name,
                character.last_name,
                null,
                character.gender,
                character.image,
                character.profession,
                character.email,
                character.age,
                character.country,
                character.height,
                character.id
            )
        }
        return characterModel
    }
}






