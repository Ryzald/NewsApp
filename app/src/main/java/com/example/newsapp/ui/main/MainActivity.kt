package com.example.newsapp.ui.main

import NewsAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.BuildConfig
import com.example.newsapp.R
import com.example.newsapp.data.remote.ApiClient
import com.example.newsapp.data.response.ArticlesItem
import com.example.newsapp.data.response.NewsResponse
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.databinding.PartialShimmerBinding
import com.example.newsapp.ui.detail.DetailActivity
import com.example.newsapp.ui.detail.DetailActivity.Companion.EXTRA_NEWS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: NewsAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var shimmerBinding: PartialShimmerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shimmerBinding = PartialShimmerBinding.bind(findViewById(R.id.SfLoading))

        initRecyclerView()
        getNews()
    }

    private fun initRecyclerView() {
        adapter = NewsAdapter {
            moveActivity(it)
        }
        binding.rvNews.layoutManager = LinearLayoutManager(this)
        binding.rvNews.adapter = adapter
    }

    private fun moveActivity(news: ArticlesItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(EXTRA_NEWS, news)
        startActivity(intent)
    }

    private fun getNews() {
        loading(true)

        val apiKey = BuildConfig.API_KEY
        val apiService = ApiClient.create()

        apiService.getNews("us", apiKey).enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                loading(false)
                if (response.isSuccessful) {
                    val articles: List<ArticlesItem> = response.body()?.articles?.filterNotNull() ?: emptyList()
                    adapter.setNews(articles)
                } else {
                    Log.e("MainActivity", "Failed to fetch news: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                loading(false)
                Log.e("MainActivity", "Error fetching news", t)
            }
        })
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            shimmerBinding.SfLoading.visibility = View.VISIBLE
            shimmerBinding.SfLoading.startShimmer()
        } else {
            shimmerBinding.SfLoading.visibility = View.GONE
            shimmerBinding.SfLoading.stopShimmer()
        }
    }
}
