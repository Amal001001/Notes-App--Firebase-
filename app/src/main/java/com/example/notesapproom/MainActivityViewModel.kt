package com.example.notesapproom

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore

class MainActivityViewModel(application: Application): AndroidViewModel(application) {

    var db = FirebaseFirestore.getInstance()
    private val notes: MutableLiveData<List<Notes>> = MutableLiveData()

    fun readFromDB(): LiveData<List<Notes>> {
        return notes
    }

    fun getNotes(){
        db.collection("notes")
            .get()
            .addOnSuccessListener { result ->
                val tempNotes = arrayListOf<Notes>()
                for (document in result) {
                   // tempNotes.add(Notes(document.id, document.data.values.toString()))
                   document.data.map { (key, value) -> tempNotes.add(Notes(document.id, value.toString())) }
                }
                notes.postValue(tempNotes)
            }
            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents.", exception)
            }
    }

     fun insert(note: Notes){
         CoroutineScope(Dispatchers.IO).launch {
             val newNote = hashMapOf("noteText" to note.noteText)
             db.collection("notes").add(newNote)
             getNotes()
         }
     }

     fun update(noteID: String, noteText: String){
         CoroutineScope(Dispatchers.IO).launch {
             db.collection("notes")
                 .get()
                 .addOnSuccessListener { result ->
                     for (document in result) {
                         if(document.id == noteID){
                             db.collection("notes").document(noteID).update("noteText", noteText)
                         }
                     }
                     getNotes()
                 }
                 .addOnFailureListener { exception ->
                     Log.w("MainActivity", "Error getting documents.", exception)
                 }
         }
    }

    fun delete(noteID: String){
        CoroutineScope(Dispatchers.IO).launch {
            db.collection("notes")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if(document.id == noteID){
                            db.collection("notes").document(noteID).delete()
                        }
                    }
                    getNotes()
                }
                .addOnFailureListener { exception ->
                    Log.w("MainActivity", "Error getting documents.", exception)
                }
        }
    }

}