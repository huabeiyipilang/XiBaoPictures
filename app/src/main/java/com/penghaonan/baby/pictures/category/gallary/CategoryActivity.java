package com.penghaonan.baby.pictures.category.gallary;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.iflytek.cloud.SpeechSynthesizer;
import com.penghaonan.appframework.utils.UiUtils;
import com.penghaonan.baby.pictures.R;
import com.penghaonan.baby.pictures.UrlConstants;
import com.penghaonan.baby.pictures.base.BaseActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends BaseActivity {

    public static final String EXTRAS_CATEGORY_NAME = "category_name";

    @BindView(R.id.vp_gallary)
    ViewPager mViewPager;

    @BindView(R.id.view_loading)
    View mLoadingView;

    private String mCategoryName;
    private PicAdapter mAdapter;
    private List<PicBean> mPicList = Collections.EMPTY_LIST;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiUtils.initActivityStatusNavigationBarColor(this, Color.BLACK);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        mCategoryName = getIntent().getStringExtra(EXTRAS_CATEGORY_NAME);
        loadData();
    }

    private void loadData() {
        Request request = new StringRequest(
                String.format(UrlConstants.URL_CATEGORIES, mCategoryName),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onDataReceived(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onDataReceived(null);
                    }
                });
        Volley.newRequestQueue(this).add(request);
    }

    private void onDataReceived(String response) {
        Gson gson = new Gson();
        CategoryResponse res = null;
        if (!TextUtils.isEmpty(response)) {
            res = gson.fromJson(response, CategoryResponse.class);
        }
        if (res == null) {
            response = getLocatData();
            res = gson.fromJson(response, CategoryResponse.class);
        }
        if (res != null) {
            mPicList = res.pic_list;
            updateUI();
        }
    }

    private String getLocatData() {
        String localData = null;
        try {
            InputStream is = getResources().getAssets().open("category_cards/" + mCategoryName + ".json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            localData = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return localData;
    }

    private void updateUI() {
        mLoadingView.setVisibility(View.GONE);
        mViewPager.setVisibility(View.VISIBLE);
        mAdapter = new PicAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                speakCardAtPostion(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        speakCardAtPostion(mViewPager.getCurrentItem());
    }

    private void speakCardAtPostion(int position) {
        SpeechSynthesizer.getSynthesizer().stopSpeaking();
        SpeechSynthesizer.getSynthesizer().startSpeaking(mPicList.get(position).name, null);
    }

    private class PicAdapter extends FragmentStatePagerAdapter {

        PicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mPicList.size();
        }

        @Override
        public Fragment getItem(int position) {
            PicBean picBean = mPicList.get(position);
            String path = picBean.path + "?auto=compress&cs=tinysrgb&w="
                    + UiUtils.getWindowWidth();
            return CategoryPicFragment.newInstance(picBean.name, path);
        }
    }
}
