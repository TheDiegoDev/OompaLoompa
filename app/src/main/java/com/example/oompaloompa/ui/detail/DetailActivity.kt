package com.example.oompaloompa.ui.detail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.oompaloompa.R
import org.koin.android.ext.android.inject
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.oompaloompa.model.OompaLoompaModel
import kotlinx.coroutines.launch

class DetailActivity: AppCompatActivity() {

    private val viewModel by inject<DetailActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)
        val id = intent.getIntExtra("Id", 1)
        viewModel.getCharacterDataBase(id.toString())
        setObserver()
    }

    private fun setObserver() {
        lifecycleScope.launch {
            viewModel.result.collect {
                setView(it)
            }
        }
    }

    private fun setView(item: OompaLoompaModel) {
        val name: TextView = findViewById(R.id.detailName)
        val image: ImageView = findViewById(R.id.detailImage)
        val job: TextView = findViewById(R.id.detailJob)
        val age: TextView = findViewById(R.id.detailAge)
        val country: TextView = findViewById(R.id.detailCountry)
        val email: TextView = findViewById(R.id.detailEmail)
        val gender: TextView = findViewById(R.id.detailGender)
        val height: TextView = findViewById(R.id.detailHeight)


        Glide.with(this)
            .load(item.image)
            .into(image)

        name.text = "${item.first_name} ${item.last_name}"
        job.text = "Profession: ${item.profession}"
        age.text = "Age: ${item.age.toString()}"
        country.text = "Country: ${item.country}"
        email.text = "Contact by Email: ${item.email}"
        height.text = "Height: ${item.height.toString()}"
        if (item.gender?.lowercase() == "f") {
            gender.text = "Gender: Female"
        } else {
            gender.text = "Gender: Male"
        }
    }
}