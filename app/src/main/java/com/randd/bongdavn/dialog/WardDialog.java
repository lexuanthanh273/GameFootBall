package com.randd.bongdavn.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.randd.bongdavn.R;
import com.randd.bongdavn.models.Ward;
import com.randd.bongdavn.sqlite.DBManager;

import java.util.ArrayList;

/**
 * Created by Thanh Le on 29/05/16.
 */
public class WardDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private EditText edBong;
    private EditText edAo;
    private EditText edBut;
    private EditText edCase;
    private EditText edBalo;
    private EditText edMu;
    private EditText edAoMua;
    private EditText edUsername;
    private EditText edPass;
    private Button btEnter;
    private Button btLogin;
    private DBManager dbManager;
    private RelativeLayout layoutLogin;
    private LinearLayout layoutWard;

    public WardDialog(Context context) {
        super(context);
        this.mContext = context;
        dbManager = new DBManager(mContext);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_ward);
        initViews();

    }

    private void initViews() {
        edBong = (EditText) findViewById(R.id.ed_bong);
        edAo = (EditText) findViewById(R.id.ed_ao);
        edBut = (EditText) findViewById(R.id.ed_but);
        edCase = (EditText) findViewById(R.id.ed_case);
        edBalo = (EditText) findViewById(R.id.ed_balo);
        edMu = (EditText) findViewById(R.id.ed_mu);
        edAoMua = (EditText) findViewById(R.id.ed_ao_mua);
        edUsername = (EditText) findViewById(R.id.ed_username);
        edPass = (EditText) findViewById(R.id.ed_password);
        layoutLogin = (RelativeLayout) findViewById(R.id.layout_login);
        layoutWard = (LinearLayout) findViewById(R.id.layout_ward);
        layoutWard.setVisibility(View.GONE);
        layoutLogin.setVisibility(View.VISIBLE);
        btEnter = (Button) findViewById(R.id.bt_enter);
        btEnter.setOnClickListener(this);
        btLogin = (Button) findViewById(R.id.bt_login);
        btLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                String user = edUsername.getText().toString();
                String pass = edPass.getText().toString();
                boolean result = dbManager.checkAccount(user, pass);
                if (result) {
                    layoutLogin.setVisibility(View.GONE);
                    layoutWard.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(mContext, "Tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_enter:
                doUpdateWard();
                dismiss();
                break;

        }
    }

    private void doUpdateWard() {
        int bong = -1;
        int ao = -1;
        int but = -1;
        int caseIP6 = -1;
        int balo = -1;
        int mu = -1;
        int aoMua = -1;
        if (!edBong.getText().toString().equals(""))
            bong = Integer.parseInt(edBong.getText().toString());
        if (!edAo.getText().toString().equals(""))
            ao = Integer.parseInt(edAo.getText().toString());
        if (!edBut.getText().toString().equals(""))
            but = Integer.parseInt(edBut.getText().toString());
        if (!edCase.getText().toString().equals(""))
            caseIP6 = Integer.parseInt(edCase.getText().toString());
        if (!edBalo.getText().toString().equals(""))
            balo = Integer.parseInt(edBalo.getText().toString());
        if (!edMu.getText().toString().equals(""))
            mu = Integer.parseInt(edMu.getText().toString());
        if (!edAoMua.getText().toString().equals(""))
            aoMua = Integer.parseInt(edAoMua.getText().toString());
        dbManager.upDateAllWard(bong, ao, but, caseIP6, balo, mu, aoMua);
        ArrayList<Ward> listWard;
        listWard = dbManager.getAllWard();
        for (int i = 0;  i < listWard.size(); i++) {
            Log.e("FUCK WARD: ", listWard.get(i).toString());
        }
        Toast.makeText(mContext, "Cập nhập thành công!", Toast.LENGTH_SHORT).show();
    }
}
