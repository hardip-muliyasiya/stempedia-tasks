// This ViewModel manages the authentication state and operations. It exposes LiveData
// objects that AuthActivity will observe to react to login success, errors, or loading states.

package com.stempediatask.mvvmappdemo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private MutableLiveData<FirebaseUser> userLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();

    public AuthViewModel() {
        // Check if user is already logged in when ViewModel is created
        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
        }
    }

    // --- Public methods for the View to call ---

    public void login(String email, String password) {
        loadingLiveData.postValue(true);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    loadingLiveData.postValue(false);
                    if (task.isSuccessful()) {
                        userLiveData.postValue(firebaseAuth.getCurrentUser());
                    } else {
                        errorLiveData.postValue(task.getException().getMessage());
                    }
                });
    }

    // --- Getters for LiveData (for the View to observe) ---
    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }
}