package gui;

import chess.Chess;
import chess.TextGrabber;
import network.ChessClientHandler;
import network.ChessServerHandler;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainMenu
{
    private ArrayList<GuiComponent> comp = new ArrayList<GuiComponent>();
    private int num = 0;

    public void init()
    {
        comp.add(new GuiTextfield("IP Address:", 25, Chess.getHeight2() / 2 - 150, 300, 50, 16));

        comp.add(new GuiButton("Play as Client", 25, Chess.getHeight2() / 2 - 60, 300, 90, "", new Runnable()
        {

            @Override
            public void run()
            {

                if (getActiveField() != null && getActiveField().getText() != null && !getActiveField().getText().equalsIgnoreCase(""))
                {
                    new Thread(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            try
                            {
                                Chess.playSound("resources/button.wav");
                                Chess.isConnecting = true;
                                ChessClientHandler.connect(getActiveField().getText(), 5657);
                                if (ChessClientHandler.connected)
                                {
                                    Chess.isClient = true;
                                    Chess.myTurn = true;
                                    Chess.gameStarted = true;
                                    Chess.isConnecting = false;
                                }
                            }
                            catch (IOException e)
                            {
                                System.out.println("Cannot join server: " + e.getMessage());
                                Chess.isConnecting = false;
                            }
                        }
                    }).start();
                }
            }
        }));

        comp.add(new GuiButton("Play as Server", 25, Chess.getHeight2() / 2 + 100, 300, 90, "", new Runnable()
        {

            @Override
            public void run()
            {
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            Chess.playSound("resources/button.wav");
                            Chess.isConnecting = true;
                            ChessServerHandler.recieveConnect(5657);
                            if (ChessServerHandler.connected)
                            {
                                Chess.isClient = false;
                                Chess.myTurn = false;
                                Chess.gameStarted = true;
                                Chess.isConnecting = false;
                            }
                        }
                        catch (IOException e)
                        {
                            System.out.println("Cannot host server: " + e.getMessage());
                            Chess.isConnecting = false;
                        }
                    }
                }).start();
            }
        }));

        comp.add(new GuiButton("Help Menu", 25, Chess.getHeight2() / 2 + 250, 300, 90, "", new Runnable()
        {

            @Override
            public void run()
            {
                Chess.playSound("resources/button.wav");
                Chess.viewingHelp = true;
            }
        }));
    }

    public void reset()
    {

    }

    public void paint(Chess chess)
    {
        Graphics2D g = Chess.g;

        if (Chess.viewingHelp)
        {
            TextGrabber textGrabber = new TextGrabber("resources/instruct.txt");
            ArrayList<String> text = textGrabber.getLines();

            for (int i = 0; i < text.size(); i++)
            {
                g.setColor(Color.black);
                g.setFont(new Font("Comic Sans", Font.ROMAN_BASELINE, 15));
                g.drawString(text.get(i), Chess.getX(100), 15 + Chess.getY(i * 20));
            }
        }
        else
        {
            for (GuiComponent b : comp)
            {
                if (b instanceof GuiButton)
                {
                	Map<TextAttribute, Integer> fontAttributes = new HashMap<TextAttribute, Integer>();
                	fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                    g.setFont(new Font("Comic Sans", Font.ROMAN_BASELINE, 20).deriveFont(fontAttributes));
                    chess.drawScaledImage(Toolkit.getDefaultToolkit().getImage(((GuiButton) b).isPop() ? "resources/button.gif" : "resources/cbutton.gif"), b.getX(), b.getY(), b.getWidth(), b.getHeight());
                    g.setColor(((GuiButton) b).getColor());
                    g.drawString(((GuiButton) b).getName(), b.getX() + 15, b.getY() + 30);
                }
                else
                {
                    g.setFont(new Font("Comic Sans", Font.ROMAN_BASELINE, 20));
                    g.setColor(Color.black);
                    g.drawString(((GuiTextfield) b).getName(), b.getX(), b.getY() - 5);
                    RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(b.getX(), b.getY(), b.getWidth(), b.getHeight(), 15, 15);
                    g.draw(roundedRectangle);
                    g.setColor(Color.black);
                    g.drawString(((GuiTextfield) b).getText(), b.getX(), b.getY() + 30);
                    if (num % 3 == 2 && ((GuiTextfield) b).isSelected())
                    {
                        g.drawString("|", b.getX() + g.getFontMetrics(g.getFont()).stringWidth(((GuiTextfield) b).getText()), b.getY() + 30);
                    }
                }
            }

            try
            {
                g.setFont(new Font("Comic Sans", Font.ROMAN_BASELINE, 20));
                g.setColor(Color.black);
                g.drawString("Your IP address: " + InetAddress.getLocalHost().getHostAddress(), Chess.getX(10), Chess.getY(20));
            }
            catch (UnknownHostException e)
            {
                e.printStackTrace();
            }
            
            g.setFont(new Font("Comic Sans", Font.ROMAN_BASELINE, 15));
            g.setColor(Color.black);
            g.drawString("By: Andrew Yates and William Stamant", Chess.getX(Chess.getWidth2() - g.getFontMetrics(g.getFont()).stringWidth("By: Andrew Yates and William Stamant")) - 5, Chess.getY(Chess.getHeight2()) - 5);
        }
        num++;
    }

    public ArrayList<GuiComponent> getComponents()
    {
        return comp;
    }

    public void unselectFields()
    {
        for (GuiComponent guiComponent : comp)
        {
            if (guiComponent instanceof GuiTextfield)
            {
                ((GuiTextfield) guiComponent).setSelected(false);
            }
        }
    }

    public GuiTextfield getActiveField()
    {
        for (GuiComponent guiComponent : this.comp)
        {
            if (guiComponent instanceof GuiTextfield)
            {
                if (((GuiTextfield) guiComponent).isSelected())
                {
                    return (GuiTextfield) guiComponent;
                }
            }
        }

        return null;
    }
}
