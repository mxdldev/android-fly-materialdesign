package com.yesway.channel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yesway.channel.demochannel.ChannelActivity;
import com.yesway.channel.demodrag.DragActivity;

@Route(path = "/channel/manager/main")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_channel_manager);

        Button mBtnDrag = (Button) findViewById(R.id.btn_drag);
        Button mBtnChannel = (Button) findViewById(R.id.btn_channl);
        mBtnDrag.setOnClickListener(this);
        mBtnChannel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_drag){
            startActivity(new Intent(MainActivity.this, DragActivity.class));
        }else if(v.getId() == R.id.btn_channl){
            startActivity(new Intent(MainActivity.this, ChannelActivity.class));
        }
    }
}
