package com.recovery.scene.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.recovery.netwrok.model.PurchaseInfo;
import com.recovery.scene.R;
import com.recovery.scene.widget.EndlessRecyclerOnScrollListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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

    public static final String TIME_START = "timeStart";
    public static final String TIME_END = "timeEnd";
    public static final String VENDER_NAME = "venderName";
    @BindView(R.id.tv_query)
    Button tvQuery;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainContet(R.layout.activity_collect_history);
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

        Intent intent = new Intent(this, RecordListsActivity.class);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(TIME_START,calendartimeStart.getTimeInMillis());
        hashMap.put(TIME_END,calendartimeEnd.getTimeInMillis());
        hashMap.put(VENDER_NAME,etVendor.getText().toString());
        intent.putExtra("map",hashMap);
        startActivity(intent);
    }


}
