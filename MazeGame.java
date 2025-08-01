import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Stack;
import javax.swing.*; 
public class MazeGame extends JFrame  
{ 
    private MazePanel mazePanel; 
    public MazeGame()  
    { 
        mazePanel = new MazePanel(); 
        add(mazePanel); 
        setTitle("Maze Game"); 
        setSize(600, 600);  
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  
        setLocationRelativeTo(null); 
        setResizable(true);  
        addWindowListener(new java.awt.event.WindowAdapter()  
        { 
            public void windowClosing(java.awt.event.WindowEvent windowEvent)  
            {    
                if (JOptionPane.showConfirmDialog(null,"Are you sure you want to exit  the game?","Exit Confirmation",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)== JOptionPane.YES_OPTION)  
                { 
                    System.exit(0); 
                } 
            } 
        }); 
    } 
    public static void main(String[] args)  
    { 
        MazeGame game = new MazeGame(); 
        game.setVisible(true); 
    } 
} 
class MazePanel extends JPanel  
{ 
    private final int TILE_SIZE = 30;  
    private final int MAZE_SIZE = 19;  
    private int[][] maze; 
    private int playerX = 1; 
    private int playerY = 1; 
    private final int startX = 1; 
    private final int startY = 1; 
    private final int endX = MAZE_SIZE - 2; 
    private final int endY = MAZE_SIZE - 2; 
    private int[][] distances; 
    private int movesCount = 0; 
    private int algorithmMovesCount; 
    public MazePanel()  
    { 
        setFocusable(true); 
        addKeyListener(new KeyAdapter()  
        { 
            public void keyPressed(KeyEvent e)  
            { 
                int key = e.getKeyCode(); 
                if (key == KeyEvent.VK_UP)  
                { 
                    movePlayer(0, -1); 
                }  
                else if (key == KeyEvent.VK_DOWN)  
                { 
                    movePlayer(0, 1); 
                }  
                else if (key == KeyEvent.VK_LEFT)  
                { 
                    movePlayer(-1, 0); 
                }  
                else if (key == KeyEvent.VK_RIGHT)  
                { 
                    movePlayer(1, 0); 
                } 
                repaint(); 
                checkWin(); 
            } 
        }); 
        generateMaze(); 
        ensurePath(); 
        calculateDistances(); 
    } 
    private void generateMaze()  
    { 
        maze = new int[MAZE_SIZE][MAZE_SIZE]; 
        for (int[] row : maze)  
        { 
            Arrays.fill(row, 1); 
        } 
        Stack<Point> stack = new Stack<>(); 
        Random rand = new Random(); 
        int startX = 1; 
        int startY = 1; 
        maze[startY][startX] = 0; 
        stack.push(new Point(startX, startY)); 

        while (!stack.isEmpty())  
        { 
            Point current = stack.peek(); 
            java.util.List<Point> neighbors = getNeighbors(current); 

            if (neighbors.isEmpty())  
            { 
                stack.pop(); 
            }  
            else  
            { 
                Point next = neighbors.get(rand.nextInt(neighbors.size())); 
                maze[next.y][next.x] = 0; 
                maze[(current.y + next.y) / 2][(current.x + next.x) / 2] = 0; 
                stack.push(next); 
            } 
        } 
        for (int y = 1; y < MAZE_SIZE - 1; y += 2)  
        { 
            for (int x = 1; x < MAZE_SIZE - 1; x += 2)  
            { 
                if (rand.nextInt(3) == 0)
                { 
                    int direction = rand.nextInt(4);
                    int dx = (direction == 0) ? 1 : (direction == 2) ? -1 : 0;
                    int dy = (direction == 1) ? 1 : (direction == 3) ? -1 : 0;
                    if (maze[y + dy][x + dx] == 1) 
                    {
                        maze[y + dy][x + dx] = 0; 
                    }
                } 
            } 
        } 
    }

