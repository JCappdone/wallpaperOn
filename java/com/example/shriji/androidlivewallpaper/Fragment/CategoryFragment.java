package com.example.shriji.androidlivewallpaper.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.shriji.androidlivewallpaper.Activity.ListWallpaper;
import com.example.shriji.androidlivewallpaper.Common.Common;

import com.example.shriji.androidlivewallpaper.Model.CategoryItem;
import com.example.shriji.androidlivewallpaper.R;
import com.example.shriji.androidlivewallpaper.ViewHolder.categoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    //FirebaseUI Adapter
    FirebaseRecyclerOptions<CategoryItem> options;
    FirebaseRecyclerAdapter<CategoryItem, categoryViewHolder> adapter;

    //view
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    Unbinder unbinder;

    private static CategoryFragment INSTANCE = null;


    public CategoryFragment() {
        // Required empty public constructor
    Query query = FirebaseDatabase.getInstance()
            .getReference()
            .child(Common.STR_CATEGORY_BACKGROUND);

        options = new FirebaseRecyclerOptions.Builder<CategoryItem>()
                .setQuery(query, CategoryItem.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<CategoryItem, categoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final categoryViewHolder holder, final int position, @NonNull final CategoryItem model) {
                Picasso.get()
                        .load(model.getImageLink())
                        .into(holder.imgBG);

                holder.txtName.setText(model.getName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.CATEGORY_ID_SELECTED = adapter.getRef(position).getKey(); //get key of item
                        Common.CATEGORY_SELECTED = model.getName();
                        Intent intent = new Intent(getActivity(), ListWallpaper.class);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public categoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.category_item_layout, parent, false);
                return new categoryViewHolder(itemView);
            }
        };
    }

    public static CategoryFragment getInstanse() {
        if (INSTANCE == null)
            INSTANCE = new CategoryFragment();
        return INSTANCE;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        unbinder = ButterKnife.bind(this, view);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        setCategory();
        return view;
    }

    private void setCategory() {
        adapter.startListening();
        recycleView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}

