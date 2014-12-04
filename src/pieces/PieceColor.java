package pieces;

public enum PieceColor
{
    BLACK("black"), WHITE("white");

    private String name;

    private PieceColor(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
