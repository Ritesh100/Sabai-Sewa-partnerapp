package com.cscodetech.partner.activity;

import static com.cscodetech.partner.utils.Utility.paymentsucsses;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.partner.R;
import com.cscodetech.partner.adepter.CreditAdapter;
import com.cscodetech.partner.model.Credit;
import com.cscodetech.partner.model.Payment;
import com.cscodetech.partner.model.PaymentItem;
import com.cscodetech.partner.model.ResponseMessge;
import com.cscodetech.partner.model.User;
import com.cscodetech.partner.retrofit.APIClient;
import com.cscodetech.partner.retrofit.GetResult;
import com.cscodetech.partner.utils.CustPrograssbar;
import com.cscodetech.partner.utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class AddCreditActivity extends BasicActivity implements GetResult.MyListener, CreditAdapter.RecyclerTouchListener {

    @BindView(R.id.recycle_addone)
    RecyclerView recycleCretid;
    @BindView(R.id.lvl_submit)
    LinearLayout lvlSubmit;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    CreditAdapter creditAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credit);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        getSupportActionBar().setTitle("Add Credit");
        sessionManager = new SessionManager(AddCreditActivity.this);
        user = sessionManager.getUserDetails("");
        recycleCretid.setLayoutManager(new GridLayoutManager(this, 2));
        recycleCretid.setItemAnimator(new DefaultItemAnimator());
        creditList();
    }

    private void getPayment() {
        custPrograssbar.prograssCreate(this);

        JSONObject jsonObject = new JSONObject();
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().paymentlist(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

    }

    private void creditList() {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pid", user.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().creditList(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    private void addCredit(String credit) {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pid", user.getId());
            jsonObject.put("credit", credit);

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().pCreditUp(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "3");

    }

    @OnClick({R.id.lvl_submit})
    public void onClick(View view) {
        if (view.getId() == R.id.lvl_submit) {
            getPayment();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Credit credit = gson.fromJson(result.toString(), Credit.class);
                if (credit.getResult().equalsIgnoreCase("true")) {
                    creditAdapter = new CreditAdapter(AddCreditActivity.this, credit.getCatlist(), this);
                    recycleCretid.setAdapter(creditAdapter);
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson1 = new Gson();
                Payment payment = gson1.fromJson(result.toString(), Payment.class);
                bottonPaymentList(payment.getData());
            } else if (callNo.equalsIgnoreCase("3")) {
                Gson gson1 = new Gson();
                ResponseMessge payment = gson1.fromJson(result.toString(), ResponseMessge.class);
                if (payment.getResult().equalsIgnoreCase("true")) {
                    user.setCredit(payment.getPartnerCredit());
                    sessionManager.setUserDetails("", user);
                    finish();
                }

            }
        } catch (Exception e) {
            Log.e("Error","-->"+e.toString());
        }
    }

    @Override
    public void onClickAddonsItem(String titel, int position) {

    }

    BottomSheetDialog mBottomSheetDialog;

    public void bottonPaymentList(List<PaymentItem> paymentList) {
        mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.custome_payment, null);
        LinearLayout listView = sheetView.findViewById(R.id.lvl_list);
        TextView txtTotal = sheetView.findViewById(R.id.txt_total);
        txtTotal.setText("item total " + sessionManager.getStringData(SessionManager.currency) + creditAdapter.getSelectItem().getAmt());
        for (int i = 0; i < paymentList.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(AddCreditActivity.this);
            PaymentItem paymentItem = paymentList.get(i);
            View view = inflater.inflate(R.layout.custome_paymentitem, null);
            ImageView imageView = view.findViewById(R.id.img_icon);
            TextView txtTitle = view.findViewById(R.id.txt_title);
            TextView txtSubtitel = view.findViewById(R.id.txt_subtitel);
            txtTitle.setText("" + paymentList.get(i).getmTitle());
            txtSubtitel.setText("" + paymentList.get(i).getSubtitle());
            Glide.with(AddCreditActivity.this).load(APIClient.baseUrl + "/" + paymentList.get(i).getmImg()).into(imageView);
            int finalI = i;
            view.setOnClickListener(v -> {

                try {
                    switch (paymentList.get(finalI).getmTitle()) {
                        case "Razorpay":
                            int temtoal = (int) Math.round(Double.parseDouble(creditAdapter.getSelectItem().getAmt()));
                            startActivity(new Intent(AddCreditActivity.this, RazerpayActivity.class).putExtra("amount", temtoal).putExtra("detail", paymentItem));
                            break;
                        case "Paypal":
                            startActivity(new Intent(AddCreditActivity.this, PaypalActivity.class).putExtra("amount", Double.parseDouble(creditAdapter.getSelectItem().getAmt())).putExtra("detail", paymentItem));
                            break;
                        case "Stripe":
                            startActivity(new Intent(AddCreditActivity.this, StripPaymentActivity.class).putExtra("amount", Double.parseDouble(creditAdapter.getSelectItem().getAmt())).putExtra("detail", paymentItem));
                            break;
                        default:
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            listView.addView(view);
        }
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paymentsucsses == 1) {
            paymentsucsses = 0;
            addCredit(creditAdapter.getSelectItem().getCreditAmt());

        }
    }
}