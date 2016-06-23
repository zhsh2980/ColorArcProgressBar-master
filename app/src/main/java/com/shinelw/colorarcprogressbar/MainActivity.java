package com.shinelw.colorarcprogressbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_progress1)
    Button btnProgress1;
    @BindView(R.id.btn_progress2)
    Button btnProgress2;
    @BindView(R.id.btn_progress3)
    Button btnProgress3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_progress1, R.id.btn_progress2, R.id.btn_progress3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_progress1:
                Intent intent = new Intent(MainActivity.this, Progress1Activity.class);
                startActivity(intent);
                break;
            case R.id.btn_progress2:
                Intent intent2 = new Intent(MainActivity.this, Progress2Activity.class);
                startActivity(intent2);
                break;
            case R.id.btn_progress3:
                Intent intent3 = new Intent(MainActivity.this, Progress3Activity.class);
                startActivity(intent3);
                break;
        }
    }
}
