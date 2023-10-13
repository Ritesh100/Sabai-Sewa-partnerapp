package com.cscodetech.partner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.partner.R;
import com.cscodetech.partner.model.Category;
import com.cscodetech.partner.model.CatlistItem;
import com.cscodetech.partner.model.LoginUser;
import com.cscodetech.partner.model.User;
import com.cscodetech.partner.retrofit.APIClient;
import com.cscodetech.partner.retrofit.GetResult;
import com.cscodetech.partner.utils.CustPrograssbar;
import com.cscodetech.partner.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;



public class CategoryActivity extends BasicActivity implements GetResult.MyListener {
    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.txt_cuntinue)
    TextView txtCuntinue;
    List<CatlistItem> nodelrrayList = new ArrayList<>();
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    CatAdepter adepter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(CategoryActivity.this);
        user = sessionManager.getUserDetails("");
        custPrograssbar = new CustPrograssbar();
        getCategory();
        recycleview.setLayoutManager(new GridLayoutManager(this, 1));
        recycleview.setItemAnimator(new DefaultItemAnimator());

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adepter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void getCategory() {
        custPrograssbar.prograssCreate(CategoryActivity.this);
        JSONObject jsonObject = new JSONObject();
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getCategory(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    private void sendCategory() {
        custPrograssbar.prograssCreate(CategoryActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("category_id", nodelrrayList.get(lastCheckedPos).getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<JsonObject> call = APIClient.getInterface().updateCategory(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Category category = gson.fromJson(result.toString(), Category.class);
                nodelrrayList = category.getCatlist();
                adepter = new CatAdepter( nodelrrayList);
                recycleview.setAdapter(adepter);
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                LoginUser loginUser = gson.fromJson(result.toString(), LoginUser.class);
                Toast.makeText(this, loginUser.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (loginUser.getResult().equalsIgnoreCase("true")) {
                    sessionManager.setUserDetails("", loginUser.getUser());

                    startActivity(new Intent(this, HomeActivity.class));
                }

            }
        } catch (Exception e) {
            Log.e("Error","-->"+e.toString());

        }
    }

    int lastCheckedPos = 0;

    public class CatAdepter extends RecyclerView.Adapter<CatAdepter.MyViewHolder> implements Filterable {

        private List<CatlistItem> mCatlist;
        private final List<CatlistItem> contactListFiltered;


        RadioButton lastChecked = null;

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mCatlist = contactListFiltered;
                    } else {
                        List<CatlistItem> filteredList = new ArrayList<>();
                        for (CatlistItem row : contactListFiltered) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getCatName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }

                        mCatlist = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mCatlist;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mCatlist = (ArrayList<CatlistItem>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public ImageView thumbnail;
            public ImageView imgChackb;
            public LinearLayout lvlclick;

            public MyViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.txt_title);
                thumbnail = view.findViewById(R.id.imageView);
                imgChackb = view.findViewById(R.id.img_chackb);
                lvlclick = view.findViewById(R.id.lvl_itemclick);
            }
        }

        public CatAdepter( List<CatlistItem> mCatlist) {


            this.mCatlist = mCatlist;
            this.contactListFiltered = mCatlist;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView;
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_cetegory, parent, false);


            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {


            CatlistItem model = mCatlist.get(position);
            Glide.with(CategoryActivity.this).load(APIClient.baseUrl + "/" + model.getCatImg()).centerCrop().into(holder.thumbnail);
            holder.title.setText("" + model.getCatName());
            if (model.isSelected()) {
                holder.imgChackb.setBackground(getResources().getDrawable(R.drawable.ic_chack));
            } else {
                holder.imgChackb.setBackground(getResources().getDrawable(R.drawable.ic_unchake));

            }

            holder.lvlclick.setOnClickListener(v -> {
                Log.e("lksajdklja", "sakd;lasd");
                mCatlist.get(lastCheckedPos).setSelected(false);
                lastCheckedPos = position;
                model.setSelected(true);

                notifyDataSetChanged();
            });

        }

        @Override
        public int getItemCount() {
            return mCatlist.size();
        }


    }

    @OnClick({R.id.txt_cuntinue})
    public void onClick(View view) {
        if (view.getId() == R.id.txt_cuntinue) {
            sendCategory();
        }
    }
}