package com.example.newsapp.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newsapp.R
import com.example.newsapp.data.response.ArticlesItem
import com.example.newsapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_NEWS = "extraNews"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val news = intent.getParcelableExtra<ArticlesItem>(EXTRA_NEWS)
        Log.d("DetailActivity", news!!.author.toString())

        news?.let { initView(it) }
    }

    private fun initView(news: ArticlesItem) {
        binding.tvDetail.text = news.title
        binding.detailSource.text = news.source?.name
        binding.detailAuthor.text = news.author

        Glide.with(this@DetailActivity)
            .load(news.urlToImage)
            .apply(RequestOptions().dontTransform().placeholder(R.drawable.loading_img))
            .into(binding.detailPicture)

        binding.BtnBerita.setOnClickListener {
            openWebPage(news.url)
        }
    }

    private fun openWebPage(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
