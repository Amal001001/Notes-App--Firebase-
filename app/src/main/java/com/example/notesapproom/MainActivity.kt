package com.example.notesapproom

import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var mainActivityViewModel: MainActivityViewModel

    private lateinit var rv: RecyclerView
    lateinit var adapter: Adapter
    lateinit var items: ArrayList<Notes>

    lateinit var et:EditText
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        items = arrayListOf()
        rv = findViewById(R.id.rv)
        adapter = Adapter(this)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        mainActivityViewModel.readFromDB().observe(this, { items -> adapter.update(items) })

        et = findViewById(R.id.et)
        button = findViewById(R.id.button)
        button.setOnClickListener {
            var newNote = et.text.toString()
            if(newNote != "") {
                mainActivityViewModel.insert(Notes("",newNote))
                et.text.clear()
                Toast.makeText(this, "Note added", Toast.LENGTH_LONG).show()
            }
        }

        mainActivityViewModel.getNotes()
    }

    fun raiseDialog(id: String, note:String){

        val alert = AlertDialog.Builder(this)
        alert.setTitle("Update Recipe")

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val updatedNote = EditText(this)
        updatedNote.setText(note)
        layout.addView(updatedNote)

        layout.setPadding(50, 40, 50, 10)
        alert.setView(layout)

        alert.setPositiveButton("Update") { _, _ ->
            val updatedNote = updatedNote.text.toString()
            mainActivityViewModel.update(id, updatedNote)
            Toast.makeText(this, "Updated Sucessfully", Toast.LENGTH_LONG).show()
        }

        alert.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        alert.setCancelable(false)
        alert.show()
    }

        @SuppressLint("SetTextI18n")
        fun deleteDialog(item: String) {
            val dialogBuilder = AlertDialog.Builder(this)
            val confirmDelete = TextView(this)
            confirmDelete.text = "  Are you sure you want to delete this note?"
            dialogBuilder.setCancelable(false).setPositiveButton("Yes",
                    DialogInterface.OnClickListener { _, _ -> mainActivityViewModel.delete(item) })
                   .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })
            val alert = dialogBuilder.create()
            alert.setTitle("Delete Note")
            alert.setView(confirmDelete)
            alert.show()

        }

}