package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class CreateBTSResponse(

	@field:SerializedName("newBTS")
	val newBTS: NewBTS? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class NewBTS(

	@field:SerializedName("bts")
	val bts: String? = null
)
