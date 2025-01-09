package com.example.clientaccesscontrol.data.mikrotikresponse

import com.google.gson.annotations.SerializedName

data class CreateMangleDownloadResponse(

	@field:SerializedName("new-packet-mark")
	val newPacketMark: String? = null,

	@field:SerializedName("chain")
	val chain: String? = null,

	@field:SerializedName("out-interface")
	val outInterface: String? = null,

	@field:SerializedName("passthrough")
	val passthrough: String? = null,

	@field:SerializedName("action")
	val action: String? = null,

	@field:SerializedName("comment")
	val comment: String? = null,

	@field:SerializedName("dst-address")
	val dstAddress: String? = null
)
