package dabkick.com.basicsampleapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dabkick.com.basicsampleapp.R;
import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dabkick.engine.Public.UserInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ParticipantListAdapter extends RecyclerView.Adapter<ParticipantListAdapter.ParticipantProfileHolder> {

    private List<UserInfo> participantList;
    private Context mContext;

    public ParticipantListAdapter(Context context, List<UserInfo> participantList) {
        mContext = context;
        this.participantList = participantList;
    }

    @NonNull
    @Override
    public ParticipantProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participant_list_item, parent, false);

        return new ParticipantProfileHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantProfileHolder holder, int position) {
        UserInfo userInfo = participantList.get(position);
        String name = userInfo.getName();
        if (name != null)
            holder.userName.setText(name);

        String profileImgUrl = participantList.get(position).getProfilePicUrl();
//        if (profileImgUrl != null && !profileImgUrl.trim().isEmpty())
//            Picasso.get().load(profileImgUrl).placeholder(R.drawable.avatar_img).error(R.drawable.avatar_img).into(holder.profileImg);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.avatar_img);

        Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions)
                .load(profileImgUrl)
                .into(holder.profileImg);
    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }

    public class ParticipantProfileHolder extends RecyclerView.ViewHolder {
        AppCompatTextView userName;
        CircleImageView profileImg;

        public ParticipantProfileHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.profile_name);
            profileImg = itemView.findViewById(R.id.profile_pic_img);
        }
    }
}
