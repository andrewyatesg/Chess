package chess;

import pieces.*;

/**
 * Contains methods for each piece type (excluding knight and king) that check
 * if the path from his initial pos to his final pos is free from other pieces.
 *
 * @author 148003089
 */
public class PiecePathCheck
{
    // Returns true if path is legal

    public static boolean check(int initRow, int initCol, int postRow, int postCol) throws OutOfBoardException
    {
        if (Chess.board[initRow][initCol] == null)
        {
            throw new OutOfBoardException(initCol, initRow);
        }

        Piece p = Chess.board[initRow][initCol];

        if (p instanceof PiecePawn)
        {
            return pawn(initRow, initCol, postRow, postCol);
        }
        else if (p instanceof PieceJumpPawn)
        {
            return jumpPawn(initRow, initCol, postRow, postCol);
        }
        else if (p instanceof PieceRook)
        {
            return rook(initRow, initCol, postRow, postCol);
        }
        else if (p instanceof PieceBishop)
        {
            return bishop(initRow, initCol, postRow, postCol);
        }
        else if (p instanceof PieceQueen)
        {
            return queen(initRow, initCol, postRow, postCol);
        }
        else if (p instanceof PieceKnight)
        {
            return true;
        }
        else if (p instanceof PieceKing)
        {
            return true;
        }

        return false;
    }

    private static boolean pawn(int initRow, int initCol, int postRow, int postCol)
    {
        PiecePawn p = (PiecePawn) Chess.board[initRow][initCol];

        if (p.getFirstMove())
        {
            if (postRow == initRow - 2)
            {
                if (Chess.board[initRow - 1][initCol] != null)
                {
                    return false;
                }
            }
            else if (postRow == initRow + 2)
            {
                if (Chess.board[initRow + 1][initCol] != null)
                {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean jumpPawn(int initRow, int initCol, int postRow, int postCol)
    {
        PieceJumpPawn p = (PieceJumpPawn) Chess.board[initRow][initCol];

        if (p.getFirstMove())
        {
            if (postRow == initRow - 2 && postCol == initCol)
            {
                if (Chess.board[initRow - 1][initCol] != null)
                {
                    return false;
                }
            }
            else if (postRow == initRow + 2 && postCol == initCol)
            {
                if (Chess.board[initRow + 1][initCol] != null)
                {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean rook(int initRow, int initCol, int postRow, int postCol)
    {
        if (postCol > initCol)// checks if piece is moving right
        {
            for (int zi = postCol - 1; zi > initCol; zi--)// checks if a piece
            // is in the way
            {
                if (Chess.board[initRow][zi] != null)
                    return (false);
            }
        }
        else if (postCol < initCol)// checks if piece is moving left
        {
            for (int zi = postCol + 1; zi < initCol; zi++)// checks if a piece
            // is in the way
            {
                if (Chess.board[initRow][zi] != null)
                    return (false);
            }
        }

        if (postRow > initRow)// checks if piece is moving down
        {
            for (int zi = postRow - 1; zi > initRow; zi--)// checks if a piece
            // is in the way
            {
                if (Chess.board[zi][initCol] != null)
                    return (false);
            }
        }
        else if (postRow < initRow)// checks if piece is moving up
        {
            for (int zi = postRow + 1; zi < initRow; zi++)// checks if a piece
            // is in the way
            {
                if (Chess.board[zi][initCol] != null)
                    return (false);
            }
        }
        return (true);
    }

    private static boolean bishop(int initRow, int initCol, int postRow, int postCol)
    {
        Piece p = Chess.board[initRow][initCol];

        if (postCol > initCol) // Piece moves right
        {

            if (postRow > initRow) // Piece moves down
            {
                int x = postCol - 1;
                int y = postRow - 1;

                while (true)
                {

                    if (x <= initCol && y <= initRow)
                    {
                        break;
                    }
                    else if (Chess.board[y][x] != null)
                    {
                        return false;
                    }

                    x--;
                    y--;
                }
            }
            else
            // up
            {
                int x = postCol - 1;
                int y = postRow + 1;

                while (true)
                {

                    if (x <= initCol && y >= initRow)
                    {
                        break;
                    }
                    else if (Chess.board[y][x] != null)
                    {
                        return false;
                    }

                    x--;
                    y++;
                }
            }
        }
        else
        // left
        {
            if (postRow > initRow) // Piece moves down
            {
                int x = postCol + 1;
                int y = postRow - 1;

                while (true)
                {
                    if (x >= initCol && y <= initRow)
                    {
                        break;
                    }
                    else if (Chess.board[y][x] != null)
                    {
                        return false;
                    }

                    x++;
                    y--;
                }
            }
            else
            // up
            {
                int x = postCol + 1;
                int y = postRow + 1;

                while (true)
                {
                    if (x >= initCol && y >= initRow)
                    {
                        break;
                    }
                    else if (Chess.board[y][x] != null)
                    {
                        return false;
                    }

                    x++;
                    y++;
                }
            }
        }

        return true;
    }

    private static boolean queen(int initRow, int initCol, int postRow, int postCol)
    {
        return initRow != postRow && initCol != postCol ? bishop(initRow, initCol, postRow, postCol) : rook(initRow, initCol, postRow, postCol);
    }
}
