package com.example.ybanhsflashcardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView flashcard_question;
    TextView flashcard_answer;
    TextView wronganswer1;
    TextView wronganswer2;


    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int currentCardDisplayedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcard_question = findViewById(R.id.flashcard_question);
        flashcard_answer = findViewById(R.id.flashcard_answer);
        ImageView ToggleAnswer = findViewById(R.id.toggle_choices_visibility);
        wronganswer1 = findViewById(R.id.answer1);
        wronganswer2 = findViewById(R.id.answer2);
        final boolean[] IsShowingAnswer = {true};
        final boolean[] IsAnswerClicked = {false};

        // Have to set up in onCreate because before the app was created there is no data (= null)
        flashcardDatabase = new FlashcardDatabase(getApplicationContext()); // or this
        // To make sure both onCreate and onActivityResult have the most up-to-date List
        allFlashcards = flashcardDatabase.getAllCards();


        if (allFlashcards != null && allFlashcards.size() > 0) {
            flashcard_question.setText(allFlashcards.get(0).getQuestion());
            flashcard_answer.setText(allFlashcards.get(0).getAnswer());
            wronganswer1.setText(allFlashcards.get(0).getWrongAnswer1());
            wronganswer2.setText(allFlashcards.get(0).getWrongAnswer2());
        }


        // Function to hide and show answer
        ToggleAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsShowingAnswer[0]) {
                    ToggleAnswer.setImageResource(R.drawable.ic_eye_crossed);
                    wronganswer1.setVisibility(View.INVISIBLE);
                    wronganswer2.setVisibility(View.INVISIBLE);
                    flashcard_answer.setVisibility(View.INVISIBLE);
                    IsShowingAnswer[0] = false;
                } else {
                    ToggleAnswer.setImageResource(R.drawable.ic_eye);
                    wronganswer1.setVisibility(View.VISIBLE);
                    wronganswer2.setVisibility(View.VISIBLE);
                    flashcard_answer.setVisibility(View.VISIBLE);
                    IsShowingAnswer[0] = true;
                }
            }
        });

        // Reset answer
        findViewById(R.id.parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wronganswer1.setTextColor(getResources().getColor(R.color.darkblue));
                wronganswer2.setTextColor(getResources().getColor(R.color.darkblue));
                flashcard_answer.setTextColor(getResources().getColor(R.color.darkblue));
                IsAnswerClicked[0] = false;
            }
        });


        // Function to add a new flashcard (start AddCardActivity class and expect some data in return)
        findViewById(R.id.add_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
//                MainActivity.this.startActivity(intent); //This is to only start the activity
                startActivityForResult(intent, 100);
            }
        });

        // Function to edit an already exist flashcard (start AddCardActivity class and expect some data in return)
        findViewById(R.id.edit_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                intent.putExtra("flashcard_question", flashcard_question.getText().toString());
                intent.putExtra("flashcard_answer", flashcard_answer.getText().toString());
                intent.putExtra("wronganswer1", wronganswer1.getText().toString());
                intent.putExtra("wronganswer2", wronganswer2.getText().toString());
                startActivityForResult(intent, 100);
            }
        });


        //The next 3 Listeners may turn into a function?
        flashcard_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!IsAnswerClicked[0]) {
                    flashcard_answer.setTextColor(getResources().getColor(R.color.green));
                    IsAnswerClicked[0] = true;
                }
            }
        });

        wronganswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!IsAnswerClicked[0]) {
                    wronganswer2.setTextColor(getResources().getColor(R.color.darkred));
                    flashcard_answer.setTextColor(getResources().getColor(R.color.green));
                    IsAnswerClicked[0] = true;
                }
            }
        });

        wronganswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!IsAnswerClicked[0]) {
                    wronganswer1.setTextColor(getResources().getColor(R.color.darkred));
                    flashcard_answer.setTextColor(getResources().getColor(R.color.green));
                    IsAnswerClicked[0] = true;
                }
            }
        });

        findViewById(R.id.next_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allFlashcards == null || allFlashcards.size() == 0){
                    return;
                }
                // advance our pointer index so we can show the next card
                currentCardDisplayedIndex++;
                // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                if (currentCardDisplayedIndex >= allFlashcards.size()) {
                    Snackbar.make(view,
                            "You have reach the end of the cards, go back to the start.",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    currentCardDisplayedIndex = 0; //Reset the index so user can go back to the beginning of the cards
                }
                // set the question and answer TextViews with data from the database
                Flashcard currentCard = allFlashcards.get(currentCardDisplayedIndex);
                flashcard_question.setText(currentCard.getQuestion());
                flashcard_answer.setText(currentCard.getAnswer());
                wronganswer1.setText(currentCard.getWrongAnswer1());
                wronganswer2.setText(currentCard.getWrongAnswer2());
            }
        });

    }

    // This function is to receive data and do something with it
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            if (data != null) {
                //Get the data and convert it to String
                String question = data.getExtras().getString("QUESTION_KEY");
                String answer = data.getExtras().getString("ANSWER_KEY");
                String WrongAnswer1 = data.getExtras().getString("WRONG_ANSWER1");
                String WrongAnswer2 = data.getExtras().getString("WRONG_ANSWER2");

                //Change the TextView with the received Strings
                flashcard_question.setText(question);
                flashcard_answer.setText(answer);
                wronganswer1.setText(WrongAnswer1);
                wronganswer2.setText(WrongAnswer2);

                //Transform the Q&A data to an flashcard object/entity
                flashcardDatabase.insertCard(new Flashcard(question, answer, WrongAnswer1, WrongAnswer2));

                //Update the most recent flashcard list with all the object in the database
                allFlashcards = flashcardDatabase.getAllCards();

            }
        }
        Snackbar.make(findViewById(R.id.flashcard_question),
                "The message to display",
                Snackbar.LENGTH_SHORT)
                .show();
    }

}

//        // Tap the question to see the flashcard answer
//        flashcard_question.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Show the answer and hide the question
//                flashcard_question.setVisibility(View.INVISIBLE);
//                flashcard_answer.setVisibility(View.VISIBLE);
////                findViewById(R.id.parent).setBackgroundColor(getResources().getColor(R.color.beige));
//            }
//        });
//
//        // Tap to toggle the view
//        flashcard_answer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Show the question again and hide the answer
//                flashcard_question.setVisibility(View.VISIBLE);
//                flashcard_answer.setVisibility(View.INVISIBLE);
////                findViewById(R.id.parent).setBackgroundColor(getResources().getColor(R.color.darkred));
//            }
//        });