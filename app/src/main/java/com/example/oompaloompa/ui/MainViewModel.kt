package com.example.oompaloompa.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oompaloompa.data.repository.CharacterRepository
import com.example.oompaloompa.model.ListOompaLoompa
import com.example.oompaloompa.model.OompaLoompaModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: CharacterRepository
): ViewModel() {
    private var currentPage: Int = 1
    private var moreItems: Boolean = true
    private val _result: MutableStateFlow<ListOompaLoompa> = MutableStateFlow(ListOompaLoompa())
    val result = _result.asStateFlow()

    private val _resultDao: MutableStateFlow<List<OompaLoompaModel>> = MutableStateFlow(listOf(OompaLoompaModel()))
    val resultDao = _resultDao.asStateFlow()

    private val _resultError: MutableStateFlow<Throwable> = MutableStateFlow(Throwable())
    val resultError = _resultError.asStateFlow()

    fun getCharacters() {
        viewModelScope.launch {
            while (moreItems) {
                repository.getCharacters(currentPage.toString())
                    .onSuccess {
                        _result.value = it
                        if (it.current!! == it.total!!) {
                                moreItems = false
                        }
                        currentPage = it.current!!.plus(1)
                    }
                    .onFailure {
                        _resultError.value = it
                    }
            }

        }
    }

    fun getCharacterDao() {
        viewModelScope.launch {
            _resultDao.value = repository.getDataByDataBase()
        }
    }
}


