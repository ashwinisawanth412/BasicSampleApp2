package dabkick.com.basicsampleapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import dabkick.com.basicsampleapp.Adapters.Adapter;
import dabkick.com.basicsampleapp.Model.Room;
import dabkick.com.basicsampleapp.Utils.Utils;

public class ChatRoomFragment extends Fragment {

    private static String mRoomName;

    private Unbinder unbinder;

    @BindView(R.id.edittext)
    EditText editText;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.button)
    AppCompatImageView button;
    @BindView(R.id.back_arrow)
    AppCompatImageView backBtnImg;
    @BindView(R.id.room_name_text_view)
    AppCompatTextView mRoomTitle;
    @BindView(R.id.over_flow_icon)
    AppCompatImageView mOverFlowIcon;
    @BindView(R.id.view_participants_frag_container)
    FrameLayout mViewParticipantsFragContainer;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    volatile Adapter adapter;
    private LiveChatCallbackListener liveChatCallbackListener;
    private UserPresenceCallBackListener userPresenceCallBackListener;

    public ChatRoomFragment() {
    }

    public static ChatRoomFragment newInstance(String roomName) {
        ChatRoomFragment fragment = new ChatRoomFragment();
        Bundle args = new Bundle();
        args.putString("roomName", roomName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_chat_room, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (getArguments() != null) {
            mRoomName = getArguments().getString("roomName");
        }

        Log.d("chatRoom", "roomTitle: " + mRoomName);
        mRoomTitle.setText(mRoomName);

        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //clear unread msg list
        if (BaseActivity.mCurrentActivity.getClass() == HomePageActivity.class) {
            Room room = ((HomePageActivity) getActivity()).mRoomListAdapter.getRoomItem(mRoomName);
            if (room != null) {
                room.clearUnreadMsgList();
                ((HomePageActivity) getActivity()).mRoomListAdapter.notifyDataSetChanged();
            }
        }

        try {
            if (getActivity().getClass() == HomePageActivity.class) {
                SplashScreenActivity.dkLiveChat.joinSession(mRoomName, createUserInfo(), new CallbackListener() {
                    @Override
                    public void onSuccess(String msg, Object... obj) {
                        //call subscribe here
                    }

                    @Override
                    public void onError(String msg, Object... obj) {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (SplashScreenActivity.dkLiveChat.isSubscribed(mRoomName)) {
            adapter.addAllMessages(SplashScreenActivity.dkLiveChat.getAllMessageList(mRoomName));
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        }

        liveChatCallbackListener = new LiveChatCallbackListener() {
            @Override
            public void receivedChatMessage(String roomName, MessageInfo message) {
                BaseActivity.mCurrentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("ChatRoomFrag", "receivedChatMsg: roomName: " + roomName + " msg: " + message.getChatMessage());
                        String name = PreferenceHandler.getUserName(BaseActivity.mCurrentActivity);
                        if (roomName.equalsIgnoreCase(mRoomName)) {

                            if(recyclerView ==  null)
                                recyclerView = view.findViewById(R.id.recycler);

                            adapter.addMessage(message);
                            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                        } else if (!message.getUserName().equalsIgnoreCase(name)) {
                            //i am not in the same room as the msg received and am not the sender of the msg. So add it as unread msg
                            Log.d("ChatRoomFrag", "else of receivedChatMsg: roomName: " + roomName);
                            Room room = ((HomePageActivity) BaseActivity.mCurrentActivity).mRoomListAdapter.getRoomItem(roomName);
                            Log.d("ChatRoomFrag", "else of receivedChatMsg: room obj: " + room);
                            if (room != null) {
                                room.addUnreadMsg(message);
                                ((HomePageActivity) BaseActivity.mCurrentActivity).mRoomListAdapter.updateRoomUponNewMsg(room);
                            }
                        }
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
        };

        if (!SplashScreenActivity.dkLiveChat.isSubscribed(mRoomName)) {
            mProgressBar.setVisibility(View.VISIBLE);
            SplashScreenActivity.dkLiveChat.subscribe(mRoomName, liveChatCallbackListener, userPresenceCallBackListener, new CallbackListener() {
                @Override
                public void onSuccess(String msg, Object... obj) {
                    try {
                        mProgressBar.setVisibility(View.GONE);
                        BaseActivity.mCurrentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar.setVisibility(View.GONE);
                                adapter.addAllMessages(SplashScreenActivity.dkLiveChat.chatEventListener.getChatMessages(mRoomName));
                                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                            }
                        });
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onError(String msg, Object... obj) {
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        } else {
            adapter.addAllMessages(SplashScreenActivity.dkLiveChat.getAllMessageList(mRoomName));
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        }


        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                                              sendMessage(mRoomName, editText.getText().toString());
                                              recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                                          } else {
                                              Toast.makeText(BaseActivity.mCurrentActivity, "Please enter message", Toast.LENGTH_LONG).show();
                                          }
                                      }
                                  }
        );

        return view;
    }

    @OnClick(R.id.back_arrow)
    public void backBtnClicked()
    {
        Utils.hideKeyboard(getActivity());
        getActivity().onBackPressed();
    }

    public void sendMessage(String roomName, final String message) {
        if (BaseActivity.mCurrentActivity.getClass() == HomePageActivity.class) {
            DKLiveChat dkLiveChat = SplashScreenActivity.dkLiveChat;
            if (dkLiveChat == null)
                return;
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setChatMessage(message);

            messageInfo.setUserId(dkLiveChat.getUserId());
            dkLiveChat.chatEventListener.sendMessage(roomName, messageInfo, new CallbackListener() {
                @Override
                public void onSuccess(String msg, Object... obj) {
                    Log.d("ChatRoomActivity", "onSuccess chat msg");
                    editText.setText("");
                }

                @Override
                public void onError(String msg, Object... obj) {
                    Log.d("ChatRoomActivity", "onError chat msg");
                }
            });

        }
    }

    @OnClick(R.id.over_flow_icon)
    public void onClickOverflowMenu() {
        PopupMenu popup = new PopupMenu(getActivity(), mOverFlowIcon);
        popup.getMenuInflater().inflate(R.menu.chat_tool_bar_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.view_participants:
                        ViewParticipantFragment participantFragment = ViewParticipantFragment.newInstance(mRoomName);
                        android.support.v4.app.FragmentTransaction transaction = ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.view_participants_frag_container, participantFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;

                    case R.id.unsubscribe:
                        SplashScreenActivity.dkLiveChat
                                .unSubscribe(mRoomName, liveChatCallbackListener, userPresenceCallBackListener, new CallbackListener() {
                                    @Override
                                    public void onSuccess(String msg, Object... obj) {
                                        //move room to last pos
                                        backBtnClicked();
                                        if (((HomePageActivity) BaseActivity.mCurrentActivity).mRoomListAdapter != null) {
                                            Room room = ((HomePageActivity) BaseActivity.mCurrentActivity).mRoomListAdapter.getRoomItem(mRoomName);
                                            if (room != null)
                                                ((HomePageActivity) BaseActivity.mCurrentActivity).mRoomListAdapter.updateRoomUponUnsubscribe(room);
                                        }
                                    }

                                    @Override
                                    public void onError(String msg, Object... obj) {

                                    }
                                });


                        break;
                }

                return true;
            }
        });
        popup.show();//showing popup menu
    }

    private UserInfo createUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setAppSpecificUserID(UUID.randomUUID().toString());
        userInfo.setName(PreferenceHandler.getUserName(getActivity()));
        return userInfo;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRoomName = "";
        unbinder.unbind();
    }
}
