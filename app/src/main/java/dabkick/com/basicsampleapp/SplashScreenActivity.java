package dabkick.com.basicsampleapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;

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

        preferences = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String name = preferences.getString("userName", "");
        if (name != null && !name.trim().isEmpty()) {
            //user name is already set
            launchHomePage();
        }

    }

    @OnClick(R.id.done_btn)
    public void doneClick() {
        mUserName = mUserNameEditText.getText().toString().trim();
        editor = preferences.edit();
        if (TextUtils.isEmpty(mUserName))
            mUserName = "anonymous";
        editor.putString("userName", mUserName);
        editor.commit();
        launchHomePage();
    }

    public void launchHomePage() {
        Intent intent = new Intent(SplashScreenActivity.this, HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
