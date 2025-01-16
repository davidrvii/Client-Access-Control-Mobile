package com.example.clientaccesscontrol.view.ui.networkfilter

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientaccesscontrol.R
import com.example.clientaccesscontrol.data.cacresponse.FilteredClientsItem
import com.example.clientaccesscontrol.data.mikrotikresponse.GetFilterRulesResponseItem
import com.example.clientaccesscontrol.data.mikrotikresponse.GetQueueTreeResponseItem
import com.example.clientaccesscontrol.data.result.Results
import com.example.clientaccesscontrol.databinding.ActivityNetworkFilterBinding
import com.example.clientaccesscontrol.databinding.CustomDeleteDialogBinding
import com.example.clientaccesscontrol.view.ui.clientdetail.ClientDetailActivity
import com.example.clientaccesscontrol.view.utils.FactoryViewModel

class NetworkFilterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNetworkFilterBinding
    private var networkId: Int = 0
    private lateinit var networkName: String
    private lateinit var network: String
    private lateinit var bindingDialog: CustomDeleteDialogBinding
    private lateinit var clientAdapter: FilteredClientAdapter
    private var queueTrees: List<GetQueueTreeResponseItem>? = null
    private var clients: List<FilteredClientsItem?>? = null
    private var filterRules: List<GetFilterRulesResponseItem>? = null
    private val networkFilterViewModel by viewModels<NetworkFilterVM> {
        FactoryViewModel.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNetworkFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkId = intent.getIntExtra(NETWORK_ID, 0)
        networkName = intent.getStringExtra(NETWORK_NAME).toString()
        network = intent.getStringExtra(NETWORK).toString()
        binding.tvTopbar.text = networkName

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupClientList()
        buttonBackAction()
    }

    private fun buttonBackAction() {
        binding.btBack.setOnClickListener {
            finish()
        }

        buttonDeleteAction()
    }

    private fun getFilteredClient() {
        when (network) {
            "BTS" -> networkFilterViewModel.getFilteredClient(networkId, 0, 0, 0, 0 )
            "Radio" -> networkFilterViewModel.getFilteredClient(0, networkId, 0, 0, 0)
            "Mode" -> networkFilterViewModel.getFilteredClient(0, 0, networkId, 0, 0)
            "Channel Width" -> networkFilterViewModel.getFilteredClient(0, 0, 0, networkId, 0)
            "Preshared Key" -> networkFilterViewModel.getFilteredClient(0, 0, 0, 0, networkId)
        }
    }

    override fun onResume() {
        super.onResume()
        if (ClientDetailActivity.UPDATE_CLIENT == "TRUE") {
            ClientDetailActivity.UPDATE_CLIENT = "TRUE"
            getFilteredClient()
        }  else {
            Log.d("MainActivity", "There is No Client to Update")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun buttonDeleteAction() {
        binding.btDelete.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            bindingDialog = CustomDeleteDialogBinding.inflate(layoutInflater)

            dialog.setContentView(bindingDialog.root)
            dialog.setCancelable(true)

            dialog.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val cardView = bindingDialog.root.findViewById<CardView>(R.id.DeleteCard)
            val layoutParams = cardView.layoutParams as ViewGroup.MarginLayoutParams
            val margin = (40 * resources.displayMetrics.density).toInt()
            layoutParams.setMargins(margin, 0, margin, 0)
            cardView.layoutParams = layoutParams

            bindingDialog.tvDelete.text = "Delete $networkName ?"
            bindingDialog.tvDeleteDesc.text = "This $network will be removed from the list"

            bindingDialog.btYesDelete.setOnClickListener {
                dialog.dismiss()
                UPDATE_NETWORK_LIST = "TRUE"
                when (network) {
                    "BTS" -> networkFilterViewModel.deleteBTS(networkId)
                    "Radio" -> networkFilterViewModel.deleteRadio(networkId)
                    "Mode" -> networkFilterViewModel.deleteMode(networkId)
                    "Channel Width" -> networkFilterViewModel.deleteChannelWidth(networkId)
                    "Preshared Key" -> networkFilterViewModel.deletePresharedKey(networkId)
                }
                finish()
                deleteResult()
            }
            bindingDialog.btCancelDelete.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun deleteResult() {
        networkFilterViewModel.deleteBTS.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Results.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error deleting BTS: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }

        networkFilterViewModel.deleteMode.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Results.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error deleting Mode: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }

        networkFilterViewModel.deleteRadio.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Results.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error deleting Radio: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }

        networkFilterViewModel.deleteChannelWidth.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Results.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error deleting ChannelWidth: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }

        networkFilterViewModel.deletePresharedKey.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Results.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error deleting PresharedKey: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun setupClientList() {
        getFilteredClient()
        networkFilterViewModel.getFilteredClient.removeObservers(this)
        networkFilterViewModel.getFilteredClient.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    clients = result.data.filteredClients
                    showLoading(false)
                    Log.d("MainActivity", "Received Client Data: ${result.data.filteredClients}")

                    if (clients!!.isNotEmpty()) {
                        clientAdapter.updateData(result.data.filteredClients?.filterNotNull() ?: emptyList())
                        networkFilterViewModel.getQueueTree()
                    } else {
                        Toast.makeText(this, "No Client Found", Toast.LENGTH_SHORT).show()
                        Log.d("MainActivity", "No Client Found")
                    }
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.e("MainActivity", "Error Getting Client: ${result.error}")
                    finish()
                }
                is Results.Loading -> {
                    showLoading(true)
                    Log.d("MainActivity", "Loading Client Data")
                }
            }
        }

        networkFilterViewModel.getQueueTree.removeObservers(this)
        networkFilterViewModel.getQueueTree.observe(this) { queueTreeResult ->
            when (queueTreeResult) {
                is Results.Success -> {
                    queueTrees = queueTreeResult.data
                    showLoading(false)
                    networkFilterViewModel.getFilterRules()
                    Log.d("MainActivity", "Received Queue Tree Data: ${queueTreeResult.data}")
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.e("MainActivity", "Error Getting Queue Tree: ${queueTreeResult.error}")
                    finish()
                }
                is Results.Loading -> {
                    showLoading(true)
                    Log.d("MainActivity", "Loading Get Queue Tree Data")
                }
            }
        }

        networkFilterViewModel.getFilterRules.removeObservers(this)
        networkFilterViewModel.getFilterRules.observe(this) { filterRulesResult ->
            when (filterRulesResult) {
                is Results.Success -> {
                    filterRules = filterRulesResult.data
                    showLoading(false)
                    updateClientList()
                    Log.d("MainActivity", "Received filter rules data: ${filterRulesResult.data}")
                }

                is Results.Error -> {
                    showLoading(false)
                    Log.e("MainActivity", "Error Getting Filter Rules: ${filterRulesResult.error}")
                }

                is Results.Loading -> {
                    showLoading(true)
                    Log.d("MainActivity", "Loading Filter Rules Data")
                }
            }
        }
        setupClientRecyclerView()
    }

    private fun updateClientList() {
        val queueTreeComments = queueTrees!!.map { it.comment?.trim() }.toSet()
        val clientComments = clients!!.map { it?.comment?.trim() }.toSet()
        val filterRulesComment = filterRules!!.map { it.comment?.trim() }.toSet()

        if (clients != null && queueTrees != null && filterRules != null) {

            //Client Access Change to Not Found If Not Found in Mikrotik but Exist in Database
            val clientCommentNotInQueueTree = clientComments.subtract(queueTreeComments)
            clientCommentNotInQueueTree.forEach { comment ->
                val client = clients!!.find { it?.comment == comment }
                Log.d("MainActivity", "Client to update: $client")
                if (client?.clientId != null) {
                    networkFilterViewModel.updateAccess(client.clientId, 3)
                } else {
                    Log.d("MainActivity", "Client or Rule is null")
                }
            }

            //Client Access Change to Actived/Non-Actived If There is a Filter Rules On/Off
            val commentInFilterRulesAndClient = filterRulesComment.intersect(clientComments)
            commentInFilterRulesAndClient.forEach { comment ->
                val client = clients!!.find { it?.comment == comment }
                val rule = filterRules!!.find { it.comment == comment }

                if (client?.clientId != null) {
                    val access = if (rule?.disabled == "false") 1 else 2
                    networkFilterViewModel.updateAccess(client.clientId, access)
                } else {
                    Log.d("MainActivity", "Client or Rule is null")
                }
            }
        } else {
            Log.d("MainActivity", "Queue Tree or Client or Rules data is null")
        }

        //Update Access Observer
        networkFilterViewModel.updateAccess.removeObservers(this)
        networkFilterViewModel.updateAccess.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    getFilteredClient()
                    Log.d("MainActivity", "Client updated: ${result.data.updatedClient?.clientId}")
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.d("MainActivity", "Error Updating Client: ${result.error}")
                }
                is Results.Loading -> {
                    showLoading(true)
                    Log.d("MainActivity", "Loading Updating Client")
                }
            }
        }


        networkFilterViewModel.getFilteredClient.removeObservers(this)
        networkFilterViewModel.getFilteredClient.observe(this) { clientResult ->
            when (clientResult) {
                is Results.Success -> {
                    clients = clientResult.data.filteredClients
                    // Update data pada adapter
                    clientAdapter.updateData(clients?.filterNotNull() ?: emptyList())
                    Log.d("MainActivity", "Client updated UI: ${clientResult.data.filteredClients}")
                }
                is Results.Error -> {
                    Log.e("MainActivity", "Error Getting Clients UI: ${clientResult.error}")
                }
                is Results.Loading -> {
                    Log.d("MainActivity", "Loading Clients Data UI")
                }
            }
        }
    }

    private fun setupClientRecyclerView() {
        clientAdapter = FilteredClientAdapter(emptyList())
        binding.rvClientList.apply {
            adapter = clientAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val NETWORK_ID = "NETWORK_ID"
        const val NETWORK_NAME = "NETWORK_NAME"
        const val NETWORK = "NETWORK"
        var UPDATE_NETWORK_LIST = "UPDATE_NETWORK_LIST"
    }
}