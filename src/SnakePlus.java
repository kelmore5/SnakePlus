import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * <pre class="doc_header">
 * <p>
 * </pre>
 *
 * @author kelmore5
 * @custom.date 3/19/17
 */
@SuppressWarnings("FieldCanBeLocal")
public class SnakePlus extends JFrame implements Runnable, KeyListener, ActionListener
{
    private MainPanel game;
    private Snake[] snakes;
    private final double NORTH = Math.PI/2;
    private final double SOUTH = 3*Math.PI/2;
    private final double EAST = 0;
    private final double WEST = Math.PI;
    private int width, height, snakeDim;
    private Position[] grid;
    private final Color[] colors = {Color.blue, Color.red, Color.green, Color.pink, Color.magenta, Color.yellow};
    private Timer timer;
    private CardLayout layout;
    private JPanel mainPanel, gameOver;

    public SnakePlus() {
        super("Snake++");
        layout = new CardLayout();
        mainPanel = new JPanel(layout);

        JPanel startScreen = createStartScreen();

        setDefaults();
        game = new MainPanel();

        mainPanel.add(startScreen, "Start");
        mainPanel.add(game, "Game Panel");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createMenus();

        add(mainPanel);
        addKeyListener(this);
    }

    private void setDefaults() {
        snakes = new Snake[2];
        snakeDim = 25;
        grid = new Position[(int) Math.pow(snakeDim, 2)];
        width = (int) (Math.pow(snakeDim, 2));
        height = (int) (Math.pow(snakeDim, 2));
        Snake mainSnake = new Snake(width/50, height/50, colors[0], snakeDim, SOUTH);
        snakes[0] = mainSnake;
        createGrid();
    }

    private void createMenus() {
        JMenuBar mbar = new JMenuBar();

        JMenu menu = new JMenu("File");
        JMenuItem start = new JMenuItem("Start the Game!");
        JMenuItem quit = new JMenuItem("Quit");

        start.addActionListener(e -> {
            layout.show(mainPanel, "Game Panel");
            move();
        });

        quit.addActionListener(this);

        menu.add(start);
        menu.add(quit);
        mbar.add(menu);
        this.setJMenuBar(mbar);
    }

    private void createGrid() {
        for(int k = 0; k < snakeDim; k++) {
            for(int i = 0; i < snakeDim; i++) {
                grid[k*snakeDim + i] = new Position(k, i);
            }
        }
    }

    public void run() {
        setSize(width+2, height+54);
        setVisible(true);
    }

