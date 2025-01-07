package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class CreateModeResponse(

	@field:SerializedName("newMode")
	val newMode: NewMode? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class NewMode(

	@field:SerializedName("mode")
	val mode: String? = null
)
