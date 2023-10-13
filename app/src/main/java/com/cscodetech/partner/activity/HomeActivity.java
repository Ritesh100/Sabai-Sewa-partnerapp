package com.cscodetech.partner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.cscodetech.partner.R;
import com.cscodetech.partner.fragment.CompleteFragment;
import com.cscodetech.partner.fragment.EarningFragment;
import com.cscodetech.partner.fragment.HomeFragment;
import com.cscodetech.partner.fragment.OngoingFragment;
import com.cscodetech.partner.fragment.ProfileFragment;
import com.cscodetech.partner.imagepicker.ImageCompressionListener;
import com.cscodetech.partner.imagepicker.ImagePicker;
import com.cscodetech.partner.model.PayTrazection;
import com.cscodetech.partner.model.Payout;
import com.cscodetech.partner.model.PayoutListDataItem;
import com.cscodetech.partner.model.User;
import com.cscodetech.partner.retrofit.APIClient;
import com.cscodetech.partner.retrofit.GetResult;
import com.cscodetech.partner.utils.CustPrograssbar;
import com.cscodetech.partner.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, GetResult.MyListener {

    SessionManager sessionManager;
    BottomNavigationView navigation;
    User user;
    CustPrograssbar custPrograssbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_home);
        sessionManager = new SessionManager(HomeActivity.this);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.page_1);
        user = sessionManager.getUserDetails("");
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.page_1:
                item.setChecked(true);
                loadFragment(new HomeFragment());
                break;
            case R.id.page_2:
                item.setChecked(true);
                loadFragment(new OngoingFragment());
                break;
            case R.id.page_3:
                item.setChecked(true);
                loadFragment(new EarningFragment());
                break;
            case R.id.page_4:
                item.setChecked(true);
                loadFragment(new ProfileFragment());
                break;
            case R.id.page_5:
                item.setChecked(true);
                loadFragment(new CompleteFragment());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }

        return loadFragment(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_option, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
            alertDialogBuilder.setMessage(getResources().getString(R.string.logoutmessege));
            alertDialogBuilder.setPositiveButton("yes",
                    (arg0, arg1) -> {
                        sessionManager.logoutUser();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    });

            alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
        if (item.getItemId() == R.id.payout) {
            custPrograssbar = new CustPrograssbar();
            getPayout();

        }
        if (item.getItemId() == R.id.payouttra) {
            custPrograssbar = new CustPrograssbar();
            getPayoutList();

        }
        return true;
    }

    private void getPayout() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pid", user.getId());

            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getPayoutData(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPayoutList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pid", user.getId());

            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getPayoutlist(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "3");
            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestPayout() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pid", user.getId());
            jsonObject.put("bname", edBankname.getText().toString());
            jsonObject.put("ifsc", edIfsccode.getText().toString());
            jsonObject.put("rname", edRname.getText().toString());
            jsonObject.put("acno", edAcountnumber.getText().toString());
            jsonObject.put("paypalid", edPaypalid.getText().toString());
            jsonObject.put("upi", edUpi.getText().toString());
            jsonObject.put("amt", edAcount.getText().toString());


            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().requestPayout(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Payout payout = gson.fromJson(result.toString(), Payout.class);

                if (payout.getResult().equalsIgnoreCase("true")) {
                    bottonPaymentList(payout.getPayoutlimit(), payout.getWalletbalance());
                }

            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Payout payout = gson.fromJson(result.toString(), Payout.class);
                Toast.makeText(HomeActivity.this, payout.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (payout.getResult().equalsIgnoreCase("true")) {
                    mBottomSheetDialog.cancel();
                }
            } else if (callNo.equalsIgnoreCase("3")) {
                Gson gson = new Gson();
                PayTrazection payout = gson.fromJson(result.toString(), PayTrazection.class);

                if (payout.getResult().equalsIgnoreCase("true")) {
                    bottonPayOutList(payout.getPayoutListData());
                }
            }

        } catch (Exception e) {
            e.toString();
        }
    }

    EditText edBankname;
    EditText edIfsccode;
    EditText edRname;
    EditText edAcountnumber;
    EditText edPaypalid;
    EditText edUpi;
    EditText edAcount;
    TextView btnCountinue;
    TextView txtIncome;
    TextView txtExpence;
    BottomSheetDialog mBottomSheetDialog;


        public void bottonPaymentList(String incoms, String exp) {
            String data = incoms;
            mBottomSheetDialog = new BottomSheetDialog(this);
            View rootView = getLayoutInflater().inflate(R.layout.custome_payout, null);

            edBankname = rootView.findViewById(R.id.ed_bankname);
            edIfsccode = rootView.findViewById(R.id.ed_ifsccode);
            edRname = rootView.findViewById(R.id.ed_rname);
            edAcountnumber = rootView.findViewById(R.id.ed_acountnumber);
            edPaypalid = rootView.findViewById(R.id.ed_paypalid);
            edUpi = rootView.findViewById(R.id.ed_upi);
            edAcount = rootView.findViewById(R.id.ed_acount);
            btnCountinue = rootView.findViewById(R.id.btn_countinue);
            txtIncome = rootView.findViewById(R.id.txt_income);
            txtExpence = rootView.findViewById(R.id.txt_expence);
            txtIncome.setText("" + sessionManager.getStringData(SessionManager.currency) + incoms);
            txtExpence.setText("" + sessionManager.getStringData(SessionManager.currency) + exp);

            btnCountinue.setOnClickListener(v -> {
                if (TextUtils.isEmpty(edBankname.getText().toString())) {
                    edBankname.setError("");
                    return;
                }
                if (TextUtils.isEmpty(edIfsccode.getText().toString())) {
                    edIfsccode.setError("");
                    return;
                }
                if (TextUtils.isEmpty(edRname.getText().toString())) {
                    edRname.setError("");
                    return;
                }
                if (TextUtils.isEmpty(edAcountnumber.getText().toString())) {
                    edAcountnumber.setError("");
                    return;
                }
                if (TextUtils.isEmpty(edPaypalid.getText().toString())) {
                    edPaypalid.setError("");
                    return;
                }
                if (TextUtils.isEmpty(edUpi.getText().toString())) {
                    edUpi.setError("");
                    return;
                }
                if (TextUtils.isEmpty(edAcount.getText().toString())) {
                    edAcount.setError("");
                    return;
                }
                Log.e("data", "-->" + data);
                Log.e("data", "-->" + edAcount.getText().toString());
                int a = Integer.parseInt(data);
                int b = Integer.parseInt(edAcount.getText().toString());
                if (a < b) {
                    requestPayout();
                } else {
                    Toast.makeText(HomeActivity.this, "Minimum Withdraw Limit : " + data, Toast.LENGTH_LONG).show();
                }

            });
            mBottomSheetDialog.setContentView(rootView);
            mBottomSheetDialog.show();
        }

    public void bottonPayOutList(List<PayoutListDataItem> list) {

        mBottomSheetDialog = new BottomSheetDialog(this);
        View rootView = getLayoutInflater().inflate(R.layout.custome_payoutlist, null);

        LinearLayout listView = rootView.findViewById(R.id.lvl_list);
        for (int i = 0; i < list.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
            PayoutListDataItem payout = list.get(i);
            View view = inflater.inflate(R.layout.custome_payoutitem, null);
            ImageView imgProof = view.findViewById(R.id.img_proof);
            TextView txtStatus = view.findViewById(R.id.txt_status);
            TextView txtRequst = view.findViewById(R.id.txt_requst);
            TextView txtAmt = view.findViewById(R.id.txt_amt);
            TextView txtPayby = view.findViewById(R.id.txt_payby);
            TextView txtRDate = view.findViewById(R.id.txt_r_date);
            TextView txtBankname = view.findViewById(R.id.txt_bankname);
            TextView txtIfccode = view.findViewById(R.id.txt_ifccode);
            TextView txtAcno = view.findViewById(R.id.txt_acno);
            TextView txtRname = view.findViewById(R.id.txt_rname);
            TextView txtPaypalid = view.findViewById(R.id.txt_paypalid);
            TextView txtUpi = view.findViewById(R.id.txt_upi);

            Glide.with(HomeActivity.this).load(APIClient.baseUrl + "/" + payout.getProof()).into(imgProof);

            txtStatus.setText("" + payout.getStatus());
            txtRequst.setText("" + payout.getRequestId());
            txtAmt.setText("" + payout.getAmt());
            txtPayby.setText("" + payout.getPBy());
            txtRDate.setText("" + payout.getRDate());
            txtBankname.setText("" + payout.getBname());
            txtIfccode.setText("" + payout.getIfsc());
            txtAcno.setText("" + payout.getAcno());
            txtRname.setText("" + payout.getRname());
            txtPaypalid.setText("" + payout.getPaypalid());
            txtUpi.setText("" + payout.getUpi());

            listView.addView(view);
        }

        mBottomSheetDialog.setContentView(rootView);
        mBottomSheetDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.SELECT_IMAGE && resultCode == RESULT_OK) {

            ProfileFragment.getInstance().imagePicker.addOnCompressListener(new ImageCompressionListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onCompressed(String filePath) {
                    if (filePath != null) {
                        //return filepath

                        ProfileFragment.getInstance().setProfile(filePath);
                    }
                }
            });
            String filePath = ProfileFragment.getInstance().imagePicker.getImageFilePath(data);
            if (filePath != null) {
                //return filepath
                ProfileFragment.getInstance().setProfile(filePath);
            }
        }
    }


}