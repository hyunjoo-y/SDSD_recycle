package com.example.sdsd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class PopUpActivity extends Activity {

    TextView txtText;
    private QuizAdapter quizAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quizAdapter = new QuizAdapter();

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        //UI 객체생성
        txtText = (TextView) findViewById(R.id.txtText);

        int r_num = 0;
        //데이터 가져오기
        try{
            Bundle intent = getIntent().getExtras();
            r_num = intent.getInt("r_num");
            txtText.setText(quizAdapter.explain[r_num]);
        }catch (Exception ex){
            r_num = -1;
        }

    }

    //확인 버튼 클릭
    public void mOnClose(View v) {
        //데이터 전달하기
       // Intent intent = new Intent();
      //  intent.putExtra("result", "Close Popup");
        //setResult(RESULT_OK, intent);
        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}