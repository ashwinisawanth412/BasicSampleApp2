package dabkick.com.basicsampleapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dabkick.engine.Public.Authentication;
import com.dabkick.engine.Public.CallbackListener;
import com.dabkick.engine.Public.DKLiveChat;
import com.dabkick.engine.Public.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dabkick.com.basicsampleapp.Adapters.Adapter;
import dabkick.com.basicsampleapp.Adapters.RoomListAdapter;

public class HomePageActivity extends AppCompatActivity {

    private Unbinder mUnbinder;

    @BindView(R.id.disconnect_text_view) AppCompatTextView mDisconnect;
    @BindView(R.id.room_list_view) RecyclerView mRoomListView;
    RoomListAdapter mRoomListAdapter;

    @BindView(R.id.tool_bar_layout) android.support.v7.widget.Toolbar mToolBar;

    DKLiveChat dkLiveChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mUnbinder = ButterKnife.bind(this);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //show progress bar
        initEngine();
        initChatRooms();

    }

    public void initEngine(){
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
    }

    public void initChatRooms(){
        List<String> mRoomList = new ArrayList<String>();
        mRoomList = new ArrayList<String>();
        for(int i = 0; i < 5; i++){
            mRoomList.add("Room" + i);
        }

        mRoomListAdapter = new RoomListAdapter(mRoomList, HomePageActivity.this);
        mRoomListView.setAdapter(mRoomListAdapter);
        mRoomListView.setLayoutManager(new LinearLayoutManager(this));
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
                initChatRooms();
            }

            @Override
            public void onError(String msg, Object... obj) {
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
