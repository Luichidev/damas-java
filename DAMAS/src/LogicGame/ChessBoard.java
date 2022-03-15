package LogicGame;

import java.util.ArrayList;

public class ChessBoard {

	public final static int COLS = 8;
	public final static int ROWS = 8;
	private ArrayList<Piece> killedWhite; 
	private ArrayList<Piece> killedBlack; 
	private ArrayList<String> savedMoves;
	private boolean pendingMovesToWrite;
	
	ChessBoard(){
		killedWhite = new ArrayList<Piece>();
		killedBlack = new ArrayList<Piece>();
		savedMoves = new ArrayList<String>();
		pendingMovesToWrite = false;
		board = new Cell[ROWS][COLS];
		initializeBoard();
		initializePieces();
	}
	
	boolean movePiece( String initialPosition, String finalPosition, boolean player) throws CheckersException {
		
		Cell initialCell = getCell(initialPosition);
		
		if(!initialCell.hasPiece()) 
			throw new CheckersException(
					CheckersException.NO_PIECE_ON_CELL);
		
		Piece initialPiece = initialCell.getPiece();
		
		if(player != initialPiece.GetColour()) 
			throw new CheckersException(
					CheckersException.PLAYER_MUST_MOVE_HIS_PIECES, player ? "WHITE" : "BLACK");
		
		ArrayList<String> moves = initialCell.getPiece().GetValidMoves(this, initialPosition);
		boolean moveIsValid = false;
		for(String move: moves) {
			if(move.compareTo(finalPosition) == 0) {
				moveIsValid = true;
			}
		}
		
		if(!moveIsValid) {
			throw new CheckersException(
					CheckersException.INCORRECT_MOVE_VALUE);
		}
		
		Piece aux = initialCell.getPiece();
		int row = getRow(finalPosition);
		if(row == 0 || row == ROWS - 1) {
			aux = new Queen(aux.GetColour());
		}
		
		setPiece(aux, finalPosition);
		initialCell.empty();
		
		if(isThereAJump(initialPosition, finalPosition)) {
			Cell cell = getAkillerCell(initialPosition, finalPosition);
			Piece piece = cell.getPiece();
			if (piece.colour == Piece.WHITE) {
				killedWhite.add(piece);
			} else {
				killedBlack.add(piece);
			}
			cell.empty();
		}
		
		savedMoves.add(initialPosition + " " + finalPosition);
		setPendingMovesToWrite(true);
		
		return true;	
	}
	
	private boolean isThereAJump(String initialPosition , String finalPosition) {
		int col_ini = getCol(initialPosition);
		int row_ini = getRow(initialPosition);
		
		int col_fin = getCol(finalPosition);
		int row_fin = getRow(finalPosition);
		
		return (Math.abs(col_ini - col_fin) + Math.abs(row_ini - row_fin) > 2);
	}
	
	private Cell getAkillerCell(String initialPosition, String finalPosition) {
		int col_ini = getCol(initialPosition);
		int row_ini = getRow(initialPosition);
	
		int col_fin = getCol(finalPosition);
		int row_fin = getRow(finalPosition);
	
		int col = (col_ini + col_fin) / 2;
		int row = (row_ini + row_fin) / 2;
		
		return board[col][row];
	}
	
	public boolean getWinnerColour() {
		return Piece.TOTAL_BLACK_PIECES == killedBlack.size()
			? Piece.WHITE
			: Piece.BLACK;
	}
	
	public boolean isThereWinner() {
		return Piece.TOTAL_BLACK_PIECES == killedBlack.size();
	}
	
	
	public ArrayList<String> getMoves() {
		return savedMoves;
	}
	
	public boolean isPendingMovesToWrite() {
		return pendingMovesToWrite;
	}
	
	public void setPendingMovesToWrite(boolean toWrite) {
		pendingMovesToWrite = toWrite;
	}
	
	
	public void ResetBoard() {
		Piece.TOTAL_BLACK_PIECES = 0;
		Piece.TOTAL_WHITE_PIECES = 0;
		this.killedBlack = new ArrayList<Piece>();
		this.killedWhite = new ArrayList<Piece>();
		
		for (int row = ROWS - 1; row >= 0; row--) {
			for (int col = 0; col < COLS; col++) {
				board[row][col].SetPiece(Piece.EMPTY);
			}
		}
	}
	
	private void initializeBoard() {
		for(int row = ROWS -1; row >= 0; row--) {
			for(int col = 0; col < COLS; col++) {
				if ((row+col)%2==0) {
					board[row][col] = new Cell(Cell.BLACK, Piece.EMPTY);
				}else {
					board[row][col] = new Cell(Cell.WHITE, Piece.EMPTY);
				}
			}
		}
	}
	
