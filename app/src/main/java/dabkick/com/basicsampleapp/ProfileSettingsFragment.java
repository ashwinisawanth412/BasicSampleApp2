package dabkick.com.basicsampleapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileSettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_profile_settings, container, false);

        if(getActivity().getClass() == HomePageActivity.class) {
            ((HomePageActivity)getActivity()).updateFloatingBtn(false);
        }




        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(getActivity().getClass() == HomePageActivity.class) {
            ((HomePageActivity)getActivity()).updateFloatingBtn(true);
        }
    }
}
