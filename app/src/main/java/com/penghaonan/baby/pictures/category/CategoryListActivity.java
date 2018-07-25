package com.penghaonan.baby.pictures.category;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.penghaonan.baby.pictures.R;
import com.penghaonan.baby.pictures.base.BaseActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryListActivity extends BaseActivity {

    @BindView(R.id.lv_category)
    ListView mListView;
    private List<CategoryBean> mCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        mCategoryList = getLocalCategoryList();
        mListView.setAdapter(new CategoryAdapter());
    }

    private List<CategoryBean> getLocalCategoryList() {
        String localData;
        try {
            InputStream is = getResources().getAssets().open("local_category_data.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            localData = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        Gson gson = new Gson();
        CategoryListResponse response = gson.fromJson(localData, CategoryListResponse.class);
        return response.category_list;
    }

    private class CategoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mCategoryList.size();
        }

        @Override
        public CategoryBean getItem(int position) {
            return mCategoryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.itemview_category_list, null);
                viewHolder = new ViewHolder();
                ButterKnife.bind(viewHolder, convertView);
                convertView.setTag(viewHolder);
                ViewGroup.LayoutParams params = viewHolder.posterView.getLayoutParams();
                params.height = parent.getWidth() * 470 / 1080;
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            CategoryBean categoryBean = mCategoryList.get(position);
            viewHolder.titleView.setText(categoryBean.name);
            Glide.with(CategoryListActivity.this)
                    .load(categoryBean.poster)
                    .into(viewHolder.posterView);
            return convertView;
        }
    }

    class ViewHolder {
        @BindView(R.id.imageView)
        ImageView posterView;
        @BindView(R.id.textView)
        TextView titleView;
    }
}