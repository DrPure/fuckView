package ml.qingsu.fuckview;

import android.app.Application;
import android.content.Context;

import ml.qingsu.fuckview.services.LazyLoadService;
import ml.qingsu.fuckview.utils.root.AppRulesUtils;


/**
 * Created by w568w on 2017-6-30.
 *
 * @author w568w
 * @author YanLu
 */

public class MyApplication extends Application {
    // 暂时关闭，按需修改
    private boolean isOpenSharedFile = false;
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        //Debug.startMethodTracing("fuckview");
        // 设置文件夹权限，让其他 App 可以读取
//        if (isOpenSharedFile) {
//            AppRulesUtils.setFilePermissions(getApplicationInfo().dataDir, 0757, -1, -1);
//            AppRulesUtils.setFilePermissions(getDir(AppRulesUtils.RULES_DIR, Context.MODE_PRIVATE), 0777, -1, -1);
//        }

        LazyLoadService.start(this);
    }

}
