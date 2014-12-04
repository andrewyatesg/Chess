package pieces;

import chess.Chess;

public class PieceKing implements Piece
{

    PieceColor color;

    public PieceKing(PieceColor c)
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
        int startRow = initrow - 1;
        int startCol = initcol - 1;

        for (int i = startRow; i <= startRow + 2; i++)
        {
            for (int j = startCol; j <= startCol + 2; j++)
            {
                if ((i == initrow && j == initcol) || (i < 0 || i >= Chess.NUM_ROWS || j < 0 || j >= Chess.NUM_COLUMNS))
                {
                    continue;
                }

                if (row == i && col == j)
                {
                    return true;
                }
            }
        }

        return false;
    }

}
