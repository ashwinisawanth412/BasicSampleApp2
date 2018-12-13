package dabkick.com.basicsampleapp.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dabkick.com.basicsampleapp.ChatRoomFragment;
import dabkick.com.basicsampleapp.R;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomHolder> {

    private List<String> roomInfoList = new ArrayList<>();
    private Activity context;

    public RoomListAdapter(List<String> roomList, Activity activity) {
        this.roomInfoList = roomList;
        this.context = activity;
    }

    @NonNull
    @Override
    public RoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_list_item, parent, false);

        return new RoomHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomHolder holder, int position) {
        String roomName = roomInfoList.get(position);
        if (roomName != null)
            holder.roomName.setText(roomName);

    }

    @Override
    public int getItemCount() {
        return roomInfoList.size();
    }

    public class RoomHolder extends RecyclerView.ViewHolder {
        public TextView roomName;

        public RoomHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.room_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatRoomFragment chatRoom = ChatRoomFragment.newInstance(roomName.getText().toString());
                    android.support.v4.app.FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frag_container, chatRoom);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
    }

    public void addRoom(String roomInfo) {
        roomInfoList.add(roomInfo);
        notifyItemInserted(roomInfoList.size() - 1);
    }

}
