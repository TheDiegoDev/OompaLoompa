package com.example.oompaloompa.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oompaloompa.data.repository.CharacterRepository
import com.example.oompaloompa.model.OompaLoompaModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailActivityViewModel(private val repository: CharacterRepository
): ViewModel() {
    private val _result: MutableStateFlow<OompaLoompaModel> = MutableStateFlow(OompaLoompaModel())
    val result = _result.asStateFlow()

    private val _resultError: MutableStateFlow<Throwable> = MutableStateFlow(Throwable())
    val resultError = _resultError.asStateFlow()

    private fun getCharacterById(id: String) {
        viewModelScope.launch {
            repository.getCharacterByID(id)
                .onSuccess {
                    _result.value = it
                }
                .onFailure {
                    _resultError.value = it
                }
        }
    }

    fun getCharacterDataBase(id: String) {
        viewModelScope.launch {
            if (repository.getCharacterByDataBase(id) != null) {
                repository.getCharacterByDataBase(id)?.let {
                    _result.value = it
                }
            } else {
                getCharacterById(id)
            }
        }
    }
}