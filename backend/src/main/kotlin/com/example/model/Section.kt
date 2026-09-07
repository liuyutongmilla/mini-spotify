package com.example.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Groups albums under one home-feed section, e.g. "Popular Albums". */
@Serializable
data class Section(
    @SerialName("section_title")
    val sectionTitle: String,
    val albums: List<Album>
)
