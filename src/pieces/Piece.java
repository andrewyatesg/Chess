package pieces;

public interface Piece
{
    public PieceColor getColor();

    public void setColor(PieceColor color);

    /**
     * Only checks if move is legal concerning the piece type. Does not take
     * into consideration whether it moved on its own color, a piece is in the
     * way of its move, who's turn it is, etc.
     *
     * @param row
     * @param col
     * @param initrow
     * @param initcol
     * @return
     */
    public boolean moveCheck(int row, int col, int initrow, int initcol);
}
