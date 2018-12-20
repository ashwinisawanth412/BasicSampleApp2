package dabkick.com.basicsampleapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dabkick.engine.Public.CallbackListener;
import com.dabkick.engine.Public.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dabkick.com.basicsampleapp.Adapters.RoomListAdapter;
import dabkick.com.basicsampleapp.Model.Room;

public class HomePageActivity extends BaseActivity {

    private Unbinder mUnbinder;

    @BindView(R.id.disconnect_text_view)
    AppCompatTextView mDisconnect;
    @BindView(R.id.room_list_view)
    RecyclerView mRoomListView;
    RoomListAdapter mRoomListAdapter;

    @BindView(R.id.tool_bar_layout)
    RelativeLayout mToolBar;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.create_room)
    FloatingActionButton mCreateRoomBtn;

    List<Room> mRoomList = new ArrayList<Room>();

    public static boolean isNewRoomCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mUnbinder = ButterKnife.bind(this);

        mProgressBar.setVisibility(View.VISIBLE);
        updateName();
        initChatRooms();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initChatRooms() {
        if (SplashScreenActivity.dkLiveChat.chatRoomListener != null) {
            SplashScreenActivity.dkLiveChat.chatRoomListener.getRoomList(new CallbackListener() {
                @Override
                public void onSuccess(String msg, Object... obj) {

                    mRoomList.clear();
                    if (mProgressBar != null)
                        mProgressBar.setVisibility(View.GONE);
                    List<String> list = new ArrayList<>();
                    list = (List<String>) obj[0];

                    for (String roomName : list) {
                        Room room = new Room();
                        room.setRoomName(roomName);
                        mRoomList.add(room);
                    }

                    if (mRoomListView != null) {
                        mRoomListAdapter = new RoomListAdapter(mRoomList, HomePageActivity.this);
                        mRoomListView.setAdapter(mRoomListAdapter);
                        mRoomListView.setLayoutManager(new LinearLayoutManager(HomePageActivity.this));
                    }

                }

                @Override
                public void onError(String msg, Object... obj) {
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        } else {
            if (mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                initChatRooms();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateName() {
        String name = PreferenceHandler.getUserName(HomePageActivity.this);
        UserInfo userInfo = new UserInfo();
        userInfo.setName(name);
        userInfo.setProfilePicUrl("");
        userInfo.setAppSpecificUserID("A12345" + UUID.randomUUID().toString());

        SplashScreenActivity.dkLiveChat.updateName(userInfo, new CallbackListener() {
            @Override
            public void onSuccess(String s, Object... objects) {

            }

            @Override
            public void onError(String s, Object... objects) {

            }
        });
    }

    @OnClick(R.id.disconnect_text_view)
    public void onClickDisconnect() {
        //clear user details
        Intent intent = new Intent(HomePageActivity.this, SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //disconnect from firebase
        SplashScreenActivity.dkLiveChat.endLiveChat();
    }

    @OnClick(R.id.create_room)
    public void createNewRoom() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.create_new_room_layout, null);
        builder.setView(view);

        AppCompatEditText roomNameEditText = view.findViewById(R.id.create_room_edit_text);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String roomName = roomNameEditText.getText().toString().replaceAll("^\\s+|\\s+$", "");
                if (TextUtils.isEmpty(roomName)) {
                    Toast.makeText(HomePageActivity.this, "Enter Room Name", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.cancel();
                    //should add the dk room creation apis here
                    isNewRoomCreated = true;
                    Room mRoom = new Room();
                    mRoom.setRoomName(roomName);
                    for (int i = 0; i < mRoomList.size(); i++) {
                        if (mRoomList.get(i).getRoomName().equalsIgnoreCase(roomName)) {
                            Snackbar.make(getCurrentFocus(), "Room Name must be unique", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                    }

                    mRoomList.add(mRoom);
                    if (mRoomListAdapter != null) {
                        mRoomListAdapter.notifyDataSetChanged();
                        mRoomListAdapter.enterRoomOnCreation(roomName);
                    } else {
                        mRoomListAdapter = new RoomListAdapter(mRoomList, HomePageActivity.this);
                        mRoomListAdapter.notifyDataSetChanged();
                        mRoomListAdapter.enterRoomOnCreation(roomName);
                    }
                }
                roomNameEditText.setText("");
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                roomNameEditText.setText("");
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    @OnClick(R.id.settings_icon)
    public void onClickSetttingsIcon() {
        ProfileSettingsFragment profileSettingsFragment = ProfileSettingsFragment.newInstance();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_container, profileSettingsFragment, "profile_settings_frag");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @OnClick(R.id.refresh_icon)
    public void onClickRefresh() {
        initChatRooms();
    }

    public void updateFloatingBtn(boolean show) {
        if (show)
            mCreateRoomBtn.show();
        else
            mCreateRoomBtn.hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //disconnect from firebase but retain user details
        SplashScreenActivity.dkLiveChat.endLiveChat();

        mUnbinder.unbind();
    }
}
