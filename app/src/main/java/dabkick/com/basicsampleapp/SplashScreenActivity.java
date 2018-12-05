package dabkick.com.basicsampleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);

    }

    @OnClick(R.id.done_btn)
    public void doneClick() {
        if (!TextUtils.isEmpty(mUserNameEditText.getText())) {
            mUserName = mUserNameEditText.getText().toString();

            startActivity(new Intent(SplashScreenActivity.this, ChatRoomActivity.class));
        } else {
            Toast.makeText(SplashScreenActivity.this, "Enter valid user name", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbinder.unbind();
    }
}
