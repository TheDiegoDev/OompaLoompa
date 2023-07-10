package com.example.oompaloompa.ui

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oompaloompa.R
import com.example.oompaloompa.model.OompaLoompaModel
import com.example.oompaloompa.ui.adapter.OompaLoompaAdapter
import com.example.oompaloompa.utils.showLoadingDialog
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private var loadingDialog: Dialog? = null
    private val viewModel: MainViewModel by inject()
    private var listOompaLoompa: ArrayList<OompaLoompaModel> = arrayListOf()
    private var listOompaLoompaFiltered: ArrayList<OompaLoompaModel> = arrayListOf()
    private var jobList: ArrayList<String> = arrayListOf()
    private var genderList: ArrayList<String> = arrayListOf()
    private lateinit var recyclerView: RecyclerView
    private val adapter = OompaLoompaAdapter(listOompaLoompa,this)
    private lateinit var textInputLayoutGender: TextInputLayout
    private lateinit var textInputLayoutJob: TextInputLayout
    private lateinit var resetButton: Button
    private lateinit var autoCompleteGender: AutoCompleteTextView
    private lateinit var autoCompleteJob: AutoCompleteTextView
    private lateinit var errorImageView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showDialog()
        relateViewWithVariables()
        setView()
        setObservers()
        setData()
    }

    private fun relateViewWithVariables() {
        recyclerView = findViewById(R.id.recyclerView)
        autoCompleteGender = findViewById(R.id.auto_complete_gender)
        autoCompleteJob = findViewById(R.id.auto_complete_job)
        textInputLayoutGender = findViewById(R.id.dropDownGender)
        textInputLayoutJob = findViewById(R.id.dropDownJob)
        resetButton = findViewById(R.id.resetFilter)
        errorImageView = findViewById(R.id.errorImage)
    }

    private fun setData() {
        if (viewModel.resultDao.value.isNotEmpty()) {
            hideLoading()
            setRecyclerView()
            listOompaLoompa.clear()
            listOompaLoompa.addAll(viewModel.resultDao.value)
            addItemsJobAndGenderList()
            adapter.notifyDataSetChanged()
        } else {
            viewModel.getCharacters()
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.result.collect {
                viewModel.getCharacterDao()
            }
        }
        lifecycleScope.launch {
            viewModel.resultError.collect {
                setErrorView()
                setData()
            }
        }
        lifecycleScope.launch {
            viewModel.resultDao.collect {
                setData()
            }
        }
    }

    private fun addItemsJobAndGenderList() {
        listOompaLoompa.map {
            if (!jobList.contains(it.profession)) {
                it.profession?.let { it1 -> jobList.add(it1) }
            }
            if (!genderList.contains(it.gender)) {
                it.gender?.let { it1 -> genderList.add(it1) }
            }
        }
        configDorpDownsMenu()
    }

    private fun configDorpDownsMenu() {
        val adapterGender = ArrayAdapter(this, R.layout.list_items_dorpdownmenu,genderList)
        val adapterJob = ArrayAdapter(this,R.layout.list_items_dorpdownmenu,jobList)

        autoCompleteGender.setAdapter(adapterGender)
        autoCompleteJob.setAdapter(adapterJob)

        autoCompleteGender.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, position, id ->
            val itemSelected = parent.getItemAtPosition(position).toString()
            listOompaLoompaFiltered = getFilteredList(itemSelected, true)
            adapter.filterData(listOompaLoompaFiltered)
        }

        autoCompleteJob.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, position, id ->
            val itemSelected = parent.getItemAtPosition(position).toString()
            listOompaLoompaFiltered = getFilteredList(itemSelected, false)
            adapter.filterData(listOompaLoompaFiltered)
        }
        confinResetFilterButton()
    }

    private fun getFilteredList(itemSelected: String, isByGender: Boolean): ArrayList<OompaLoompaModel> {
        val list = if (isByGender) {
            if (listOompaLoompaFiltered.isNotEmpty()) {
                listOompaLoompaFiltered.filter {
                    it.gender?.lowercase()!!.contains(itemSelected.lowercase())
                }
            } else {
                listOompaLoompa.filter {
                    it.gender?.lowercase()!!.contains(itemSelected.lowercase())
                }
            }
        } else {
             if (listOompaLoompaFiltered.isNotEmpty()) {
                listOompaLoompaFiltered.filter {
                    it.profession?.lowercase()!!.contains(itemSelected.lowercase())
                }
            } else {
                listOompaLoompa.filter {
                    it.profession?.lowercase()!!.contains(itemSelected.lowercase())
                }
            }
        }
        return list as ArrayList<OompaLoompaModel>
    }

    private fun confinResetFilterButton() {
        resetButton.setOnClickListener {
            adapter.filterData(listOompaLoompa)
            textInputLayoutGender.clearFocus()
            textInputLayoutJob.clearFocus()
            autoCompleteJob.setText("")
            autoCompleteGender.setText("")
            listOompaLoompaFiltered.clear()
        }
    }

    private fun setRecyclerView() {
        recyclerView.visibility = View.VISIBLE
        errorImageView.visibility = View.GONE
    }

    private fun setErrorView() {
        recyclerView.visibility = View.GONE
        errorImageView.visibility = View.VISIBLE
    }

    private fun setView() {
        recyclerView.visibility = View.GONE
        errorImageView.visibility = View.GONE
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showDialog() {
        hideLoading()
        loadingDialog = this.showLoadingDialog()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val menuItemSearch = menu!!.findItem(R.id.action_search)
        val searchView = menuItemSearch.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val newList = listOompaLoompa.filter {
                    it.first_name?.lowercase()!!.contains(query!!.lowercase())
                }
                adapter.filterData(newList)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newList = listOompaLoompa.filter {
                    it.first_name?.lowercase()!!.contains(newText!!.lowercase())
                }
                adapter.filterData(newList)
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filters -> {
                if (textInputLayoutGender.visibility == View.GONE) {
                    textInputLayoutGender.visibility = View.VISIBLE
                    textInputLayoutJob.visibility = View.VISIBLE
                    resetButton.visibility = View.VISIBLE
                } else {
                    textInputLayoutGender.visibility = View.GONE
                    textInputLayoutJob.visibility = View.GONE
                    resetButton.visibility = View.GONE
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}