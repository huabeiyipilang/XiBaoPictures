package com.penghaonan.baby.pictures.category.gallary;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.iflytek.cloud.SpeechSynthesizer;
import com.penghaonan.baby.pictures.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryPicFragment extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_PIC_PATH = "pic_path";

    @BindView(R.id.tv_animal_name)
    TextView mTitleView;

    @BindView(R.id.iv_pic)
    ImageView mPicView;

    @BindView(R.id.view_loading)
    View mLoadingView;

    private String mTitle;
    private String mPicPath;

    public CategoryPicFragment() {
    }

    public static CategoryPicFragment newInstance(String title, String picPath) {
        CategoryPicFragment fragment = new CategoryPicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_PIC_PATH, picPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
            mPicPath = getArguments().getString(ARG_PIC_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category_pic, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @OnClick(R.id.iv_pic)
    void speakCard() {
        SpeechSynthesizer.getSynthesizer().stopSpeaking();
        SpeechSynthesizer.getSynthesizer().startSpeaking(mTitle, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        mTitleView.setText(mTitle);
        mLoadingView.setVisibility(View.VISIBLE);
        mPicView.setVisibility(View.GONE);
        Glide.with(this)
                .load(mPicPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource,
                                                @Nullable Transition<? super Drawable> transition) {
                        mLoadingView.setVisibility(View.GONE);
                        mPicView.setVisibility(View.VISIBLE);
                        mPicView.setImageDrawable(resource);
                    }
                });
    }
}
