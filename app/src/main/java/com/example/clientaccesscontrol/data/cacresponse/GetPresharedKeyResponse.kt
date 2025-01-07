package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class GetPresharedKeyResponse(

	@field:SerializedName("presharedKeys")
	val presharedKeys: List<PresharedKeysItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class PresharedKeysItem(

	@field:SerializedName("update_at")
	val updateAt: String? = null,

	@field:SerializedName("preshared_key")
	val presharedKey: String? = null,

	@field:SerializedName("create_at")
	val createAt: String? = null,

	@field:SerializedName("preshared_key_id")
	val presharedKeyId: Int? = null
)
