package com.stempediatask.mvvmappdemo.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stempediatask.mvvmappdemo.R;
import com.stempediatask.mvvmappdemo.databinding.FragmentItemListBinding;
import com.stempediatask.mvvmappdemo.viewmodel.NoteViewModel;

// This Fragment (View) displays the list of notes.
// It observes the NoteViewModel for changes in the list data.
public class ItemListFragment extends Fragment {

    private FragmentItemListBinding binding;
    private NoteViewModel noteViewModel;
    private NoteAdapter adapter;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentItemListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        // Initialize NoteViewModel (scoped to the Activity for shared data)
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        // Setup RecyclerView
        binding.recyclerViewNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NoteAdapter();
        binding.recyclerViewNotes.setAdapter(adapter);

        // --- Observe LiveData from ViewModel ---

        // Observe all notes: When the list changes, update the RecyclerView adapter.
        noteViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> {
            // When the LiveData changes, we update the RecyclerView adapter.
            adapter.submitList(notes);
            // UX: Show empty message if list is empty
            binding.textViewEmptyList.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
        });

        // Observe loading state to show/hide ProgressBar
        noteViewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // Comment: The ViewModel prepares data for the Fragment.
            // The Fragment (View) is "dumb", it just observes the ViewModel
            // and updates its UI based on the state provided by the ViewModel.
            // It does not know where the data comes from (Firestore, local DB, etc.).
            binding.progressBarList.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Observe error messages from the ViewModel
        noteViewModel.getErrorMessages().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // --- Set up UI Listeners ---

        binding.fabAddNote.setOnClickListener(v -> {
            // Navigate to the AddItemFragment when FAB is clicked
            navController.navigate(R.id.action_itemListFragment_to_addItemFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clear binding reference
    }
}