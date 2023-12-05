package Prueba;

import java.io.*;

public class HighScoreManager {
    private static HighScoreManager instance;
    private int highScore;

    private HighScoreManager() {
        loadHighScore();
    }

    public static HighScoreManager getInstance() {
        if (instance == null) {
            instance = new HighScoreManager();
        }
        return instance;
    }

    private void loadHighScore() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("highscore.dat"))) {
            highScore = ois.readInt();
        } catch (IOException e) {
            highScore = 0;
        }
    }

    public void saveHighScore() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("highscore.dat"))) {
            oos.writeInt(highScore);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getHighScore() {
        return highScore;
    }

    public void checkNewScore(int score) {
        if (score > highScore) {
            highScore = score;
            saveHighScore();
        }
    }
}
