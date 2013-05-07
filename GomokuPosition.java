package gomoku;

public class GomokuPosition extends Position {
	public final static short BLANK = 0;
	public final static short PROGRAM = 1;
	public final static short HUMAN = -1;
	
	public final static int CHESSBOARD_SIZE = 10;
	
	short[][] grid = new short[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
	
	public GomokuPosition() {
		setDefaultState();
	}
	
	public void setDefaultState() {
		for(int i = 0; i < CHESSBOARD_SIZE; i++)
			for(int j = 0; j < CHESSBOARD_SIZE; j++)
				grid[i][j] = BLANK;
	}
	
	public GomokuPosition getNewPosition() {
		GomokuPosition pos = new GomokuPosition();
		for(int i = 0; i < CHESSBOARD_SIZE; i++)
			for(int j = 0; j < pos.CHESSBOARD_SIZE; j++)
				pos.grid[i][j] = grid[i][j];
		return pos;
	}
}
