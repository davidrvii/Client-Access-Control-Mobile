package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class DeleteChannelWidthResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("channelWidthId")
	val channelWidthId: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)
