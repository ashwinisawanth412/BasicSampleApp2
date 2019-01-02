package dabkick.com.basicsampleapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dabkick.engine.Public.CallbackListener;
import com.dabkick.engine.Public.UserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dabkick.com.basicsampleapp.Adapters.ParticipantListAdapter;

public class ViewParticipantFragment extends Fragment {

    private Unbinder mUnbinder;

    @BindView(R.id.back_arrow_participant)
    AppCompatImageView mBackArrow;
    @BindView(R.id.room_name_text_view)
    AppCompatTextView mRoomNameTextView;
    @BindView(R.id.participant_list)
    RecyclerView mParticipantListView;
    ParticipantListAdapter participantListAdapter;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    String mRoomName = "";
    List<UserInfo> participantList = new ArrayList<UserInfo>();
    public ViewParticipantFragment() {
    }

    public static ViewParticipantFragment newInstance(String roomName) {
        ViewParticipantFragment fragment = new ViewParticipantFragment();
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
        View view = inflater.inflate(R.layout.fragment_view_participant, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        mRoomNameTextView.setText(mRoomName);

        participantList = SplashScreenActivity.dkLiveChat.getUsers(mRoomName, new CallbackListener() {
            @Override
            public void onSuccess(String s, Object... objects) {
                participantList = (List<UserInfo>) objects[0];
                setAdapter();
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onError(String s, Object... objects) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "error loading participants", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    public void setAdapter() {
        participantListAdapter = new ParticipantListAdapter(getContext(), participantList);
        mParticipantListView.setAdapter(participantListAdapter);
        mParticipantListView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        participantList.clear();
    }

    @OnClick(R.id.back_arrow_participant)
    public void backArrowClicked(){
        getActivity().onBackPressed();
    }
}
