package com.recovery.scene.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.recovery.netwrok.model.PurchaseInfo;
import com.recovery.scene.R;
import com.recovery.scene.widget.EndlessRecyclerOnScrollListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hotjavi.lei.com.base_module.activity.BaseSetMainActivity;
import hotjavi.lei.com.base_module.present.BaseObjectPresent;

/**
 * Created by tom on 2017/4/21.
 */

public class CollectHistoryActivity extends BaseSetMainActivity implements View.OnClickListener {

    @BindView(R.id.tv_query)
    Button tvQuery;
    @BindView(R.id.rv_collect_history)
    RecyclerView rvCollectHistory;
    @BindView(R.id.et_vendor)
    EditText etVendor;
    private long timeEnd;
    private long timeStart;
    private View rlTimeStart;
    private View rlTimeEnd;
    private Calendar calendartimeEnd;
    private Calendar calendartimeStart;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private TextView tvTimeStart;
    private TextView tvTimeEnd;
    private HisToryAdapter hisToryAdapter;
    private Present mPresent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainContet(R.layout.activity_collect_history);
        mPresent=new Present(this);
        ButterKnife.bind(this);
        getCustomToolBar().setTitle("采收记录");
        timeEnd = System.currentTimeMillis();
        timeStart = timeEnd - 30L * 24 * 3600 * 1000L;
        tvTimeStart = (TextView) findViewById(R.id.tv_time_start);
        tvTimeEnd = (TextView) findViewById(R.id.tv_time_end);
        tvTimeStart.setOnClickListener(this);
        tvTimeEnd.setOnClickListener(this);
        tvTimeEnd.setText(simpleDateFormat.format(new Date(timeEnd)));
        tvTimeStart.setText(simpleDateFormat.format(new Date(timeStart)));
        calendartimeEnd = Calendar.getInstance();
        calendartimeEnd.setTime(new Date(timeEnd));
        calendartimeStart = Calendar.getInstance();
        calendartimeStart.setTime(new Date(timeStart));
        hisToryAdapter=new HisToryAdapter();
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

    private static class Present extends BaseObjectPresent<CollectHistoryActivity> {
        public Present(CollectHistoryActivity collectHistoryActivity) {
            super(collectHistoryActivity);
        }
        public void loadMore(){
            if (!isAvaiable())return;
            for (int i=0;i<10;i++){
                PurchaseInfo.ListBean listBean=new PurchaseInfo.ListBean();
                listBean.setProduct("鸡蛋"+i+"号");
                listBean.setQuantity(15+i);
                listBean.setTime("2015-11-11 11:11:11");
                listBean.setUnit("斤");
                listBean.setVendor("老王家");
                getRefObj().listBeanArrayList.add(listBean);
            }
            getRefObj().hisToryAdapter.notifyDataSetChanged();
        }

        public void iniData(){
            getRefObj().listBeanArrayList=new ArrayList<>();
            for (int i=0;i<10;i++){
                PurchaseInfo.ListBean listBean=new PurchaseInfo.ListBean();
                listBean.setProduct("鸡蛋"+i+"号");
                listBean.setQuantity(15+i);
                listBean.setTime("2015-11-11 11:11:11");
                listBean.setUnit("斤");
                listBean.setVendor("老王家");
                getRefObj().listBeanArrayList.add(listBean);
            }
            getRefObj().hisToryAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_time_start:
                showDateCheck(calendartimeStart, tvTimeStart);
                break;
            case R.id.tv_time_end:
                showDateCheck(calendartimeEnd, tvTimeEnd);

                break;
        }
    }

    private void showDateCheck(final Calendar calendar, final TextView tv) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                tv.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.tv_query)
    public void onViewClicked() {
    }

    public  class HisViewHolder extends RecyclerView.ViewHolder {
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
            Unbinder buinder = ButterKnife.bind(this,itemView);
        }

        public void set(PurchaseInfo.ListBean bean){
            tvName.setText(bean.getVendor());
            tvTime.setText(bean.getTime());
            tvValueNum.setText(bean.getQuantity()+bean.getUnit());
            tvValueProduct.setText(bean.getProduct());
        }

    }

    private ArrayList<PurchaseInfo.ListBean> listBeanArrayList;

    private class HisToryAdapter extends RecyclerView.Adapter<HisViewHolder> {


        @Override
        public HisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CollectHistoryActivity.this).inflate(R.layout.item_collect_his, parent, false);
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
