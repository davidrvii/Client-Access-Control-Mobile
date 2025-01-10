package com.example.clientaccesscontrol.data.mikrotikresponse

import com.google.gson.annotations.SerializedName

data class UpdateFilterRulesResponse(

	@field:SerializedName("chain")
	val chain: String? = null,

	@field:SerializedName("log")
	val log: String? = null,

	@field:SerializedName("log-prefix")
	val logPrefix: String? = null,

	@field:SerializedName("bytes")
	val bytes: String? = null,

	@field:SerializedName("invalid")
	val invalid: String? = null,

	@field:SerializedName(".id")
	val id: String? = null,

	@field:SerializedName("action")
	val action: String? = null,

	@field:SerializedName("comment")
	val comment: String? = null,

	@field:SerializedName("disabled")
	val disabled: String? = null,

	@field:SerializedName("dynamic")
	val dynamic: String? = null,

	@field:SerializedName("packets")
	val packets: String? = null
)
