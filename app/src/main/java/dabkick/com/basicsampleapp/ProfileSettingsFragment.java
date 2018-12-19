package dabkick.com.basicsampleapp;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class ProfileSettingsFragment extends Fragment {

    private Unbinder mUnbinder;
    @BindView(R.id.profile_user_name)
    AppCompatTextView mUserName;
    @BindView(R.id.profile_img_view)
    AppCompatImageView mProfileImgView;

    public static final int PERMISSIONS_REQUEST_CAMERA = 0;
    public static final int CAMERA_REQUEST_CODE = 100;
    boolean isCameraPermissionGranted;
    File mProfileImgFile = null;

    public static ProfileSettingsFragment newInstance() {
        ProfileSettingsFragment fragment = new ProfileSettingsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_profile_settings, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        if (getActivity().getClass() == HomePageActivity.class) {
            ((HomePageActivity) getActivity()).updateFloatingBtn(false);
        }

        mUserName.setText("Username: " + PreferenceHandler.getUserName(BaseActivity.mCurrentActivity));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            isCameraPermissionGranted = false;
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CAMERA);
        } else {
            isCameraPermissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA:
                isCameraPermissionGranted = true;
                break;
        }
    }

    private void takePicture() {
        if (isCameraPermissionGranted) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            mProfileImgFile = new File(dir, "DabkickProfileImg.jpeg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mProfileImgFile));
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CAMERA);
        }
    }


    @OnClick(R.id.profile_pic_edit_view)
    public void editProfilePic() {
        takePicture();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Picasso.get().load(Uri.fromFile(mProfileImgFile)).placeholder(R.drawable.avatar_img).error(R.drawable.avatar_img).into(mProfileImgView);
                PreferenceHandler.setUserProfileImg(BaseActivity.mCurrentActivity, Uri.fromFile(mProfileImgFile).toString());
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (getActivity().getClass() == HomePageActivity.class) {
            ((HomePageActivity) getActivity()).updateFloatingBtn(true);
        }
        mUnbinder.unbind();
    }
}
