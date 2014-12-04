package gui;

public class GuiTextfield implements GuiComponent
{
    private String name;
    private String text = "";
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean isSelected;
    private int length;

    public GuiTextfield(String name, int x, int y, int width, int height, int length)
    {
        super();
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.length = length;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String name)
    {
        this.text = name;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void appendChar(char c)
    {
        if (text.length() < this.length)
        {
            text = text + c;
        }
    }

    public void delChar()
    {
        if (text.length() > 0)
        {
            text = text.substring(0, text.length() - 1);
        }
    }
}
