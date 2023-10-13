package com.cscodetech.partner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cscodetech.partner.R;
import com.cscodetech.partner.model.ResponseMessge;
import com.cscodetech.partner.model.User;
import com.cscodetech.partner.retrofit.APIClient;
import com.cscodetech.partner.retrofit.GetResult;
import com.cscodetech.partner.utils.CustPrograssbar;
import com.cscodetech.partner.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class AddOnsActivity extends BasicActivity implements  GetResult.MyListener {


    @BindView(R.id.lvl_next)
    LinearLayout lvlNext;
    @BindView(R.id.ed_title)
    EditText edTitle;
    @BindView(R.id.ed_price)
    EditText edPrice;

    String cid;
    String oid;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ons);
        ButterKnife.bind(this);
        oid = getIntent().getStringExtra("oid");

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails("");
        cid = getIntent().getStringExtra("cid");


    }



    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson=new Gson();
                ResponseMessge messge = gson.fromJson(result.toString(), ResponseMessge.class);
                Toast.makeText(AddOnsActivity.this, messge.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (messge.getResult().equalsIgnoreCase("true")) {
                    startActivity(new Intent(AddOnsActivity.this, HomeActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            }
        } catch (Exception e) {
            Log.e("error", "-->" + e.toString());
        }
    }


    @OnClick({R.id.lvl_next})
    public void onClick(View view) {
        if (view.getId() == R.id.lvl_next) {

            if (!TextUtils.isEmpty(edTitle.getText())&& !TextUtils.isEmpty(edPrice.getText())) {

                addAddon();
            } else {
                Toast.makeText(AddOnsActivity.this, "Enter add-on ..", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void addAddon() {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oid",oid);
            jsonObject.put("pid", user.getId());
            jsonObject.put("status", "add_on");
            jsonObject.put("desc", edTitle.getText().toString());
            jsonObject.put("price", edPrice.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().statusChange(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }
}