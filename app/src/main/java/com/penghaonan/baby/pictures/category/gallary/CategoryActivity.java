package com.penghaonan.baby.pictures.category.gallary;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.google.gson.Gson;
import com.penghaonan.appframework.utils.UiUtils;
import com.penghaonan.baby.pictures.R;
import com.penghaonan.baby.pictures.base.BaseActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends BaseActivity {

    public static final String EXTRAS_CATEGORY_NAME = "category_name";

    @BindView(R.id.vp_gallary)
    ViewPager mViewPager;

    private String mCategoryName;
    private PicAdapter mAdapter;
    private List<PicBean> mPicList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiUtils.initActivityStatusNavigationBarColor(this, Color.BLACK);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        mCategoryName = getIntent().getStringExtra(EXTRAS_CATEGORY_NAME);
        mPicList = getLocalPicList();

        mAdapter = new PicAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
    }

    private List<PicBean> getLocalPicList() {
        String localData;
        try {
            InputStream is = getResources().getAssets().open("category_cards/" + mCategoryName + ".json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            localData = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        Gson gson = new Gson();
        PicListResponse response = gson.fromJson(localData, PicListResponse.class);
        return response.pic_list;
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
