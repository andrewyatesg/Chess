package pieces;

public class PieceKnight implements Piece
{

    PieceColor color;

    public PieceKnight(PieceColor c)
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

        if ((row == row1 && col == col1) || (row == row2 && col == col2) || (row == row3 && col == col3) || (row == row4 && col == col4) || (row == row5 && col == col5) || (row == row6 && col == col6) || (row == row7 && col == col7) || (row == row8 && col == col8))
        {
            return true;
        }

        return false;
    }

}
