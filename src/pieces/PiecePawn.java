package pieces;

import chess.Chess;

public class PiecePawn implements Piece
{

    PieceColor color;
    boolean firstMove;

    public PiecePawn(PieceColor c)
    {
        color = c;
        firstMove = true;
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

    public boolean getFirstMove()
    {
        return firstMove;
    }

    public void setFirstMove(boolean move)
    {
        this.firstMove = move;
    }

    @Override
    public boolean moveCheck(int row, int col, int initrow, int initcol)
    {
        boolean pieceExists = Chess.board[row][col] != null;

        if (this.color == PieceColor.WHITE)
        {
            if (row == initrow - (firstMove ? 2 : 1) || row == initrow - 1)
            {
                if ((col == initcol && !pieceExists) || ((col == initcol - 1 || col == initcol + 1) && pieceExists && row == initrow - 1))
                {
                    return true;
                }
            }
        }
        else
        {
            if (row == initrow + (firstMove ? 2 : 1) || row == initrow + 1)
            {
                if ((col == initcol && !pieceExists) || ((col == initcol - 1 || col == initcol + 1) && pieceExists && row == initrow + 1))
                {
                    return true;
                }
            }
        }

        return false;
    }
}
