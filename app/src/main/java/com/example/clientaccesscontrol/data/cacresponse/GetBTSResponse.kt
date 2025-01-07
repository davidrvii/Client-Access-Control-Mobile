package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class GetBTSResponse(

	@field:SerializedName("bts")
	val bts: List<BtsItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class BtsItem(

	@field:SerializedName("bts")
	val bts: String? = null,

	@field:SerializedName("update_at")
	val updateAt: String? = null,

	@field:SerializedName("bts_id")
	val btsId: Int? = null,

	@field:SerializedName("create_at")
	val createAt: String? = null
)
