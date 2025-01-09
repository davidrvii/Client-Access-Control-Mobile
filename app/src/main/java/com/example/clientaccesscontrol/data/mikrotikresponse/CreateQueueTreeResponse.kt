package com.example.clientaccesscontrol.data.mikrotikresponse

import com.google.gson.annotations.SerializedName

data class CreateQueueTreeResponse(

	@field:SerializedName("parent")
	val parent: String? = null,

	@field:SerializedName("burst-limit")
	val burstLimit: String? = null,

	@field:SerializedName("burst-threshold")
	val burstThreshold: String? = null,

	@field:SerializedName("burst-time")
	val burstTime: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("packet-mark")
	val packetMark: String? = null,

	@field:SerializedName("comment")
	val comment: String? = null,

	@field:SerializedName("max-limit")
	val maxLimit: String? = null,

	@field:SerializedName("limit-at")
	val limitAt: String? = null
)
