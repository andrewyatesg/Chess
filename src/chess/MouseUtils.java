package chess;

import gui.GuiComponent;

public class MouseUtils
{
    public static int getRow(int y) throws OutOfBoardException
    {
        if (Chess.getY(y) < Chess.getY(0) || Chess.getY(y) > Chess.getY(Chess.WINDOW_HEIGHT))
        {
            throw new OutOfBoardException(-1, Chess.getY(y));
        }

        int ydelta = Chess.getHeight2() / Chess.NUM_ROWS;

        int row = 0;
        while (true)
        {
            if (y < Chess.getY((row + 1) * ydelta + 2))
            {
                return row;
            }

            row++;
        }
    }

    public static int getCol(int x) throws OutOfBoardException
    {
        if (Chess.getX(x) < Chess.getX(0) || Chess.getX(x) > Chess.getX(Chess.WINDOW_WIDTH))
        {
            throw new OutOfBoardException(Chess.getX(x), -1);
        }

        int xdelta = Chess.getWidth2() / Chess.NUM_COLUMNS;

        int col = 0;
        while (true)
        {
            if (x < Chess.getX((col + 1) * xdelta + 2))
            {
                return col;
            }

            col++;
        }
    }

    public static GuiComponent getComp(int x, int y) throws OutOfBoardException
    {
        if ((x) < Chess.getX(0) || (x) > Chess.getX(Chess.WINDOW_WIDTH) || (y) < Chess.getY(0) || (y) > Chess.getY(Chess.WINDOW_HEIGHT))
        {
            throw new OutOfBoardException((x), (y));
        }

        for (GuiComponent b : Chess.mainMenu.getComponents())
        {
            int bX = (b.getX());
            int bY = (b.getY());
            int bX2 = (b.getX() + b.getWidth());
            int bY2 = (b.getY() + b.getHeight());

            if (x > bX && x < bX2 && y > bY && y < bY2)
            {
                return b;
            }
        }

        throw new OutOfBoardException((x), (y));
    }
}
