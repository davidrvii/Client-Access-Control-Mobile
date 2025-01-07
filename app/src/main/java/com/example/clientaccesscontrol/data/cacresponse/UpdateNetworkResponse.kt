package com.example.clientaccesscontrol.data.cacresponse

import com.google.gson.annotations.SerializedName

data class UpdateNetworkResponse(

	@field:SerializedName("updatedNetwork")
	val updatedNetwork: UpdatedNetwork? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class UpdatedNetwork(

	@field:SerializedName("network_id")
	val networkId: Int? = null,

	@field:SerializedName("radio_signal")
	val radioSignal: String? = null,

	@field:SerializedName("bts")
	val bts: String? = null,

	@field:SerializedName("ip_radio")
	val ipRadio: String? = null,

	@field:SerializedName("ip_address")
	val ipAddress: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("channel_width")
	val channelWidth: String? = null,

	@field:SerializedName("ssid")
	val ssid: String? = null,

	@field:SerializedName("frequency")
	val frequency: String? = null,

	@field:SerializedName("mode")
	val mode: String? = null,

	@field:SerializedName("radio_name")
	val radioName: String? = null,

	@field:SerializedName("wlan_mac_address")
	val wlanMacAddress: String? = null,

	@field:SerializedName("ap_location")
	val apLocation: String? = null,

	@field:SerializedName("preshared_key")
	val presharedKey: String? = null
)
