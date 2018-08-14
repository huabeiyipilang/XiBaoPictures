package com.penghaonan.baby.pictures;

import android.app.Application;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.penghaonan.appframework.AppDelegate;
import com.penghaonan.appframework.reporter.Reporter;
import com.penghaonan.appframework.utils.Logger;
import com.penghaonan.baby.pictures.reporter.UmengReporter;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // framework
        AppDelegate.init(this);
        AppDelegate.setDebug(BuildConfig.DEBUG);

        // 友盟统计
        Reporter.getInstance().addReporter(new UmengReporter());

        // 科大讯飞 在线语音合成SDK
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5b34d84f");
        SpeechSynthesizer tts = SpeechSynthesizer.createSynthesizer(this, new InitListener() {

            @Override
            public void onInit(int i) {
                Logger.i("SpeechSynthesizer init result:" + i);
            }
        });
        // 清空参数
        tts.setParameter(SpeechConstant.PARAMS, null);
        // 设置在线云端
        tts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置发音人
        tts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        // 设置发音语速
        tts.setParameter(SpeechConstant.SPEED, "50");
        // 设置音调
        tts.setParameter(SpeechConstant.PITCH, "50");
        // 设置合成音量
        tts.setParameter(SpeechConstant.VOLUME, "100");
        // 设置播放器音频流类型
        tts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        tts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        // 设置音频保存路径，需要申请WRITE_EXTERNAL_STORAGE权限，如不需保存注释该行代码
        tts.setParameter(SpeechConstant.TTS_AUDIO_PATH,"./sdcard/iflytek.pcm");

    }
}
