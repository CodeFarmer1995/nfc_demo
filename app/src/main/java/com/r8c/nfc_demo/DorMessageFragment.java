package com.r8c.nfc_demo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 管理员 发布 界面
 */

public  class DorMessageFragment extends android.support.v4.app.Fragment {
    private Button btFore,btNotice;
    private TextView tvtime;
    private Button btFa;
    private Spinner spyuan;
    private LinearLayout ll,ll2;
    private EditText etname,etLoc,etIn;


    DatePickerDialog.OnDateSetListener onDateSetListener;
    private int TAG;
    int mYear, mMonth, mDay;;
    final int DATE_DIALOG = 1;

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dormessage_fragment_layout,container,false);
        btFore=view.findViewById(R.id.btferen);
        btNotice=view.findViewById(R.id.btnotice);
        tvtime=view.findViewById(R.id.tvtime);
        btFa=view.findViewById(R.id.btFa);
        ll=view.findViewById(R.id.ll);
        ll2=view.findViewById(R.id.ll2);
        spyuan=view.findViewById(R.id.spyuan);
        etname=view.findViewById(R.id.etname);
        etIn=view.findViewById(R.id.etIn);
        etLoc=view.findViewById(R.id.etLoc);
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        btFa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (TAG){
                    case 0:
                        Sign sign=new Sign();
                        sign.setName(etname.getText().toString());
                        sign.setReason((String) spyuan.getSelectedItem());
                        sign.setTime(tvtime.getText().toString());
                        sign.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                             if(e==null){
                                 Toast.makeText(getActivity(),"缺勤人员发布成功", Toast.LENGTH_LONG).show();
                             }
                            }
                        });



                        break;
                    case 1:
                        Meeting meeting=new Meeting();
                        meeting.setContent(etIn.getText().toString());
                        meeting.setLoc(etLoc.getText().toString());
                        meeting.setTime(tvtime.getText().toString());
                        meeting.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                            if(e==null){
                                Toast.makeText(getActivity(),"会议公告发布成功", Toast.LENGTH_LONG).show();
                            }
                            }
                        });

                        break;

                }
            }
        });
        //发布缺席
        btFore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 tvtime.setVisibility(View.VISIBLE);
                 btFa.setVisibility(View.VISIBLE);
                 ll.setVisibility(View.VISIBLE);
                 ll2.setVisibility(View.GONE);
                 TAG=0;
            }
        });

        //发布会议公告
        btNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvtime.setVisibility(View.VISIBLE);
                btFa.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);

                TAG=1;
            }
        });


        tvtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(),onDateSetListener, mYear, mMonth, mDay).show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                String days;
                if (mMonth + 1 < 10) {
                    if (mDay < 10) {
                        days = new StringBuffer().append(mYear).append("年").append("0").
                                append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                    } else {
                        days = new StringBuffer().append(mYear).append("年").append("0").
                                append(mMonth + 1).append("月").append(mDay).append("日").toString();
                    }

                } else {
                    if (mDay < 10) {
                        days = new StringBuffer().append(mYear).append("年").
                                append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                    } else {
                        days = new StringBuffer().append(mYear).append("年").
                                append(mMonth + 1).append("月").append(mDay).append("日").toString();
                    }

                }
                tvtime.setText(days);
            }
        };

        return view;

    }


}
