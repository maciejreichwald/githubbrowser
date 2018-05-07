package com.rudearts.githubbrowser.data.model

import com.google.gson.annotations.SerializedName

data class UserRest(
        val id:Long,
        val login:String?,
        @SerializedName("avatar_url") val avatar:String?,
        val followers:Int
)