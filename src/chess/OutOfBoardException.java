package chess;

public class OutOfBoardException extends Exception
{
    private int x = -1;
    private int y = -1;

    public OutOfBoardException(int x, int y)
    {
        super();
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}
