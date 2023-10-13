package com.cscodetech.partner.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.partner.R;
import com.cscodetech.partner.model.AddOnDataItem;
import com.cscodetech.partner.model.OrderDataItem;
import com.cscodetech.partner.model.OrderProductDataItem;
import com.cscodetech.partner.model.ResponseMessge;
import com.cscodetech.partner.model.User;
import com.cscodetech.partner.retrofit.APIClient;
import com.cscodetech.partner.retrofit.GetResult;
import com.cscodetech.partner.utils.CustPrograssbar;
import com.cscodetech.partner.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ServiceDetailActivity extends BasicActivity implements GetResult.MyListener {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_creadit)
    TextView txtCreadit;
    @BindView(R.id.txt_total)
    TextView txtTotal;
    @BindView(R.id.txt_date)
    TextView txtDate;
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.txt_pannding)
    TextView txtPannding;
    @BindView(R.id.txt_complet)
    TextView txtComplet;
    @BindView(R.id.txt_cancel)
    TextView txtCancel;
    @BindView(R.id.txt_reject)
    TextView txtReject;
    @BindView(R.id.txt_location)
    TextView txtLocation;
    @BindView(R.id.txt_map)
    TextView txtMap;
    @BindView(R.id.txt_call)
    TextView txtCall;
    @BindView(R.id.txt_waiting)
    TextView txtWaiting;
    @BindView(R.id.lvl_location)
    LinearLayout lvlLocation;
    @BindView(R.id.recycleview_service)
    RecyclerView recycleviewService;
    @BindView(R.id.lvl_addon)
    LinearLayout lvlAddon;

    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    User user;
    OrderDataItem item;
    ArrayList<OrderProductDataItem> orderProductData = new ArrayList<>();
    ArrayList<AddOnDataItem> addOnData = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        ButterKnife.bind(this);
        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);

        sessionManager = new SessionManager(ServiceDetailActivity.this);
        custPrograssbar = new CustPrograssbar();
        user = sessionManager.getUserDetails("");
        item = getIntent().getParcelableExtra("myclass");
        addOnData = getIntent().getParcelableArrayListExtra("addon");
        orderProductData = getIntent().getParcelableArrayListExtra("itemlist");
        recycleviewService.setLayoutManager(new LinearLayoutManager(this));
        txtTitle.setText("" + item.getCustomerName());
        txtCreadit.setText("Credit : " + item.getRCredit());
        txtDate.setText("" + item.getOrderDateslot());
        txtTime.setText("" + item.getOrderTime());
        txtTotal.setText("Total : " + sessionManager.getStringData(SessionManager.currency) + item.getOrderTotal());
        txtLocation.setText("" + item.getCustomerAddress());
        if (item.getOrderStatus().equalsIgnoreCase("Pending")) {
            if (item.getHireType().equalsIgnoreCase("Direct")) {
                txtReject.setVisibility(View.VISIBLE);
            } else {
                txtReject.setVisibility(View.GONE);
            }
            txtPannding.setVisibility(View.VISIBLE);
            txtComplet.setVisibility(View.GONE);
            txtCancel.setVisibility(View.GONE);
            txtCall.setVisibility(View.GONE);
            lvlLocation.setVisibility(View.GONE);
        } else if (item.getOrderStatus().equalsIgnoreCase("Processing")) {
            if (item.getAddDesc() != null && item.getAddPrice() != null && !item.getAddDesc().equalsIgnoreCase("") && !item.getAddPrice().equalsIgnoreCase("")) {
                txtComplet.setVisibility(View.GONE);
                txtCancel.setVisibility(View.GONE);
                txtWaiting.setVisibility(View.VISIBLE);
            } else {
                txtComplet.setVisibility(View.VISIBLE);
                txtCancel.setVisibility(View.VISIBLE);
                txtWaiting.setVisibility(View.GONE);

            }
            txtPannding.setVisibility(View.GONE);

            txtCall.setVisibility(View.VISIBLE);
            lvlLocation.setVisibility(View.VISIBLE);

        }
        CartAdapter cartAdapter = new CartAdapter(this, orderProductData);
        recycleviewService.setAdapter(cartAdapter);
        setAddonList(lvlAddon, addOnData);
    }

    private void updateStatus(String status, String no) {
        custPrograssbar.prograssCreate(ServiceDetailActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oid", item.getOrderId());
            jsonObject.put("pid", user.getId());
            jsonObject.put("status", status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().statusChange(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, no);

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                ResponseMessge messge = gson.fromJson(result.toString(), ResponseMessge.class);
                Toast.makeText(ServiceDetailActivity.this, messge.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (messge.getResult().equalsIgnoreCase("true")) {
                    user.setCredit(messge.getPartnerCredit());
                    sessionManager.setUserDetails("", user);
                    txtPannding.setVisibility(View.GONE);
                    txtComplet.setVisibility(View.VISIBLE);
                    txtCancel.setVisibility(View.VISIBLE);

                    startActivity(new Intent(this, HomeActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                ResponseMessge messge = gson.fromJson(result.toString(), ResponseMessge.class);
                Toast.makeText(ServiceDetailActivity.this, messge.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (messge.getResult().equalsIgnoreCase("true")) {
                    user.setCredit(messge.getPartnerCredit());
                    sessionManager.setUserDetails("", user);
                    txtPannding.setVisibility(View.GONE);
                    txtComplet.setVisibility(View.GONE);
                    txtCancel.setVisibility(View.GONE);
                    startActivity(new Intent(this, HomeActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            } else if (callNo.equalsIgnoreCase("3")) {
                Gson gson = new Gson();
                ResponseMessge messge = gson.fromJson(result.toString(), ResponseMessge.class);
                Toast.makeText(ServiceDetailActivity.this, messge.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (messge.getResult().equalsIgnoreCase("true")) {

                    txtPannding.setVisibility(View.GONE);
                    txtComplet.setVisibility(View.GONE);
                    txtCancel.setVisibility(View.GONE);
                    startActivity(new Intent(this, HomeActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));


                }
            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());

        }
    }


    @OnClick({R.id.txt_call, R.id.txt_map, R.id.txt_pannding, R.id.txt_cancel, R.id.txt_complet, R.id.txt_reject})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_call:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + item.getCustomerMobile()));
                if (ActivityCompat.checkSelfPermission(ServiceDetailActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
                break;
            case R.id.txt_map:
                startActivity(new Intent(this, MapActivity.class).putExtra("lat", item.getLats()).putExtra("longs", item.getLongs()));

                break;
            case R.id.txt_pannding:
                if (Integer.parseInt(item.getRCredit()) <= user.getCredit()) {
                    updateStatus("accept", "1");
                } else {
                    Toast.makeText(ServiceDetailActivity.this, "Add Credit ", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ServiceDetailActivity.this, AddCreditActivity.class));
                }

                break;
            case R.id.txt_cancel:
                updateStatus("cancle", "2");

                break;
            case R.id.txt_reject:
                updateStatus("reject", "2");
                break;
            case R.id.txt_complet:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ServiceDetailActivity.this);
                alertDialogBuilder.setMessage("Any Extra Addon?");
                alertDialogBuilder.setPositiveButton("yes",
                        (arg0, arg1) -> {
                            startActivity(new Intent(this, AddOnsActivity.class)
                                    .putExtra("oid", item.getOrderId())
                                    .putExtra("cid", item.getCategoryId())
                            );

                        });

                alertDialogBuilder.setNegativeButton("No", (dialog, which) -> {
                    dialog.cancel();
                    updateStatus("complete", "3");
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    private void setAddonList(LinearLayout lnrView, List<AddOnDataItem> dataList) {

        lnrView.removeAllViews();

        for (int i = 0; i < dataList.size(); i++) {
            if (!dataList.get(i).getPrice().equalsIgnoreCase("0")) {
                LayoutInflater inflater = LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.item_adon, null);
                TextView itemTitle = view.findViewById(R.id.txt_title);
                TextView txtPrice = view.findViewById(R.id.txt_price);
                itemTitle.setText("" + dataList.get(i).getTitle());
                txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + dataList.get(i).getPrice());
                lnrView.addView(view);
            }
        }

    }


    public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
        private final Context context;
        private final List<OrderProductDataItem> mBanner;
        SessionManager sessionManager;


        public CartAdapter(Context context, List<OrderProductDataItem> mBanner) {
            this.context = context;
            this.mBanner = mBanner;
            sessionManager = new SessionManager(context);

        }

        @NonNull
        @Override
        public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_cart_item, parent, false);
            return new CartHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CartHolder holder, int position) {
            OrderProductDataItem item = mBanner.get(position);
            holder.txtTitle.setText("" + item.getProductName());
            holder.txtQty.setText("Qty " + item.getProductQuantity());
            double res = (item.getProductPrice() / 100.0f) * item.getProductDiscount();
            double pp = item.getProductPrice() - res;
            holder.txtPriced.setText(sessionManager.getStringData(SessionManager.currency) + new DecimalFormat("##.##").format(pp));
            holder.txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + item.getProductPrice());
            holder.txtPrice.setPaintFlags(holder.txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        }

        @Override
        public int getItemCount() {
            return mBanner.size();

        }

        public class CartHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.txt_title)
            TextView txtTitle;
            @BindView(R.id.txt_qty)
            TextView txtQty;

            @BindView(R.id.txt_price)
            TextView txtPrice;
            @BindView(R.id.txt_priced)
            TextView txtPriced;

            public CartHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }


    }
}