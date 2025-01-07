package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class GetModeResponse(

	@field:SerializedName("modes")
	val modes: List<ModesItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class ModesItem(

	@field:SerializedName("mode")
	val mode: String? = null,

	@field:SerializedName("mode_id")
	val modeId: Int? = null,

	@field:SerializedName("update_at")
	val updateAt: String? = null,

	@field:SerializedName("create_at")
	val createAt: String? = null
)
