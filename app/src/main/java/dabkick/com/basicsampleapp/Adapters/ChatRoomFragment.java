package dabkick.com.basicsampleapp.Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dabkick.com.basicsampleapp.R;

public class ChatRoomFragment extends Fragment {

    private String mRoomName;

    public ChatRoomFragment() {
    }

    public static ChatRoomFragment newInstance(String roomName) {
        ChatRoomFragment fragment = new ChatRoomFragment();
        Bundle args = new Bundle();
        args.putString(roomName, roomName);
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
        View view = inflater.inflate(R.layout.fragment_chat_room, container, false);

        TextView tv = view.findViewById(R.id.room_name);
        tv.setText(mRoomName);

        return view;
    }
}