	private void  initializePieces() {
		 
		this.setPiece(new Pawn (Piece.BLACK),"b8");
		this.setPiece(new Pawn (Piece.BLACK),"d8");
		this.setPiece(new Pawn (Piece.BLACK),"f8");
		this.setPiece(new Pawn (Piece.BLACK),"h8");
		this.setPiece(new Pawn (Piece.BLACK),"a7");
		this.setPiece(new Pawn (Piece.BLACK),"c7");
		this.setPiece(new Pawn (Piece.BLACK),"e7");
		this.setPiece(new Pawn (Piece.BLACK),"g7");
		this.setPiece(new Pawn (Piece.BLACK),"b6");
		this.setPiece(new Pawn (Piece.BLACK),"d6");
		this.setPiece(new Pawn (Piece.BLACK),"f6");
		this.setPiece(new Pawn (Piece.BLACK),"h6");
		
		this.setPiece(new Pawn (Piece.WHITE),"a3");
		this.setPiece(new Pawn (Piece.WHITE),"c3");
		this.setPiece(new Pawn (Piece.WHITE),"e3");
		this.setPiece(new Pawn (Piece.WHITE),"g3");
		this.setPiece(new Pawn (Piece.WHITE),"b2");
		this.setPiece(new Pawn (Piece.WHITE),"d2");
		this.setPiece(new Pawn (Piece.WHITE),"f2");
		this.setPiece(new Pawn (Piece.WHITE),"h2");
		this.setPiece(new Pawn (Piece.WHITE),"a1");
		this.setPiece(new Pawn (Piece.WHITE),"c1");
		this.setPiece(new Pawn (Piece.WHITE),"e1");
		this.setPiece(new Pawn (Piece.WHITE),"g1");

	}
	
	public void setPiece(Piece piece, String position) {
		//Cell cellAux=this.getCell(position);
		int col = getCol(position);
		int row = getRow(position);
		 board[row][col].SetPiece(piece);
	}
	
	public Cell getCell(String position) {
		int col = getCol(position);
		int row = getRow(position);
		return board[row][col];
	}
	
	/**
    * Returns the x-axis of a position on ChessBoard.
    * @param position  a position on ChessBoard.
    * @return int  Returns the x-axis of a position.
    */	
	public int getCol(String position) {
		return position.charAt(0) - 'a';
	}

   /**
    * Returns the y-axis of a position on ChessBoard.
    * @param position  a position on ChessBoard.
    * @return int  Returns the y-axis of a position.
    */	
	public int getRow(String position) {
		return position.charAt(1) - '1';
	}
	
	public String toString() {

		String horizontal = "\u2500";
		String vertical = "\u2502";
		String top = "\u252C";
		String bottom = "\u2534";
		String left = "\u251C";
		String right = "\u2524";
		String center = "\u253C";
		String top_left = "\u250C";
		String top_right = "\u2510";
		String bottom_left = "\u2514";
		String bottom_right = "\u2518";
		String NL = System.getProperty("line.separator");
		String space = " ";
		String white_cell = "\u2591";
		String black_cell = "\u2588";	
		
		String chessBoard = "";
		
		// -----Line Top------------------------------
		String lineTop = "   a  b  c  d  e  f  g  h   Killed pieces" + NL;
		lineTop += "  " + top_left;
		for (int col = 0; col < COLS - 1; col++) {
			lineTop += horizontal + horizontal + top;
		}
		lineTop += horizontal + horizontal + top_right;

		// -----Line Bottom------------------------------
		String lineBottom = " " + bottom_left;
		for (int col = 0; col < COLS - 1; col++) {
			lineBottom += horizontal + horizontal + bottom;
		}
		lineBottom += horizontal + horizontal + bottom_right;

		// -----Line Middle--------------------------------
		String lineMiddle = " " + left;
		for (int col = 0; col < COLS - 1; col++) {
			lineMiddle += horizontal + horizontal + center;
		}
		lineMiddle += horizontal + horizontal + right;

		// ---- Print all the board
		chessBoard = lineTop + NL;
		for (int row = ROWS - 1; row >= 0; row--) {
			String fila = "";
			for (int col = 0; col < COLS; col++) {
				fila += board[row][col].toString() + vertical;

			}
			chessBoard += (row + 1) + space + vertical + fila;
			if (row + 1 == 8) {
				chessBoard += "[";
				for (Piece piece : killedBlack) {
					chessBoard += piece.toString() + ",";
				}
				chessBoard += "]";
			} else if (row + 1 == 1) {
				chessBoard += "[";
				for (Piece piece : killedWhite) {
					chessBoard += piece.toString() + ",";
				}
				chessBoard += "]";
			}
			chessBoard += NL;

			if (row > 0) {
				chessBoard += space + lineMiddle + NL;
			} else {
				chessBoard += space + lineBottom + NL;
			}

		}

		chessBoard += "   a  b  c  d  e  f  g  h   Killed pieces" + NL;
		return chessBoard;
	}
	
	
	private Cell[][] board;
}