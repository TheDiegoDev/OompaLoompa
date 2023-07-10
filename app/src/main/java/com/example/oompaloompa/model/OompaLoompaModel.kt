package com.example.oompaloompa.model

data class OompaLoompaModel(
    var first_name: String? = null,
    var last_name: String? = null,
    var favorite: FavoriteModel? = null,
    var gender: String? = null,
    var image: String? = null,
    var profession: String? = null,
    var email: String? = null,
    var age: Int? = null,
    var country: String? = null,
    var height: Int? = null,
    var id: Int? = null
)

class ListOompaLoompa(
    var current: Int? = null,
    var total: Int? = null,
    var results: List<OompaLoompaModel>? = null
    )