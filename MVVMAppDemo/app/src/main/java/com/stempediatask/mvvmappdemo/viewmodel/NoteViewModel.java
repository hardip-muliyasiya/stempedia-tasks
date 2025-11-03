package com.stempediatask.mvvmappdemo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.stempediatask.mvvmappdemo.model.Note;
import com.stempediatask.mvvmappdemo.repository.NoteRepository;

import java.util.List;

// The ViewModel for managing notes (listing and adding).
// It gets notes from the NoteRepository and exposes them to the UI as LiveData.
public class NoteViewModel extends ViewModel {

    private NoteRepository noteRepository;
    private LiveData<List<Note>> allNotes; // Observe notes from repository
    private MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private LiveData<String> errorMessages;

    public NoteViewModel() {
        noteRepository = new NoteRepository();
        allNotes = noteRepository.getAllNotes();
        errorMessages = noteRepository.getErrorMessages();
        loading.postValue(false); // Initial loading state
    }

    // --- Public methods for the View to call ---
    public void addNewNote(String title, String description) {
        loading.postValue(true);
        Note newNote = new Note(null, title, description);
        noteRepository.saveNote(newNote).observeForever(success -> {
            saveSuccess.postValue(success);
            loading.postValue(false);
        });
    }

    // --- Getters for LiveData (for the View to observe) ---
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<Boolean> getSaveSuccess() {
        return saveSuccess;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getErrorMessages() {
        return errorMessages;
    }
}