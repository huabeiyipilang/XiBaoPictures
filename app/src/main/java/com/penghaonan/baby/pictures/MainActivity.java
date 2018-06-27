package com.penghaonan.baby.pictures;

import android.content.Intent;
import android.os.Bundle;

import com.penghaonan.baby.pictures.base.BaseActivity;
import com.penghaonan.baby.pictures.category.CategoryListActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        finish();
        startActivity(new Intent(this, CategoryListActivity.class));
    }
}
