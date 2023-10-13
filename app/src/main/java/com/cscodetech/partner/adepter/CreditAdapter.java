package com.cscodetech.partner.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.partner.R;
import com.cscodetech.partner.model.CreditList;
import com.cscodetech.partner.utils.SessionManager;

import java.util.List;

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.MyViewHolder> {
    private final List<CreditList> mCatlist;
    private final RecyclerTouchListener listener;
    SessionManager sessionManager;
    int lastCheckedPos = 0;

    public interface RecyclerTouchListener {
        void onClickAddonsItem(String titel, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtAmount;
        public TextView txtCreadit;
        public LinearLayout imgSelect;
        public LinearLayout lvlclick;

        public MyViewHolder(View view) {
            super(view);
            txtAmount = view.findViewById(R.id.txt_amount);
            txtCreadit = view.findViewById(R.id.txt_creadit);
            imgSelect = view.findViewById(R.id.img_select);
            lvlclick = view.findViewById(R.id.lvl_itemclick);
        }
    }

    public CreditAdapter(Context mContext, List<CreditList> mCatlist, final RecyclerTouchListener listener) {

        this.mCatlist = mCatlist;
        this.listener = listener;
        sessionManager = new SessionManager(mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_addone, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        final CreditList model = mCatlist.get(position);
        holder.txtCreadit.setText("" + model.getCreditAmt());
        holder.txtAmount.setText(sessionManager.getStringData(SessionManager.currency) + model.getAmt() + "");
        if (model.isSelected()) {
            holder.imgSelect.setVisibility(View.VISIBLE);
        } else {
            holder.imgSelect.setVisibility(View.GONE);
        }

        holder.lvlclick.setOnClickListener(v -> {

            mCatlist.get(lastCheckedPos).setSelected(false);
            lastCheckedPos = position;
            model.setSelected(true);
            notifyDataSetChanged();
            listener.onClickAddonsItem("category.getCatname()", lastCheckedPos);


        });
    }

    @Override
    public int getItemCount() {
        return mCatlist.size();
    }

    public CreditList getSelectItem() {
        return mCatlist.get(lastCheckedPos);
    }
}