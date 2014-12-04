package chess;

import network.ChessClientHandler;
import network.ChessServerHandler;
import pieces.*;

public class PieceMoveUtil
{
	private static Piece dragging = null;
	private static int predragRow = -1;
	private static int predragCol = -1;
	private static int dragXOffset = -1;
	private static int dragYOffset = -1;
	private static int currentDragX = -1;
	private static int currentDragY = -1;

	/**
	 * Moves dragging to a (x, y) coordinate on the board Condition: dragging is
	 * currently being dragged
	 *
	 * @param x
	 * @param y
	 */
	public static void movePiece(int x, int y)
	{
		try
		{
			if (!(MouseUtils.getRow(y) == predragRow && MouseUtils.getCol(x) == predragCol))
			{
				if (!dragging.moveCheck(MouseUtils.getRow(y), MouseUtils.getCol(x), predragRow, predragCol))
				{
					reset();
					return;
				}

				if (Chess.board[MouseUtils.getRow(y)][MouseUtils.getCol(x)] != null && Chess.board[MouseUtils.getRow(y)][MouseUtils.getCol(x)].getColor() == dragging.getColor())
				{
					reset();
					return;
				}

				if (!PiecePathCheck.check(predragRow, predragCol, MouseUtils.getRow(y), MouseUtils.getCol(x)))
				{
					reset();
					return;
				}

				if (!Chess.myTurn)
				{
					reset();
					return;
				}

				if ((Chess.isClient && dragging.getColor() != PieceColor.WHITE) || (!Chess.isClient && dragging.getColor() != PieceColor.BLACK))
				{
					reset();
					return;
				}

				if (dragging instanceof PiecePawn)
				{
					((PiecePawn) dragging).setFirstMove(false);
				}
				else if (dragging instanceof PieceJumpPawn)
				{
					((PieceJumpPawn) dragging).setFirstMove(false);
				}

				if (Chess.board[MouseUtils.getRow(y)][MouseUtils.getCol(x)] instanceof PieceKing)
				{
					Chess.winner = Chess.isClient ? PieceColor.WHITE : PieceColor.BLACK;
				}

				if (!Chess.finalBattle)
					if (dragging instanceof PiecePawn || dragging instanceof PieceJumpPawn)
					{
						if (dragging.getColor() == PieceColor.BLACK && MouseUtils.getRow(y) == 7)
						{
							dragging = new PieceQueen(PieceColor.BLACK);
						}
						else if (dragging.getColor() == PieceColor.WHITE && MouseUtils.getRow(y) == 0)
						{
							dragging = new PieceQueen(PieceColor.WHITE);
						}
					}

				Chess.board[MouseUtils.getRow(y)][MouseUtils.getCol(x)] = dragging;
				Chess.board[predragRow][predragCol] = null;

				if (Chess.isClient)
					ChessClientHandler.sendPieceMove(MouseUtils.getRow(y), MouseUtils.getCol(x), predragRow, predragCol);
				else
					ChessServerHandler.sendPieceMove(MouseUtils.getRow(y), MouseUtils.getCol(x), predragRow, predragCol);
				
				Chess.playSound("resources/button.wav");

				reset();
			}
			else
			{
				reset();
			}
		}
		catch (OutOfBoardException e1)
		{
			reset();
		}
	}

	public static void startDragging(int x, int y)
	{
		try
		{
			int xdelta = Chess.getWidth2() / Chess.NUM_COLUMNS;
			int ydelta = Chess.getHeight2() / Chess.NUM_ROWS;

			dragging = Chess.board[MouseUtils.getRow(y)][MouseUtils.getCol(x)];
			predragRow = MouseUtils.getRow(y);
			predragCol = MouseUtils.getCol(x);
			dragXOffset = x - (Chess.getX(MouseUtils.getCol(x) * xdelta));
			dragYOffset = y - (Chess.getY(MouseUtils.getRow(y) * ydelta));
			currentDragX = x;
			currentDragY = y;
		}
		catch (OutOfBoardException e1)
		{
			dragging = null;
			predragRow = -1;
			predragCol = -1;
			dragXOffset = -1;
			dragYOffset = -1;
			currentDragX = -1;
			currentDragY = -1;
		}
	}

	public static void updateDrag(int x, int y)
	{
		currentDragX = x;
		currentDragY = y;
	}

	public static boolean isDragging()
	{
		return dragging != null;
	}

	public static Piece getDragging()
	{
		return dragging;
	}

	public static int getPredragRow()
	{
		return predragRow;
	}

	public static int getPredragCol()
	{
		return predragCol;
	}

	public static int getDragXOffset()
	{
		return dragXOffset;
	}

	public static int getDragYOffset()
	{
		return dragYOffset;
	}

	public static int getCurrentDragX()
	{
		return currentDragX;
	}

	public static int getCurrentDragY()
	{
		return currentDragY;
	}

	public static void reset()
	{
		dragging = null;
		predragRow = -1;
		predragCol = -1;
		dragXOffset = -1;
		dragYOffset = -1;
		currentDragX = -1;
		currentDragY = -1;
	}
}