    private void move() {
        timer = new Timer(50, e -> {
            for(Snake s: snakes) {
                if(s != null) {
                    if(!s.move()) {
                        gameOver("You ran into the wall! You looooose");
                    }
                }
            }
            repaint();
        });
        timer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public Dimension getSize() {
        return new Dimension(this.getWidth(), this.getHeight());
    }

    private JPanel createStartScreen() {
        JPanel panel = new JPanel(new BorderLayout());

        Icon icon = new ImageIcon("Images/StartScreen.png");
        panel.add(new JLabel(icon), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createGameOverPanel(String message) {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new JLabel(message));

        JPanel buttonPanel = new JPanel();

        JButton reset = new JButton("Reset");
        reset.setFocusable(false);
        reset.addActionListener(e -> {
            setDefaults();
            layout.show(mainPanel, "Game Panel");
        });

        buttonPanel.add(reset);

        JButton quit = new JButton("Quit");
        quit.addActionListener(this);

        buttonPanel.add(quit);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void gameOver(String message) {
        game.reset();
        gameOver = createGameOverPanel(message);
        mainPanel.add(gameOver, "Game Over");
        timer.stop();
        layout.show(mainPanel, "Game Over");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();


        if(key == KeyEvent.VK_UP) {
            changeDirection(snakes[0], NORTH, SOUTH);
        }
        else if(key == KeyEvent.VK_DOWN) {
            changeDirection(snakes[0], SOUTH, NORTH);
        }
        else if(key == KeyEvent.VK_RIGHT) {
            changeDirection(snakes[0], WEST, EAST);
        }
        else if(key == KeyEvent.VK_LEFT) {
            changeDirection(snakes[0], EAST, WEST);
        }

        if(snakes[1] != null) {
            if(key == KeyEvent.VK_W) {
                changeDirection(snakes[1], NORTH, SOUTH);
            }
            else if(key == KeyEvent.VK_S) {
                changeDirection(snakes[1], SOUTH, NORTH);
            }
            else if(key == KeyEvent.VK_D) {
                changeDirection(snakes[1], WEST, EAST);
            }
            else if(key == KeyEvent.VK_A) {
                changeDirection(snakes[1], EAST, WEST);
            }
        }
    }

    private void changeDirection(Snake s, double test, double set) {
        if(s.getNodes() > 0) {
            if(s.getDirection() == test) {
                return;
            }
        }
        s.setDirection(set);
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        SnakePlus sp = new SnakePlus();
        javax.swing.SwingUtilities.invokeLater(sp);
    }

    private class MainPanel extends JPanel {
        private SnakeLink[] nodes;
        private boolean multiStart, multi;

        public MainPanel() {
            super();
            nodes = new SnakeLink[2];
            nodes[0] = newNode(snakes[0].getColor());
            multi = false;
            multiStart = false;
        }

        public void paint(Graphics g) {
            super.paint(g);

            g.setColor(Color.black);

            for(Position p: grid) {
                g.fillRect(p.getX()*25, p.getY()*25, snakeDim, snakeDim);
            }

            for(Snake s: snakes) {

                if(s != null) {
                    g.setColor(s.getColor());

                    if(nodes[1] == null && s.getNodes() > 2) {
                        nodes[1] = newNode(colors[1]);
                    }

                    Queue<SnakeLink> past = s.getPast();

                    Iterator<SnakeLink> iter = past.getIterator();
                    SnakeLink current = past.peek();

                    while(iter.hasNext()) {
                        Position pos = iter.next().getPosition();
                        int x = pos.getX();
                        int y = pos.getY();

                        if(iter.hasNext()) {
                            if(current.getPosition().getX() == x && current.getPosition().getY() == y) {
                                gameOver("You ran into your own snake. You loooose");
                            }
                        }



                        for(int k = 0; k < nodes.length; k++) {
                            SnakeLink link = nodes[k];
                            if(link != null) {
                                Position nodePos = link.getPosition();
                                if(x == nodePos.getX() && y == nodePos.getY()) {
                                    if(link.getColor() == s.getColor()) {
                                        s.increment();
                                        if(!multi) {
                                            nodes[0] = newNode(s.getColor());
                                            if(nodes[1] != null) {
                                                nodes[1] = newNode(colors[1]);
                                            }
                                        }
                                        else {
                                            for(int i = 0; i < nodes.length; i++) {
                                                if(nodes[i] != null) {
                                                    if(nodes[i].getColor().equals(s.getColor())) {
                                                        nodes[i] = null;
                                                    }
                                                }
                                            }
                                        }
                                        if(multiStart) {
                                            multiply();
                                            multiStart = false;
                                            multi = true;
                                        }
                                    }
                                    else {
                                        gameOver("Type error! You tried to add an integer value to your list of strings! You looooose");
                                    }
                                }
                            }
                        }

                        g.fillRect(x*25, y*25, snakeDim, snakeDim);
                    }
                }
            }

            for(int k = 0; k < nodes.length; k++) {
                SnakeLink sl = nodes[k];
                if(sl != null) {
                    g.setColor(sl.getColor());
                    g.fillRect(sl.getPosition().getX()*25, sl.getPosition().getY()*25, snakeDim, snakeDim);
                    if(snakes[0].getNodes()%4 == 0 && snakes[0].getNodes() != 0 && k == 0 && snakes[1] == null) {
                        g.setColor(nodes[1].getColor());
                        g.fillRect(sl.getPosition().getX()*25, sl.getPosition().getY()*25 + (snakeDim/2) + 1, snakeDim + 1, snakeDim/2);
                        multiStart = true;
                    }
                }
            }

            if(nodes[0] == null && nodes[1] == null) {
                multi = false;
                snakes[1] = null;
                nodes[0] = newNode(colors[0]);
                nodes[1] = newNode(colors[1]);
            }
        }

        private void multiply() {
            Queue<SnakeLink> past = snakes[0].getPast();
            int length = past.size();
            Queue<SnakeLink> q = new Queue<>();
            int i = 0;
            while(i < length/2) {
                SnakeLink temp = past.pop();
                temp.setPosition(new Position(temp.getPosition().getX(), temp.getPosition().getY()+1));
                q.push(temp);
                i++;
            }
            snakes[1] = new Snake(q.peek().getPosition().getX(), q.peek().getPosition().getY(), colors[1], snakeDim, snakes[0].getDirection(), q.size()-1, q);
        }

        void reset() {
            nodes = new SnakeLink[2];
            nodes[0] = newNode(snakes[0].getColor());
            multi = false;
            multiStart = false;
        }

        private SnakeLink newNode(Color color) {
            int nodex = (int) (Math.random()*(snakeDim-1));
            int nodey = (int) (Math.random()*(snakeDim-1));

            SnakeLink sl = new SnakeLink(new Position(nodex, nodey), color);

            for(Snake s: snakes) {
                if(s != null) {
                    while(s.getPast().contains(sl)) {
                        newNode(color);
                    }
                }
            }

            return sl;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
