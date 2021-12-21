package com.example.andersen_homework_6

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "firstName")
    val name: String,
    @Json(name = "lastName")
    val surName: String,
    val phone: String = Random.nextLong(80000000, 9999999999).toString(),
    val picture: String,
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class GeneralResponse(
    val data: MutableList<User>
) : Parcelable