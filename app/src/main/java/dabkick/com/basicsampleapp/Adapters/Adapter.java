package dabkick.com.basicsampleapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dabkick.engine.Public.MessageInfo;

import java.util.ArrayList;
import java.util.List;

import dabkick.com.basicsampleapp.R;

public class Adapter extends RecyclerView.Adapter<Adapter.MessageHolder> {

    private List<MessageInfo> messageInfoList = new ArrayList<>();

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item_msg, viewGroup, false);

        return new MessageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder messageHolder, int i) {
        String name = messageInfoList.get(i).getUserName();
        //dont diplay user name if not set
        if (name != null && !name.equalsIgnoreCase("anonymous"))
            messageHolder.name.setText(messageInfoList.get(i).getUserName());
        else
            messageHolder.name.setVisibility(View.GONE); //if user name is not set, make it gone

        messageHolder.msg.setText(messageInfoList.get(i).getChatMessage());
    }

    @Override
    public int getItemCount() {
        return messageInfoList.size();
    }

    public class MessageHolder extends RecyclerView.ViewHolder {
        public TextView msg, name;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.message_text_view);
            name = itemView.findViewById(R.id.user_name_text_view);
        }
    }

    public void addMessage(MessageInfo messageInfo) {
        messageInfoList.add(messageInfo);
        notifyItemInserted(messageInfoList.size() - 1);
    }

    public void addAllMessages(List<MessageInfo> messageList){
        messageInfoList.addAll(messageList);
        notifyDataSetChanged();
    }
}
