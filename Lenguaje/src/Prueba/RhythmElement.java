package Prueba;

import java.awt.*;
import java.util.Random;

public class RhythmElement extends Rectangle {

    private static boolean ONLY_ALLOW_VALID_CHARACTERS = true;

    public static final int SIZE = 100;

    private static final char[] VALID_CHARACTERS = {'A','S','J','K'};

    private static final Color[] COLOURS = {new Color(255,0,0,200), new Color(0,255,0,200), new Color(0,0,255,200), new Color(186,152,28,200)};

    private final Font font = new Font("Arial", Font.BOLD, SIZE-20);

    private char character;

    private String characterStr;

    private Color drawColour;

    private static final int[] X_POSITIONS = {100, 200, 300, 400};
    
    //Llama al constructor de Rectangle
    public RhythmElement(Position position, Random rand) {
        super(new Position(X_POSITIONS[rand.nextInt(X_POSITIONS.length)], position.y), SIZE, SIZE);
        int charChoice = rand.nextInt(VALID_CHARACTERS.length);
        character = VALID_CHARACTERS[charChoice];
        drawColour = COLOURS[charChoice];
        characterStr = String.valueOf(character);
        this.position.x = X_POSITIONS[charChoice];
    }
    //Realiza la bajada de elementos
    public void update(int fallAmount) {
        position.y += fallAmount;
    }

    public boolean isCharacterCorrect(char character) {
        return this.character == character;
    }

    public static boolean isCharacterValid(char character) {
        if(!ONLY_ALLOW_VALID_CHARACTERS) return true;

        for(int i = 0; i < VALID_CHARACTERS.length; i++) {
            if(character == VALID_CHARACTERS[i]) {
                return true;
            }
        }
        return false;
    }

    //Dibuja los elementos en pantalla
    public void paint(Graphics g) {
        g.setColor(drawColour);
        g.fillOval(position.x, position.y, width, height);
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString(characterStr, position.x+15, position.y+SIZE*3/4+5);
    }
}
