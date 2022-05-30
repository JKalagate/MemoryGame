package com.example.memorygame;

public class MemoryGameModelClass {

    String bestScore;
    String step;


    public MemoryGameModelClass(String bestScore, String step) {
        this.bestScore = bestScore;
        this.step = step;
    }

    public String getStep() {
        return step;
    }

    public String getBestScore() {
        return bestScore;
    }

}
