package com.compet.petdoc.fragment;

import android.content.Context;

import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import com.compet.petdoc.R;

public class BookmarkFragment extends BaseFragment {

    Context mContext;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public static BookmarkFragment newInstance() {
        BookmarkFragment fragment = new BookmarkFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        initToolBar("즐겨찾기", view, mContext);
        return view;
    }

}
