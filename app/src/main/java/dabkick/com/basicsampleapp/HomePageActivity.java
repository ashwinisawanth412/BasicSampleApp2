package dabkick.com.basicsampleapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dabkick.engine.Public.Authentication;
import com.dabkick.engine.Public.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dabkick.com.basicsampleapp.Adapters.ChatRoomFragment;
import dabkick.com.basicsampleapp.Adapters.RoomListAdapter;

public class HomePageActivity extends AppCompatActivity {

    private Unbinder mUnbinder;

    @BindView(R.id.disconnect_text_view) AppCompatTextView mDisconnect;
    @BindView(R.id.room_list_view) RecyclerView mRoomListView;
    RoomListAdapter mRoomListAdapter;

    @BindView(R.id.tool_bar_layout) android.support.v7.widget.Toolbar mToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mUnbinder = ButterKnife.bind(this);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        updateName();
        initChatRooms();

    }

    public void initChatRooms(){
        List<String> mRoomList = new ArrayList<String>();
        mRoomList = SplashScreenActivity.dkLiveChat.chatEventListener.getRoomList();

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
    }

    public void dummyMethod(){
        ChatRoomFragment chatRoom = ChatRoomFragment.newInstance("myRoom");
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_container, chatRoom);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @OnClick(R.id.disconnect_text_view)
    public void onClickDisconnect(){
        //clear user details
        Intent intent = new Intent(HomePageActivity.this, SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //clear shared pref
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();

        //disconnect from firebase
        SplashScreenActivity.dkLiveChat.endLiveChat();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        //disconnect from firebase but retain user details
        SplashScreenActivity.dkLiveChat.endLiveChat();
    }
}
