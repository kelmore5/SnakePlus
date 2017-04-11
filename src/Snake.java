import java.awt.Color;

/**
 * <pre class="doc_header">
 * <p>
 * </pre>
 *
 * @author kelmore5
 * @custom.date 3/19/17
 */
public class Snake {
    private int size;
    private int nodes;
    private int currentX, currentY;
    private Queue<SnakeLink> past;
    private Color color;
    private double currentDirection;

    public Snake(int x, int y, Color _color, int _size, double direction) {
        currentDirection = direction;
        past = new Queue<>();
        color = _color;
        past.push(new SnakeLink(new Position(x, y), color));
        currentX = x;
        currentY = y;
        nodes = 0;
        size = _size;
    }

    public Snake(int x, int y, Color _color, int _size, double direction, int _nodes, Queue<SnakeLink> queue) {
        this(x, y, _color, _size, direction);
        past = queue;
        nodes = _nodes;
    }

    Queue<SnakeLink> getPast() {
        return past;
    }

    public boolean move() {
        currentX += Math.cos(currentDirection);
        currentY += Math.sin(currentDirection);
        if(currentX < 0) {
            currentX = size-1;
            return false;
        }
        else if(currentY < 0) {
            currentY = size -1;
            return false;
        }
        else if(currentX >= size) {
            currentX = 0;
            return false;
        }
        else if(currentY >= size) {
            currentY = 0;
            return false;
        }
        past.push(new SnakeLink(new Position(currentX, currentY), color));
        if(nodes+2 == past.size()) {
            past.pop();
        }

        return true;
    }

    void setDirection(double direction) {
        currentDirection = direction;
    }

    void increment() {
        nodes++;
    }

    public int getNodes() {
        return nodes;
    }

    public Color getColor() {
        return color;
    }

    double getDirection() {
        return currentDirection;
    }
}