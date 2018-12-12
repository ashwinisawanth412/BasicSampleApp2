package dabkick.com.basicsampleapp.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.dabkick.engine.Public.CallbackListener;
import com.dabkick.engine.Public.DKLiveChat;
import com.dabkick.engine.Public.LiveChatCallbackListener;
import com.dabkick.engine.Public.MessageInfo;
import com.dabkick.engine.Public.UserInfo;
import com.dabkick.engine.Public.UserPresenceCallBackListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dabkick.com.basicsampleapp.HomePageActivity;
import dabkick.com.basicsampleapp.R;

public class ChatRoomFragment extends Fragment {

    private String mRoomName;

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

    Adapter adapter;
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
        if (getArguments() != null) {
            mRoomName = getArguments().getString("roomName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_chat_room, container, false);
        unbinder = ButterKnife.bind(this, view);

        Log.d("chatRoom", "roomTitle: " + mRoomName);
        mRoomTitle.setText(mRoomName);

        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        liveChatCallbackListener = new LiveChatCallbackListener() {
            @Override
            public void receivedChatMessage(String roomName, MessageInfo message) {
                getActivity().runOnUiThread(new Runnable() {
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
        };

        UserInfo info = new UserInfo();
        info.setName("testuser");


        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          sendMessage(mRoomName, editText.getText().toString());
                                      }
                                  }
        );

        return view;
    }

    @OnClick(R.id.back_arrow)
    public void backBtnClicked() {
        getActivity().onBackPressed();
    }

    public void sendMessage(String roomName, final String message) {
        if (getActivity().getClass() == HomePageActivity.class) {
            DKLiveChat dkLiveChat = ((HomePageActivity) getActivity()).dkLiveChat;
            MessageInfo messageInfo = new MessageInfo();
            if (!TextUtils.isEmpty(message)) {
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
            } else {
                Toast.makeText(getActivity(), "Please enter message", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity().getClass() == HomePageActivity.class &&  ((HomePageActivity) getActivity()).dkLiveChat != null)
            ((HomePageActivity) getActivity()).dkLiveChat.endLiveChat();
        unbinder.unbind();
    }
}
