package com.example.clientaccesscontrol.view.ui.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientaccesscontrol.R
import com.example.clientaccesscontrol.data.cacresponse.SearchedClientItem
import com.example.clientaccesscontrol.data.mikrotikresponse.GetFilterRulesResponseItem
import com.example.clientaccesscontrol.data.mikrotikresponse.GetQueueTreeResponseItem
import com.example.clientaccesscontrol.data.result.Results
import com.example.clientaccesscontrol.databinding.ActivityMainBinding
import com.example.clientaccesscontrol.databinding.CustomLogoutDialogBinding
import com.example.clientaccesscontrol.view.ui.clientdetail.ClientDetailActivity
import com.example.clientaccesscontrol.view.ui.connect.ConnectActivity
import com.example.clientaccesscontrol.view.ui.network.NetworkActivity
import com.example.clientaccesscontrol.view.ui.newclientprofile.NewClientProfileActivity
import com.example.clientaccesscontrol.view.utils.FactoryViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingDialog: CustomLogoutDialogBinding
    private lateinit var clientAdapter: ClientAdapter
    private var queueTrees: List<GetQueueTreeResponseItem>? = null
    private var clients: List<SearchedClientItem?>? = null
    private var filterRules: List<GetFilterRulesResponseItem>? = null
    private var clicked = false
    private val mainViewModel by viewModels<MainVM> {
        FactoryViewModel.getInstance(this)
    }

    //Button Menu Animation
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromTop: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_top_anim) }
    private val toTop: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_top_anim) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainViewModel.getSession()
        buttonMenuAction()
        searchBarAction()
        setupClientList()
    }

    override fun onResume() {
        super.onResume()
        if (ClientDetailActivity.UPDATE_CLIENT == "TRUE") {
            ClientDetailActivity.UPDATE_CLIENT = "FALSE"
            mainViewModel.getSearchedClient("")
        }  else {
            Log.d("MainActivity", "There is No Client to Update")
        }
    }

    private fun setupClientList() {
        mainViewModel.getSearchedClient("")
        mainViewModel.getSearchedClient.removeObservers(this)
        mainViewModel.getSearchedClient.observe(this) { getSearchedClientResult ->
            when (getSearchedClientResult) {
                is Results.Success -> {
                    clients = getSearchedClientResult.data.searchedClient
                    showLoading(false)
                    Log.d("MainActivity", "Received Client Data: ${getSearchedClientResult.data.searchedClient}")
                    clientAdapter.updateData(getSearchedClientResult.data.searchedClient?.filterNotNull() ?: emptyList())
                    mainViewModel.getQueueTree()
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.e("MainActivity", "Error Getting Client: ${getSearchedClientResult.error}")
                    mainViewModel.logout()
                    val intent = Intent(this, ConnectActivity::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this, "Gagal Terhubung Ke Router", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {
                    showLoading(true)
                    Log.d("MainActivity", "Loading Client Data")
                }
            }
        }

        mainViewModel.getQueueTree.removeObservers(this)
        mainViewModel.getQueueTree.observe(this) { queueTreeResult ->
            when (queueTreeResult) {
                is Results.Success -> {
                    queueTrees = queueTreeResult.data
                    showLoading(false)
                    mainViewModel.getFilterRules()
                    Log.d("MainActivity", "Received Queue Tree Data: ${queueTreeResult.data}")
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.e("MainActivity", "Error Getting Queue Tree: ${queueTreeResult.error}")
                    mainViewModel.logout()
                    val intent = Intent(this, ConnectActivity::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this, "Gagal Terhubung Ke Router", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {
                    showLoading(true)
                    Log.d("MainActivity", "Loading Get Queue Tree Data")
                }
            }
        }

        mainViewModel.getFilterRules.removeObservers(this)
        mainViewModel.getFilterRules.observe(this) { filterRulesResult ->
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

    private fun setupClientRecyclerView() {
        clientAdapter = ClientAdapter(emptyList())
        binding.rvClientList.apply {
            adapter = clientAdapter
            layoutManager = LinearLayoutManager(context)
        }
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
                    mainViewModel.updateAccess(client.clientId, 3)
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
                    mainViewModel.updateAccess(client.clientId, access)
                } else {
                    Log.d("MainActivity", "Client or Rule is null")
                }
            }

            //Create New Client If Not Found in Database but Exist in Mikrotik
            val queueTreeCommentsNotInClient = queueTreeComments.subtract(clientComments)
            queueTreeCommentsNotInClient.forEach { comment ->
                if (comment?.contains("Total") == false) {
                    val existingClient = clients?.find { it?.comment == comment }
                    if (existingClient == null) {
                        createNewClient(comment.toString())
                        Log.d("queueTreeCommentsNotInClient", "New client created: $comment")
                    } else {
                        Log.d("queueTreeCommentsNotInClient", "Client with comment '$comment' already exists.")
                    }
                } else {
                    Log.d("queueTreeCommentsNotInClient", "Client with comment '$comment' not included")
                }
            }
        } else {
            Log.d("MainActivity", "Queue Tree or Client or Rules data is null")
        }

        //Update Access Observer
        mainViewModel.updateAccess.removeObservers(this)
        mainViewModel.updateAccess.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    mainViewModel.getSearchedClient("")
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


        mainViewModel.getSearchedClient.removeObservers(this)
        mainViewModel.getSearchedClient.observe(this) { clientResult ->
            when (clientResult) {
                is Results.Success -> {
                    clients = clientResult.data.searchedClient
                    // Update data pada adapter
                    clientAdapter.updateData(clients?.filterNotNull() ?: emptyList())
                    Log.d("MainActivity", "Client updated UI: ${clientResult.data.searchedClient}")
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

    private fun createNewClient(comment: String) {
        //Crate New Client
        mainViewModel.createNewClient(comment, "", "", 1, 1)
        mainViewModel.createNewClient.removeObservers(this)
        mainViewModel.createNewClient.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    val newClientID = result.data.newClient?.clientId!!.toInt()
                    mainViewModel.updateNetwork(
                        newClientID,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        1,
                        1,
                        1,
                        1,
                        comment,
                        "",
                        1
                    )
                    mainViewModel.getSearchedClient("")
                }
                is Results.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Create New Client Error: ${result.error}", Toast.LENGTH_SHORT).show()
                    Log.d("NewClientRouterActivity", "Create New Client Error: ${result.error}")
                }
                is Results.Loading -> {
                    showLoading(true)
                    Log.d("NewClientRouterActivity", "Loading Create New Client")
                }
            }
        }

        mainViewModel.updateNetwork.removeObservers(this)
        mainViewModel.updateNetwork.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                }
                is Results.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Update New Client Failed", Toast.LENGTH_SHORT).show()
                    Log.d("NewClientRouterActivity", "Update New Client Error: ${result.error}")
                }

                is Results.Loading -> {
                    showLoading(true)
                    Log.d("NewClientRouterActivity", "Loading Update New Client")
                }
            }
        }
    }

    private fun buttonMenuAction() {
        binding.btMenu.setOnClickListener {
            onButtonMenuClicked()
        }

        binding.btNetwork.setOnClickListener {
            val intent = Intent(this, NetworkActivity::class.java)
            startActivity(intent)
            onButtonMenuClicked()
        }

        binding.btLogout.setOnClickListener {
            onButtonMenuClicked()
            showCustomDialog()
        }

        binding.btNewClient.setOnClickListener {
            val intent = Intent(this, NewClientProfileActivity::class.java)
            startActivity(intent)
            if (clicked) onButtonMenuClicked()
        }
    }

    private fun onButtonMenuClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.btNetwork.visibility = View.VISIBLE
            binding.btLogout.visibility = View.VISIBLE
        } else {
            binding.btNetwork.visibility = View.INVISIBLE
            binding.btLogout.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.btNetwork.startAnimation(fromTop)
            binding.btLogout.startAnimation(fromTop)
            binding.btMenu.startAnimation(rotateOpen)
        } else {
            binding.btNetwork.startAnimation(toTop)
            binding.btLogout.startAnimation(toTop)
            binding.btMenu.startAnimation(rotateClose)
        }
    }

    private fun showCustomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        bindingDialog = CustomLogoutDialogBinding.inflate(layoutInflater)

        dialog.setContentView(bindingDialog.root)
        dialog.setCancelable(true)

        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val cardView = bindingDialog.root.findViewById<CardView>(R.id.LogoutCard)
        val layoutParams = cardView.layoutParams as ViewGroup.MarginLayoutParams
        val margin = (40 * resources.displayMetrics.density).toInt()
        layoutParams.setMargins(margin, 0, margin, 0)
        cardView.layoutParams = layoutParams

        bindingDialog.btYesLogout.setOnClickListener {
            dialog.dismiss()
            mainViewModel.logout()
            val intent = Intent(this, ConnectActivity::class.java)
            startActivity(intent)
            finish()
        }
        bindingDialog.btCancelLogout.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun searchBarAction() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    if (textView.text.toString().isNotEmpty()) {
                        mainViewModel.getSearchedClient(textView.text.toString())
                    } else {
                        Log.d("MainActivity", "Search Bar is Empty")
                    }
                    false
                }
        }

        mainViewModel.getSearchedClient.removeObservers(this)
        mainViewModel.getSearchedClient.observe(this) { getSearchedClientResult ->
            when (getSearchedClientResult) {
                is Results.Success -> {
                    clientAdapter.updateData(getSearchedClientResult.data.searchedClient?.filterNotNull() ?: emptyList())
                }
                is Results.Error -> {
                    Log.e("MainActivity", "Error Getting Searched Client: ${getSearchedClientResult.error}")
                }
                is Results.Loading -> {
                    Log.d("MainActivity", "Loading Searched Client")
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}