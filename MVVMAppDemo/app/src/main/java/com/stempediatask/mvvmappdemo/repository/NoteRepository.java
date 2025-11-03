// This class handles all direct interaction with FirebaseFirestore.
// It uses MutableLiveData to update the ViewModel when data changes in Firestore.postValue() is
//          safe to call from any thread.
// It structures data in Firestore under users/{UID}/notes for user-specific data.
// It includes error handling for Firebase operations.

package com.stempediatask.mvvmappdemo.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.stempediatask.mvvmappdemo.model.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NoteRepository {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private MutableLiveData<List<Note>> allNotesLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> saveSuccessLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public NoteRepository() {

    }

    // Get the collection reference for the current user's notes
    private CollectionReference getUserNotesCollection() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            return db.collection("users")
                    .document(currentUser.getUid())
                    .collection("notes");
        }
        return null;
    }

    // --- Public methods for the ViewModel to call ---

    // Fetches all notes for the current user from Firestore and observes changes
    public LiveData<List<Note>> getAllNotes() {
        CollectionReference notesCollection = getUserNotesCollection();
        if (notesCollection != null) {
            // Listen for real-time updates
            notesCollection.orderBy("title", Query.Direction.ASCENDING)
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            errorLiveData.postValue("Failed to fetch notes: " + e.getMessage());
                            return;
                        }

                        List<Note> notes = new ArrayList<>();
                        if (queryDocumentSnapshots != null) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                Note note = document.toObject(Note.class);
                                note.setId(document.getId()); // Set the Firestore document ID
                                notes.add(note);
                            }
                        }
                        allNotesLiveData.postValue(notes); // Update LiveData on main thread
                    });
        } else {
            errorLiveData.postValue("User not logged in or collection not available.");
            allNotesLiveData.postValue(new ArrayList<>()); // Clear notes if no user
        }
        return allNotesLiveData;
    }

    // Saves a new note to Firestore
    public LiveData<Boolean> saveNote(Note note) {
        saveSuccessLiveData.postValue(false); // Reset status
        CollectionReference notesCollection = getUserNotesCollection();
        if (notesCollection != null) {
            notesCollection.add(note)
                    .addOnSuccessListener(documentReference -> {
                        saveSuccessLiveData.postValue(true);
                    })
                    .addOnFailureListener(e -> {
                        errorLiveData.postValue("Failed to save note: " + e.getMessage());
                        saveSuccessLiveData.postValue(false);
                    });
        } else {
            errorLiveData.postValue("User not logged in, cannot save note.");
            saveSuccessLiveData.postValue(false);
        }
        return saveSuccessLiveData;
    }

    // Expose error messages
    public LiveData<String> getErrorMessages() {
        return errorLiveData;
    }
}