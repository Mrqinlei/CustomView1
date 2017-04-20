package com.qinlei.customswitch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SwitchButton switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchButton = (SwitchButton) findViewById(R.id.sw);
        switchButton.setSwitchCallBack(new SwitchButton.switchCallBack() {
            @Override
            public void callback(boolean isLeft) {
                if (isLeft) {
                    Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
