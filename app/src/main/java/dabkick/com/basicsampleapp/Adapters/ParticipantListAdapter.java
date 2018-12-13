package dabkick.com.basicsampleapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dabkick.com.basicsampleapp.R;
import  com.dabkick.engine.Public.UserInfo;

import java.util.List;

public class ParticipantListAdapter extends RecyclerView.Adapter<ParticipantListAdapter.ParticipantProfileHolder> {

    private List<UserInfo> participantList;

    public ParticipantListAdapter(List<UserInfo> participantList) {
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
        UserInfo participant = participantList.get(position);
        String name = participant.getName();
        if (name != null)
            holder.userName.setText(name);

    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }

    public class ParticipantProfileHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView userName;

        public ParticipantProfileHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.profile_name);
        }
    }
}
