package com.example.sdsd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RecycleInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_info);

        findViewById(R.id.general).setOnClickListener(onClickListener);
        findViewById(R.id.plastic).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(RecycleInfoActivity.this, NoPageActivity.class);
            startActivity(intent);
        }
    };
}
