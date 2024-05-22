import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newsapp.R
import com.example.newsapp.data.response.ArticlesItem
import com.example.newsapp.databinding.ItemNewsBinding

class NewsAdapter(private val listener: (ArticlesItem) -> Unit) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private var news: List<ArticlesItem> = listOf()

    fun setNews(newArticles: List<ArticlesItem>) {
        val oldSize = news.size
        news = newArticles
        notifyItemRangeChanged(oldSize, newArticles.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNewsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = news.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(news[position])
    }

    inner class ViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(newsItem: ArticlesItem) {
            binding.tvTitle.text = newsItem.title
            // Gunakan Glide untuk memuat gambar dari urlToImage
            Glide.with(itemView.context)
                .load(newsItem.urlToImage)
                .apply(RequestOptions().dontTransform().placeholder(R.drawable.loading_img))
                .into(binding.thumbnail) // asumsi Anda memiliki ImageView dalam layout item

            binding.root.setOnClickListener { listener(newsItem) }

        }
    }
}
