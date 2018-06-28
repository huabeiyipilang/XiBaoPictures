package com.penghaonan.baby.pictures.reporter;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.penghaonan.appframework.AppDelegate;
import com.penghaonan.appframework.base.BaseFrameworkFragment;
import com.penghaonan.appframework.reporter.IReporter;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.util.Map;

public class UmengReporter implements IReporter {
    @Override
    public void setChannel(String channel) {
        UMConfigure.init(AppDelegate.getApp(), "5b3368a4f29d9879b2000037", channel,
                UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setScenarioType(AppDelegate.getApp(), MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    @Override
    public void onActivityResume(@NonNull Activity activity) {
        MobclickAgent.onResume(activity);
    }

    @Override
    public void onActivityPause(@NonNull Activity activity) {
        MobclickAgent.onPause(activity);
    }

    @Override
    public void onFragmentResume(@NonNull BaseFrameworkFragment fragment) {
        MobclickAgent.onPageStart(fragment.getFragmentName());
    }

    @Override
    public void onFragmentPause(@NonNull BaseFrameworkFragment fragment) {
        MobclickAgent.onPageEnd(fragment.getFragmentName());
    }

    @Override
    public void onEvent(String eventId) {
        MobclickAgent.onEvent(AppDelegate.getApp(), eventId);
    }

    @Override
    public void onEvent(String eventId, String value) {
        MobclickAgent.onEvent(AppDelegate.getApp(), eventId, value);
    }

    @Override
    public void onEvent(String eventId, Map<String, String> values) {
        MobclickAgent.onEvent(AppDelegate.getApp(), eventId, values);
    }
}
