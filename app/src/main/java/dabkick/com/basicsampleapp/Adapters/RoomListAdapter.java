package dabkick.com.basicsampleapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import dabkick.com.basicsampleapp.R;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomHolder> {

    private List<String> roomInfoList = new ArrayList<>();

    public RoomListAdapter(ArrayList<String> roomList) {
        this.roomInfoList = roomList;
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
        }
    }

    public void addMessage(String roomInfo) {
        roomInfoList.add(roomInfo);
        notifyItemInserted(roomInfoList.size() - 1);
    }
}
