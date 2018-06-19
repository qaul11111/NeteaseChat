package com.hiwi.andorid.neteasechat.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hiwi.andorid.neteasechat.R;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.session.actions.PickImageAction;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.team.activity.AdvancedTeamInfoActivity;
import com.netease.nim.uikit.common.media.picker.PickImageHelper;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.InvocationFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.nos.NosService;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamBeInviteModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamInviteModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamMessageNotifyTypeEnum;
import com.netease.nimlib.sdk.team.constant.TeamUpdateModeEnum;
import com.netease.nimlib.sdk.team.constant.VerifyTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;

import java.io.File;

public class GroupSetting extends Activity implements View.OnClickListener {

    private static final int REQUEST_PICK_ICON = 104;
    private String teamId = "539393199";
    private TextView peoplrCoun;
    private TextView name;
    private TextView announcement;
    private TextView introduce;
    private TextView message;
    private ImageView avarat;
    private Button allowAnyoneToJoin;
    private Button needAuthentication;
    private Button noOneIsAllowedToJoin;
    private Button administrator_invitation;
    private Button administratorInvitation;
    private Button everyoneCanInviteThem;
    private Button administratorModification;
    private Button everyoneCanModifyIt;
    private Button alertallmessages;
    private Button onlyAlertsAdministratorsOfMessages;
    private Button noMessage;
    private AbortableFuture<String> uploadFuture;
    private static final int ICON_TIME_OUT = 30000;
    private static final String TAG = "GroupStting";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_group_setting);

        initView();
        initData();
        initListener();
    }



    private void initView() {
        peoplrCoun = findViewById(R.id.group_setting_people_count);
        name = findViewById(R.id.group_setting_name);
        //公告
        announcement = findViewById(R.id.group_setting_announcement);
        //介绍
        introduce = findViewById(R.id.group_setting_introduce);
        avarat = findViewById(R.id.group_setting_avarat);
        allowAnyoneToJoin = findViewById(R.id.allow_anyone_to_join);
        needAuthentication = findViewById(R.id.need_authentication);
        noOneIsAllowedToJoin = findViewById(R.id.no_one_is_allowed_to_join);
        administratorInvitation = findViewById(R.id.administrator_invitation);
        everyoneCanInviteThem = findViewById(R.id.everyone_can_invite_them);
        administratorModification = findViewById(R.id.administrator_modification);
        everyoneCanModifyIt = findViewById(R.id.everyone_can_modify_it);
        alertallmessages = findViewById(R.id.alert_all_messages);
        onlyAlertsAdministratorsOfMessages = findViewById(R.id.only_alerts_administrators_of_messages);
        noMessage = findViewById(R.id.no_message);
    }

    private void initData() {
        NIMClient.getService(TeamService.class).queryTeam(teamId).setCallback(new RequestCallbackWrapper<Team>() {
            @Override
            public void onResult(int code, Team t, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS) {
                    // 成功
                    String icon = t.getIcon();
                    name.setText(t.getName());
                    peoplrCoun.setText(String.valueOf(t.getMemberCount())+"人");
                    announcement.setText(t.getAnnouncement());
                    introduce.setText(t.getIntroduce());
                    Glide.with(GroupSetting.this)
                            .load(t.getIcon())
                            .into(avarat);
                } else {
                    // 失败，错误码见code
                }

                if (exception != null) {
                    // error
                }
            }
        });
    }

    private void initListener() {
        allowAnyoneToJoin.setOnClickListener(this);
        name.setOnClickListener(this);
        announcement.setOnClickListener(this);
        introduce.setOnClickListener(this);
        allowAnyoneToJoin.setOnClickListener(this);
        needAuthentication.setOnClickListener(this);
        noOneIsAllowedToJoin.setOnClickListener(this);
        administratorInvitation.setOnClickListener(this);
        everyoneCanInviteThem.setOnClickListener(this);
        administratorModification.setOnClickListener(this);
        everyoneCanModifyIt.setOnClickListener(this);
        alertallmessages.setOnClickListener(this);
        onlyAlertsAdministratorsOfMessages.setOnClickListener(this);
        noMessage.setOnClickListener(this);
        avarat.setOnClickListener(this);
        peoplrCoun.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        TeamMessageNotifyTypeEnum type;

        switch (v.getId()){
            case R.id.group_setting_name :
                String name = "这是更新的群名称";
                NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.Name, name).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        // 成功
                        Toast.makeText(GroupSetting.this,"更新群名称成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        // 失败
                        Toast.makeText(GroupSetting.this,"更新群名称失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                        Toast.makeText(GroupSetting.this,"更新群名称错误",Toast.LENGTH_SHORT).show();
                    }
                });
            case R.id.group_setting_announcement :
                String announcement = "这是更新的群公告";
                NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.Announcement, announcement).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        // 成功
                        Toast.makeText(GroupSetting.this,"更新群公告成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        // 失败
                        Toast.makeText(GroupSetting.this,"更新群公告失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                        Toast.makeText(GroupSetting.this,"更新群公告错误",Toast.LENGTH_SHORT).show();
                    }
                });
            case R.id.group_setting_introduce :
                String introduce = "这是更新的群介绍";
                NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.Introduce, introduce).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        // 成功
                        Toast.makeText(GroupSetting.this,"更新群介绍告成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        // 失败
                        Toast.makeText(GroupSetting.this,"更新群介绍失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                        Toast.makeText(GroupSetting.this,"更新群介绍错误",Toast.LENGTH_SHORT).show();
                    }
                });
            case R.id.allow_anyone_to_join :
                //允许任何人加入
                NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.VerifyType, VerifyTypeEnum.Free).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        // 成功
                        Toast.makeText(GroupSetting.this,"更新群介绍成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        // 失败
                        Toast.makeText(GroupSetting.this,"更新群介绍失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                        Toast.makeText(GroupSetting.this,"更新群介绍错误",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.need_authentication :
                //需要身份验证
                NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.VerifyType, VerifyTypeEnum.Apply).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        // 成功
                        Toast.makeText(GroupSetting.this,"需要身份验证成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        // 失败
                        Toast.makeText(GroupSetting.this,"更需要身份验证失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                        Toast.makeText(GroupSetting.this,"需要身份验证错误",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.no_one_is_allowed_to_join :
                //不允许任何人加入
                NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.VerifyType, VerifyTypeEnum.Private).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        // 成功
                        Toast.makeText(GroupSetting.this,"不允许任何人加入成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        // 失败
                        Toast.makeText(GroupSetting.this,"不允许任何人加入失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                        Toast.makeText(GroupSetting.this,"不允许任何人加入错误",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.administrator_invitation :
                //管理员邀请
                NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.InviteMode, TeamInviteModeEnum.Manager).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        // 成功
                        Toast.makeText(GroupSetting.this,"管理员邀请成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        // 失败
                        Toast.makeText(GroupSetting.this,"管理员邀请失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                        Toast.makeText(GroupSetting.this,"管理员邀请错误",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.everyone_can_invite_them :
                //所有人可以邀请
                NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.InviteMode, TeamInviteModeEnum.All).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        // 成功
                        Toast.makeText(GroupSetting.this,"所有人可以邀请成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        // 失败
                        Toast.makeText(GroupSetting.this,"所有人可以邀请失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                        Toast.makeText(GroupSetting.this,"所有人可以邀请错误",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.administrator_modification :
                //管理员修改
                NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.TeamUpdateMode, TeamUpdateModeEnum.Manager).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        // 成功
                        Toast.makeText(GroupSetting.this,"管理员修改成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        // 失败
                        Toast.makeText(GroupSetting.this,"管理员修改失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                        Toast.makeText(GroupSetting.this,"管理员修改错误",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.everyone_can_modify_it :
                //所有人可修改
                NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.TeamUpdateMode, TeamUpdateModeEnum.All).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        // 成功
                        Toast.makeText(GroupSetting.this,"所有人可修改成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        // 失败
                        Toast.makeText(GroupSetting.this,"所有人可修改失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                        Toast.makeText(GroupSetting.this,"所有人可修改错误",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.alert_all_messages :
                //提醒所有消息
                type = TeamMessageNotifyTypeEnum.All;
                NIMClient.getService(TeamService.class).muteTeam(teamId, type).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        // 设置成功
                        Toast.makeText(GroupSetting.this,"提醒所有消息成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        // 设置失败
                        Toast.makeText(GroupSetting.this,"提醒所有消息失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                        Toast.makeText(GroupSetting.this,"提醒所有消息错误",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.only_alerts_administrators_of_messages :
                //只提醒管理员消息
                type = TeamMessageNotifyTypeEnum.Manager;
                NIMClient.getService(TeamService.class).muteTeam(teamId, type).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        // 设置成功
                        Toast.makeText(GroupSetting.this,"只提醒管理员消息成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        // 设置失败
                        Toast.makeText(GroupSetting.this,"只提醒管理员消息失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                        Toast.makeText(GroupSetting.this,"只提醒管理员消息错误",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.no_message :
                //不提醒任何消息
                type = TeamMessageNotifyTypeEnum.Mute;
                NIMClient.getService(TeamService.class).muteTeam(teamId, type).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        // 设置成功
                        Toast.makeText(GroupSetting.this,"不提醒任何消息成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        // 设置失败
                        Toast.makeText(GroupSetting.this,"不提醒任何消息失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // 错误
                        Toast.makeText(GroupSetting.this,"不提醒任何消息错误",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.group_setting_avarat :
                showSelector(com.netease.nim.uikit.R.string.set_head_image, REQUEST_PICK_ICON);
                break;
            case R.id.group_setting_people_count :
                Intent intent = new Intent(GroupSetting.this,ContactSelectActivity.class);
                startActivity(intent);
        }
    }

    /**
     * 打开图片选择器
     */
    private void showSelector(int titleId, final int requestCode) {
        PickImageHelper.PickImageOption option = new PickImageHelper.PickImageOption();
        option.titleResId = titleId;
        option.multiSelect = false;
        option.crop = true;
        option.cropOutputImageWidth = 720;
        option.cropOutputImageHeight = 720;

        PickImageHelper.pickImage(GroupSetting.this, requestCode, option);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_PICK_ICON:
                String path = data.getStringExtra(Extras.EXTRA_FILE_PATH);
                updateTeamIcon(path);
                break;
        }
    }

    /**
     * 更新头像
     */
    private void updateTeamIcon(final String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        File file = new File(path);
        if (file == null) {
            return;
        }
        DialogMaker.showProgressDialog(this, null, null, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelUpload(com.netease.nim.uikit.R.string.team_update_cancel);
            }
        }).setCanceledOnTouchOutside(true);

        LogUtil.i(TAG, "start upload icon, local file path=" + file.getAbsolutePath());
        new Handler().postDelayed(outimeTask, ICON_TIME_OUT);
        uploadFuture = NIMClient.getService(NosService.class).upload(file, PickImageAction.MIME_JPEG);
        uploadFuture.setCallback(new RequestCallbackWrapper<String>() {
            @Override
            public void onResult(int code, String url, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS && !TextUtils.isEmpty(url)) {
                    LogUtil.i(TAG, "upload icon success, url =" + url);
                    final String imageUrl = url;
                    NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.ICON, url).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                            DialogMaker.dismissProgressDialog();

                            Glide.with(GroupSetting.this)
                                    .load(imageUrl)
                                    .into(avarat);


                            Toast.makeText(GroupSetting.this, com.netease.nim.uikit.R.string.update_success, Toast.LENGTH_SHORT).show();
                            onUpdateDone();
                        }

                        @Override
                        public void onFailed(int code) {
                            DialogMaker.dismissProgressDialog();
                            Toast.makeText(GroupSetting.this, String.format(getString(com.netease.nim.uikit.R.string.update_failed), code), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onException(Throwable exception) {
                            DialogMaker.dismissProgressDialog();
                        }
                    }); // 更新资料
                } else {
                    Toast.makeText(GroupSetting.this, com.netease.nim.uikit.R.string.team_update_failed, Toast
                            .LENGTH_SHORT).show();
                    onUpdateDone();
                }
            }
        });
    }

    private void cancelUpload(int resId) {
        if (uploadFuture != null) {
            uploadFuture.abort();
            Toast.makeText(GroupSetting.this, resId, Toast.LENGTH_SHORT).show();
            onUpdateDone();
        }
    }

    private Runnable outimeTask = new Runnable() {
        @Override
        public void run() {
            cancelUpload(com.netease.nim.uikit.R.string.team_update_failed);
        }
    };

    private void onUpdateDone() {
        uploadFuture = null;
        DialogMaker.dismissProgressDialog();
    }
}
