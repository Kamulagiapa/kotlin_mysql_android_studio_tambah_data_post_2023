package com.example.afteruts

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.afteruts.Model.DataItem
import com.example.afteruts.Model.Response
import com.example.afteruts.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener(this)

        // Panggil metode getPosts() untuk memuat data dari server
        getPosts()
        binding.btnTambah.setOnClickListener {
            // Membuat intent untuk pindah ke TambahActivity
            val intent = Intent(this, TambahActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRefresh() {
        // Panggil metode getPosts() untuk memuat ulang data dari server
        getPosts()
    }

    private fun getPosts() {
        NetworkConfig().getService()
            .getmahasiswa()
            .enqueue(object : Callback<Response> {
                override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                    binding.progressIndicator.visibility = View.GONE
                    if (response.isSuccessful) {
                        val receivedDatas = response.body()?.data
                        setToAdapter(receivedDatas as List<DataItem>?)
                    }
                    binding.swipeRefreshLayout.isRefreshing = false // Beritahu bahwa proses refresh selesai
                }

                @SuppressLint("SuspiciousIndentation")
                override fun onFailure(call: Call<Response>, t: Throwable) {
                    this@MainActivity.binding.progressIndicator.visibility = View.GONE
                    Log.d("Retrofit failed", "onFailure : ${t.stackTrace}")
                    binding.swipeRefreshLayout.isRefreshing = false // Beritahu bahwa proses refresh selesai
                }
            })
    }

    private fun setToAdapter(receivedDatas: List<DataItem?>?) {
        binding.newsList.adapter = null
        val adapter = MahasiswaAdapter(receivedDatas) {
            val detailNewsIntent = Intent(this@MainActivity, MahasiswaAdapter::class.java)
            detailNewsIntent.putExtra("idNews", it?.namalengkap)
            startActivity(detailNewsIntent)
        }
        val lm = LinearLayoutManager(this)
        binding.newsList.layoutManager = lm
        binding.newsList.itemAnimator = DefaultItemAnimator()
        binding.newsList.adapter = adapter
    }
    private fun searchNews(query: String?) {
        binding.progressIndicator.visibility = View.VISIBLE
        NetworkConfig()
            .getService()
            .searchPosts(query)
            .enqueue(object : Callback<Response> {
                override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                    this@MainActivity.binding.progressIndicator.visibility = View.GONE
                    if (response.isSuccessful){
                        val receivedDatas = response.body()?.data
                        setToAdapter(receivedDatas)
                    }
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    this@MainActivity.binding.progressIndicator.visibility = View.GONE
                    Log.d("Retrofit: onFailure", "onFailure:${t.stackTrace}")
                }
            })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchItem = menu?.findItem(R.id.search)
        val searchView: SearchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchNews(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
        return true
    }
}