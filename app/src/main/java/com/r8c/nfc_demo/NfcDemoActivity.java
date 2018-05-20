package com.r8c.nfc_demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class NfcDemoActivity extends Activity implements OnClickListener {
    // 文本控件
    private TextView promt = null;
    // 读、写、删按钮控件
    private Button readBtn, writeBtn, deleteBtn;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private Tag tagFromIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_demo);
        setupViews();
        initNFC();
    }

    private void initNFC() {
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[]{ndef,};
        mTechLists = new String[][]{{IsoDep.class.getName()}, {NfcA.class.getName()},};
        if (mAdapter == null) {
            Toast.makeText(this, "设备不支持NFC！", Toast.LENGTH_LONG).show();
           // finish();
            return;
        }
        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "请在系统设置中先启用NFC功能！", Toast.LENGTH_LONG).show();
            //finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 开始监听NFC设备是否连接，如果连接就发pi意图
        if (mAdapter != null) {
            mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 停止监听NFC设备是否连接
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            //识别intent后，这里处理你要intent的操作
            processIntent(intent);
        }

    }

    @Override
    public void onClick(View v) {

        // 点击读按钮后
        if (v.getId() == R.id.read_btn) {
            try {
                String content = read(tagFromIntent);
                if (content != null && !content.equals("")) {
                    promt.setText(promt.getText() + "nfc标签内容：\n" + content
                            + "\n");
                } else {
                    promt.setText(promt.getText() + "nfc标签内容：\n" + "内容为空\n");
                }
            } catch (IOException e) {
                promt.setText(promt.getText() + "错误:" + e.getMessage() + "\n");
                Log.e("myonclick", "读取nfc异常", e);
            } catch (FormatException e) {
                promt.setText(promt.getText() + "错误:" + e.getMessage() + "\n");
                Log.e("myonclick", "读取nfc异常", e);
            }
            // 点击写后写入
        } else if (v.getId() == R.id.write_btn) {
            try {
                write(tagFromIntent);
            } catch (IOException e) {
                promt.setText(promt.getText() + "错误:" + e.getMessage() + "\n");
                Log.e("myonclick", "写nfc异常", e);
            } catch (FormatException e) {
                promt.setText(promt.getText() + "错误:" + e.getMessage() + "\n");
                Log.e("myonclick", "写nfc异常", e);
            }
        } else if (v.getId() == R.id.delete_btn) {
            try {
                delete(tagFromIntent);
            } catch (IOException e) {
                promt.setText(promt.getText() + "错误:" + e.getMessage() + "\n");
                Log.e("myonclick", "删除nfc异常", e);
            } catch (FormatException e) {
                promt.setText(promt.getText() + "错误:" + e.getMessage() + "\n");
                Log.e("myonclick", "删除nfc异常", e);
            }
        }
    }

    private void setupViews() {
        // 控件的绑定
        promt = (TextView) findViewById(R.id.promt);
        readBtn = (Button) findViewById(R.id.read_btn);
        writeBtn = (Button) findViewById(R.id.write_btn);
        deleteBtn = (Button) findViewById(R.id.delete_btn);
        // 给文本控件赋值初始文本
        promt.setText("等待RFID标签");
        // 监听读、写、删按钮控件
        readBtn.setOnClickListener(this);
        writeBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    private void processIntent(Intent intent) {
        // 取出封装在intent中的TAG
        tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String readLog = "";
        MifareClassic mfc = MifareClassic.get(tagFromIntent);
        try {
            mfc.connect();
            int type = mfc.getType();
            if (type != MifareClassic.TYPE_CLASSIC) {
                return;//未知卡片
            }

            byte[] keys = getKey(mSector);  //计算卡片秘钥
            boolean cardVerify = mfc.authenticateSectorWithKeyA(mSector, keys);
            if (cardVerify) {
                readLog += "卡号：" + getTagId(intent) + "\n";
            }
            byte[] sectorData = mfc.readBlock(mSector * 4 + mBlock);
            mfc.close();

            String mData = new String(sectorData, "US-ASCII");
            readLog += "票号：" + mData + "\n";
        } catch (Exception e) {
            Toast.makeText(this, "与卡的连接丢失，或设备不支持", Toast.LENGTH_SHORT).show();
        }
        promt.setText(readLog);
        promt.setTextColor(Color.BLUE);
    }

    //获取IC卡卡号
    private String getTagId(Intent intent) {
        String id = null;
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            id = byteToArray(tag.getId());
        }
        return id;
    }

    //字节数组转字符串
    public static String byteToArray(byte[] data) {
        String result = "";
        for (int i = data.length - 1; i >= 0; i--) {
            result += Integer.toHexString((data[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3);
        }
        //16进制转10进制输出
        BigInteger srch = new BigInteger(result, 16);
        return srch.toString();
    }


    private int mSector = 1;
    private int mBlock = 1;


    public static byte[] hexString2Byte(String str) {
        int len = str.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }

        return data;
    }

    public static String byte2HexString(byte[] bytes) {
        String data = "";
        if (bytes != null) {
            for (Byte b : bytes) {
                data += String.format("%02X", b.intValue() & 0xFF);
            }
        }
        return data;
    }

    private String _companyKey = "mima";

    private byte[] getKey(int sectorNo) {
       return _companyKey.getBytes();
    }

    /**
     * 右补位，左对齐
     *
     * @param oriStr 原字符串
     * @param len    目标字符串长度
     * @param alexin 补位字符
     * @return 目标字符串
     */
    public static String padRight(String oriStr, int len, String alexin) {
        String str = "";
        int strlen = oriStr.length();
        if (strlen < len) {
            for (int i = 0; i < len - strlen; i++) {
                str = str + alexin;
            }
        }
        str = oriStr + str;
        return str;
    }

    // 读取方法
    private String read(Tag tag) throws IOException, FormatException {
        if (tag != null) {
            //解析Tag获取到NDEF实例
            Ndef ndef = Ndef.get(tag);
            //打开连接
            ndef.connect();
            //获取NDEF消息
            NdefMessage message = ndef.getNdefMessage();
            //将消息转换成字节数组
            byte[] data = message.toByteArray();
            //将字节数组转换成字符串
            String str = new String(data, Charset.forName("UTF-8"));
            //关闭连接
            ndef.close();
            return str;
        } else {
            Toast.makeText(NfcDemoActivity.this, "设备与nfc卡连接断开，请重新连接...",
                    Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    // 写入方法
    private void write(Tag tag) throws IOException, FormatException {
        if (tag != null) {
            //新建NdefRecord数组，本例中数组只有一个元素
            NdefRecord[] records = {createRecord()};
            //新建一个NdefMessage实例
            NdefMessage message = new NdefMessage(records);
            // 解析TAG获取到NDEF实例
            Ndef ndef = Ndef.get(tag);
            // 打开连接
            ndef.connect();
            // 写入NDEF信息
            ndef.writeNdefMessage(message);
            // 关闭连接
            ndef.close();
            promt.setText(promt.getText());
            Toast.makeText(NfcDemoActivity.this, "数据写入成功",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(NfcDemoActivity.this, "设备与nfc卡连接断开，请重新连接...",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // 签到方法
    private void delete(Tag tag) throws IOException, FormatException {
        String pro = promt.getText().toString();
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        Sign sign=new Sign();
        sign.setTime(dateNowStr);
        sign.setName(pro);
        sign.setReason("签到成功");
        sign.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

                if(e==null){
                    Toast.makeText(NfcDemoActivity.this, "签到成功",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //返回一个NdefRecord实例
    private NdefRecord createRecord() throws UnsupportedEncodingException {
        //组装字符串，准备好你要写入的信息
        String msg = "李小白";
        //将字符串转换成字节数组
        byte[] textBytes = msg.getBytes();
        //将字节数组封装到一个NdefRecord实例中去
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                "".getBytes(), new byte[]{}, textBytes);
        return textRecord;
    }

    private MediaPlayer ring() throws Exception, IOException {
        // TODO Auto-generated method stub
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer player = new MediaPlayer();
        player.setDataSource(this, alert);
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
            player.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            player.setLooping(false);
            player.prepare();
            player.start();
        }
        return player;
    }


}
