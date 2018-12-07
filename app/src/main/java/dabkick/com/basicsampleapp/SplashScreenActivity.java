package dabkick.com.basicsampleapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SplashScreenActivity extends AppCompatActivity {

    @BindView(R.id.done_btn)
    AppCompatButton mDoneBtn;
    @BindView(R.id.user_name_edit_text)
    AppCompatEditText mUserNameEditText;

    private String mUserName = "";

    private Unbinder unbinder;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        unbinder = ButterKnife.bind(this);

        // 0 - for private mode
        preferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        String name = preferences.getString("userName", "");
        if (name != null && !name.trim().isEmpty()) {
            //user name is already set
            launchChatRoom();
        }

    }

    @OnClick(R.id.done_btn)
    public void doneClick() {
        mUserName = mUserNameEditText.getText().toString().trim();
        editor = preferences.edit();
        if(mUserName.isEmpty())
            mUserName = "anonymous";
        editor.putString("userName", mUserName);
        editor.commit();
        launchChatRoom();
    }

    public void launchChatRoom() {
        Intent intent = new Intent(SplashScreenActivity.this, ChatRoomActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
