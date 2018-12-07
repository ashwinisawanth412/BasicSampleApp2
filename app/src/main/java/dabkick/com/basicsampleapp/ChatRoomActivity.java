package dabkick.com.basicsampleapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dabkick.engine.Public.Authentication;
import com.dabkick.engine.Public.CallbackListener;
import com.dabkick.engine.Public.DKLiveChat;
import com.dabkick.engine.Public.LiveChatCallbackListener;
import com.dabkick.engine.Public.MessageInfo;
import com.dabkick.engine.Public.UserInfo;
import com.dabkick.engine.Public.UserPresenceCallBackListener;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChatRoomActivity extends AppCompatActivity {

    public static final String roomName = "myRoom";

    private Unbinder unbinder;

    @BindView(R.id.edittext)
    EditText editText;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.button)
    AppCompatImageView button;
    @BindView(R.id.back_arrow)
    AppCompatImageView backBtnImg;
    @BindView(R.id.user_count)
    TextView viewById;
    @BindView(R.id.app_bar_layout)
    android.support.v7.widget.Toolbar mToolBar;

    DKLiveChat dkLiveChat;
    Adapter adapter;

    private LiveChatCallbackListener liveChatCallbackListener;
    private UserPresenceCallBackListener userPresenceCallBackListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        unbinder = ButterKnife.bind(this);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Authentication auth = new Authentication("DK09aff676f38011e88a1a06f", "3d8a7db548d5d91447d64d09a37f12");
        dkLiveChat = new DKLiveChat(this, auth, new CallbackListener() {
            @Override
            public void onSuccess(String s, Object... objects) {
                //update name after engine is successfully intialised
                updateName();
            }

            @Override
            public void onError(String s, Object... objects) {

            }
        });

        liveChatCallbackListener = new LiveChatCallbackListener() {
            @Override
            public void receivedChatMessage(String roomName, MessageInfo message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addMessage(message);
                    }
                });
            }
        };
        userPresenceCallBackListener = new UserPresenceCallBackListener() {
            @Override
            public void userEntered(String roomName, UserInfo participant) {
                //process user entry
            }


            @Override
            public void userExited(String roomName, UserInfo participant) {
                //process user exit
            }


            @Override
            public void userDataUpdated(String roomName, UserInfo participant) {
                //process user info change
            }


            @Override
            public void getNumberOfUsersLiveNow(String roomName, int userCount) {
                viewById.setText(String.valueOf(userCount));
            }
        };

        UserInfo info = new UserInfo();
        info.setName("testuser");

        dkLiveChat.joinSession(roomName, info, new CallbackListener() {
            @Override
            public void onSuccess(String s, Object... objects) {

            }

            @Override
            public void onError(String s, Object... objects) {

            }
        });

        dkLiveChat.subscribe(roomName, liveChatCallbackListener, userPresenceCallBackListener, new CallbackListener() {
            @Override
            public void onSuccess(String s, Object... objects) {

            }

            @Override
            public void onError(String s, Object... objects) {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          sendMessage(roomName, editText.getText().toString());
                                      }
                                  }
        );

    }

    @OnClick(R.id.back_arrow)
    public void backBtnClicked() {
        onBackPressed();
    }

    public void sendMessage(String roomName, final String message) {
        MessageInfo messageInfo = new MessageInfo();
        if (!TextUtils.isEmpty(message)) {
            messageInfo.setChatMessage(message);
            messageInfo.setUserId(dkLiveChat.getUserId());
            dkLiveChat.chatEventListener.sendMessage(roomName, messageInfo, new CallbackListener() {
                @Override
                public void onSuccess(String msg, Object... obj) {
                    editText.setText("");
                }

                @Override
                public void onError(String msg, Object... obj) {

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please enter message", Toast.LENGTH_LONG).show();
        }

    }

    public void updateName() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        String name = preferences.getString("userName", "");

        UserInfo userInfo = new UserInfo();
        userInfo.setName(name);
        userInfo.setProfilePicUrl("");
        userInfo.setAppSpecificUserID("A12345" + UUID.randomUUID().toString());
        dkLiveChat.addUserListener.updateUser("", userInfo, new CallbackListener() {
            @Override
            public void onSuccess(String msg, Object... obj) {
            }

            @Override
            public void onError(String msg, Object... obj) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dkLiveChat.endLiveChat();
        unbinder.unbind();
    }
}
