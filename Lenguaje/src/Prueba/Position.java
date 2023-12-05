package Prueba;

//Para representar la posición de x, y
public class Position {

    //Vector unitario de movimiento hacia abajo
    public static final Position DOWN = new Position(0,1);

    //Vector unitario de movimiento hacia arriba
    public static final Position UP = new Position(0,-1);

    //Vector unitario de movimiento izquierdo
    public static final Position LEFT = new Position(-1,0);

    //Vector unitario de movimiento derecho
    public static final Position RIGHT = new Position(1,0);

    //Vector unitario 0
    public static final Position ZERO = new Position(0,0);

    //Coordenada x , y
    public int x;

    public int y;

    //Establece el valor de Position, x X cordenada, y Y cordenada
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //Copia el constructor para crear una nueva Position usando los valores de otra
    // positionToCopy = Posición desde la que copiar valores
    public Position(Position positionToCopy) {
        this.x = positionToCopy.x;
        this.y = positionToCopy.y;
    }

    //Establece la posición en las coordenadas x, y especificadas
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //Actualiza position agregando valores de otherPosition
    // - otherPosition = otra posición para agregar a esta
    public void add(Position otherPosition) {
        this.x += otherPosition.x;
        this.y += otherPosition.y;
    }

    /*Calcula la distancia desde esta posición a otra posición
     - otherPosition = Posición la cual comprobar la distancia
     - Distancia entre esta posición y la otra posición.
    */
    public double distanceTo(Position otherPosition) {
        return Math.sqrt(Math.pow(x-otherPosition.x,2)+Math.pow(y-otherPosition.y,2));
    }

    //Multiplica ambos componentes de la posición por una cantidad
    // - amount = Cantidad por la que se multiplica el vector
    public void multiply(int amount) {
        x *= amount;
        y *= amount;
    }

    //Actualiza la posición restando los valores de otherPosition
    // - otherPosition = Otra posición para agregar a esta
    public void subtract(Position otherPosition) {
        this.x -= otherPosition.x;
        this.y -= otherPosition.y;
    }

    /* Compara el objeto position con otro objeto
     - Cualquier objeto que no sea de Position devolverá falso. Si no compara x,y para encontrar igualdad
     - o = Objeto con el que comparar Position
     - Verdadero si el objeto 'o' es igual a esta posición tanto x como y
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    //Obtiene uan versión de string de Position
    //Un string con la forma de (x,y)
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
