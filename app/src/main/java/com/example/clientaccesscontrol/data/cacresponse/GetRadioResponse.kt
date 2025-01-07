package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class GetRadioResponse(

	@field:SerializedName("radios")
	val radios: List<RadiosItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class RadiosItem(

	@field:SerializedName("update_at")
	val updateAt: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("create_at")
	val createAt: String? = null,

	@field:SerializedName("radio_id")
	val radioId: Int? = null
)
