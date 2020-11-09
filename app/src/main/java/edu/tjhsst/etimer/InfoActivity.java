package edu.tjhsst.etimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    private TextView info;
    private Button back;
    private double time;
    private String scramble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        time = getIntent().getExtras().getDouble("time");
        scramble = getIntent().getExtras().getString("scramble");

        info = (TextView)findViewById(R.id.info);
        back = (Button)findViewById(R.id.back);

        info.setText("Scramble: " + scramble + "\n\nTime: " + time);
        if(time==-1.0)info.setText("Scramble: " + scramble + "\n\nTime: N/A");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
