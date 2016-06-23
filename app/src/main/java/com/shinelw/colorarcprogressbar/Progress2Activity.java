package com.shinelw.colorarcprogressbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Progress2Activity extends AppCompatActivity {

    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.bar1)
    ColorArcProgressBar bar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress2);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button1, R.id.bar1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                bar1.setCurrentValues(80);
                break;
            case R.id.bar1:
                break;
        }
    }
}
