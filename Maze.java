import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Maze
{
    static private Wall[][] walls;
    static private DisjSets cells;
    static private int rows, cols;

    static private void generateMaze()
    {
        cells = new DisjSets(cols * rows);
        // randomly knock down walls
        while(cells.find(0) != cells.find(rows*cols-1)) {
            int u = ThreadLocalRandom.current().nextInt(0, rows);
            int v = ThreadLocalRandom.current().nextInt(0, cols);
            Wall wall = walls[u][v];
            if(wall.right != -1 && cells.find(wall.self) != cells.find(wall.right))
            {
                cells.union(cells.find(wall.self), cells.find(wall.right));
                wall.right = -1;
            }
            if(wall.bottom != -1 && cells.find(wall.self) != cells.find(wall.bottom))
            {
                cells.union(cells.find(wall.self), cells.find(wall.bottom));
                wall.bottom= -1;
            }
        }
    }

    static public void generateWalls()
    {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                walls[i][j] = new Wall((i*cols)+j, -1, -1);
        Wall thisWall;
        // wall between cells (right and bottom walls)
        for (int i = 0; i < rows-1; i++) {
            for (int j = 0; j < cols-1; j++) {
                thisWall = walls[i][j];
                thisWall.right = thisWall.self+1;
                thisWall.bottom = thisWall.self+cols;
            }
            // last column only bottom walls
            thisWall = walls[i][cols-1];
            thisWall.bottom = thisWall.self+cols; //self, bottom
            thisWall.right = thisWall.self;
        }
        // last row, only right walls
        for(int j=0; j<cols-1; j++) {
            thisWall = walls[rows-1][j];
            thisWall.bottom = thisWall.self;
            thisWall.right = thisWall.self+1;
        }
    }

    static public void printMaze()
    {
        // dummy top border
        for (int i = 0; i < cols; i++)
            if (i == 0) System.out.print("  ");
            else System.out.print(" _");

        System.out.println();

        for (int i = 0; i < rows; i++) {
            if (i == 0) System.out.print(' ');
            else System.out.print('|');// dummy left border
            for (int j = 0; j < cols; j++) {
                if (walls[i][j].bottom != -1)
                    System.out.print('_');
                else
                    System.out.print(' ');

                if (walls[i][j].right != -1)
                    System.out.print('|');
                else
                    System.out.print(' ');
            }
            System.out.println();
        }
    }

    public static void main(String[] args)
    {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter a width(columns): ");
        cols = s.nextInt();
        System.out.print("Enter a height(rows): ");
        rows = s.nextInt();
        walls = new Wall[rows][cols];

        generateWalls();
        //printMaze();
        generateMaze();
        printMaze();
    }
}

class Wall
{
    int self;
    int right;
    int bottom;

    public Wall() {}
    public Wall(int self, int right, int bottom)
    {
        this.self = self;
        this.right = right;
        this.bottom = bottom;
    }
}