    private java.util.List<Point> getNeighbors(Point current)  
    { 
        java.util.List<Point> neighbors = new java.util.ArrayList<>(); 
        int x = current.x; 
        int y = current.y; 
        if (y - 2 > 0 && maze[y - 2][x] == 1) neighbors.add(new Point(x, y - 2)); 
        if (y + 2 < MAZE_SIZE - 1 && maze[y + 2][x] == 1) neighbors.add (new Point 
             (x, y+2)); 
        if (x - 2 > 0 && maze[y][x - 2] == 1) neighbors.add(new Point(x - 2, y)); 
        if (x + 2 < MAZE_SIZE - 1 && maze[y][x + 2] == 1) neighbors.add (new Point 
             (x + 2,y)); 
        return neighbors; 
    } 
    private void ensurePath()  
    { 
        Stack<Point> stack = new Stack<>(); 
        stack.push(new Point(startX, startY)); 
        boolean[][] visited = new boolean[MAZE_SIZE][MAZE_SIZE]; 
        while (!stack.isEmpty())  
        { 
            Point current = stack.pop(); 
            int x = current.x; 
            int y = current.y; 
            if (x == endX && y == endY) 
            { 
                return; 
            } 
            visited[y][x] = true; 
            for (Point neighbor : getValidNeighbors(current, visited))  
            { 
                stack.push(neighbor); 
                maze[neighbor.y][neighbor.x] = 0; 
                maze[(y + neighbor.y) / 2][(x + neighbor.x) / 2] = 0; 
            } 
        } 
    } 
    private java.util.List<Point> getValidNeighbors(Point current, boolean[][] visited)  
    { 
        java.util.List<Point> neighbors = new java.util.ArrayList<>(); 
        int x = current.x; 
        int y = current.y; 
        if (y - 2 > 0 && maze[y - 2][x] == 1 && !visited[y - 2][x]) neighbors.add(new Point 
             (x, y - 2)); 
        if (y + 2 < MAZE_SIZE - 1 && maze[y + 2][x] == 1 &&  
             !visited [y + 2][x]) neighbors.add(new Point(x, y + 2)); 
        if (x - 2 > 0 && maze[y][x - 2] == 1 && !visited[y][x - 2]) neighbors.add(new Point 
             (x - 2, y)); 
        if (x + 2 < MAZE_SIZE - 1 && maze[y][x + 2] == 1 &&  
             !visited[y] [x + 2])neighbors.add(new Point(x + 2, y)); 
        return neighbors; 
    } 
    private void movePlayer(int dx, int dy)  
    { 
        if (playerX + dx >= 0 && playerX + dx < MAZE_SIZE && playerY + dy >= 0 && playerY + dy < MAZE_SIZE && maze[playerY + dy][playerX + dx] == 0)  
        { 
            playerX += dx; 
            playerY += dy; 
            movesCount++; 
        } 
    } 
    private void checkWin()  
    { 
        if (playerX == endX && playerY == endY)  
        { 
            if (distances[playerY][playerX] == Integer.MAX_VALUE)  
            { 
                JOptionPane.showMessageDialog(this, "Oops! You didn't reach the end."); 
            }  
            else if (movesCount == algorithmMovesCount)  
            { 
                JOptionPane.showMessageDialog(this, "Congratulations! You reached the " + "end using the shortest path of " + movesCount + " moves!"); 
                resetGame(true); 
            } 
            else  
            { 
                JOptionPane.showMessageDialog(this, "You reached the end, but you didn't use the " + "shortest path. You used " + movesCount + " moves. Try again!"); 
                resetGame(false);
            } 
        } 
    }

    private void resetGame(boolean regenerateMaze)  
    { 
        playerX = startX; 
        playerY = startY; 
        movesCount = 0; 

        if (regenerateMaze) { 
            generateMaze(); 
            ensurePath(); 
            calculateDistances(); 
        }

        repaint(); 
    } 
 
 
    private void calculateDistances()  
    { 
        distances = new int[MAZE_SIZE][MAZE_SIZE]; 
        for (int i = 0; i < MAZE_SIZE; i++)  
        { 
            for (int j = 0; j < MAZE_SIZE; j++)  
            {    
                distances[i][j] = Integer.MAX_VALUE; 
            } 
        } 
        PriorityQueue<Point> queue = new PriorityQueue<>(Comparator.comparingInt(p -> distances[p.y][p.x])); 
        distances[startY][startX] = 0; 
        queue.add(new Point(startX, startY)); 
        int[] dx = {1, 0, -1, 0}; 
        int[] dy = {0, 1, 0, -1}; 
        while (!queue.isEmpty())  
        { 
            Point current = queue.poll(); 
            int x = current.x; 
            int y = current.y; 
            for (int i = 0; i < 4; i++)  
            { 
                int nx = x + dx[i]; 
                int ny = y + dy[i]; 
                if (nx >= 0 && nx < MAZE_SIZE && ny >= 0 && ny < MAZE_SIZE && maze[ny][nx] == 0)  
                { 
                    int newDistance = distances[y][x] + 1; 
                    if (newDistance < distances[ny][nx])  
                    { 
                        distances[ny][nx] = newDistance; 
                        queue.add(new Point(nx, ny)); 
                    } 
                } 
            } 
        } 
        algorithmMovesCount = distances[endY][endX]; 
    } 
 
    protected void paintComponent(Graphics g)  
    { 
        super.paintComponent(g); 
        int offsetX = (getWidth() - MAZE_SIZE * TILE_SIZE) / 2; 
        int offsetY = (getHeight() - MAZE_SIZE * TILE_SIZE) / 2; 
        for (int y = 0; y < MAZE_SIZE; y++)  
        { 
            for (int x = 0; x < MAZE_SIZE; x++)  
            { 
                if (maze[y][x] == 1)  
                { 
                    g.setColor(Color.BLACK); 
                }  
                else  
                { 
                    g.setColor(Color.WHITE); 
                } 
                g.fillRect(offsetX + x * TILE_SIZE, offsetY + y * TILE_SIZE, TILE_SIZE,  TILE_SIZE); 
            } 
        } 
        g.setColor(Color.GREEN); 
        g.fillRect(offsetX + startX * TILE_SIZE, offsetY + startY * TILE_SIZE, TILE_SIZE, TILE_SIZE); 
        g.setColor(Color.BLUE); 
        g.fillRect(offsetX + endX * TILE_SIZE, offsetY + endY * TILE_SIZE, TILE_SIZE, TILE_SIZE); 
        g.setColor(Color.RED); 
        g.fillRect(offsetX + playerX * TILE_SIZE, offsetY + playerY * TILE_SIZE, TILE_SIZE, TILE_SIZE); 
    } 
}  