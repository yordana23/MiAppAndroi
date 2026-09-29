package com.example.superheroapp

import com.google.gson.annotations.SerializedName

data class Hero(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("images") val images: ImageModel,
    @SerializedName("biography") val biography: BiographyModel?,
    @SerializedName("powerstats") val powerstats: PowerStatsModel?,
    @SerializedName("work") val work: WorkModel?
)

data class ImageModel(
    @SerializedName("sm") val sm: String,
    @SerializedName("lg") val lg: String
)

data class BiographyModel(
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("publisher") val publisher: String?,
    @SerializedName("alignment") val alignment: String?,
    @SerializedName("placeOfBirth") val placeOfBirth: String?
)

data class PowerStatsModel(
    @SerializedName("intelligence") val intelligence: Int,
    @SerializedName("strength") val strength: Int,
    @SerializedName("speed") val speed: Int,
    @SerializedName("power") val power: Int,
    @SerializedName("combat") val combat: Int
)

data class WorkModel(
    @SerializedName("base") val base: String?
)