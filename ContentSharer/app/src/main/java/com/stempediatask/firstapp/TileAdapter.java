package com.stempediatask.firstapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class TileAdapter extends RecyclerView.Adapter<TileAdapter.TileViewHolder> {

    private Context context;
    private List<Tile> tileList;

    public TileAdapter(Context context, List<Tile> tileList) {
        this.context = context;
        this.tileList = tileList;
    }

    @NonNull
    @Override
    public TileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the tile_item.xml layout
        View view = LayoutInflater.from(context).inflate(R.layout.tile_item, parent, false);
        return new TileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TileViewHolder holder, int position) {
        // Get the data for the current tile
        Tile currentTile = tileList.get(position);

        // Set the text
        holder.tileText.setText(currentTile.getTitle());

        // Use Glide to load the image from the URL
        Glide.with(context)
                .load(currentTile.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background) // Optional: a placeholder
                .into(holder.tileImage);

        // --- Handle Clicks ---
        holder.itemView.setOnClickListener(v -> {
            // Check if this tile has a videoUrl
            if (currentTile.getVideoUrl() != null && !currentTile.getVideoUrl().isEmpty()) {
                // It's the YouTube tile. Open the URL in a browser or YouTube app.
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentTile.getVideoUrl()));
                context.startActivity(browserIntent);
            } else {
                // It's a normal tile. You can add other logic here if you want.
                // For now, it does nothing.
            }
        });
    }

    @Override
    public int getItemCount() {
        return tileList.size();
    }

    // The ViewHolder class holds the views for a single tile
    public static class TileViewHolder extends RecyclerView.ViewHolder {
        ImageView tileImage;
        TextView tileText;

        public TileViewHolder(@NonNull View itemView) {
            super(itemView);
            tileImage = itemView.findViewById(R.id.tileImage);
            tileText = itemView.findViewById(R.id.tileText);
        }
    }
}
