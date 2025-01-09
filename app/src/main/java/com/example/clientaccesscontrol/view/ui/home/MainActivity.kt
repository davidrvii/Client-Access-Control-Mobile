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
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientaccesscontrol.R
import com.example.clientaccesscontrol.data.cacresponse.ClientsItem
import com.example.clientaccesscontrol.data.mikrotikresponse.GetQueueTreeResponseItem
import com.example.clientaccesscontrol.data.result.Results
import com.example.clientaccesscontrol.databinding.ActivityMainBinding
import com.example.clientaccesscontrol.databinding.CustomLogoutDialogBinding
import com.example.clientaccesscontrol.view.ui.connect.ConnectActivity
import com.example.clientaccesscontrol.view.ui.filter.FilterBottomSheet
import com.example.clientaccesscontrol.view.ui.network.NetworkActivity
import com.example.clientaccesscontrol.view.ui.newclientprofile.NewClientProfileActivity
import com.example.clientaccesscontrol.view.utils.FactoryViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingDialog: CustomLogoutDialogBinding
    private lateinit var clientAdapter: ClientAdapter
    private var queueTrees: List<GetQueueTreeResponseItem>? = null
    private var clients: List<ClientsItem?>? = null
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
        buttonNewClientAction()
        setupClientList()
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getAllClient()
        mainViewModel.getQueueTree()
    }

    private fun updateClientList() {
        if (clients != null && queueTrees != null) {
            val queueTreeComments = queueTrees!!.map { it.comment }.toSet()
            clients!!.forEach { client ->
                if (!queueTreeComments.contains(client?.comment)) {
                    client?.clientId?.let { clientId ->
                        client.speedId?.let { speedId ->
                            mainViewModel.updateClient(clientId, 3, speedId)
                        }
                    }
                }
            }

            mainViewModel.updateClient.observe(this) { result ->
                when (result) {
                    is Results.Success -> {
                        showLoading(false)
                        Log.d("MainActivity", "Client updated: ${result.data.updatedClient?.clientId}")
                    }

                    is Results.Error -> {
                        showLoading(false)
                        Log.d("MainActivity", "Error Updating Client: ${result.error}")
                    }

                    is Results.Loading -> { showLoading(true) }
                }
            }
        }

        if (queueTrees != null && clients != null) {
            val clientComment = clients!!.map { it?.comment }
            queueTrees!!.forEach { queueTree ->
                if (!clientComment.contains(queueTree.comment)) {
                    Log.d("MainActivity", "Queue tree not found in client list: ${queueTree.comment}")
                }
            }
        }
    }

    private fun setupClientList() {
        mainViewModel.getAllClient()
        mainViewModel.getAllClient.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    clients =  result.data.clients
                    showLoading(false)
                    Log.d("MainActivity", "Received client data: ${result.data.clients}")
                    clientAdapter.updateData(result.data.clients?.filterNotNull() ?: emptyList())
                    mainViewModel.getQueueTree()
                }

                is Results.Error -> {
                    showLoading(false)
                    Log.d("MainActivity", "Error Getting Client: ${result.error}")
                }

                is Results.Loading -> { showLoading(true) }
            }
        }

        mainViewModel.getQueueTree.observe(this) { queueTreeResult ->
            when (queueTreeResult) {
                is Results.Success -> {
                    queueTrees = queueTreeResult.data
                    showLoading(false)
                    updateClientList()
                    Log.d("MainActivity", "Received queue tree data: ${queueTreeResult.data}")
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.d("MainActivity", "Error Getting Queue Tree: ${queueTreeResult.error}")
                }
                is Results.Loading -> { showLoading(true) }
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
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    false
                }

            binding.searchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.filter -> {
                        filterBottomSheet()
                    }
                }
                true
            }

        }
    }

    private fun buttonNewClientAction() {
        binding.btNewClient.setOnClickListener {
            val intent = Intent(this, NewClientProfileActivity::class.java)
            startActivity(intent)
            if (clicked) onButtonMenuClicked()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun filterBottomSheet() {
//        val filterDialog = BottomSheetDialog(this)
//        val filterBinding = BottomSheetFilterBinding.inflate(layoutInflater)
//        filterDialog.apply {
//            setContentView(filterBinding.root)
//            setCancelable(true)
//            show()
//        }

        // Buat instance dari FilterBottomSheet
        val filterBottomSheet = FilterBottomSheet()

        // Tampilkan BottomSheetFragment
        filterBottomSheet.show(supportFragmentManager, filterBottomSheet.tag)
    }
}