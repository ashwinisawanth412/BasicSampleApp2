package dabkick.com.basicsampleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BaseActivity extends AppCompatActivity {

    static public BaseActivity mCurrentActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mCurrentActivity = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
