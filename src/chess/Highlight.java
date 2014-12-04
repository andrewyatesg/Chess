package chess;

import pieces.PieceColor;

import java.awt.*;
import java.util.ArrayList;

public class Highlight
{
    public static ArrayList<Point> getPoints(int initRow, int initCol)
    {
        if ((initRow < 0 || initRow >= Chess.NUM_ROWS || initCol < 0 || initCol >= Chess.NUM_COLUMNS) || Chess.board[initRow][initCol] == null)
        {
            return null;
        }

        ArrayList<Point> points = new ArrayList<Point>();

        if (Chess.isClient)
        {
            if (Chess.board[initRow][initCol].getColor() == PieceColor.BLACK)
            {
                return null;
            }
        }
        else
        {
            if (Chess.board[initRow][initCol].getColor() == PieceColor.WHITE)
            {
                return null;
            }
        }

        for (int y = 0; y < Chess.NUM_ROWS; y++)
        {
            for (int x = 0; x < Chess.NUM_COLUMNS; x++)
            {
                if (!Chess.board[initRow][initCol].moveCheck(y, x, initRow, initCol))
                {
                    continue;
                }

                if (Chess.board[y][x] != null && Chess.board[y][x].getColor() == Chess.board[initRow][initCol].getColor())
                {
                    continue;
                }

                try
                {
                    if (!PiecePathCheck.check(initRow, initCol, y, x))
                    {
                        continue;
                    }
                }
                catch (OutOfBoardException e)
                {
                    continue;
                }

                points.add(new Point(x, y));
            }
        }

        return points;
    }
}
