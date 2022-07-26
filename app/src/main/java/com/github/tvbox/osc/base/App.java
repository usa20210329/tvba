package com.github.tvbox.osc.base;

import androidx.multidex.MultiDexApplication;

import com.github.tvbox.osc.callback.EmptyCallback;
import com.github.tvbox.osc.callback.LoadingCallback;
import com.github.tvbox.osc.data.AppDataManager;
import com.github.tvbox.osc.server.ControlManager;
import com.github.tvbox.osc.util.HawkConfig;
import com.github.tvbox.osc.util.OkGoHelper;
import com.github.tvbox.osc.util.PlayerHelper;
import com.kingja.loadsir.core.LoadSir;
import com.orhanobut.hawk.Hawk;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

import com.tencent.bugly.crashreport.CrashReport;
import android.text.TextUtils;

/**
 * @author pj567
 * @date :2020/12/17
 * @description:
 */
public class App extends MultiDexApplication {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // 初始化bugly
        CrashReport.initCrashReport(getApplicationContext(), "27447de2a7", false); 
        initParams();
        // OKGo
        OkGoHelper.init();
        // 初始化Web服务器
        ControlManager.init(this);
        //初始化数据库
        AppDataManager.init();
        LoadSir.beginBuilder()
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .commit();
        AutoSizeConfig.getInstance().setCustomFragment(true).getUnitsManager()
                .setSupportDP(false)
                .setSupportSP(false)
                .setSupportSubunits(Subunits.MM);
        PlayerHelper.init();
    }

    private void initParams() {
        // Hawk
        Hawk.init(this).build();
        putAbsent(HawkConfig.DEBUG_OPEN, false);
        putAbsent(HawkConfig.API_URL, "https://raw.iqiq.io/anaer/Sub/main/meow/meow.json");
        putAbsent(HawkConfig.PLAY_TYPE, 1);
        putAbsent(HawkConfig.CHANNEL_HINT,"%n 频道%c 源%s/%a");
    }

    /**
     * 如果不存在则设置值.
     * @param key
     * @param value
     */
    private void putAbsent(String key, Object value){
        if(!Hawk.contains(key)){
            Hawk.put(key, value);
        }
    }

    public static App getInstance() {
        return instance;
    }
}