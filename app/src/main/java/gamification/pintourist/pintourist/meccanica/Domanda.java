package gamification.pintourist.pintourist.meccanica;

import android.media.Image;

import gamification.pintourist.pintourist.R;

/**
 * Created by Marco on 11/06/2015.
*/
public class Domanda {

    private String question;
    private String[] possibleAnswers;
    private int correctAnswerIndex;
    private int image;

    public Domanda(String question, String[] possibleAnswers, int correctAnswerIndex, int image){
        this.question=question;
        this.possibleAnswers=possibleAnswers;
        this.correctAnswerIndex=correctAnswerIndex;
        this.image=image;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getPossibleAnswers() {
        return possibleAnswers;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public int getImage() {
        return image;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public void setPossibleAnswers(String[] possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean checkAnswer(int answer){
        if (answer==this.correctAnswerIndex) return true;
        return false;
    }
}


