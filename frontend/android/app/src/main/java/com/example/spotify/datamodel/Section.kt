package com.example.spotify.datamodel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/** Groups albums under one home-feed section, e.g. "Popular Albums". */
data class Section(
    @SerializedName("section_title")
    val sectionTitle: String,
    val albums: List<Album>
) : Serializable
