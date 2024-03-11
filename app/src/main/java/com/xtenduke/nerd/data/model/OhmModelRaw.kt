package com.xtenduke.nerd.data.model

data class OhmRawResponse(
    val Children: List<OhmRawResponse>?,
    val ImageURL: String?,
    val Max: String?,
    val Min: String?,
    val Text: String,
    val Value: String?,
    val id: Int
)