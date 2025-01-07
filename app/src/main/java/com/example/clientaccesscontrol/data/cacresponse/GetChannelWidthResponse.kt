package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class GetChannelWidthResponse(

	@field:SerializedName("channelWidths")
	val channelWidths: List<ChannelWidthsItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class ChannelWidthsItem(

	@field:SerializedName("channel_width_id")
	val channelWidthId: Int? = null,

	@field:SerializedName("update_at")
	val updateAt: String? = null,

	@field:SerializedName("channel_width")
	val channelWidth: String? = null,

	@field:SerializedName("create_at")
	val createAt: String? = null
)
