package com.example.sdsd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;


public class QuizActivity extends AppCompatActivity {
    private QuizAdapter adapter = new QuizAdapter();;
    int check = 0;
    Double turn;
    Intent goMain;
    Bundle getMain;
    String str;
    TextView randomQuiz;
    Random r = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goMain = new Intent(this,MainActivity.class);
        randomQuiz = (TextView) findViewById(R.id.QuizEditText);
       try {
            getMain = getIntent().getExtras();
            check = getMain.getInt("r_num");
            turn = getMain.getDouble("turn");
        }catch (Exception ex){
            check = 1;
            turn = 1.0;
        }

        showQuiz(turn);
    }


    Button.OnClickListener onClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            Button btn = (Button) findViewById(v.getId());
            String s = btn.getText().toString();
            checkQuiz(s,check);
            }

};

    private void showQuiz(Double turn) {

        if (turn == 1.0) {
            String r_quiz = adapter.quiz[check];
            randomQuiz.setText(r_quiz);
        }else{
            randomQuiz.setText(turn + "\n이미 참여하셨습니다.\n내일 다시 도전해주세요!");
        }
    }

    private void checkQuiz(String s, int check){

        if(turn == 1.0){
            if(s.equalsIgnoreCase(adapter.answer[check])){
                str = "정답입니다!\n 내일도 참여해주세요!";
                turn = 2.0;
            }else{
                str = "틀렸습니다.\n 다시 도전해보세요!";
            }
            goMain.putExtra("turn",turn);
            goMain.putExtra("answer",str);
            }
        else{
            String str = "이미 참여하셨습니다.\n 내일 다시 도전해주세요!";
            goMain.putExtra("answer",str);
        }

        }




    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}