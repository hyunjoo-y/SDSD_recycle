package com.example.sdsd;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 101;
    private static final String TAG = "MainActivity";
    private QuizAdapter adapter = new QuizAdapter();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private DrawerLayout mDrawerLayout;
    private Random r = new Random();
    int r_num = 0;

    FirebaseUser user;
    Intent quizIntent, goPopup;
    TextView address_view, txtResult, userName, userEmail, userPoint, randomQuiz;
    EditText answer;
    String str = "", gu = "", dong = "", name = "", email = "", r_quiz = "" , s = "";
    Button logButton;
    Double point, q_turn;
    View header;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        address_view = findViewById(R.id.textLocation);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        quizIntent = new Intent(this, QuizActivity.class);
        goPopup = new Intent(this, PopUpActivity.class);

        txtResult = (TextView) findViewById(R.id.txtText);
        randomQuiz = (TextView) findViewById(R.id.QuizEditText);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button bt = (Button) findViewById(R.id.inputButton);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        logButton = (Button) findViewById(R.id.mainpageLogin);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        userName = (TextView) header.findViewById(R.id.userName);
        userEmail = (TextView) header.findViewById(R.id.userEmail);
        userPoint = (TextView) header.findViewById(R.id.userPoint);

        user = FirebaseAuth.getInstance().getCurrentUser();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = item.getItemId();
                String title = item.getTitle().toString();

                if (id == R.id.store) {
                    myStartActivity(NoPageActivity.class);
                } else if (id == R.id.comunity || id == R.id.event || id == R.id.qna) {
                    myStartActivity(NoPageActivity.class);
                }
                return true;
            }
        });



        if (user == null) {
            logButton.setText("로그인");
            randomQuiz.setText("로그인 후 이용 가능합니다.");
            findViewById(R.id.answerText).setVisibility(View.GONE);
            findViewById(R.id.inputButton).setVisibility(View.GONE);
            //myStartActivity(MainActivity.class);
        } else {
            logButton.setText("로그아웃");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            if (document.exists()) {

                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                name = document.getString("name");
                                userName.setText(name);
                                email = user.getEmail();
                                userEmail.setText(email);
                                point = document.getDouble("point");
                                userPoint.setText(point.toString());
                                q_turn = document.getDouble("quiz");
                                r_num = r.nextInt(adapter.answer.length);
                                quizIntent.putExtra("r_num",r_num);
                                quizIntent.putExtra("turn",q_turn);
                                showQuiz(q_turn);
                                bt.setOnClickListener(new Button.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        s = ((EditText)findViewById(R.id.answerText)).getText().toString();
                                        checkQuiz(s,r_num,q_turn);
                                    }
                                });
                            } else {
                                Log.d(TAG, "No such document");
                                myStartActivity(MemberActivity.class);
                            }

                        }

                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }

                }
            });

        }

        //showQuiz(turn);
        address_view.setText(preferences.getString("address", "위치를 설정해주세요."));
        gu = preferences.getString("gu", "");
        dong = preferences.getString("dong", "");

        findViewById(R.id.mainpageLogin).setOnClickListener(onClickListener);
        findViewById(R.id.SearchButton).setOnClickListener(onClickListener);
        findViewById(R.id.locButton).setOnClickListener(onClickListener);
        findViewById(R.id.carTimeButton).setOnClickListener(onClickListener);
        findViewById(R.id.infoButton).setOnClickListener(onClickListener);


        findViewById(R.id.menuButton).setOnClickListener(onClickListener);

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mainpageLogin:
                    if (logButton.getText() == "로그인") {
                        myStartActivity(LoginActivity.class);
                    } else {
                        FirebaseAuth.getInstance().signOut();
                        myStartActivity(MainActivity.class);
                        logButton.setText("로그인");
                        finish();
                    }
                    break;
                case R.id.SearchButton:
                    myStartActivity(SearchActivity.class);
                    break;
                case R.id.locButton:
                    Intent intent = new Intent(MainActivity.this, LocActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_MENU);
                    break;
                case R.id.carTimeButton:
                    intent = new Intent(MainActivity.this, AlarmActivity.class);
                    intent.putExtra("guText", gu);
                    intent.putExtra("dongText", dong);
                    startActivity(intent);
                    break;
                case R.id.infoButton:
                    myStartActivity(RecycleInfoActivity.class);
                    break;
                case R.id.menuButton:
                    if (logButton.getText() == "로그인") {
                        myStartActivity(SignUpActivity.class);
                    } else {
                        mDrawerLayout.openDrawer(GravityCompat.START);
                    }
                    break;
            }
        }
    };




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MENU) {
            Toast.makeText(getApplicationContext(), "위치를 설정하지 못했습니다.", Toast.LENGTH_LONG).show();
        }
        if (resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(), "위치를 설정했습니다.", Toast.LENGTH_LONG).show();
            int count = 0;
            String address = data.getStringExtra("addText");
            for (int i = 0; i < address.length(); i++) {
                char ch = address.charAt(i);
                if (Character.isWhitespace(ch) && count == 0) {
                    address = address.substring(i + 1);
                    count++;
                } else if (Character.isWhitespace(ch) && count == 1) {
                    count++;
                } else if (count == 2) {
                    if (!Character.isWhitespace(ch)) {
                        gu += ch;
                    } else {
                        count++;
                    }
                } else if (count == 3) {
                    if (!Character.isWhitespace(ch)) {
                        dong += ch;
                    } else {
                        break;
                    }
                }
            }
            editor.putString("address", address);
            editor.putString("gu", gu);
            editor.putString("dong", dong);
            editor.apply();
            address_view.setText(address);
        }
    }



    private void showQuiz(Double turn) {

        if (turn == 1.0) {
            r_num = r.nextInt(adapter.answer.length);
            r_quiz = adapter.quiz[r_num];
            randomQuiz.setText(r_quiz);
            goPopup.putExtra("r_num", r_num);
            quizIntent.putExtra("r_num", r_num);
        }else{
        randomQuiz.setText("이미 참여하셨습니다.\n내일 다시 도전해주세요!");
            findViewById(R.id.answerText).setVisibility(View.GONE);
            findViewById(R.id.inputButton).setVisibility(View.GONE);
    }

}
    private void checkQuiz(String s, int check, Double turn){
        if(turn == 1.0){
            if(s.equalsIgnoreCase(adapter.answer[check])){
                str = "정답입니다!\n 내일도 참여해주세요!";
                turn = 2.0;
                findViewById(R.id.answerText).setVisibility(View.GONE);
                findViewById(R.id.inputButton).setVisibility(View.GONE);
                randomQuiz.setText(str);
                pointUpdate();
            }else{
                str = "틀렸습니다.\n 다시 도전해보세요!";
                startToast(str);
            }
        }
        else{
            str = "이미 참여하셨습니다.\n 내일 다시 도전해주세요!";
            randomQuiz.setText(str);
            findViewById(R.id.answerText).setVisibility(View.GONE);
            findViewById(R.id.inputButton).setVisibility(View.GONE);
        }



    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this,c);
        startActivity(intent);
    }

    private void pointUpdate(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.update("quiz",2.0);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document != null){
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            name = document.getString("name");
                            point = document.getDouble("point");
                            point += 10;
                            docRef.update("point", point);
                            point = document.getDouble("point");
                            point = document.getDouble("quiz");
                            userPoint.setText(point.toString());
                        } else {
                            Log.d(TAG, "No such document");
                            myStartActivity(MemberActivity.class);
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawers();
        }else{
            super.onBackPressed();
        }

    }


}