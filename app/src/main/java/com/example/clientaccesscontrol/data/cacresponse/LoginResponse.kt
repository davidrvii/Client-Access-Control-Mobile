package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("loginResult")
	val loginResult: LoginResult? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class LoginResult(

	@field:SerializedName("ip_address")
	val ipAddress: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

    @field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
