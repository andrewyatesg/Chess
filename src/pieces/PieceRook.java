package pieces;

import chess.Chess;

public class PieceRook implements Piece
{
    PieceColor color;

    public PieceRook(PieceColor c)
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

        return false;
    }

}
