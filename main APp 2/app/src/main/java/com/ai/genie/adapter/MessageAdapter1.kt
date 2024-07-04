// Import necessary Android and app-related libraries
package com.ai.genie.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.ai.genie.R
import com.ai.genie.models.MessageModel
import com.ai.genie.ui.home.view.FullImageActivity
import java.util.*

// Create an interface for a click listener when an image download is clicked
interface MessageClickListener {
    fun onImageDownloadClick(position: Int, message: String)
}

// Create a class for the adapter that extends RecyclerView.Adapter
class MessageAdapter1(
    val list: ArrayList<MessageModel>, // List of MessageModel objects
    private val clickListener: MessageClickListener // Listener for image download click
) :
    Adapter<MessageAdapter1.MessageViewHolder>() {

    // Create an inner class for the view holder that extends RecyclerView.ViewHolder
    inner class MessageViewHolder(view: View) : ViewHolder(view) {

        // Initialize views within the view holder
        val msgTxt = view.findViewById<TextView>(R.id.show_message) // Text message view
        val imageContainer = view.findViewById<RelativeLayout>(R.id.imageCard) // Container for images
        val image = view.findViewById<ImageView>(R.id.image) // ImageView for images
        val imageShare = view.findViewById<ImageView>(R.id.imageShare) // ImageView for images
        val imageDownload = view.findViewById<ImageView>(R.id.imageDownload) // ImageView for image download button
    }

    // Override method to create view holders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        var view: View? = null

        val from: LayoutInflater = LayoutInflater.from(parent.context)

        // Inflate different layouts based on the view type (0 for user message, 1 for other messages)
        if (viewType == 0) {
            view = from.inflate(R.layout.chatrightitem, parent, false)
        } else {
            view = from.inflate(R.layout.chatleftitem, parent, false)
        }

        return MessageViewHolder(view)
    }

    // Override method to determine the view type for a specific item position
    override fun getItemViewType(position: Int): Int {
        val message = list[position]
        return if (message.isUser) 0 else 1
    }

    // Override method to get the total item count
    override fun getItemCount(): Int {
        return list.size
    }

    // Override method to bind data to the view holder
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = list[position]

        // Set a click listener for the image download button
        if (holder.imageDownload != null) {
            holder.imageDownload.setOnClickListener {
                val position = holder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    clickListener.onImageDownloadClick(position, message.message)
                }
            }
        }

        // Control the visibility of the image container based on the message type
        if (!message.isUser) {
            holder.imageContainer.visibility = GONE
        }

        // Load and display images using Glide library if the message is an image
        if (message.isImage) {
            holder.imageContainer.visibility = VISIBLE
            Glide.with(holder.itemView.context)
                .load(message.message).into(holder.image)
            holder.image.setOnClickListener {
                val intent = Intent(holder.itemView.context, FullImageActivity::class.java)
                intent.putExtra("image_url", message.message)
                holder.itemView.context.startActivity(intent)
            }

            holder.imageShare.setOnClickListener {



                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                share.putExtra(Intent.EXTRA_SUBJECT, holder.itemView.context.getResources().getString(R.string.app_name))
                share.putExtra(Intent.EXTRA_TEXT, message.message)

                holder.itemView.context.startActivity(Intent.createChooser(share, "Share link!"))

            }

        } else {
            // If the message is not an image, set the text message
            holder.msgTxt.text = message.message
        }


    }
}
