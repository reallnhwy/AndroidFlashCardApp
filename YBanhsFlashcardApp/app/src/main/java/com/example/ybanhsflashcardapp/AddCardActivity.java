package com.example.ybanhsflashcardapp;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddCardActivity extends AppCompatActivity {

    public EditText question_box;
    public EditText answer_box;
    public EditText wronganswer1_box;
    public EditText wronganswer2_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        ImageView cancel_button = findViewById(R.id.cancel_button);
        ImageView save_button = findViewById(R.id.save_button);

        question_box = findViewById(R.id.question_box);
        answer_box = findViewById(R.id.answer_box);
        wronganswer1_box = findViewById(R.id.wronganswer1_box);
        wronganswer2_box = findViewById(R.id.wronganswer2_box);

        String currentQuestion = getIntent().getStringExtra("flashcard_question");
        String currentCorrectAnswer = getIntent().getStringExtra("flashcard_answer");
        String currentWrongAnswer1 = getIntent().getStringExtra("wronganswer1");
        String currentWrongAnswer2 = getIntent().getStringExtra("wronganswer2");

        //This fucntion is to cancel editing or adding a flashcard (destroy the AddCardActivity)
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //This function is to send the data from EditText to onActivityResult function
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get data from EditText
                String question = question_box.getText().toString();
                String answer = answer_box.getText().toString();
                String wronganswer1 = wronganswer1_box.getText().toString();
                String wronganswer2 = wronganswer2_box.getText().toString();

                // Must use equals() method
                if (question.equals("") || answer.equals("") || wronganswer2.equals("") || wronganswer1.equals("")) {
                    Toast.makeText(getApplicationContext(), "Must enter both Question and Answer?", Toast.LENGTH_SHORT).show();
                } else {
                    // Put data in the intent
                    Intent intent = new Intent();
                    intent.putExtra( "QUESTION_KEY",question);
                    intent.putExtra( "ANSWER_KEY",answer);
                    intent.putExtra("WRONG_ANSWER1",wronganswer1);
                    intent.putExtra("WRONG_ANSWER2",wronganswer2);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

        question_box.setText(currentQuestion);
        answer_box.setText(currentCorrectAnswer);
        wronganswer1_box.setText(currentWrongAnswer1);
        wronganswer2_box.setText(currentWrongAnswer2);

    }
}