package com.stempediatask.mvvmappdemo.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stempediatask.mvvmappdemo.R;
import com.stempediatask.mvvmappdemo.databinding.FragmentAddItemBinding;
import com.stempediatask.mvvmappdemo.viewmodel.NoteViewModel;

// This Fragment (View) allows the user to add a new note.
// It interacts with the NoteViewModel to save the note to Firestore.
public class AddItemFragment extends Fragment {

    private FragmentAddItemBinding binding;
    private NoteViewModel noteViewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        // Initialize NoteViewModel (scoped to the Activity for shared data)
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        // --- Observe LiveData from ViewModel ---

        // Observe save success: If note saved, navigate back.
        noteViewModel.getSaveSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(getContext(), "Note saved!", Toast.LENGTH_SHORT).show();
                // Comment: Upon successful save, we navigate back to the list.
                // The popUpToInclusive="true" in nav_graph ensures this fragment is cleared.
                navController.navigate(R.id.action_addItemFragment_to_itemListFragment);
            }
        });

        // Observe loading state to show/hide ProgressBar
        noteViewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBarAdd.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.buttonSaveNote.setEnabled(!isLoading); // Disable button during loading
        });

        // Observe error messages from the ViewModel
        noteViewModel.getErrorMessages().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // --- Set up UI Listener ---

        binding.buttonSaveNote.setOnClickListener(v -> {
            String title = binding.editTextNoteTitle.getText().toString().trim();
            String description = binding.editTextNoteDescription.getText().toString().trim();

            if (!title.isEmpty() && !description.isEmpty()) {
                noteViewModel.addNewNote(title, description);
            } else {
                Toast.makeText(getContext(), "Please enter both title and description", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clear binding reference
    }
}