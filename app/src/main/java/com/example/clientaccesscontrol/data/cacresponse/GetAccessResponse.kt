package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class GetAccessResponse(

	@field:SerializedName("access")
	val access: List<AccessItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class AccessItem(

	@field:SerializedName("internet_access")
	val internetAccess: String? = null
)
