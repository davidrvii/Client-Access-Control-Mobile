package com.example.clientaccesscontrol.data.mikrotikresponse

import com.google.gson.annotations.SerializedName

data class CreateFilterRulesResponse(

	@field:SerializedName("chain")
	val chain: String? = null,

	@field:SerializedName("action")
	val action: String? = null,

	@field:SerializedName("comment")
	val comment: String? = null,

	@field:SerializedName("src-address")
	val srcAddress: String? = null
)
