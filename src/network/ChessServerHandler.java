package network;

import chess.Chess;
import pieces.PieceColor;
import pieces.PieceJumpPawn;
import pieces.PieceKing;
import pieces.PiecePawn;
import pieces.PieceQueen;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class ChessServerHandler
{
    public static boolean connected = false;
    public static Point postPoints = null;
    public static Point initPoints = null;
    private static Socket client = null;
    private static PrintWriter serverOut = null;
    private static BufferedReader serverIn = null;

    public static void recieveConnect(int port) throws UnknownHostException, IOException, SocketTimeoutException
    {
        ServerSocket server = new ServerSocket(port);
        server.setSoTimeout(8000);
        client = server.accept();
        serverOut = new PrintWriter(client.getOutputStream(), true);
        serverIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
        connected = true;
        recievePieceMove();
    }

    public static void disconnect()
    {
        try
        {
            if (client != null)
                client.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        client = null;
        serverOut = null;
        serverIn = null;
        connected = false;
        Chess.gameStarted = false;
        Chess.reset();
    }

    public static void sendPieceMove(int row, int col, int initrow, int initcol)
    {
        if (connected)
        {
            serverOut.println(row + ":" + col + ":" + initrow + ":" + initcol);
            Chess.myTurn = false;
    		
    		if (Chess.isFinalBat() && !Chess.finalBattle)
    		{
				Chess.setFinalBattle();
    		}
        }
    }

    public static void sendDisconnect()
    {
        if (connected)
        {
            serverOut.println("esc");
        }
    }

    public static void updateBoard()
    {
        if (Chess.board[(int) postPoints.getY()][(int) postPoints.getX()] instanceof PieceKing)
        {
            Chess.winner = PieceColor.WHITE;
        }

		if (Chess.board[(int) initPoints.getY()][(int) initPoints.getX()] instanceof PiecePawn || Chess.board[(int) initPoints.getY()][(int) initPoints.getX()] instanceof PieceJumpPawn)
		{
			if (postPoints.getY() == 0)
			{
				Chess.board[(int) initPoints.getY()][(int) initPoints.getX()] = new PieceQueen(PieceColor.WHITE);
			}
		}
		
        Chess.board[(int) postPoints.getY()][(int) postPoints.getX()] = Chess.board[(int) initPoints.getY()][(int) initPoints.getX()];
        Chess.board[(int) initPoints.getY()][(int) initPoints.getX()] = null;
        postPoints = null;
        initPoints = null;
        Chess.myTurn = true;
		
		if (Chess.isFinalBat() && !Chess.finalBattle)
		{
			Chess.setFinalBattle();
		}
		Chess.playSound("resources/button.wav");
    }

    private static void recievePieceMove()
    {
        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                String inputLine;

                try
                {
                    while ((inputLine = serverIn.readLine()) != null)
                    {
                        try
                        {
                            if (inputLine.equals("esc"))
                            {
                                disconnect();
                                return;
                            }
                            // row:col:initrow:initcol
                            int ypost = Integer.parseInt(inputLine.split(":")[0]);
                            int xpost = Integer.parseInt(inputLine.split(":")[1]);
                            int yinit = Integer.parseInt(inputLine.split(":")[2]);
                            int xinit = Integer.parseInt(inputLine.split(":")[3]);

                            postPoints = (new Point(xpost, ypost));
                            initPoints = (new Point(xinit, yinit));

                            if (postPoints != null && initPoints != null)
                            {
                                updateBoard();
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            e.printStackTrace();
                        }
                        catch (NullPointerException e)
                        {
                            disconnect();
                        }
                    }
                }
                catch (SocketException e)
                {
                    disconnect();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public static boolean isConnected()
    {
        return connected;
    }
}
