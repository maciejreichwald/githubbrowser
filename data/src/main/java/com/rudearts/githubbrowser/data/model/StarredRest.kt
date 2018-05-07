package com.rudearts.githubbrowser.data.model

import com.google.gson.annotations.SerializedName

data class StarredRest(
        @SerializedName("stargazers_count") val stars:Int)