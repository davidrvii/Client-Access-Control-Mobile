package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class DeleteModeResponse(

	@field:SerializedName("modeId")
	val modeId: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)
