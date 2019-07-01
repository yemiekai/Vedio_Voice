package com.yemiekai.vedio_voice.utils.tools;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity栈, 通过单例模式的Activity栈来管理所有Activity
 */
public class AtyContainer {

    public AtyContainer() {
    }

    private static AtyContainer instance = new AtyContainer();
    private static List<Activity> activityStack = new ArrayList<Activity>();
    public static AtyContainer getInstance() {
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new ArrayList<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity = null;
        }
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {

        // 再结束所有活动
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }
}
