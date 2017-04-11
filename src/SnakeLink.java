import java.awt.Color;

/**
 * <pre class="doc_header">
 * <p>
 * </pre>
 *
 * @author kelmore5
 * @custom.date 3/19/17
 */
public class SnakeLink {
    private Position position;
    private Color color;

    public SnakeLink(Position pos, Color col) {
        position = pos;
        color = col;
    }

    public void setPosition(Position pos) {
        position = pos;
    }

    public Position getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public boolean equals(SnakeLink sl) {
        return sl.getPosition().getX() == position.getX() && sl.getPosition().getY() == position.getY() && sl.getColor() == color;
    }
}