package network;

import chess.Chess;
import chess.MouseUtils;
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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChessClientHandler
{
	public static boolean connected = false;
	public static Point postPoints = null;
	public static Point initPoints = null;
	private static String hostIP = null;
	private static int hostPort = -1;
	private static Socket server = null;
	private static PrintWriter serverOut = null;
	private static BufferedReader serverIn = null;

	public static void connect(String ip, int port) throws UnknownHostException, IOException
	{
		hostIP = ip;
		hostPort = port;
		server = new Socket();
		server.connect(new InetSocketAddress(ip, port), 6000);
		serverOut = new PrintWriter(server.getOutputStream(), true);
		serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
		connected = true;
		recievePieceMove();
	}

	public static void disconnect()
	{
		try
		{
			if (server != null)
				server.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hostIP = null;
		hostPort = -1;
		server = null;
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
			Chess.winner = PieceColor.BLACK;
		}

		if (Chess.board[(int) initPoints.getY()][(int) initPoints.getX()] instanceof PiecePawn || Chess.board[(int) initPoints.getY()][(int) initPoints.getX()] instanceof PieceJumpPawn)
		{
			if (postPoints.getY() == 7)
			{
				Chess.board[(int) initPoints.getY()][(int) initPoints.getX()] = new PieceQueen(PieceColor.BLACK);
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
		new Thread(new Runnable() {

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
				catch (IOException e)
				{
					disconnect();
				}

			}
		}).start();
	}

	public static boolean isConnected()
	{
		return connected;
	}
}
