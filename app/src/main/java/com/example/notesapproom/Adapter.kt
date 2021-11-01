package com.example.notesapproom

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapproom.databinding.ItemRowBinding

class Adapter (private val activity: MainActivity): RecyclerView.Adapter<Adapter.ItemViewHolder>() {
    private var notes = emptyList<Notes>()
  class ItemViewHolder(val binding: ItemRowBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
      return ItemViewHolder(
          ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val note = notes[position]
            holder.binding.apply {
                tv.text = note.noteText
                ibUpdate.setOnClickListener {
                    activity.raiseDialog(note.id,note.noteText)
                }
                ibDelete.setOnClickListener {
                    activity.deleteDialog(note.id)
                }
            }
    }
    override fun getItemCount() = notes.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(notes: List<Notes>){
        this.notes = notes
        notifyDataSetChanged()
    }
}