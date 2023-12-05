package Prueba;

import java.awt.*;

public class MessagePanel extends Rectangle {

    private final Font font = new Font("Arial", Font.BOLD, 30);

    private String topLine;

    private String bottomLine;

    public MessagePanel(Position position, int width, int height) {
        super(position, width, height);
    }

    public void showGameOver(int finalScore) {
        topLine = "Tu puntaje: " + finalScore;
        bottomLine = "Presiona ESPACIO para un nuevo juego";
    }

    public void showStartMessage() {
        topLine = "Presiona ESPACIO para comenzar";
        bottomLine = "¡Presiona las teclas para puntuar!";
    }

    public void setTopLine(String message) {
        topLine = message;
    }

    public void setBottomLine(String message) {
        bottomLine = message;
    }

    public void paint(Graphics g) {
        g.setColor(new Color(118, 35, 35, 200));
        g.fillRect(position.x, position.y, width, height);
        g.setColor(Color.BLACK);
        g.setFont(font);
        int strWidth = g.getFontMetrics().stringWidth(topLine);
        g.drawString(topLine, position.x+width/2-strWidth/2, position.y+30);
        strWidth = g.getFontMetrics().stringWidth(bottomLine);
        g.drawString(bottomLine, position.x+width/2-strWidth/2, position.y+60);
    }
}