package com.javidran.imagejournal.view.album

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.javidran.imagejournal.R
import com.javidran.imagejournal.model.Album
import com.javidran.imagejournal.model.Entry

class EntryListAdapter(private val dataSet: List<Entry>, private val onClick: (Entry) -> Unit) :
    RecyclerView.Adapter<EntryListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, val onClick: (Entry) -> Unit) : RecyclerView.ViewHolder(view) {
        val number: TextView = view.findViewById(R.id.entry_number)
        val image: ImageView = view.findViewById(R.id.entry_image)
        private var currentEntry: Entry? = null

        init {
            itemView.setOnClickListener {
                currentEntry?.let {
                    onClick(it)
                }
            }
        }

        fun bind(entry: Entry) {
            currentEntry = entry
            number.text = entry.number.toString()
            //TODO ImageView image set
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.entry_item, viewGroup, false)

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
