package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class DeleteRadioResponse(

	@field:SerializedName("radioId")
	val radioId: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)
