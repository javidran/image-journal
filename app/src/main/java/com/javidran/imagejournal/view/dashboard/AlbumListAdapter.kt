package com.javidran.imagejournal.view.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.javidran.imagejournal.R
import com.javidran.imagejournal.model.Album

class AlbumListAdapter(private val dataSet: List<Album>, private val onClick: (Album) -> Unit) :
    RecyclerView.Adapter<AlbumListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, val onClick: (Album) -> Unit) : RecyclerView.ViewHolder(view) {
        val album_title: TextView = view.findViewById(R.id.album_title)
        val album_emoji: TextView = view.findViewById(R.id.album_emoji)
        private var currentAlbum: Album? = null

        init {
            itemView.setOnClickListener {
                currentAlbum?.let {
                    onClick(it)
                }
            }
        }

        fun bind(album: Album) {
            currentAlbum = album
            album_title.text = album.title
            album_emoji.text = album.emoji
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.album_item, viewGroup, false)

        return ViewHolder(view, onClick)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bind(dataSet[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
