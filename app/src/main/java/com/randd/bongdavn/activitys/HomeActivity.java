package com.randd.bongdavn.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.randd.bongdavn.R;
import com.randd.bongdavn.dialog.WardDialog;


/**
 * Created by Thanh Le on 10/05/16.
 */
public class HomeActivity extends Activity implements View.OnClickListener {

    private Button btSetting;
    private Button btStart;
    private Context mContext = HomeActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btStart = (Button) findViewById(R.id.bt_start);
        btSetting = (Button) findViewById(R.id.bt_setting);
        btStart.setOnClickListener(this);
        btSetting.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start:
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                break;
            case R.id.bt_setting:
                WardDialog dialog = new WardDialog(mContext);
                dialog.show();

        }
    }
}
