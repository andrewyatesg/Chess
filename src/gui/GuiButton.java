package gui;

import java.awt.*;

public class GuiButton implements GuiComponent
{
    private String name;
    private int x;
    private int y;
    private int width;
    private int height;
    private Runnable act;
    private Color color;
    private boolean pop;
    private String imgPath;

    public GuiButton(String name, int x, int y, int width, int height, String imgPath, Runnable act)
    {
        super();
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.act = act;
        this.imgPath = imgPath;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public Runnable getAct()
    {
        return act;
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public String getImgPath()
    {
        return imgPath;
    }

    public void setImgPath(String imgPath)
    {
        this.imgPath = imgPath;
    }

    public boolean isPop()
    {
        return pop;
    }

    public void setPop(boolean pop)
    {
        this.pop = pop;
    }
}
