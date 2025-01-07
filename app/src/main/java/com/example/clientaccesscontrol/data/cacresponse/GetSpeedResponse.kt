package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class GetSpeedResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("speed")
	val speed: List<SpeedItem?>? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class SpeedItem(

	@field:SerializedName("internet_speed")
	val internetSpeed: String? = null,

	@field:SerializedName("update_at")
	val updateAt: String? = null,

	@field:SerializedName("speed_id")
	val speedId: Int? = null,

	@field:SerializedName("create_at")
	val createAt: String? = null
)
