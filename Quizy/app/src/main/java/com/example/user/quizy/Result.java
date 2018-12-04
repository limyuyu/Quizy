package com.example.user.quizy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    private Button mRestartButton;
    private TextView mScoreLabel;
    private TextView mResultLabel;
    private TextView mFalseLabel;
    private TextView mCheatLabel;

    public static Intent newIntent(Context packageContext){

        Intent i = new Intent(packageContext, Result.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mResultLabel = (TextView) findViewById(R.id.result_label);
        mScoreLabel = (TextView) findViewById(R.id.score_label);
        mFalseLabel = (TextView) findViewById(R.id.false_label);
        mCheatLabel = (TextView) findViewById(R.id.cheat_label);

        int score = getIntent().getIntExtra("RIGHT_ANSWER_COUNT", 0);
        switch (score){
            case 0 : mResultLabel.setText("Try Again Next Time!");
                break;
            case 1 : mResultLabel.setText("At Least You Tried !");
                break;
            case 2 : mResultLabel.setText("You Can Do It !");
                break;
            case 3 : mResultLabel.setText("Not Bad !");
                break;
            case 4 : mResultLabel.setText("Almost !");
                break;
            case 5 : mResultLabel.setText("Perfect !");
                break;
        }

        int falseScore = getIntent().getIntExtra("FALSE_ANSWER_COUNT", 0);
        int cheatScore = getIntent().getIntExtra("CHEAT_ANSWER_COUNT", 0);

        mScoreLabel.setText("Correct : " + score + " / 5");
        mFalseLabel.setText("Wrong   : " + falseScore + " / 5");
        mCheatLabel.setText("Cheat   : " + cheatScore + " / 5");

        mRestartButton = (Button) findViewById(R.id.restart_button);

        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Result.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
