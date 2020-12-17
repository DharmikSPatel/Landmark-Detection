package com.example.LandmarkDetection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

public class PageFragment extends Fragment {
    Landmark landmark;
    int positionInViewPager;
    TextView location;
    TextView author;
    ImageView imageView;
    ImageView icon;
    ConstraintLayout constraintLayout;

    public PageFragment(Landmark landmark, int positionInViewPager){
        this.landmark = landmark;
        this.positionInViewPager = positionInViewPager;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View pageFragmentView  = inflater.inflate(R.layout.fragment_page, container, false);
        location = pageFragmentView.findViewById(R.id.id_fragment_name);
        imageView = pageFragmentView.findViewById(R.id.id_fragment_image);
        author = pageFragmentView.findViewById(R.id.id_fragment_authur);
        icon = pageFragmentView.findViewById(R.id.id_fragment_icon);
        constraintLayout = pageFragmentView.findViewById(R.id.id_fragment_cl);

        author.setText("By: "+landmark.getPhotographersAtPos(positionInViewPager+1));
        location.setText(landmark.getLandmarkName());
        Picasso.get()
                .load(landmark.getURLsAtPos(positionInViewPager+1))
                .fit()
                .centerCrop()
                .into(imageView);
        Picasso.get()
                .load(landmark.getIconsAtPos(positionInViewPager+1))
                .fit()
                .centerCrop()
                .into(icon);

        return pageFragmentView;
    }
}
