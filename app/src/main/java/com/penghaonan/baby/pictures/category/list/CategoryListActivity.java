package com.penghaonan.baby.pictures.category.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.penghaonan.baby.pictures.R;
import com.penghaonan.baby.pictures.UrlConstants;
import com.penghaonan.baby.pictures.base.BaseActivity;
import com.penghaonan.baby.pictures.category.gallary.CategoryActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryListActivity extends BaseActivity {

    @BindView(R.id.lv_category)
    ListView mListView;

    @BindView(R.id.view_loading)
    View mLoadingView;

    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;

    private List<CategoryBean> mCategoryList = Collections.EMPTY_LIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        loadData();
    }

    private void loadData() {
        Request request = new StringRequest(
                UrlConstants.URL_CATEGORY_LIST,
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
        CategoryListResponse res = null;
        if (!TextUtils.isEmpty(response)) {
            res = gson.fromJson(response, CategoryListResponse.class);
        }
        if (res == null) {
            response = getLocatData();
            res = gson.fromJson(response, CategoryListResponse.class);
        }
        if (res != null) {
            mCategoryList = res.category_list;
            updateUI();
        }
    }

    private void updateUI() {
        mLoadingView.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        mListView.setAdapter(new CategoryAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryBean categoryBean = mCategoryList.get(position);
                Intent intent = new Intent(CategoryListActivity.this, CategoryActivity.class);
                intent.putExtra(CategoryActivity.EXTRAS_CATEGORY_NAME, categoryBean.name);
                startActivity(intent);
            }
        });
    }

    private String getLocatData() {
        String localData = null;
        try {
            InputStream is = getResources().getAssets().open("local_category_data.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            localData = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return localData;
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