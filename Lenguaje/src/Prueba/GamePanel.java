package Prueba;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
	// Número de elementos que aparecen antes de que termine el juego.
	   private static final int TOTAL_SPAWNS = 350;
	    
	    //Tiempo en milisegundos entre actualizaciones. 
	   private static final int TIMER_DELAY = 30;
	    
	    //Ancho del panel en píxeles.
	   private static final int GAME_WIDTH = 600;
	   
	    //Altura del panel en píxeles.
	   private static final int GAME_HEIGHT = 800;
	   
	    //Referencia a la región donde se logra una puntuación perfecta.
	   private static final int PERFECT_LINE_HEIGHT = GAME_HEIGHT-RhythmElement.SIZE-50;
	   
	    //Color utilizado para dibujar el texto "¡BIEN!".
	   private final Color FADING_NICE_COLOUR = new Color(0,62,49);
	    
	    //Color utilizado para dibujar el texto "¡INCORRECTO!". 
	   private final Color FADING_INCORRECT_COLOUR = new Color(128,0,0);
	    
	    //Color utilizado para dibujar el texto "¡PERFECTO!".
	   private final Color FADING_PERFECT_COLOUR = new Color(255, 207, 61);
	    
	   //Color utilizado para dibujar el texto "¡DEMASIADO PRONTO!".  
	   private final Color FADING_TOOSOON_COLOUR = new Color(38, 216, 239);

	   //Temporizador utilizado para mantener actualizaciones y redibujado consistentes.
	   public enum GameState { Ready, Playing, GameEnded };
	    
	   //Estado actual del juego.
	   private GameState gameState;
	    
	   //Temporizador utilizado para mantener actualizaciones y redibujados consistentes.
	   private Timer gameTimer;
	    
	   //Lista de todos los elementos rítmicos activos. 
	   private List<RhythmElement> rhythmElementList;
	    
	   //Lista de todos los textos de eventos que se desvanecen activos. 
	   private List<FadingEventText> fadingEventTexts;
	    
	   //Referencia al objeto Random compartido para aleatorizar elementos del juego.
	   private Random rand;
	    
	   //Temporizador utilizado para gestionar la generación de elementos rítmicos adicionales.
	   private int spawnTimer = 0;
	    
	   //Tiempo utilizado para el período entre la generación de elementos rítmicos.
	   private int timeBetweenSpawns = 500;
	    
	   //Puntuación actual. Aumenta jugando correctamente el juego.
	   private int score;
	    
	   //Número de entradas correctas seguidas que aumenta la puntuación.
	   private int comboStreak;
	    
	   //Número de generaciones restantes hasta que termine el juego.
	   private int spawnsRemaining;
	    
	   //Panel de mensajes superpuesto para mostrar los estados de listo y fin del juego.
	   private MessagePanel messagePanel;

	   // Modifica la velocidad a la que caen los elementos.
	   private int speedFactor = 3;
	   
	   //Añade la direccion de las canciones
	   private String[] songPaths = {"/Cordero.wav", "/Wawita.wav", "/Avispas.wav"};
    
	   private Clip clip;
    
    //Crea el estado inicial del juego y configura el panel listo para comenzar el juego.
    public GamePanel() {
        setPreferredSize(new Dimension(GAME_WIDTH,GAME_HEIGHT));
        setBackground(Color.lightGray);
        gameTimer = new Timer(TIMER_DELAY,this);
        rhythmElementList = new ArrayList<>();
        fadingEventTexts = new ArrayList<>();
        rand = new Random();
        spawnsRemaining = TOTAL_SPAWNS;
        messagePanel = new MessagePanel(new Position(0,GAME_HEIGHT/2-50), GAME_WIDTH,70);
        messagePanel.showStartMessage();
        comboStreak = 0;
        gameState = GameState.Ready;
    }

    private void playRandomSong() {
        try {
            Random rand = new Random();
            int songIndex = rand.nextInt(songPaths.length);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/Prueba/resources" + songPaths[songIndex]));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(0.1) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void handleRhythmInteraction(char key) {
        for(int rhythmIndex = 0; rhythmIndex < rhythmElementList.size(); rhythmIndex++) {
            RhythmElement element = rhythmElementList.get(rhythmIndex);

            // Reducir el rango de 'PERFECT' para que sea más estricto
            int perfectStart = PERFECT_LINE_HEIGHT - RhythmElement.SIZE / 4; // Comienzo del rango 'PERFECT' más estricto
            int perfectEnd = PERFECT_LINE_HEIGHT + RhythmElement.SIZE / 4; // Fin del rango 'PERFECT' más estricto

            // Solo actuar sobre elementos que están en la posición correcta para ser presionados
            if(element.position.y >= perfectStart && element.position.y <= perfectEnd) {
                if(element.isCharacterCorrect(key)) {
                    int scoreEarned;
                    Color fadingColour;

                    if(element.position.y >= perfectStart && element.position.y <= perfectEnd) {
                        // Lógica para cuando la interacción es 'PERFECT'
                        scoreEarned = (10 + comboStreak) * 2;
                        fadingColour = FADING_PERFECT_COLOUR;
                    } else {
                        // Lógica para cuando la interacción es 'NICE'
                        scoreEarned = 5 + comboStreak;
                        fadingColour = FADING_NICE_COLOUR;
                    }

                    score += scoreEarned;
                    addFadingText((scoreEarned == 20 ? "PERFECT! +" : "NICE! +") + scoreEarned, rhythmIndex, fadingColour);
                    comboStreak++;
                    rhythmElementList.remove(rhythmIndex);
                    break;
                } else if (RhythmElement.isCharacterValid(key)) {
                    // Si la tecla presionada es incorrecta para este elemento
                    addFadingText("INCORRECT!", rhythmIndex, FADING_INCORRECT_COLOUR);
                    comboStreak = 0;
                    rhythmElementList.remove(rhythmIndex);
                    break;
                }
            } else if (element.position.y > perfectEnd && element.isCharacterCorrect(key)) {
                // Lógica para cuando la interacción es 'NICE' fuera del rango 'PERFECT'
                int scoreEarned = 10 + comboStreak;
                score += scoreEarned;
                addFadingText("NICE! +" + scoreEarned, rhythmIndex, FADING_NICE_COLOUR);
                comboStreak++;
                rhythmElementList.remove(rhythmIndex);
                break;
            }
        }
    }
    
    // Crea un color personalizado usando valores RGB
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color customColor = new Color(173, 216, 230);
        g.setColor(customColor);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    
    //Interacciones de teclado [ESC-SPACE]
    public void handleInput(int keyCode) {
    	//Verificacion para cerrar el juego
        if(keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        } else if(gameState == GameState.Playing && RhythmElement.isCharacterValid((char)keyCode)) {
            if(rhythmElementList.size() > 0) {
                handleRhythmInteraction((char)keyCode);
            }
          //Verificacion para iniciar el juego
        } else if(gameState == GameState.Ready && keyCode == KeyEvent.VK_SPACE) {
            gameState = GameState.Playing;
            gameTimer.start();
            playRandomSong();
          //Verificacion para reiniciar el juego
        } else if(gameState == GameState.GameEnded && keyCode == KeyEvent.VK_SPACE) {
            gameState = GameState.Ready;
            spawnsRemaining = TOTAL_SPAWNS;
            messagePanel.showStartMessage();
            comboStreak = 0;
            score = 0;
            repaint();
        }
    }

    
    //Actualizaciones durante el estado de reproducción para generar elementos de ritmo adicionales en un temporizador.
    public void update() {
        if(gameState != GameState.Playing) return;

        updateRhythmSpawnTimer();
        updateRhythmElements();
        updateFadingText();

        if (spawnsRemaining == 0 && rhythmElementList.size() == 0) {
            endGame();
        }
        repaint(); //Dibujar cualquier cambio que haya ocurrido.
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        
      //Dibuja elementos según el estado del juego
        if(gameState == GameState.Playing) {
            for (RhythmElement rhythmElement : rhythmElementList) {
                rhythmElement.paint(g);
            }
            for(FadingEventText text : fadingEventTexts) {
                text.paint(g);
            }
        } else {
            messagePanel.paint(g);
        }
        
        //Muestra puntuación y círculos inferiores
        g.setColor(Color.BLACK);
        for(int x = RhythmElement.SIZE; x < GAME_WIDTH-RhythmElement.SIZE; x+=RhythmElement.SIZE) {
            g.drawOval(x,PERFECT_LINE_HEIGHT,RhythmElement.SIZE,RhythmElement.SIZE);
        }
        
        // Dibuja círculos para mostrar dónde se puede lograr la puntuación perfecta.
        drawScore(g);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Puntuación Máxima: " + HighScoreManager.getInstance().getHighScore(), 10, 30);
    }

    //Activa timer
    public void actionPerformed(ActionEvent e) {
        update();
    }

    //Agrega un nuevo RhythmElement si aún quedan más por generar.
    private void addNewRhythmElement() {
        if(spawnsRemaining == 0) return; //Cuando llegue a 0, generará uno nuevo y restablecerá el temporizador.
        spawnsRemaining--;

        int xPosition = rand.nextInt((getWidth()-2*RhythmElement.SIZE)/RhythmElement.SIZE)*RhythmElement.SIZE+RhythmElement.SIZE;
        rhythmElementList.add(new RhythmElement(new Position(xPosition,-RhythmElement.SIZE),rand));
    }

    private void updateRhythmSpawnTimer() {
        spawnTimer += TIMER_DELAY;
        if(spawnTimer >= timeBetweenSpawns) {
            addNewRhythmElement();
            spawnTimer = 0;
        }
    }
    //Actualiza todos los RhythmElements actualmente activos haciéndolos moverse hacia abajo según
    private void updateRhythmElements() {
        for(int i = 0; i < rhythmElementList.size(); i++) {
            rhythmElementList.get(i).update(TIMER_DELAY/speedFactor);
            if(rhythmElementList.get(i).position.y > getHeight()) {
                rhythmElementList.remove(i);
                i--;
                comboStreak = 0;
            }
        }
    }

    //Actualiza los mensajes de texto para asi eliminarlos
    private void updateFadingText() {
        for(int i = 0; i < fadingEventTexts.size(); i++) { //Determina si sigue siendo visible o no
            fadingEventTexts.get(i).update(TIMER_DELAY);
            if(fadingEventTexts.get(i).isExpired()) {	//Si ya no es visible lo elimina
                fadingEventTexts.remove(i);
                i--;
            }
        }
    }

    

    private void addFadingText(String message, int elementIndexToSpawnOff, Color colour) {
        fadingEventTexts.add(new FadingEventText(message,
                new Position(rhythmElementList.get(elementIndexToSpawnOff).position), colour));
    }
    
    //Muestra la puntuacion acumulada
    private void drawScore(Graphics g) {
        String scoreText = String.valueOf(score);
        g.setFont(new Font("Arial", Font.BOLD, 35));
        int width = g.getFontMetrics().stringWidth(scoreText);
        g.setColor(Color.BLACK);
        g.drawString(scoreText, GAME_WIDTH/2 - width/2, 40);
    }
    
    private void endGame() {
        HighScoreManager.getInstance().checkNewScore(score);
        gameState = GameState.GameEnded;
        fadingEventTexts.clear();
        messagePanel.showGameOver(score);
        gameTimer.stop();
    }
}