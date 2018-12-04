package com.example.user.quizy;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_CORRECT = "CorrectIndex";
    private static final String KEY_FALSE = "FalseIndex";
    private static final String KEY_CHEAT = "CheatIndex";
    private static final String KEY_CLICKED = "Clicked";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;

    private Questions[] mQuestionBank = new Questions[]{
            new Questions(R.string.first_question, false),
            new Questions(R.string.second_question, false),
            new Questions(R.string.third_question, false),
            new Questions(R.string.forth_question, false),
            new Questions(R.string.fifth_question, true),
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater;
    private int mRightAnswerCount = 0;
    private int mCheatAnswerCount = 0;
    private int mFalseAnswerCount = 0;
    private boolean mIsClicked;

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (mIsCheater){
            mCheatAnswerCount += 1;
            messageResId = R.string.judgement_toast;
        }
        else{
            if (userPressedTrue == answerIsTrue) {
                mRightAnswerCount += 1;
                mIsClicked = true;
                messageResId = R.string.correct_toast;
            } else {
                mFalseAnswerCount += 1;
                mIsClicked = true;
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTrueButton.setClickable(false);
                mFalseButton.setClickable(false);
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFalseButton.setClickable(false);
                mTrueButton.setClickable(false);
                checkAnswer(false);
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                if (mCurrentIndex == 0){
                    Intent i = new Intent(MainActivity.this, Result.class);
                    i.putExtra("RIGHT_ANSWER_COUNT", mRightAnswerCount);
                    i.putExtra("FALSE_ANSWER_COUNT", mFalseAnswerCount);
                    i.putExtra("CHEAT_ANSWER_COUNT", mCheatAnswerCount);
                    startActivity(i);
                }
                mIsCheater = false;
                mTrueButton.setClickable(true);
                mFalseButton.setClickable(true);
                mIsClicked = false;
                updateQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsClicked = savedInstanceState.getBoolean(KEY_CLICKED, mIsClicked);
            mRightAnswerCount = savedInstanceState.getInt(KEY_CORRECT, 0);
            mFalseAnswerCount = savedInstanceState.getInt(KEY_FALSE, 0);
            mCheatAnswerCount = savedInstanceState.getInt(KEY_CHEAT, 0);
            if (savedInstanceState.getBoolean(KEY_CLICKED, false))
            {
                mTrueButton.setClickable(false);
                mFalseButton.setClickable(false);
            }
            else
            {
                mTrueButton.setClickable(true);
                mFalseButton.setClickable(true);
            }
        }
        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_CLICKED, mIsClicked);
        savedInstanceState.putInt(KEY_CORRECT, mRightAnswerCount);
        savedInstanceState.putInt(KEY_FALSE, mFalseAnswerCount);
        savedInstanceState.putInt(KEY_CHEAT, mCheatAnswerCount);
    }
}