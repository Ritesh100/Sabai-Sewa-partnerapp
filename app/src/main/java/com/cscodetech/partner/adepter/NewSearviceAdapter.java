package com.cscodetech.partner.adepter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.partner.R;
import com.cscodetech.partner.activity.ServiceDetailActivity;
import com.cscodetech.partner.model.OrderDataItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewSearviceAdapter extends RecyclerView.Adapter<NewSearviceAdapter.NewsHolder> {
    private final Context context;
    private final List<OrderDataItem> itemList;

    public NewSearviceAdapter(Context context, List<OrderDataItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_newservice_item, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        OrderDataItem item = itemList.get(position);
        holder.txtTitle.setText("" + item.getCustomerName());
        holder.txtCreadit.setText("Credit " + item.getRCredit());
        holder.txtAddress.setText("" + item.getCustomerAddress());
        holder.txtDate.setText("" + item.getOrderDateslot());
        holder.txtTime.setText("" + item.getOrderTime());
        holder.txtOid.setText("" + item.getOrderId());
        holder.lvlClick.setOnClickListener(v ->
                context.startActivity(new Intent(context, ServiceDetailActivity.class)
        .putExtra("myclass",item)
        .putParcelableArrayListExtra("addon",item.getAddOnData())
        .putParcelableArrayListExtra("itemlist",item.getOrderProductData())));

    }

    @Override
    public int getItemCount() {
        return itemList.size();

    }

    public class NewsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.txt_creadit)
        TextView txtCreadit;
        @BindView(R.id.txt_address)
        TextView txtAddress;
        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.txt_oid)
        TextView txtOid;
        @BindView(R.id.lvl_click)
        LinearLayout lvlClick;

        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}