package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("registerResult")
	val registerResult: RegisterResult? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class RegisterResult(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("ip_address")
	val ipAddress: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
