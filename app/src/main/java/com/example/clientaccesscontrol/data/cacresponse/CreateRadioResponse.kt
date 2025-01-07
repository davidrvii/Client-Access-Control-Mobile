package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class CreateRadioResponse(

	@field:SerializedName("newRadio")
	val newRadio: NewRadio? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class NewRadio(

	@field:SerializedName("radio")
	val radio: String? = null
)
