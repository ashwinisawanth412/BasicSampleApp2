package dabkick.com.basicsampleapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dabkick.engine.Public.Authentication;
import com.dabkick.engine.Public.CallbackListener;
import com.dabkick.engine.Public.DKLiveChat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dabkick.com.basicsampleapp.Utils.Utils;

public class SplashScreenActivity extends BaseActivity {

    @BindView(R.id.done_btn)
    AppCompatButton mDoneBtn;
    @BindView(R.id.user_name_edit_text)
    AppCompatEditText mUserNameEditText;
    @BindView(R.id.dev_key_edit_text)
    AppCompatEditText mUserDevKeyEdiText;
    @BindView(R.id.dev_id_edit_text)
    AppCompatEditText mUserDevId;

    @BindView(R.id.container)
    RelativeLayout mNameEditTextContainer;
    private Unbinder unbinder;

    public static DKLiveChat dkLiveChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        unbinder = ButterKnife.bind(this);
        setUserDetailsIfUpdated();
        mUserDevKeyEdiText.setText("3d8a7db548d5d91447d64d09a37f12");
        mUserDevId.setText("DK09aff676f38011e88a1a06f");
    }

    public void setUserDetailsIfUpdated(){
        String name = PreferenceHandler.getUserName(SplashScreenActivity.this);
        mUserNameEditText.setText(name);
        mUserNameEditText.setSelection(name.length());

        String devKey = PreferenceHandler.getDevKey(SplashScreenActivity.this);
        mUserDevKeyEdiText.setText(devKey);
        mUserDevKeyEdiText.setSelection(devKey.length());

        String devId = PreferenceHandler.getDevId(SplashScreenActivity.this);
        mUserDevId.setText(devId);
        mUserDevId.setSelection(devId.length());

    }

    public void initEngine() {
        String devId = mUserDevId.getText().toString().trim();
        String devKey = mUserDevKeyEdiText.getText().toString().trim();

        Authentication auth = new Authentication(devId, devKey);
        dkLiveChat = new DKLiveChat(this, auth, new CallbackListener() {
            @Override
            public void onSuccess(String s, Object... objects) {
                PreferenceHandler.setUserName(SplashScreenActivity.this, mUserNameEditText.getText().toString().trim());
                PreferenceHandler.setDevId(BaseActivity.mCurrentActivity, devId);
                PreferenceHandler.setDevKey(BaseActivity.mCurrentActivity, devKey);

                launchHomePage();
            }

            @Override
            public void onError(String s, Object... objects) {
                Toast.makeText(BaseActivity.mCurrentActivity, "Authentication failed.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.done_btn)
    public void doneClick() {
        //initialize engine
        initEngine();
    }

    public void launchHomePage() {
        Intent intent = new Intent(SplashScreenActivity.this, HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //hides kb
        Utils.hideKeyboard(SplashScreenActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
