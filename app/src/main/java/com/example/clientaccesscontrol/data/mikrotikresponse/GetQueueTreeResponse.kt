package com.example.clientaccesscontrol.data.mikrotikresponse

import com.google.gson.annotations.SerializedName

data class GetQueueTreeResponse(

	@field:SerializedName("queueTree")
	val queueTree: List<GetQueueTreeResponseItem?>? = null
)

data class GetQueueTreeResponseItem(

	@field:SerializedName("parent")
	val parent: String? = null,

	@field:SerializedName("queued-packets")
	val queuedPackets: String? = null,

	@field:SerializedName("burst-time")
	val burstTime: String? = null,

	@field:SerializedName("dropped")
	val dropped: String? = null,

	@field:SerializedName("packet-rate")
	val packetRate: String? = null,

	@field:SerializedName(".id")
	val id: String? = null,

	@field:SerializedName("priority")
	val priority: String? = null,

	@field:SerializedName("queued-bytes")
	val queuedBytes: String? = null,

	@field:SerializedName("bucket-size")
	val bucketSize: String? = null,

	@field:SerializedName("packets")
	val packets: String? = null,

	@field:SerializedName("limit-at")
	val limitAt: String? = null,

	@field:SerializedName("burst-limit")
	val burstLimit: String? = null,

	@field:SerializedName("burst-threshold")
	val burstThreshold: String? = null,

	@field:SerializedName("rate")
	val rate: String? = null,

	@field:SerializedName("bytes")
	val bytes: String? = null,

	@field:SerializedName("invalid")
	val invalid: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("packet-mark")
	val packetMark: String? = null,

	@field:SerializedName("comment")
	val comment: String? = null,

	@field:SerializedName("disabled")
	val disabled: String? = null,

	@field:SerializedName("max-limit")
	val maxLimit: String? = null,

	@field:SerializedName("queue")
	val queue: String? = null
)
