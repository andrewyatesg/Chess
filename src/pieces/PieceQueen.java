package pieces;

import chess.Chess;

public class PieceQueen implements Piece
{

    PieceColor color;

    public PieceQueen(PieceColor c)
    {
        color = c;
    }

    @Override
    public PieceColor getColor()
    {
        return (color);
    }

    @Override
    public void setColor(PieceColor c)
    {
        color = c;
    }

    @Override
    public boolean moveCheck(int row, int col, int initrow, int initcol)
    {
        {
            int y = 0;
            int x = initcol;

            while (true)
            {
                if (row == y && col == x)
                {
                    return true;
                }
                else if (y >= Chess.NUM_ROWS)
                {
                    break;
                }

                y++;
            }
        }

        {
            int y = initrow;
            int x = 0;

            while (true)
            {
                if (row == y && col == x)
                {
                    return true;
                }
                else if (x >= Chess.NUM_COLUMNS)
                {
                    break;
                }

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
                if (row == y && col == x)
                {
                    return true;
                }
                else if (x >= Chess.NUM_COLUMNS || y >= Chess.NUM_ROWS)
                {
                    break;
                }

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
                if (row == y && col == x)
                {
                    return true;
                }
                else if (x < 0 || y >= Chess.NUM_ROWS)
                {
                    break;
                }

                x--;
                y++;
            }
        }

        return false;
    }

}
