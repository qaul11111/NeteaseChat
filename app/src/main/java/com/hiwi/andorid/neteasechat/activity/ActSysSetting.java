package com.hiwi.andorid.neteasechat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.hiwi.andorid.neteasechat.R;
import com.hiwi.andorid.neteasechat.config.preference.UserPreferences;
import com.hiwi.andorid.neteasechat.module.SettingTemplate;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.ui.widget.SwitchButton;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.misc.MiscService;
import com.netease.nimlib.sdk.mixpush.MixPushService;
import com.netease.nimlib.sdk.msg.MsgService;

import java.util.ArrayList;
import java.util.List;

public class ActSysSetting extends Activity{

    private SwitchButton messageReminding;
    private List<SettingTemplate> items = new ArrayList<SettingTemplate>();
    private String noDisturbTime;
    private SettingTemplate disturbItem;
    private static final int TAG_NO_DISTURBE = 3;
    private boolean notificationFlag;
    private boolean ringFlag;
    private SwitchButton ringPattern;
    private SwitchButton doNotDisturb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sys_setting);
        init();
        initData();
        initLestener();
    }


    private void init() {
        //消息提醒
        messageReminding = findViewById(R.id.setting_message_reminding);
        ringPattern = findViewById(R.id.setting_answer_ring_pattern);
        doNotDisturb = findViewById(R.id.setting_do_not_disturb);
    }

    private void initData() {
        if (UserPreferences.getStatusConfig() == null || !UserPreferences.getStatusConfig().downTimeToggle) {
            noDisturbTime = getString(R.string.setting_close);
        } else {
            noDisturbTime = String.format("%s到%s", UserPreferences.getStatusConfig().downTimeBegin,
                    UserPreferences.getStatusConfig().downTimeEnd);
        }
        initItems();
//        messageReminding.setCheck(true);
    }

    private void initItems() {

        //消息提醒
        notificationFlag = UserPreferences.getNotificationToggle();
        messageReminding.setCheck(notificationFlag);

        //响铃模式
        ringFlag = UserPreferences.getRingToggle();
        ringPattern.setCheck(ringFlag);
        //听筒模式
        NimUIKit.isEarPhoneModeEnable();

        //免打扰
        disturbItem = new SettingTemplate(TAG_NO_DISTURBE, getString(R.string.no_disturb), noDisturbTime);
        if("已关闭".equals(noDisturbTime)){
            doNotDisturb.setCheck(false);
        }else{
            doNotDisturb.setCheck(true);
        }


        //清空缓存
//        NIMClient.getService(MsgService.class).clearMsgDatabase(true);
    }


    private void initLestener() {

        messageReminding.setOnChangedListener(new SwitchButton.OnChangedListener() {
            @Override
            public void OnChanged(View v, boolean checkState) {
                setMessageNotify(checkState);
            }
        });

        ringPattern.setOnChangedListener(new SwitchButton.OnChangedListener() {
            @Override
            public void OnChanged(View v, boolean checkState) {
                UserPreferences.setRingToggle(checkState);
                StatusBarNotificationConfig config = UserPreferences.getStatusConfig();
                config.ring = checkState;
                UserPreferences.setStatusConfig(config);
                NIMClient.updateStatusBarNotificationConfig(config);
            }
        });

        doNotDisturb.setOnChangedListener(new SwitchButton.OnChangedListener() {
            @Override
            public void OnChanged(View v, final boolean checkState) {
                NIMClient.getService(MixPushService.class).setPushNoDisturbConfig(checkState,
                        "22:00", "08:00").setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
//                        // save
                        saveStatusConfig(checkState);

                        Toast.makeText(ActSysSetting.this, "免打扰设置成功 ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
//                        resetFail(ischecked);
                        Toast.makeText(ActSysSetting.this, "免打扰设置失败 " + code, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
            }
        });
    }


    private void setMessageNotify(final boolean checkState) {
        // 如果接入第三方推送（小米），则同样应该设置开、关推送提醒
        // 如果关闭消息提醒，则第三方推送消息提醒也应该关闭。
        // 如果打开消息提醒，则同时打开第三方推送消息提醒。

        NIMClient.getService(MixPushService.class).enable(checkState).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                Toast.makeText(ActSysSetting.this, R.string.user_info_update_success, Toast.LENGTH_SHORT).show();
                messageReminding.setCheck(checkState);
                setToggleNotification(checkState);
            }

            @Override
            public void onFailed(int code) {
                messageReminding.setCheck(checkState);
                // 这种情况是客户端不支持第三方推送
                if (code == ResponseCode.RES_UNSUPPORT) {
                    messageReminding.setCheck(checkState);
                    setToggleNotification(checkState);
                } else if (code == ResponseCode.RES_EFREQUENTLY) {
                    Toast.makeText(ActSysSetting.this, R.string.operation_too_frequent, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActSysSetting.this, R.string.user_info_update_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }

    private void setToggleNotification(boolean checkState) {
        try {
            setNotificationToggle(checkState);
            NIMClient.toggleNotification(checkState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setNotificationToggle(boolean on) {
        UserPreferences.setNotificationToggle(on);
    }

    private void saveStatusConfig(boolean checkState) {
        UserPreferences.setDownTimeToggle(checkState);
        StatusBarNotificationConfig config = UserPreferences.getStatusConfig();
        config.downTimeToggle = checkState;
        config.downTimeBegin = "22:00";
        config.downTimeEnd = "08:00";
        UserPreferences.setStatusConfig(config);
        NIMClient.updateStatusBarNotificationConfig(config);
    }
}
