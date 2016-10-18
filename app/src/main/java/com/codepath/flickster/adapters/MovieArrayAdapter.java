package com.codepath.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flickster.R;
import com.codepath.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by briasullivan on 10/16/16.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private static final int POPULAR = 0;
    private static final int UNPOPULAR = 1;

    private static class PopularViewHolder {
        TextView title;
        TextView overview;
        ImageView movieImage;
    }

    private static class ViewHolder {
        TextView title;
        TextView overview;
        ImageView movieImage;
    }


    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        int type = getItemViewType(position);
        boolean isPopularMovie = (movie.getVoteAvg() > 5.0);
        int orientation = getContext().getResources().getConfiguration().orientation;
        String imageUrl = "";

        switch (type) {
            case POPULAR:
                View v = convertView;
                PopularViewHolder popViewHolder;

                if (v == null) {
                    popViewHolder = new PopularViewHolder();
                    v = getInflatedLayoutForPopularity(isPopularMovie, parent);
                    popViewHolder.title = (TextView) v.findViewById(R.id.tvTitle);
                    popViewHolder.overview = (TextView) v.findViewById(R.id.tvOverview);
                    popViewHolder.movieImage = (ImageView) v.findViewById(R.id.ivPopMovieImage);
                    v.setTag(popViewHolder);
                } else {
                    popViewHolder = (PopularViewHolder) v.getTag();
                }

                imageUrl = movie.getBackdropPath();
                int imageWidth = getContext().getResources().getDisplayMetrics().widthPixels - 100;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    imageWidth = getContext().getResources().getDisplayMetrics().widthPixels / 3 * 2 - 100;
                }
                Picasso.with(getContext()).load(imageUrl)
                        .resize(imageWidth, 0)
                        //       .centerCrop()
                        .placeholder(R.drawable.ic_movie_filter_black_150dp)
                        .into(popViewHolder.movieImage);

                if (popViewHolder.overview != null && popViewHolder.title != null) {
                    popViewHolder.title.setText(movie.getOriginalTitle());
                    popViewHolder.overview.setText(movie.getOverview());
                }
                return v;
            case UNPOPULAR:
                ViewHolder viewHolder;
                View v2 = convertView;
                if (v2 == null) {
                    viewHolder = new ViewHolder();
                    v2 = getInflatedLayoutForPopularity(isPopularMovie, parent);
                    viewHolder.title = (TextView) v2.findViewById(R.id.tvTitle);
                    viewHolder.overview = (TextView) v2.findViewById(R.id.tvOverview);
                    viewHolder.movieImage = (ImageView) v2.findViewById(R.id.ivMovieImage);
                    v2.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) v2.getTag();
                }
                imageUrl = movie.getPosterPath();
                imageWidth = getContext().getResources().getDisplayMetrics().widthPixels / 2 - 100;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    imageWidth = getContext().getResources().getDisplayMetrics().widthPixels / 3 - 100;
                }
                Picasso.with(getContext()).load(imageUrl)
                        .resize(imageWidth, 0)
                        //       .centerCrop()
                        .placeholder(R.drawable.ic_movie_filter_black_130dp)
                        .into(viewHolder.movieImage);

                viewHolder.title.setText(movie.getOriginalTitle());
                viewHolder.overview.setText(movie.getOverview());
                return v2;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public int getItemViewType(int position) {
        // return 1 for popular movie, return 2 for unpopular movie
        Movie movie = getItem(position);
        if (movie.getVoteAvg() > 5.0) {
            return POPULAR;
        }
        return UNPOPULAR;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private View getInflatedLayoutForPopularity(boolean isPopularMovie, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if(isPopularMovie) {
            return inflater.inflate(R.layout.item_popular_movie, parent, false);
        }
        return inflater.inflate(R.layout.item_movie, parent, false);
    }
}
