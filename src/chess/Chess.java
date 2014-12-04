package chess;

import gui.GuiButton;
import gui.GuiComponent;
import gui.GuiTextfield;
import gui.MainMenu;
import network.ChessClientHandler;
import network.ChessServerHandler;
import pieces.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class Chess extends JFrame implements Runnable
{
    public static final int XBORDER = 20;
    public static final int YBORDER = 20;
    public static final int YTITLE = 25;
    public static final int WINDOW_WIDTH = 963;
    public static final int WINDOW_HEIGHT = 988;
    final public static int NUM_ROWS = 8;
    final public static int NUM_COLUMNS = 8;
    public static boolean animateFirstTime = true;
    public static int xsize = -1;
    public static int ysize = -1;
    public static Image image;
    public static Image background;
    public static Graphics2D g;
    /**
     * Variables to do with gameplay
     */
    public static boolean gameStarted = false;
    public static Piece[][] board = new Piece[NUM_ROWS][NUM_COLUMNS];
    public static boolean myTurn;
    public static int hoverRow;
    public static int hoverCol;
    public static PieceColor winner = null;
    public static boolean finalBattle = false;
    /**
     * Variables to do with main menu
     */
    public static MainMenu mainMenu;
    public static boolean viewingHelp = false;
    /**
     * Colors
     */
    public static Color LIGHT_BROWN = new Color(227, 209, 174);
    public static Color DARK_BROWN = new Color(85, 63, 21);
    /**
     * Multiplayer variables
     */
    public static boolean isConnecting = false;
    public static boolean isClient;
    Thread relaxer;

    public Chess()
    {
        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                if (e.BUTTON1 == e.getButton())
                {
                    if (gameStarted)
                    {
                        if (!PieceMoveUtil.isDragging())
                        {
                            PieceMoveUtil.startDragging(e.getX(), e.getY());
                        }
                    }
                    else if (!isConnecting && !viewingHelp)
                    {
                        try
                        {
                            if (MouseUtils.getComp(e.getX(), e.getY()) instanceof GuiButton)
                            {
                                ((GuiButton) MouseUtils.getComp(e.getX(), e.getY())).getAct().run();
                            }
                            else
                            {
                                mainMenu.unselectFields();
                                ((GuiTextfield) MouseUtils.getComp(e.getX(), e.getY())).setSelected(true);
                            }
                        }
                        catch (OutOfBoardException e1)
                        {
                            System.out.println(e1.getX() + ", " + e1.getY() + " -> Out of bounds");
                        }
                    }
                }

                repaint();

            }
        });

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.BUTTON1 == e.getButton())
                {
                    if (gameStarted && PieceMoveUtil.isDragging())
                    {
                        PieceMoveUtil.movePiece(e.getX(), e.getY());
                    }
                }

                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                if (gameStarted && PieceMoveUtil.isDragging())
                {
                    PieceMoveUtil.updateDrag(e.getX(), e.getY());
                }

                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseMoved(MouseEvent e)
            {
                try
                {
                    hoverRow = MouseUtils.getRow(e.getY());
                    hoverCol = MouseUtils.getCol(e.getX());
                }
                catch (OutOfBoardException e2)
                {
                }

                try
                {
                    if (MouseUtils.getComp(e.getX(), e.getY()) instanceof GuiButton)
                    {
                        ((GuiButton) MouseUtils.getComp(e.getX(), e.getY())).setColor(Color.red);
                        ((GuiButton) MouseUtils.getComp(e.getX(), e.getY())).setPop(true);
                    }
                    else
                    {
                        for (GuiComponent b : mainMenu.getComponents())
                        {
                            if (b instanceof GuiButton)
                            {
                                ((GuiButton) b).setColor(Color.black);
                                ((GuiButton) b).setPop(false);
                            }
                        }
                    }
                }
                catch (OutOfBoardException e1)
                {
                    for (GuiComponent b : mainMenu.getComponents())
                    {
                        if (b instanceof GuiButton)
                        {
                            ((GuiButton) b).setColor(Color.black);
                            ((GuiButton) b).setPop(false);
                        }
                    }
                }

                repaint();
            }
        });

        addKeyListener(new KeyAdapter()
        {

            public void keyPressed(KeyEvent e)
            {
                if (!gameStarted && !isConnecting && !viewingHelp)
                {
                    for (GuiComponent guiComponent : mainMenu.getComponents())
                    {
                        if (guiComponent instanceof GuiTextfield)
                        {
                            if (((GuiTextfield) guiComponent).isSelected())
                            {
                                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                                    ((GuiTextfield) guiComponent).delChar();
                                else
                                    ((GuiTextfield) guiComponent).appendChar(e.getKeyChar());
                            }
                        }
                    }
                }
                else
                {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !isConnecting)
                    {
                        if (gameStarted)

                            if (isClient)
                            {
                                ChessClientHandler.sendDisconnect();
                                ChessClientHandler.disconnect();
                            }
                            else
                            {
                                ChessServerHandler.sendDisconnect();
                                ChessServerHandler.disconnect();
                            }
                        gameStarted = false;
                        viewingHelp = false;
                        reset();
                    }
                }

                repaint();
            }
        });
        init();
        start();
    }

    public static void main(String[] args)
    {
        Chess frame = new Chess();
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setTitle("Chess");
        frame.setResizable(false);
    }

    /**
     * Resets all variables and restarts game
     */
    public static void reset()
    {
        mainMenu = new MainMenu();
        mainMenu.init();
        board = new Piece[NUM_ROWS][NUM_COLUMNS];
        board[0][0] = new PieceRook(PieceColor.BLACK);
        board[0][7] = new PieceRook(PieceColor.BLACK);
        board[7][0] = new PieceRook(PieceColor.WHITE);
        board[7][7] = new PieceRook(PieceColor.WHITE);
        board[0][5] = new PieceBishop(PieceColor.BLACK);
        board[0][2] = new PieceBishop(PieceColor.BLACK);
        board[7][5] = new PieceBishop(PieceColor.WHITE);
        board[7][2] = new PieceBishop(PieceColor.WHITE);
        board[0][6] = new PieceKnight(PieceColor.BLACK);
        board[0][1] = new PieceKnight(PieceColor.BLACK);
        board[7][6] = new PieceKnight(PieceColor.WHITE);
        board[7][1] = new PieceKnight(PieceColor.WHITE);
        board[0][3] = new PieceQueen(PieceColor.BLACK);
        board[7][3] = new PieceQueen(PieceColor.WHITE);
        board[0][4] = new PieceKing(PieceColor.BLACK);
        board[7][4] = new PieceKing(PieceColor.WHITE);
        board[1][0] = new PiecePawn(PieceColor.BLACK);
        board[1][1] = new PieceJumpPawn(PieceColor.BLACK);
        board[1][2] = new PiecePawn(PieceColor.BLACK);
        board[1][3] = new PiecePawn(PieceColor.BLACK);
        board[1][4] = new PiecePawn(PieceColor.BLACK);
        board[1][5] = new PiecePawn(PieceColor.BLACK);
        board[1][6] = new PieceJumpPawn(PieceColor.BLACK);
        board[1][7] = new PiecePawn(PieceColor.BLACK);
        board[6][0] = new PiecePawn(PieceColor.WHITE);
        board[6][1] = new PieceJumpPawn(PieceColor.WHITE);
        board[6][2] = new PiecePawn(PieceColor.WHITE);
        board[6][3] = new PiecePawn(PieceColor.WHITE);
        board[6][4] = new PiecePawn(PieceColor.WHITE);
        board[6][5] = new PiecePawn(PieceColor.WHITE);
        board[6][6] = new PieceJumpPawn(PieceColor.WHITE);
        board[6][7] = new PiecePawn(PieceColor.WHITE);
        
        background= Toolkit.getDefaultToolkit().getImage("resources/background.png");
    }

    public static void playSound(final String url)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            new BufferedInputStream(new FileInputStream(new File(url))));
                    clip.open(inputStream);
                    clip.start();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // ///////////////////////////////////////////////////////////////////////
    public static int getX(int x)
    {
        return (x + XBORDER);
    }

    public static int getY(int y)
    {
        return (y + YBORDER + YTITLE);
    }

    public static int getYNormal(int y)
    {
        return (-y + YBORDER + YTITLE + getHeight2());
    }

    public static int getWidth2()
    {
        return (xsize - getX(0) - XBORDER);
    }

    public static int getHeight2()
    {
        return (ysize - getY(0) - YBORDER);
    }

    private boolean isInt(char c)
    {
        try
        {
            Integer.parseInt(String.valueOf(c));
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    // //////////////////////////////////////////////////////////////////////////
    public void init()
    {
        requestFocus();
    }

    // //////////////////////////////////////////////////////////////////////////
    public void destroy()
    {
    }

    /**
     * Paints the graphic
     */
    public void paint(Graphics gOld)
    {
        if (image == null || xsize != getSize().width || ysize != getSize().height)
        {
            xsize = getSize().width;
            ysize = getSize().height;
            image = createImage(xsize, ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        if (animateFirstTime)
        {
            gOld.drawImage(image, 0, 0, null);
            return;
        }

        int x[] = {getX(0), getX(getWidth2()), getX(getWidth2()), getX(0), getX(0)};
        int y[] = {getY(0), getY(0), getY(getHeight2()), getY(getHeight2()), getY(0)};
        int ydelta = getHeight2() / NUM_ROWS;
        int xdelta = getWidth2() / NUM_COLUMNS;
        // put all paint commands under this line

        
        
        
        // far outer border
        g.setColor(Color.black);
        g.fillRect(0, 0, xsize, ysize);
        // ----------------

        // background
        g.setColor(LIGHT_BROWN);
        g.fillPolygon(x, y, 4);
        
       if(!gameStarted)
        g.drawImage(background,getX(0),getY(0),getWidth2(),getHeight2(),this);

        // ---------------
        if (gameStarted && winner == null)
        {
            // line around board
            g.setColor(Color.yellow);
            g.drawPolyline(x, y, 5);

            // ----------------

            /**
             * Drawing the board.
             */
            boolean alt = false;
            for (int zi = 0; zi < NUM_ROWS; zi++)
            {
                for (int zx = 0; zx < NUM_COLUMNS; zx++)
                {
                    if (alt)
                    {
                        g.setColor(DARK_BROWN);
                        g.fillRect(getX(zx * xdelta + 2), getY(zi * ydelta + 2), xdelta, ydelta);
                    }
                    alt = !alt;
                }
                alt = !alt;
            }

            paintPieces();
            /**
             * Drawing dragged piece
             */
            if (PieceMoveUtil.isDragging())
            {
                String path = "resources/" + PieceMoveUtil.getDragging().getClass().getSimpleName().substring(5).toLowerCase() + (PieceMoveUtil.getDragging().getColor() == PieceColor.WHITE ? "white" : "black") + ".gif";
                if (new File(path).exists())
                {
                    drawScaledImage(Toolkit.getDefaultToolkit().getImage(path), PieceMoveUtil.getCurrentDragX() - PieceMoveUtil.getDragXOffset(), PieceMoveUtil.getCurrentDragY() - PieceMoveUtil.getDragYOffset(), xdelta, ydelta);
                }
                else
                {
                    g.setColor(PieceMoveUtil.getDragging().getColor() == PieceColor.WHITE ? Color.white : Color.black);
                    g.fillRect(PieceMoveUtil.getCurrentDragX() - PieceMoveUtil.getDragXOffset(), PieceMoveUtil.getCurrentDragY() - PieceMoveUtil.getDragYOffset(), 100, 100);
                }
            }
            else
            {
                if (Highlight.getPoints(hoverRow, hoverCol) != null)
                    for (Point p : Highlight.getPoints(hoverRow, hoverCol))
                    {
                        int x1 = (int) p.getX();
                        int y1 = (int) p.getY();
                        g.setColor(Color.YELLOW);
                        String simplName = board[hoverRow][hoverCol].getClass().getSimpleName().substring(5).toLowerCase() + board[hoverRow][hoverCol].getColor().getName() + ".png";
                        String path = "resources/ghost" + simplName;
                        if (new File(path).exists())
                        {
                            drawScaledImage(Toolkit.getDefaultToolkit().getImage(path), (int) p.getX() * xdelta + 20, (int) p.getY() * ydelta + 45, xdelta, ydelta);
                        }
                        else
                        {
                            g.fillRect(getX(x1 * xdelta + 2), getY(y1 * ydelta + 2), xdelta, ydelta);
                        }
                    }
            }
        }
        else if (winner != null)
        {
            g.setFont(new Font("Comic Sans", Font.ROMAN_BASELINE, 20));
            g.setColor(Color.black);
            g.drawString("GAME OVER! Winner is " + winner.getName(), getWidth2() / 2 - (g.getFontMetrics(g.getFont()).stringWidth("GAME OVER! Winner is " + winner.getName()) / 2), getHeight2() / 2);
        }
        else if (isConnecting)
        {
            g.setFont(new Font("Comic Sans", Font.ROMAN_BASELINE, 20));
            g.setColor(Color.black);
            g.drawString("Connecting/Waiting for Connection...", getWidth2() / 2 - (g.getFontMetrics(g.getFont()).stringWidth("Connecting/Waiting for Connection...") / 2), getHeight2() / 2);
        }
        else
        {
            mainMenu.paint(this); // Paints main menu and help menu if viewing
            // it
        }

        // put all paint commands above this line
        gOld.drawImage(image, 0, 0, null);
    }

    /**
     *
     */
    public void paintPieces()
    {
        int xdelta = getWidth2() / NUM_COLUMNS;
        int ydelta = getHeight2() / NUM_ROWS;

        for (int i = 0; i < NUM_ROWS; i++)
        {
            for (int j = 0; j < NUM_COLUMNS; j++)
            {
                if (board[i][j] != null && PieceMoveUtil.getDragging() != board[i][j])
                {
                    String path = "resources/" + board[i][j].getClass().getSimpleName().substring(5).toLowerCase() + board[i][j].getColor().getName() + ".gif";
                    if (new File(path).exists())
                    {
                        drawScaledImage(Toolkit.getDefaultToolkit().getImage(path), j * xdelta + 20, i * ydelta + 45, xdelta, ydelta);
                    }
                    else
                    {
                        g.setColor(board[i][j].getColor() == PieceColor.WHITE ? Color.white : Color.black);
                        g.fillRect(getX(j * xdelta + 2), getY(i * ydelta + 2), 100, 100);
                    }
                }
            }
        }
    }

    // ///////////////////////////////////////////////////////////////////////////
    public void drawImage(Image image, int xpos, int ypos, double rot, double xscale, double yscale)
    {
        int width = image.getWidth(this);
        int height = image.getHeight(this);
        g.translate(xpos, ypos);
        g.rotate(rot * Math.PI / 180.0);
        g.scale(xscale, yscale);

        g.drawImage(image, 0, 0, width, height, this);

        g.scale(1.0 / xscale, 1.0 / yscale);
        g.rotate(-rot * Math.PI / 180.0);
        g.translate(-xpos, -ypos);
    }

    public void drawScaledImage(Image i, int x, int y, int width, int height)
    {
        drawImage(resize(i, width, height), x, y, 0, 1, 1);
    }

    public Image resize(Image image, int width, int height)
    {
        Image bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) (((BufferedImage) bi).createGraphics());
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return bi;
    }
    
    public static boolean isFinalBat()
    {
    	for (int i = 0; i < NUM_ROWS; i++)
    	{
    		for (int j = 0; j < NUM_COLUMNS; j++)
    		{
    			if (board[i][j] instanceof PiecePawn || board[i][j] instanceof PieceJumpPawn)
    			{
    				return false;
    			}
    		}
    	}
    	
    	return true;
    }
    
    public static void setFinalBattle()
    {
    	finalBattle = true;
    	
    	board = new Piece[NUM_ROWS][NUM_COLUMNS];
    	
    	board[1][2] = new PieceKnight(PieceColor.BLACK);
    	board[1][5] = new PieceKnight(PieceColor.BLACK);
    	
    	board[2][3] = new PieceKing(PieceColor.BLACK);
    	board[2][4] = new PieceKing(PieceColor.BLACK);
    	board[5][3] = new PieceKing(PieceColor.WHITE);
    	board[5][4] = new PieceKing(PieceColor.WHITE);
    	
    	board[6][2] = new PieceKnight(PieceColor.WHITE);
    	board[6][5] = new PieceKnight(PieceColor.WHITE);
    	
    	if (isClient)
    	{
    		myTurn = true;
    	}
    	else
    	{
    		myTurn = false;
    	}
    }

    // //////////////////////////////////////////////////////////////////////////
    // needed for implement runnable
    public void run()
    {
        while (true)
        {
            update();
            repaint();
            double seconds = .1; // time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try
            {
                Thread.sleep(miliseconds);
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    /**
     * Updates state of game
     */
    public void update()
    {

        if (animateFirstTime)
        {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height)
            {
                xsize = getSize().width;
                ysize = getSize().height;
            }

            reset();
        }
    }

    // //////////////////////////////////////////////////////////////////////////
    public void start()
    {
        if (relaxer == null)
        {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }

    // //////////////////////////////////////////////////////////////////////////
    public void stop()
    {
        if (relaxer.isAlive())
        {
            relaxer.stop();
        }
        relaxer = null;
    }
}
