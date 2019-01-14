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
import dabkick.com.basicsampleapp.Utils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class ChatMsgAdapter extends RecyclerView.Adapter<ChatMsgAdapter.MessageHolder> {

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
        Timber.d("Value of user id is " + messageInfoList.get(i).getUserId());
        if (name != null && !name.trim().isEmpty())
            messageHolder.name.setText(messageInfoList.get(i).getUserName());
        else
            messageHolder.name.setText("anonymous"); //if user name is not set, make it gone

        messageHolder.msg.setText(messageInfoList.get(i).getChatMessage());

        //for profile pic
//        String profileImgUrl = messageInfoList.get(i).getImg();
//        Picasso.get().load("").placeholder(R.drawable.avatar_img).error(R.drawable.avatar_img).into(messageHolder.profileImg);

        //for time stamp
        try {
            messageHolder.timeStamp.setText(Utils.millisToTime(messageInfoList.get(i).getMessageTime()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.messageInfoList.size();
    }

    public MessageInfo getItem(int pos) {
        if (messageInfoList.size() > pos)
            return messageInfoList.get(pos);

        return null;
    }

    public class MessageHolder extends RecyclerView.ViewHolder {
        TextView msg, name, timeStamp;
        CircleImageView profileImg;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.message_text_view);
            name = itemView.findViewById(R.id.user_name_text_view);
            profileImg = itemView.findViewById(R.id.profile_pic_img);
            timeStamp = itemView.findViewById(R.id.time_stamp_text_view);
        }
    }

    public void addMessage(MessageInfo messageInfo) {
        messageInfoList.add(messageInfo);
        notifyItemInserted(messageInfoList.size() - 1);
    }

    public void addAllMessages(List<MessageInfo> messageList) {
        messageInfoList.addAll(messageList);
        notifyDataSetChanged();
    }

    public void clearMsgs() {
        this.messageInfoList.clear();
    }

}
