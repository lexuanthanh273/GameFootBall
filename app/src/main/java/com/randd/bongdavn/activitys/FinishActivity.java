package com.randd.bongdavn.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.randd.bongdavn.R;
import com.randd.bongdavn.models.Ward;
import com.randd.bongdavn.sqlite.DBManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * Created by Thanh Le on 19/05/16.
 */
public class FinishActivity extends Activity implements View.OnClickListener {
    private static final int BONG = 45;
    private static final int MU = 90;
    private static final int BUT_BI = 135;
    private static final int CASE = 180;
    private static final int BALO = 225;
    private static final int TIEP_TUC = 270;
    private static final int AO = 315;
    private static final int AO_MUA = 360;

    private int score;
    private DBManager dbManager;
    private Context mContext = FinishActivity.this;

    //Layout perfomation
    private RelativeLayout layoutPerfomation;
    private TextView tvScore;
    private EditText edName;
    private EditText edPhone;
    private EditText edEmail;
    private Button btLucky;

    //Layout Lucky
    private RelativeLayout layoutLucky;
    private ImageView ivWheel;
    private Button btStartWheel;

    //Layout Gift
    private RelativeLayout layoutGift;
    private TextView tvGift;
    private ImageView ivGift;
    private Button btOut;

    //Array
    private ArrayList<Ward> listWard = new ArrayList<>();
    private ArrayList<Integer> listAllWard = new ArrayList<>();


    private String name;
    private String phone;
    private String email;
    private boolean isStartLucky = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        score = getIntent().getIntExtra(MainActivity.SCORE, -1);
        dbManager = new DBManager(mContext);
        initWard();
        initViews();
        tvScore.setText(String.valueOf(score));
    }

    private void initWard() {
        listWard = dbManager.getAllWard();
        listAllWard.clear();
        for (int i = 0; i < listWard.size(); i++) {
            for (int j = 0;  j < listWard.get(i).getCount(); j++) {
                switch (listWard.get(i).getId()) {
                    case 1:
                        if (score >= 5)
                            listAllWard.add(BONG);
                        break;
                    case 2:
                        if (score >= 5)
                            listAllWard.add(AO);
                        break;
                    case 3:
                        if (score == 0 || score == 1)
                            listAllWard.add(BUT_BI);
                        break;
                    case 4:
                        if (score == 3 || score == 4)
                            listAllWard.add(CASE);
                        break;
                    case 5:
                        if (score == 3 || score == 4)
                            listAllWard.add(BALO);
                        break;
                    case 6:
                        if (score == 2)
                        listAllWard.add(MU);
                        break;
                    case 7:
                            if (score == 3 || score == 4)
                        listAllWard.add(AO_MUA);
                        break;
                }
            }
        }
        listAllWard.add(TIEP_TUC);
    }

    private void initViews() {
        //Layout perfomation
        layoutPerfomation = (RelativeLayout) findViewById(R.id.layout_perfomation);
        tvScore = (TextView) findViewById(R.id.tv2);
        edName = (EditText) findViewById(R.id.ed_name);
        edPhone = (EditText) findViewById(R.id.ed_phone);
        edEmail = (EditText) findViewById(R.id.ed_email);
        btLucky = (Button) findViewById(R.id.bt_quaythuong);
        btLucky.setOnClickListener(this);

        //Layout Lucky
        layoutLucky = (RelativeLayout) findViewById(R.id.layout_quaythuong);
        ivWheel = (ImageView) findViewById(R.id.iv_wheel);
        btStartWheel = (Button) findViewById(R.id.bt_start_lucky);
        btStartWheel.setOnClickListener(this);

        //Layout Gift
        layoutGift = (RelativeLayout) findViewById(R.id.layout_gift);
        tvGift = (TextView) findViewById(R.id.tv_gift);
        ivGift = (ImageView) findViewById(R.id.iv_gift);
        btOut = (Button) findViewById(R.id.bt_out);
        btOut.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FinishActivity.this, HomeActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_quaythuong:
                name = edName.getText().toString();
                phone = edPhone.getText().toString();
                email = edEmail.getText().toString();
                if (name.equals("") || phone.equals("") || email.equals(""))
                    Toast.makeText(mContext, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                else {
                layoutLucky.setVisibility(View.VISIBLE);
                startAnimationView(layoutLucky, R.anim.scale2);
                layoutPerfomation.setVisibility(View.GONE);
                }
                break;
            case R.id.bt_start_lucky:
                if (!isStartLucky) {
                    doStartLucky();
                    isStartLucky = true;
                }
                break;

            case R.id.bt_out:
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void doStartLucky() {
        final Random rd = new Random();
        int b = rd.nextInt(listAllWard.size());
        Collections.shuffle(listAllWard);
        final int angel = listAllWard.get(b);
        switch (angel) {
            case BONG:
                ivGift.setImageResource(R.drawable.ic_bongbong);
                tvGift.setText("1 quả Bóng logo Z.com");
                dbManager.updateQuayThuong(1);
                dbManager.savePerfomation(name, phone, email, "Bóng đá");
                break;
            case AO:
                ivGift.setImageResource(R.drawable.ic_ao);
                tvGift.setText("1 chiếc Áo phông logo Z.com");
                dbManager.updateQuayThuong(2);
                dbManager.savePerfomation(name, phone, email, "Áo phông");
                break;
            case BUT_BI:
                ivGift.setImageResource(R.drawable.ic_but);
                tvGift.setText("1 chiếc Bút bi logo Z.com");
                dbManager.updateQuayThuong(3);
                dbManager.savePerfomation(name, phone, email, "Bút bi");
                break;
            case CASE:
                ivGift.setImageResource(R.drawable.ic_case);
                tvGift.setText("1 Case Iphone 6 logo Z.com");
                dbManager.updateQuayThuong(4);
                dbManager.savePerfomation(name, phone, email, "Case Iphone 6");
                break;
            case BALO:
                ivGift.setImageResource(R.drawable.ic_balo);
                tvGift.setText("1 chiếc Balo dây rút logo Z.com");
                dbManager.updateQuayThuong(5);
                dbManager.savePerfomation(name, phone, email, "Balo dây rút");
                break;
            case MU:
                ivGift.setImageResource(R.drawable.ic_mu);
                tvGift.setText("1 chiếc Mũ logo Z.com");
                dbManager.updateQuayThuong(6);
                dbManager.savePerfomation(name, phone, email, "Mũ");
                break;
            case AO_MUA:
                ivGift.setImageResource(R.drawable.ic_ao_mua);
                tvGift.setText("1 chiếc Áo mưa logo Z.com");
                dbManager.updateQuayThuong(7);
                dbManager.savePerfomation(name, phone, email, "Áo mưa");
                break;
        }
        int c = rd.nextInt(5) + 10;
        final RotateAnimation rotateAnimation =
                new RotateAnimation(0.0f, angel + (c * 360), Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (animation.hasEnded()) {
                    if (angel != TIEP_TUC) {
                        startAnimationView(layoutLucky, R.anim.scale);
                        layoutLucky.setVisibility(View.GONE);
                        layoutGift.setVisibility(View.VISIBLE);
                    } else {
//                        doStartLucky();
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivWheel.startAnimation(rotateAnimation);
    }

    private void startAnimationView(View view, int animation) {
        Animation animation1 = AnimationUtils.loadAnimation(mContext, animation);
        view.startAnimation(animation1);

    }
}
