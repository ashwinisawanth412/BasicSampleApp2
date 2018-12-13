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

import com.dabkick.engine.Public.Authentication;
import com.dabkick.engine.Public.CallbackListener;
import com.dabkick.engine.Public.DKLiveChat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SplashScreenActivity extends AppCompatActivity {

    @BindView(R.id.done_btn)
    AppCompatButton mDoneBtn;
    @BindView(R.id.user_name_edit_text)
    AppCompatEditText mUserNameEditText;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.relativeLayoutProgressbar)
    RelativeLayout mRelativeLayoutProgressbar;

    private String mUserName = "";

    private Unbinder unbinder;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public DKLiveChat dkLiveChat;

    private static SplashScreenActivity splashScreenActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        unbinder = ButterKnife.bind(this);
        splashScreenActivity = this;

        preferences = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        /*String name = preferences.getString("userName", "");
        if (name != null && !name.trim().isEmpty()) {
            //user name is already set
            launchHomePage();
        }*/
        initEngine();

    }

    public static SplashScreenActivity getInstance(){
        return splashScreenActivity;
    }

    public void initEngine(){
        Authentication auth = new Authentication("DK09aff676f38011e88a1a06f", "3d8a7db548d5d91447d64d09a37f12");
        dkLiveChat = new DKLiveChat(this, auth, new CallbackListener() {
            @Override
            public void onSuccess(String s, Object... objects) {
                //update name after engine is successfully intialised
//                updateName();
            }

            @Override
            public void onError(String s, Object... objects) {

            }
        });
    }

    @OnClick(R.id.done_btn)
    public void doneClick() {
        mUserName = mUserNameEditText.getText().toString().trim();
        mRelativeLayoutProgressbar.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        editor = preferences.edit();
        if (TextUtils.isEmpty(mUserName))
            mUserName = "anonymous";
        editor.putString("userName", mUserName);
        editor.commit();
        /*mProgressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);


            }
        }, 3000);
*/

        launchHomePage();
    }

    public void launchHomePage() {

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
                mRelativeLayoutProgressbar.setVisibility(View.GONE);
                Intent intent = new Intent(SplashScreenActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        }, 2000);



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
