package com.recovery.scene.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.recovery.netwrok.apiservice.ApiService;
import com.recovery.netwrok.model.PurchaseInfo;
import com.recovery.netwrok.subscriber.ApiSubscriber;
import com.recovery.scene.R;
import com.recovery.scene.widget.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hotjavi.lei.com.base_module.activity.BaseSetMainActivity;
import hotjavi.lei.com.base_module.present.BaseObjectPresent;

/**
 * Created by tom on 2017/4/23.
 */

public class RecordListsActivity extends BaseSetMainActivity {

    @BindView(R.id.rv_collect_history)
    RecyclerView rvCollectHistory;
    private HisToryAdapter hisToryAdapter;
    private Present mPresent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainContet(R.layout.activity_record_list);
        setTitle("查询结果");
        ButterKnife.bind(this);
        mPresent = new Present(this);
        hisToryAdapter = new HisToryAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvCollectHistory.setLayoutManager(layoutManager);
        rvCollectHistory.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                mPresent.loadMore();
            }
        });
        rvCollectHistory.setAdapter(hisToryAdapter);
        mPresent.iniData();

    }

    private static class Present extends BaseObjectPresent<RecordListsActivity> {
        private long timeStart;
        private long timeEnd;
        private String etName;
        private int page = 0;

        public Present(RecordListsActivity recordListsActivity) {
            super(recordListsActivity);
        }

        public void loadMore() {
            if (!isAvaiable()) return;
            queryHistory();
        }

        public void iniData() {
            Intent i = getRefObj().getIntent();
            HashMap<String, Object> map = (HashMap<String, Object>) i.getSerializableExtra("map");
            timeStart = (long) map.get(CollectHistoryActivity.TIME_START);
            timeEnd = (long) map.get(CollectHistoryActivity.TIME_END);
            etName = (String) map.get(CollectHistoryActivity.VENDER_NAME);
            queryHistory();

        }

        public void queryHistory() {
            ApiService.purchaseHistory(timeStart, timeEnd, etName, page).subscribe(new ApiSubscriber<PurchaseInfo>() {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    if (!isAvaiable()) return;
                    getRefObj().hideLoading();
                }

                @Override
                public void onNext(PurchaseInfo purchaseInfo) {
                    super.onNext(purchaseInfo);
                    if (!isAvaiable()) return;
                    getRefObj().hideLoading();
                    if (purchaseInfo==null)return;
                    page = purchaseInfo.getCurrentPage() + 1;
                    getRefObj().listBeanArrayList.addAll(purchaseInfo.getList());
                    getRefObj().hisToryAdapter.notifyDataSetChanged();
                }
            });
            if (page == 0) {
                getRefObj().showLoading("查询历史记录");
            }
        }

    }


    public class HisViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_value_product)
        TextView tvValueProduct;
        @BindView(R.id.tv_value_num)
        TextView tvValueNum;

        public HisViewHolder(View itemView) {
            super(itemView);
            Unbinder buinder = ButterKnife.bind(this, itemView);
        }

        public void set(PurchaseInfo.ListBean bean) {
            tvName.setText(bean.getVendor());
            tvTime.setText(bean.getTime());
            tvValueNum.setText(bean.getQuantity() + bean.getUnit());
            tvValueProduct.setText(bean.getProduct());
        }

    }

    private ArrayList<PurchaseInfo.ListBean> listBeanArrayList=new ArrayList<>();

    private class HisToryAdapter extends RecyclerView.Adapter<HisViewHolder> {


        @Override
        public HisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(RecordListsActivity.this).inflate(R.layout.item_collect_his, parent, false);
            return new HisViewHolder(view);
        }

        @Override
        public void onBindViewHolder(HisViewHolder holder, int position) {
            holder.set(listBeanArrayList.get(position));
        }

        @Override
        public int getItemCount() {
            if (listBeanArrayList == null)
                return 0;
            return listBeanArrayList.size();
        }
    }


}
