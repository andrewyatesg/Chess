package chess;

import pieces.PiecePawn;

import java.awt.*;
import java.util.ArrayList;

public class HighlightOld
{
    public ArrayList<Point> rook(int initrow, int initcol)
    {
        ArrayList<Point> p = new ArrayList<Point>();
        {
            int y = 0;
            int x = initcol;

            while (true)
            {
                if (y >= Chess.NUM_ROWS)
                {
                    break;
                }
                p.add(new Point(y, x));
                y++;
            }
        }

        {
            int y = initrow;
            int x = 0;

            while (true)
            {
                if (x >= Chess.NUM_COLUMNS)
                {
                    break;
                }
                p.add(new Point(y, x));
                x++;
            }
        }

        return p;
    }

    public ArrayList<Point> bishop(int initrow, int initcol)
    {
        ArrayList<Point> p = new ArrayList<Point>();
        int startRow = -1;
        int startCol = -1;

        {
            int row1 = initrow;
            int col1 = initcol;

            if (row1 == col1)
            {
                startRow = 0;
                startCol = 0;
            }
            else
            {
                startCol = row1 > col1 ? 0 : col1 - row1;
                startRow = col1 > row1 ? 0 : row1 - col1;
            }

            int x = startCol;
            int y = startRow;
            while (true)
            {
                if (x >= Chess.NUM_COLUMNS || y >= Chess.NUM_ROWS)
                {
                    break;
                }
                p.add(new Point(y, x));
                x++;
                y++;
            }
        }

        {
            startRow = -1;
            startCol = -1;

            int row1 = initrow;
            int col1 = Chess.NUM_COLUMNS - initcol - 1;

            if (row1 == col1)
            {
                startRow = 0;
                startCol = 7;
            }
            else
            {
                startCol = col1 > row1 ? Chess.NUM_COLUMNS - col1 + row1 - 1 : 7;
                startRow = row1 > col1 ? row1 - col1 : 0;
            }

            int x = startCol;
            int y = startRow;
            while (true)
            {
                if (x < 0 || y >= Chess.NUM_ROWS)
                {
                    break;
                }
                p.add(new Point(y, x));
                x--;
                y++;
            }
        }

        return p;
    }

    public ArrayList<Point> king(int initrow, int initcol)
    {
        ArrayList<Point> p = new ArrayList<Point>();
        int startRow = initrow - 1;
        int startCol = initcol - 1;

        for (int i = startRow; i <= startRow + 2; i++)
        {
            for (int j = startCol; j <= startCol + 2; j++)
            {
                if ((i == initrow && j == initcol) || (i < 0 || i >= Chess.NUM_ROWS || j < 0 || j >= Chess.NUM_COLUMNS))
                {
                    p.add(new Point(i, j));

                }

            }
        }

        return p;
    }

    public ArrayList<Point> queen(int initrow, int initcol)
    {
        ArrayList<Point> p = new ArrayList<Point>();
        {
            int y = 0;
            int x = initcol;

            while (true)
            {
                if (y >= Chess.NUM_ROWS)
                {
                    break;
                }

                p.add(new Point(y, x));
                y++;
            }
        }

        {
            int y = initrow;
            int x = 0;

            while (true)
            {
                if (x >= Chess.NUM_COLUMNS)
                {
                    break;
                }

                p.add(new Point(y, x));
                x++;
            }
        }

        int startRow = -1;
        int startCol = -1;

        {
            int row1 = initrow;
            int col1 = initcol;

            if (row1 == col1)
            {
                startRow = 0;
                startCol = 0;
            }
            else
            {
                startCol = row1 > col1 ? 0 : col1 - row1;
                startRow = col1 > row1 ? 0 : row1 - col1;
            }

            int x = startCol;
            int y = startRow;
            while (true)
            {
                if (x >= Chess.NUM_COLUMNS || y >= Chess.NUM_ROWS)
                {
                    break;
                }

                p.add(new Point(y, x));
                x++;
                y++;
            }
        }

        {
            startRow = -1;
            startCol = -1;

            int row1 = initrow;
            int col1 = Chess.NUM_COLUMNS - initcol - 1;

            if (row1 == col1)
            {
                startRow = 0;
                startCol = 7;
            }
            else
            {
                startCol = col1 > row1 ? Chess.NUM_COLUMNS - col1 + row1 - 1 : 7;
                startRow = row1 > col1 ? row1 - col1 : 0;
            }

            int x = startCol;
            int y = startRow;
            while (true)
            {
                if (x < 0 || y >= Chess.NUM_ROWS)
                {
                    break;
                }

                p.add(new Point(y, x));
                x--;
                y++;
            }
        }

        return p;
    }

    public ArrayList<Point> knight(int initrow, int initcol)
    {
        ArrayList<Point> p = new ArrayList<Point>();

        int row1 = initrow - 2;
        int col1 = initcol - 1;

        int row2 = initrow - 1;
        int col2 = initcol - 2;

        int row3 = initrow + 1;
        int col3 = initcol - 2;

        int row4 = initrow + 2;
        int col4 = initcol - 1;

        int row5 = initrow + 2;
        int col5 = initcol + 1;

        int row6 = initrow + 1;
        int col6 = initcol + 2;

        int row7 = initrow - 1;
        int col7 = initcol + 2;

        int row8 = initrow - 2;
        int col8 = initcol + 1;

        p.add(new Point(row1, col1));
        p.add(new Point(row2, col2));
        p.add(new Point(row3, col3));
        p.add(new Point(row4, col4));
        p.add(new Point(row5, col5));
        p.add(new Point(row6, col6));
        p.add(new Point(row7, col7));
        p.add(new Point(row8, col8));

        return p;
    }

    public ArrayList<Point> pawn(int initrow, int initcol)
    {
        ArrayList<Point> p = new ArrayList<Point>();
        if (Chess.board[initrow][initcol] instanceof PiecePawn)
        {

            if (((PiecePawn) Chess.board[initrow][initcol]).getFirstMove())
            {
                int y = initrow + 2;
                int x = initcol + 2;
                p.add(new Point(y, x));
            }
            else
            {
                int y = initrow + 1;
                int x = initcol + 1;
                p.add(new Point(y, x));
            }

        }
        return p;
    }

}
