package com.example.clientaccesscontrol.view.ui.clientdetail

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clientaccesscontrol.R
import com.example.clientaccesscontrol.data.mikrotikresponse.GetFilterRulesResponseItem
import com.example.clientaccesscontrol.data.result.Results
import com.example.clientaccesscontrol.databinding.ActivityClientDetailBinding
import com.example.clientaccesscontrol.databinding.CustomDeleteDialogBinding
import com.example.clientaccesscontrol.view.ui.editrouter.EditRouterActivity
import com.example.clientaccesscontrol.view.utils.FactoryViewModel

class ClientDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientDetailBinding
    private lateinit var bindingDialog: CustomDeleteDialogBinding

    private var clientId: Int = 0

    private var previousAccessSelectedId: Int = 0
    private var accessSelectedId: Int = 0
    private var clientAccess: String = ""

    private var previousSpeedSelectedId: Int = 0
    private var speedSelectedId: Int = 0
    private var clientSpeed: String = ""

    private var comment: String = ""
    private var srcAddress: String = ""

    private var limitAt: String = ""
    private var maxLimit: String = ""
    private var burstTime: String = ""
    private var burstThreshold: String = ""
    private var burstLimit: String = ""

    private var filterRules: List<GetFilterRulesResponseItem>? = null

    private val clientDetailViewModel by viewModels<ClientDetailVM> {
        FactoryViewModel.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityClientDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        clientId = intent.getIntExtra(CLIENT_ID, 0)
        setupClientDetail()
        setupActionButton()
    }

    private fun setupClientDetail() {
        clientDetailViewModel.getClientDetail(clientId)
        clientDetailViewModel.getClientDetail.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)

                    val clientDetail = result.data.clientDetail?.firstOrNull()
                    if (clientDetail != null) {
                        comment = clientDetail.comment.toString()
                        srcAddress = clientDetail.ipAddress.toString()
                        clientAccess = clientDetail.internetAccess.toString()
                        clientSpeed = clientDetail.internetSpeed.toString()

                        binding.tvClientName.text = clientDetail.name
                        binding.tvPhone.text = clientDetail.phone
                        binding.tvAddress.text = clientDetail.address
                        binding.tvClientBTS.text = clientDetail.bts
                        binding.tvClientMode.text = clientDetail.mode
                        binding.tvClientSSID.text = clientDetail.ssid
                        binding.tvClientSSID.text = clientDetail.ssid
                        binding.tvClientIPInternet.text = clientDetail.ipAddress
                        binding.tvClientRadio.text = clientDetail.type
                        binding.tvClientRadioName.text = clientDetail.radioName
                        binding.tvClientFrequency.text = clientDetail.frequency
                        binding.tvClientChannelWidth.text = clientDetail.channelWidth
                        binding.tvClientSignal.text = clientDetail.radioSignal
                        binding.tvClientPresharedKey.text = clientDetail.presharedKey
                        binding.tvClientAPLocation.text = clientDetail.apLocation
                        binding.tvClientWLANMacAddress.text = clientDetail.wlanMacAddress
                        binding.tvClientUserPassword.text = clientDetail.password
                        binding.tvClientComment.text = clientDetail.comment
                    } else {
                        Log.d("ClientDetailActivity", "Client Detail is Null")
                    }
                    setupActionSpinner()
                }

                is Results.Error -> {
                    Log.e("ClientDetailActivity", "Error Get Client Detail: ${result.error}")
                    showLoading(false)
                }

                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun setupActionSpinner() {
        clientDetailViewModel.getAccess.removeObservers(this)
        clientDetailViewModel.getAccess.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)

                    //Setup Spinner Internet Access
                    val accessList = result.data.access?.mapNotNull { it?.internetAccess } ?: emptyList()
                    val accessAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, accessList)
                    accessAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spInternetAccess.adapter = accessAdapter

                    if (accessList.isNotEmpty()) {
                        binding.spInternetAccess.onItemSelectedListener = null
                        previousAccessSelectedId = accessList.indexOf(clientAccess) + 1
                        binding.spInternetAccess.setSelection(accessList.indexOf(clientAccess))
                    } else {
                        Log.d("ClientDetailActivity", "Access List is Empty")
                    }

                    binding.spInternetAccess.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                accessSelectedId = position + 1
                                if (accessSelectedId != previousAccessSelectedId && accessSelectedId != 3) {
                                    updateAccess()
                                    previousAccessSelectedId = accessSelectedId
                                } else if (accessSelectedId == 3){
                                    Toast.makeText(this@ClientDetailActivity, "Can't Choose The Selection", Toast.LENGTH_SHORT).show()
                                    binding.spInternetAccess.setSelection(previousAccessSelectedId - 1)
                                }
                            }
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                accessSelectedId = previousAccessSelectedId
                            }
                        }
                }

                is Results.Error -> {
                    showLoading(false)
                    Log.e("ClientDetailActivity", "Error Get Access: ${result.error}")
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }

                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }

        clientDetailViewModel.getSpeed.removeObservers(this)
        clientDetailViewModel.getSpeed.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)

                    //Setup Spinner Internet Speed
                    val speedList = result.data.speed?.mapNotNull { it?.internetSpeed } ?: emptyList()
                    val speedAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, speedList)
                    speedAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spInternetSpeed.adapter = speedAdapter

                    if (speedList.isNotEmpty()) {
                        binding.spInternetSpeed.onItemSelectedListener = null
                        previousSpeedSelectedId = speedList.indexOf(clientSpeed) + 1
                        binding.spInternetSpeed.setSelection(speedList.indexOf(clientSpeed))
                    } else {
                        Log.d("ClientDetailActivity", "Speed List is Empty")
                    }

                    binding.spInternetSpeed.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                speedSelectedId = position + 1
                                if (speedSelectedId != previousSpeedSelectedId) {
                                    clientSpeed(speedSelectedId)
                                    updateSpeed()
                                    previousSpeedSelectedId = speedSelectedId
                                } else {
                                    Log.d("ClientDetailActivity", "Speed Selected ID Doesn't Change")
                                }
                            }
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                speedSelectedId = previousSpeedSelectedId
                            }
                        }
                }

                is Results.Error -> {
                    showLoading(false)
                    Log.e("ClientDetailActivity", "Error Get Speed: ${result.error}")
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }

                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun updateAccess() {
        Log.d("ClientDetailActivity", "Update Access Called")
        if (accessSelectedId in 1..2) {
            Log.d("ClientDetailActivity", "Update Access Called with Access ID: $accessSelectedId")
            UPDATE_CLIENT = "TRUE"

            //Update Access
            clientDetailViewModel.updateAccess(clientId, accessSelectedId)

            //Update Access Observer
            clientDetailViewModel.updateAccess.removeObservers(this)
            clientDetailViewModel.updateAccess.observe(this) { updateAccessResult ->
                when (updateAccessResult) {
                    is Results.Success -> {
                        //Get Filter Rules
                        clientDetailViewModel.getFilterRules()
                        showLoading(false)
                    }

                    is Results.Error -> {
                        showLoading(false)
                        Log.e("ClientDetailActivity", "Error Update Access: ${updateAccessResult.error}")
                        Toast.makeText(this, "Error: ${updateAccessResult.error}", Toast.LENGTH_SHORT).show()
                    }

                    is Results.Loading -> {
                        showLoading(true)
                    }
                }
            }

            //Get Filter Rules Observer
            clientDetailViewModel.getFilterRules.removeObservers(this)
            clientDetailViewModel.getFilterRules.observe(this) { rulesResult ->
                when (rulesResult) {
                    is Results.Success -> {
                        showLoading(false)
                        filterRules = rulesResult.data

                        //Take Filter Rules That Match With Client by Comment
                        val matchedFilterRules = filterRules!!.filter { it.comment?.trim() == comment.trim() }
                        val filterRulesComment = matchedFilterRules.map { it.comment?.trim() }
                        val filterRulesId = matchedFilterRules.firstOrNull()?.id?.replace("[", "")?.replace("]", "")

                        //If Client Doesn't Have Rules, Make New Rules
                        if (comment.trim() !in filterRulesComment) {
                            Log.d("ClientDetailActivity", "Creating Rules with Access: $accessSelectedId")
                            when (accessSelectedId) {
                                1 -> clientDetailViewModel.createFilterRules(comment, "forward", srcAddress, "drop", "false")
                                2 -> clientDetailViewModel.createFilterRules(comment, "forward", srcAddress, "drop", "true")
                                else -> Log.d("ClientDetailActivity", "Selected Access Doesn't Counted")
                            }

                            //If Client Already Have Rules, Update the Disabled
                        } else if (comment.trim() in filterRulesComment) {
                            Log.d("ClientDetailActivity", "Updating Rules with ID: $filterRulesId to Access: $accessSelectedId")
                            when (accessSelectedId) {
                                1 ->  clientDetailViewModel.updateFilterRules(filterRulesId.toString(), "false")
                                2 ->  clientDetailViewModel.updateFilterRules(filterRulesId.toString(), "true")
                                else -> Log.d("ClientDetailActivity", "Selected Access Doesn't Counted")
                            }
                        } else {
                            Log.d("ClientDetailActivity", "Comment Not Found in Filter Rules")
                        }
                    }

                    is Results.Error -> {
                        showLoading(false)
                        Log.e("ClientDetailActivity", "Error Get Filter Rules: ${rulesResult.error}")
                        Toast.makeText(this, "Error: ${rulesResult.error}", Toast.LENGTH_SHORT).show()
                    }

                    is Results.Loading -> {
                        showLoading(true)
                    }
                }
            }
        } else {
            Toast.makeText(this, "Can Not Choose Selection", Toast.LENGTH_SHORT).show()
            Log.d("ClientDetailActivity", "Selected Access Doesn't Counted")
        }
        createFilterRulesObserver()
        updateFilterRulesObserver()
    }

    private fun updateSpeed() {
        if (speedSelectedId in 1..7) {
            UPDATE_CLIENT = "TRUE"

            //Update Speed
            clientDetailViewModel.updateSpeed(clientId, speedSelectedId)
            Log.d("ClientDetailActivity", "Update Speed Called with Speed ID: $speedSelectedId")

            //Update Speed Observer
            clientDetailViewModel.updateSpeed.removeObservers(this)
            clientDetailViewModel.updateSpeed.observe(this) { updateSpeedResult ->
                when (updateSpeedResult) {
                    is Results.Success -> {
                        //Get Queue Tree
                        clientDetailViewModel.getQueueTree()
                        showLoading(false)
                    }

                    is Results.Error -> {
                        showLoading(false)
                        Log.e("ClientDetailActivity", "Error Update Speed: ${updateSpeedResult.error}")
                        Toast.makeText(this, "Error: ${updateSpeedResult.error}", Toast.LENGTH_SHORT).show()
                    }

                    is Results.Loading -> {
                        showLoading(true)
                    }
                }
            }

            //Get Queue Tree Observer
            clientDetailViewModel.getQueueTree.removeObservers(this)
            clientDetailViewModel.getQueueTree.observe(this) { getQueueTreeResult ->
                when (getQueueTreeResult) {
                    is Results.Success -> {
                        val clientQueueTrees = getQueueTreeResult.data.filter { it.comment?.trim() == comment.trim() }
                        Log.d("ClientDetailActivity", "Client Queue Trees: $clientQueueTrees")

                        if (clientQueueTrees.isNotEmpty()) {
                            clientQueueTrees.forEach { queueTree ->
                                //Update Queue Tree Speed
                                clientDetailViewModel.updateQueueTreeSpeed(queueTree.id.toString(), limitAt, maxLimit, burstTime, burstThreshold, burstLimit)
                            }
                        } else {
                            Log.d("ClientDetailActivity", "Queue Tree Not Found")
                            Toast.makeText(this, "Queue Tree Not Found", Toast.LENGTH_SHORT).show()
                        }
                        showLoading(false)
                    }
                    is Results.Error -> {
                        showLoading(false)
                        Log.e("ClientDetailActivity", "Error Get Queue Tree: ${getQueueTreeResult.error}")
                        Toast.makeText(this, "Error: ${getQueueTreeResult.error}", Toast.LENGTH_SHORT).show()
                    }
                    is Results.Loading -> {
                        showLoading(true)
                    }
                }
            }

            //Update Speed Observer
            clientDetailViewModel.updateQueueTreeSpeed.removeObservers(this)
            clientDetailViewModel.updateQueueTreeSpeed.observe(this) { updateQueueTreeSpeedResult ->
                when (updateQueueTreeSpeedResult) {
                    is Results.Success -> {
                        showLoading(false)
                    }
                    is Results.Error -> {
                        showLoading(false)
                        Log.e("ClientDetailActivity", "Error Update Speed: ${updateQueueTreeSpeedResult.error}")
                        Toast.makeText(this, "Error: ${updateQueueTreeSpeedResult.error}", Toast.LENGTH_SHORT).show()
                    }
                    is Results.Loading -> {
                        showLoading(true)
                    }
                }
            }
        }
    }

    private fun deleteClient() {
        //Get Queue Tree
        clientDetailViewModel.getQueueTree()
        clientDetailViewModel.getQueueTree.removeObservers(this)
        clientDetailViewModel.getQueueTree.observe(this) { getQueueTreeResult ->
            when (getQueueTreeResult) {
                is Results.Success -> {
                    val queueTrees = getQueueTreeResult.data.filter { it.comment?.trim() == comment.trim() }
                    val queueTreesId = queueTrees.map { it.id }

                    if (queueTreesId.isNotEmpty()) {
                        //Delete Queue Tree
                        queueTreesId.forEach { queueTreeId ->
                            clientDetailViewModel.deleteQueueTree(queueTreeId?.replace("[", "")?.replace("]", "").toString())
                        }
                        clientDetailViewModel.getMangle()
                    } else {
                        //Get Mangle
                        clientDetailViewModel.getMangle()
                    }
                    Log.d("ClientDetailActivity", "Client Queue Trees: $queueTrees")
                    showLoading(false)
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.e("ClientDetailActivity", "Error Get Queue Tree: ${getQueueTreeResult.error}")
                    Toast.makeText(this, "Error: ${getQueueTreeResult.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }

        //Delete Queue Tree Observer
        clientDetailViewModel.deleteQueueTree.removeObservers(this)
        clientDetailViewModel.deleteQueueTree.observe(this) { deleteQueueTreeResult ->
            when (deleteQueueTreeResult) {
                is Results.Success -> {
                    showLoading(false)
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.e("ClientDetailActivity", "Error Delete Queue Tree: ${deleteQueueTreeResult.error}")
                    Toast.makeText(this, "Error: ${deleteQueueTreeResult.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }

        //Get Mangle Observer
        clientDetailViewModel.getMangle.removeObservers(this)
        clientDetailViewModel.getMangle.observe(this) { getMangleResult ->
            when (getMangleResult) {
                is Results.Success -> {
                    val mangles = getMangleResult.data.filter { it.comment?.trim() == comment.trim() }
                    val manglesId = mangles.map { it.id }

                    if (manglesId.isNotEmpty()) {
                        manglesId.forEach { mangleId ->
                            //Delete Mangle
                            clientDetailViewModel.deleteMangle(mangleId?.replace("[", "")?.replace("]", "").toString())
                        }
                        clientDetailViewModel.getFilterRules()
                    } else {
                        clientDetailViewModel.getFilterRules()
                    }
                    Log.d("ClientDetailActivity", "Client Mangles: $mangles")
                    showLoading(false)
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.e("ClientDetailActivity", "Error Get Mangle: ${getMangleResult.error}")
                    Toast.makeText(this, "Error: ${getMangleResult.error}", Toast.LENGTH_SHORT).show()
                    }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }

        //Delete Mangle Observer
        clientDetailViewModel.deleteMangle.removeObservers(this)
        clientDetailViewModel.deleteMangle.observe(this) { deleteMangleResult ->
            when (deleteMangleResult) {
                is Results.Success -> {
                    showLoading(false)
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.e("ClientDetailActivity", "Error Delete Mangle: ${deleteMangleResult.error}")
                    Toast.makeText(this, "Error: ${deleteMangleResult.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }

        //Get Filter Rules
        clientDetailViewModel.getFilterRules.removeObservers(this)
        clientDetailViewModel.getFilterRules.observe(this) { getFilterRulesResult ->
            when (getFilterRulesResult) {
                is Results.Success -> {
                    val rules = getFilterRulesResult.data.filter { it.comment?.trim() == comment.trim() }
                    val rulesId = rules.map { it.id }

                    if (rulesId.isNotEmpty()) {
                        rulesId.forEach { ruleId ->
                            //Delete Filter Rules
                            clientDetailViewModel.deleteFilterRules(ruleId?.replace("[", "")?.replace("]", "").toString())
                        }
                    }else {
                        clientDetailViewModel.deleteClient(clientId)
                    }
                    Log.d("ClientDetailActivity", "Client Filter Rules: $rules")
                    showLoading(false)
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.e("ClientDetailActivity", "Error Get Filter Rules: ${getFilterRulesResult.error}")
                    Toast.makeText(this, "Error: ${getFilterRulesResult.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }

        //Delete Filter Rules Observer
        clientDetailViewModel.deleteFilterRules.removeObservers(this)
        clientDetailViewModel.deleteFilterRules.observe(this) { deleteFilterRulesResult ->
            when (deleteFilterRulesResult) {
                is Results.Success -> {
                    showLoading(false)
                    //Delete Client
                    clientDetailViewModel.deleteClient(clientId)
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.e("ClientDetailActivity", "Error Delete Filter Rules: ${deleteFilterRulesResult.error}")
                    Toast.makeText(this, "Error: ${deleteFilterRulesResult.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }

        //Delete Client Observer
        clientDetailViewModel.deleteClient.removeObservers(this)
        clientDetailViewModel.deleteClient.observe(this) { deleteClientResult ->
            when (deleteClientResult) {
                is Results.Success -> {
                    showLoading(false)
                    UPDATE_CLIENT = "TRUE"
                    //Finish Activity
                    Toast.makeText(this, deleteClientResult.data.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.e("ClientDetailActivity", "Error deleting client: ${deleteClientResult.error}")
                    Toast.makeText(this, "Error deleting client: ${deleteClientResult.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {
                    showLoading(true)
                    Log.d("ClientDetailActivity", "Loading deleting client")
                }
            }
        }
    }

    private fun createFilterRulesObserver() {
        clientDetailViewModel.createFilterRules.removeObservers(this)
        clientDetailViewModel.createFilterRules.observe(this) { createRulesResult ->
            when (createRulesResult) {
                is Results.Success -> {
                    showLoading(false)
                    Log.d("ClientDetailActivity", "Filter Rules Created")
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.e("ClientDetailActivity", "Error Create Filter Rules: ${createRulesResult.error}")
                    Toast.makeText(this, "Error: ${createRulesResult.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun updateFilterRulesObserver() {
        clientDetailViewModel.updateFilterRules.removeObservers(this)
        clientDetailViewModel.updateFilterRules.observe(this) { updateRulesResult ->
            when (updateRulesResult) {
                is Results.Success -> {
                    showLoading(false)
                    Log.d("ClientDetailActivity", "Filter Rules Updated")
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.e("ClientDetailActivity", "Error Update Filter Rules: ${updateRulesResult.error}")
                    Toast.makeText(this, "Error: ${updateRulesResult.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun clientSpeed(speedId: Int) {
        when (speedId) {
            1 -> {
                limitAt = "0M"
                maxLimit = "0M"
                burstLimit = "0M"
                burstThreshold = "0M"
                burstTime = "16"
            }
            2 -> {
                limitAt = "4M"
                maxLimit = "6M"
                burstLimit = "12M"
                burstThreshold = "10M"
                burstTime = "16"
            }
            3 -> {
                limitAt = "6M"
                maxLimit = "8M"
                burstLimit = "16M"
                burstThreshold = "12M"
                burstTime = "16"
            }
            4 -> {
                limitAt = "8M"
                maxLimit = "12M"
                burstLimit = "22M"
                burstThreshold = "15M"
                burstTime = "16"
            }
            5 -> {
                limitAt = "10M"
                maxLimit = "15M"
                burstLimit = "32M"
                burstThreshold = "20M"
                burstTime = "16"
            }
            6 -> {
                limitAt = "25M"
                maxLimit = "35M"
                burstLimit = "55M"
                burstThreshold = "45M"
                burstTime = "16"
            }
            7 -> {
                limitAt = "45M"
                maxLimit = "65M"
                burstLimit = "105M"
                burstThreshold = "85M"
                burstTime = "16"
            }
        }
    }

    private fun setupActionButton() {
        binding.btDelete.setOnClickListener {
            customDialogDelete()
        }
        binding.btBack.setOnClickListener {
            finish()
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
        binding.btEdit.setOnClickListener {
            val intent = Intent(this, EditRouterActivity::class.java).apply {
                putExtra(EditRouterActivity.CLIENT_ID, clientId)
            }
            startActivity(intent)
        }
    }

    private fun customDialogDelete() {
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

        bindingDialog.btYesDelete.setOnClickListener {
            UPDATE_CLIENT = "TRUE"
            deleteClient()
            dialog.dismiss()
        }
        bindingDialog.btCancelDelete.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        if (EditRouterActivity.UPDATE_DETAIL_CLIENT == "TRUE") {
            EditRouterActivity.UPDATE_DETAIL_CLIENT = "FALSE"
            UPDATE_CLIENT = "TRUE"
            clientDetailViewModel.getClientDetail(clientId)
        } else {
            Log.d("ClientDetailActivity", "There is No Client Update")
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val CLIENT_ID = "CLIENT_ID"
        var UPDATE_CLIENT = "UPDATE_CLIENT"
    }
}