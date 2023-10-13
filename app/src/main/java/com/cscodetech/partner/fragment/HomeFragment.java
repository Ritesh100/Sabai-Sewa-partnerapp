package com.cscodetech.partner.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.partner.R;
import com.cscodetech.partner.adepter.NewSearviceAdapter;
import com.cscodetech.partner.model.Order;
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
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;


public class HomeFragment extends Fragment implements GetResult.MyListener {

    @BindView(R.id.recycleview_new)
    RecyclerView recycleviewNew;
    @BindView(R.id.lvl_nodata)
    LinearLayout lvlNodata;
    @BindView(R.id.lvl_notaporv)
    LinearLayout lvlNotaporv;
    @BindView(R.id.txt_nodata)
    TextView txtNodata;
    SessionManager sessionManager;
    User user;
    CustPrograssbar custPrograssbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        sessionManager = new SessionManager(getActivity());
        custPrograssbar = new CustPrograssbar();
        user = sessionManager.getUserDetails("");
        recycleviewNew.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        if (user.getAprove().equalsIgnoreCase("0")) {
            lvlNotaporv.setVisibility(View.VISIBLE);
            recycleviewNew.setVisibility(View.GONE);

        } else {
            lvlNotaporv.setVisibility(View.GONE);
            recycleviewNew.setVisibility(View.VISIBLE);

        }
        sgetNewService();

        return view;
    }

    private void sgetNewService() {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pid", user.getId());
            jsonObject.put("status", "new");
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getHome(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Order order = gson.fromJson(result.toString(), Order.class);
                if (order.getResult().equalsIgnoreCase("true")) {
                    user.setAprove(order.getIsApprove());
                    sessionManager.setUserDetails("", user);
                    if (order.getIsApprove().equalsIgnoreCase("0")) {
                        lvlNotaporv.setVisibility(View.VISIBLE);
                        recycleviewNew.setVisibility(View.GONE);

                    } else {
                        lvlNotaporv.setVisibility(View.GONE);
                        recycleviewNew.setVisibility(View.VISIBLE);
                        lvlNodata.setVisibility(View.GONE);
                        NewSearviceAdapter adapter = new NewSearviceAdapter(getActivity(), order.getOrderData());
                        recycleviewNew.setAdapter(adapter);
                    }

                } else {
                    txtNodata.setText(order.getResponseMsg());
                    lvlNodata.setVisibility(View.VISIBLE);
                    recycleviewNew.setVisibility(View.GONE);

                }
            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());

        }
    }
}