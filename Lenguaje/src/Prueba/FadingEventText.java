package Prueba;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

// Creara el texto que se irá lentamente hacia arriba y desaparecerá
public class FadingEventText {

    // El color base que desaparecerá
    private Color colour;

    //Fuente para dibujar el texto
    private Font font = new Font("Arial", Font.BOLD, 20);

    //Color actual para dibujar desde su actualización más reciente
    private Color drawColour;

    //Posición actual para dibujar
    private Position position;

    //El mensaje para mostrar
    private String text;

    //Desaparece de 255 a 0, representa el valor alfta del texto dibujado
    private int fadeValue;

    /* Configura el texto listo para ser dibujado/actualizado 
     - startColour: El color que se utiliza para el texto que desaparecerá
     - Posición en la que comienza el texto
     - El mensaje a mostrar
    */
    public FadingEventText(String text, Position position, Color startColour) {
        this.colour = startColour;
        fadeValue = 255;
        drawColour = new Color(colour.getRed(),colour.getGreen(),colour.getBlue(),fadeValue);
        this.position = position;
        this.text = text;
    }

    /* Actualiza el canal de color y la posición segun deltaTime
     - deltaTime: Cantidad de tiempo desde la ultima actualización
    */
    public void update(int deltaTime) {
        int changeAmount = deltaTime / 6;
        fadeValue = Math.max(0,fadeValue - changeAmount);
        position.y -= changeAmount / 2;
        drawColour = new Color(colour.getRed(),colour.getGreen(),colour.getBlue(),fadeValue);
    }

    //Prueba si el texto ya no es visible, verdadero si llego a 0
    public boolean isExpired() {
        return fadeValue == 0;
    }

    //Dibuja el texto con las propiedades almacenadas, g = referencia al objeto Graphics para renderizar
    public void paint(Graphics g) {
        g.setColor(drawColour);
        g.setFont(font);
        g.drawString(text,position.x, position.y);
    }
}
