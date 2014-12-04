package pieces;

import chess.Chess;

public class PieceJumpPawn implements Piece
{

    PieceColor color;
    boolean firstMove;

    public PieceJumpPawn(PieceColor c)
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
            if (row == initrow - 2 && col == initcol + 2)
            {
                if (Chess.board[initrow - 1][initcol + 1] != null)
                {
                    return true;
                }
            }
            else if (row == initrow - 2 && col == initcol - 2)
            {
                if (Chess.board[initrow - 1][initcol - 1] != null)
                {
                    return true;
                }
            }
            else if ((row == initrow - (firstMove ? 2 : 1) || row == initrow - 1) && col == initcol && !pieceExists)
            {
                return true;
            }
        }
        else
        {
            if (row == initrow + 2 && col == initcol + 2)
            {
                if (Chess.board[initrow + 1][initcol + 1] != null)
                {
                    return true;
                }
            }
            else if (row == initrow + 2 && col == initcol - 2)
            {
                if (Chess.board[initrow + 1][initcol - 1] != null)
                {
                    return true;
                }
            }
            else if ((row == initrow + (firstMove ? 2 : 1) || row == initrow + 1) && col == initcol && !pieceExists)
            {
                return true;
            }
        }

        return false;
    }
}
