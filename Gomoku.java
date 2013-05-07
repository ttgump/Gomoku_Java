package gomoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class Gomoku extends GameSearch {
	private JLabel jblWhite = new JLabel("White: 0s");
	private JLabel jblBlack = new JLabel("Black: 0s");
	private JButton jbtIcon = new JButton("Start!");
	private ChessBoard chessboard = new ChessBoard();
	private GomokuPosition board = new GomokuPosition();
	private boolean gameState = false;  // to show statue of game: true for continue, false for end.
	private boolean canClick = true;
	private boolean player = true; // true for human, false for program 
	private float white = 0; // record white player's chess number
	private float black = 0; // record black player's chess number
	
	private JRadioButton jrbEasy = new JRadioButton("Easy", false);
	private JRadioButton jrbNormal = new JRadioButton("Normal", false);
	private JRadioButton jrbHard = new JRadioButton("Hard", true);
	
	private int maxDepth = 5;
	
	static Thread blackTimeThread;
	static Thread whiteTimeThread;
	
    public Gomoku() {
    	JPanel p1 = new JPanel();
    	p1.setLayout(new GridLayout(1, 3, 20, 5));
    	
    	p1.add(jblWhite);
    	p1.add(jbtIcon);
    	p1.add(jblBlack);
    	
    	jbtIcon.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (!gameState) {
    				gameState = true;
    				jbtIcon.setText("New game");
    				
    				if(jrbEasy.isSelected()) maxDepth = 2;
    				else if(jrbNormal.isSelected()) maxDepth = 4;
    				else if(jrbHard.isSelected()) maxDepth = 5;
    			}
    			else {
    				setDefaultStatue();
        			chessboard.repaint();
    			}
    		}
    	});
    	
    	JPanel p2 = new JPanel();
    	p2.setLayout(new GridLayout(1, 3, 10, 0));
    	
    	p2.add(jrbEasy);
    	p2.add(jrbNormal);
    	p2.add(jrbHard);
    	
    	ButtonGroup group = new ButtonGroup();
    	group.add(jrbEasy);
    	group.add(jrbNormal);
    	group.add(jrbHard);
    	
    	add(p1, BorderLayout.NORTH);
    	add(chessboard, BorderLayout.CENTER);
    	add(p2, BorderLayout.SOUTH);
    	
    	blackTimeThread = new Thread(new Runnable() {
    		public void run() {
    			try {
        			while(true) {
        				if(gameState) {
        					if(player) {
                				black += 0.2;
                				jblBlack.setText("Black: " + (int)black + "s");
        					}
                			if(!player)
                				black = 0;
        				}
            			
            			Thread.sleep(200);
            		}
        		}
        		catch(InterruptedException ex) {
        			
        		}
    		}
    	});
    	
    	whiteTimeThread = new Thread(new Runnable() {
    		public void run() {
    			try {
        			while(true) {
        				if(gameState) {
        					if(!player) {
                				white += 0.2;
                				jblWhite.setText("White: " + (int)white + "s");
        					}
                			if(player)
                				white = 0;
        				}
            			
            			Thread.sleep(200);
            		}
        		}
        		catch(InterruptedException ex) {
        			
        		}
    		}
    	});
    }
    

    public void setDefaultStatue() {
    	board.setDefaultState();
    	
    	gameState = false;
    	//isWinned = false;
    	player = true;
    	
    	white = 0;
    	black = 0;
    	jblWhite.setText("White: 0s");
    	jblBlack.setText("Black: 0s");
    	
    	jbtIcon.setText("Start!");
    	
    	jrbHard.setSelected(true);
    	maxDepth = 5;
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Thread() {
            public void run() {
                Gomoku frame = new Gomoku();
                frame.setTitle("Gomoku Game");
                frame.setSize(460, 540);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
    	
                whiteTimeThread.start();
                blackTimeThread.start();
            }
        });
    }
    
    /**
     * 
     * @param p position
     * @return true if meets a draw situation
     */
    public boolean drawnPosition(Position p){
    	GomokuPosition pos = (GomokuPosition)p;
    	for(int i = 0; i < pos.grid.length; i++)
    		for(int j = 0; j < pos.grid[0].length; j++)
    			if(pos.grid[i][j] == GomokuPosition.BLANK) return false;
    	return true;
    }
    
    public boolean wonPosition(Position p, boolean player){
    	GomokuPosition pos = (GomokuPosition)p;
    	short b;
    	if(player) b = GomokuPosition.HUMAN;
    	else b = GomokuPosition.PROGRAM;
    	
    	for (int i = 0; i < board.grid.length; i++) {
			for (int j = 0; j < board.grid[0].length; j++) {
				if (i < (board.grid.length - 4))
					if (board.grid[i][j] == b 
					 && board.grid[i][j] == board.grid[i + 1][j]
					 && board.grid[i][j] == board.grid[i + 2][j]
					 && board.grid[i][j] == board.grid[i + 3][j]
					 && board.grid[i][j] == board.grid[i + 4][j])
						return true;
				
				if (j < (board.grid[0].length - 4))
					if (board.grid[i][j] == b
					 && board.grid[i][j] == board.grid[i][j + 1]
					 && board.grid[i][j] == board.grid[i][j + 2]
					 && board.grid[i][j] == board.grid[i][j + 3]
					 && board.grid[i][j] == board.grid[i][j + 4])
						return true;
				
				if (i < (board.grid.length - 4)  && j < (board.grid[0].length - 4))
					if (board.grid[i][j] == b
					 && board.grid[i][j] == board.grid[i + 1][j + 1]
					 && board.grid[i][j] == board.grid[i + 2][j + 2]
					 && board.grid[i][j] == board.grid[i + 3][j + 3]
					 && board.grid[i][j] == board.grid[i + 4][j + 4])
						return true;
				
				if (i < (board.grid.length - 4) && j >= 4)
					if (board.grid[i][j] != 0
					 && board.grid[i][j] == board.grid[i + 1][j - 1]
					 && board.grid[i][j] == board.grid[i + 2][j - 2]
					 && board.grid[i][j] == board.grid[i + 3][j - 3]
					 && board.grid[i][j] == board.grid[i + 4][j - 4])
						return true;
				}
			}
		
		return false;
    }
    
    /**
     * evaluation a position
     * @param p
     * @param player
     * @return 
     */
    public float positionEvaluation(Position p, boolean player){
    	int[][] myConnects = connectN(p, player, 5);
    	int[][] enemyConnects = connectN(p, !player, 5);
    	
        // if there is a connect 5 or two open connect 4, then return positive infinity of negative infinity
    	if(myConnects[3][0] > 0 || myConnects[2][2] > 0)
    		return Float.POSITIVE_INFINITY;
    	if(enemyConnects[3][0] > 0 || enemyConnects[2][2] > 0)
    		return Float.NEGATIVE_INFINITY;
    	
    	int ret = 0;
    	GomokuPosition pos = (GomokuPosition)p;
    	
    	int[] score = {10, 100, 1000};
    	
    	short myChessman;
		short enemyChessman;
		if(player) {
			myChessman = GomokuPosition.HUMAN;
			enemyChessman = GomokuPosition.PROGRAM;
		}
		else {
			myChessman = GomokuPosition.PROGRAM;
			enemyChessman = GomokuPosition.HUMAN;
		}
    	
    	for(int i = 0; i < pos.grid.length; i++) {
    		for(int j = 0; j < pos.grid[0].length; j++) {
    			if(pos.grid[i][j] == myChessman) {
    				if(distanceToBoundary(i, j) == 0)
    					ret += 1;
    				else if(distanceToBoundary(i, j) == 1)
    					ret += 2;
    				else if(distanceToBoundary(i, j) == 2)
    					ret += 4;
    				else
    					ret += 8;
    			}
    			else if(pos.grid[i][j] == enemyChessman) {
    				if(distanceToBoundary(i, j) == 0)
    					ret -= 1;
    				else if(distanceToBoundary(i, j) == 1)
    					ret -= 2;
    				else if(distanceToBoundary(i, j) == 2)
    					ret -= 4;
    				else
    					ret -= 8;
    			}
    		}
    	}
    	
    	ret += myConnects[0][1] * score[0];
    	ret += myConnects[0][2] * (int)(score[1] * 0.9);
    	ret += myConnects[1][1] * score[1];
    	ret += myConnects[1][2] * (int)(score[2] * 0.9);
    	ret += myConnects[2][1] * score[2];
    	
    	ret -= enemyConnects[0][1] * score[0];
    	ret -= enemyConnects[0][2] * (int)(score[1] * 0.9);
    	ret -= enemyConnects[1][1] * score[1];
    	ret -= enemyConnects[1][2] * (int)(score[2] * 0.9);
    	ret -= enemyConnects[2][1] * score[2];
    	
    	return ret;
    }
    
    /**
     * return number of connect 2, 3, ..., number, the result is stored in an array[][].
     * the first dimension is connect number, 0 represents 2,...
     * second dimension is number of connects, 0 represents all, 1 represents one end open, 2 represents two ends open
     * @param p
     * @param player
     * @param number
     * @return 
     */
    private int[][] connectN(Position p, boolean player, int number) {
    	GomokuPosition pos = (GomokuPosition)p;
    	short b;
    	if(player) b = GomokuPosition.HUMAN;
    	else b = GomokuPosition.PROGRAM;
    	
    	int count[][] = new int[number-1][3];
    	for(int i = 0; i < count.length; i++)
    		for(int j = 0; j < count[0].length; j++)
    			count[i][j] = 0;
    	
    	for(int n = 2; n <= number; n++) {
    		for(int i = 0; i < pos.grid.length; i++) {
        		for(int j = 0; j < pos.grid[0].length; j++) {
        			if(i+n-1 < pos.grid.length) {
        				if(downSame(p, i, j, n-1, b)) {
        					while(true) {
        						if(i+n < pos.grid.length)
        							if(pos.grid[i+n][j] == b) break;
        						if(i-1 >= 0)
        							if(pos.grid[i-1][j] == b) break;
        						
        						count[n-2][0]++;
        						
        						if(i-1 >= 0 && i+n >= pos.grid.length) {
            	    				if(pos.grid[i-1][j] == GomokuPosition.BLANK)
            	    					count[n-2][1]++;
            	    			}
            	    			else if(i-1 < 0 && i+n < pos.grid.length) {
            	    				if(pos.grid[i+n][j] == GomokuPosition.BLANK)
            	    					count[n-2][1]++;
            	    			}
            	    			else if(i-1 >= 0 && i+n < pos.grid.length) {
            	    				if(pos.grid[i-1][j] == GomokuPosition.BLANK 
            	    						&& pos.grid[i+n][j] != GomokuPosition.BLANK)
            	    					count[n-2][1]++;
            	    				if(pos.grid[i+n][j] == GomokuPosition.BLANK
            	    						&& pos.grid[i-1][j] != GomokuPosition.BLANK)
            	    					count[n-2][1]++;
            	    				
            	    				if(pos.grid[i-1][j] == GomokuPosition.BLANK
            	    						&& pos.grid[i+n][j] == GomokuPosition.BLANK)
            	    					count[n-2][2]++;
            	    			}
            	    			break;
        					}
        				}
        				
        				if(j+n-1 < pos.grid[0].length) {
        					if(rightSame(p, i, j, n-1, b)) {
        						while(true) {
            						if(j+n < pos.grid[0].length)
            							if(pos.grid[i][j+n] == b) break;
            						if(j-1 >= 0)
            							if(pos.grid[i][j-1] == b) break;
            						count[n-2][0]++;
                					
                					if(j-1 >= 0 && j+n >= pos.grid[0].length) {
                						if(pos.grid[i][j-1] == GomokuPosition.BLANK)
                							count[n-2][1]++;
                					}
                					else if(j-1 < 0 && j+n < pos.grid[0].length) {
                						if(pos.grid[i][j+n] == GomokuPosition.BLANK)
                							count[n-2][1]++;
                					}
                					else if(j-1 >= 0 && j+n < pos.grid[0].length) {
                						if(pos.grid[i][j-1] == GomokuPosition.BLANK 
                	    						&& pos.grid[i][j+n] != GomokuPosition.BLANK)
                	    					count[n-2][1]++;
                	    				if(pos.grid[i][j+n] == GomokuPosition.BLANK
                	    						&& pos.grid[i][j-1] != GomokuPosition.BLANK)
                	    					count[n-2][1]++;
                	    				
                	    				if(pos.grid[i][j-1] == GomokuPosition.BLANK
                	    						&& pos.grid[i][j+n] == GomokuPosition.BLANK)
                	    					count[n-2][2]++;
                					}
                					break;
            					}
        					}
        				}
        				
        				if(i+n-1 < pos.grid.length && j+n-1 < pos.grid[0].length) {
        					if(rightDownSame(p, i, j, n-1, b)) {
        						while(true) {
            						if(i+n < pos.grid.length && j+n < pos.grid[0].length)
            							if(pos.grid[i+n][j+n] == b) break;
            						if(i-1 >= 0 && j-1 >= 0)
            							if(pos.grid[i-1][j-1] == b) break;
            						count[n-2][0]++;
                					
                					if((i-1 >= 0 && j-1 >= 0) && (i+n >= pos.grid.length || j+n >= pos.grid[0].length)) {
                						if(pos.grid[i-1][j-1] == GomokuPosition.BLANK)
                							count[n-2][1]++;
                					}
                					else if((i-1 < 0 || j-1 < 0) && (i+n < pos.grid.length && j+n < pos.grid[0].length)) {
                						if(pos.grid[i+n][j+n] == GomokuPosition.BLANK)
                							count[n-2][1]++;
                					}
                					else if((i-1 >= 0 && j-1 >= 0) && (i+n < pos.grid.length && j+n < pos.grid[0].length)) {
                						if(pos.grid[i-1][j-1] == GomokuPosition.BLANK
                								&& pos.grid[i+n][j+n] != GomokuPosition.BLANK)
                							count[n-2][1]++;
                						if(pos.grid[i+n][j+n] == GomokuPosition.BLANK
                								&& pos.grid[i-1][j-1] != GomokuPosition.BLANK)
                							count[n-2][1]++;
                						if(pos.grid[i-1][j-1] == GomokuPosition.BLANK && pos.grid[i+n][j+n] == GomokuPosition.BLANK)
                							count[n-2][2]++;
                					}
                					break;
        						}
        					}
        				}
        				
        				if(i-n+1 >= 0 && j+n-1 < pos.grid[0].length) {
        					if(rightUpSame(p, i, j, n-1, b)) {
        						while(true) {
            						if(i-n >= 0 && j+n < pos.grid[0].length)
            							if(pos.grid[i-n][j+n] == b) break;
            						if(i+1 < pos.grid.length && j-1 >= 0)
            							if(pos.grid[i+1][j-1] == b) break;
            						count[n-2][0]++;
                					
                					if((i-n >= 0 && j+n < pos.grid[0].length) && (i+1 >= pos.grid.length || j-1 < 0)) {
                						if(pos.grid[i-n][j+n] == GomokuPosition.BLANK)
                							count[n-2][1]++;
                					}
                					else if((i-n < 0 || j+n > pos.grid[0].length) && (i+1 < pos.grid.length && j-1 >= 0)) {
                						if(pos.grid[i+1][j-1] == GomokuPosition.BLANK)
                							count[n-2][1]++;
                					}
                					else if((i-n >= 0 && j+n < pos.grid[0].length) && (i+1 < pos.grid.length && j-1 >= 0)) {
                						if(pos.grid[i-n][j+n] == GomokuPosition.BLANK
                								&& pos.grid[i+1][j-1] != GomokuPosition.BLANK)
                							count[n-2][1]++;
                						if(pos.grid[i+1][j-1] == GomokuPosition.BLANK
                								&& pos.grid[i-n][j+n] != GomokuPosition.BLANK)
                							count[n-2][1]++;
                						if(pos.grid[i-n][j+n] == GomokuPosition.BLANK && pos.grid[i+1][j-1] == GomokuPosition.BLANK)
                							count[n-2][2]++;
                					}
                					break;
            					}
        					}
        				}
        			}
        		}
        	}
    	}
    	
    	
    	
    	return count;
    }
    
    private boolean downSame(Position p, int i, int j, int n, short b) {
    	GomokuPosition pos = (GomokuPosition)p;
    	for(int k=0; k<=n; k++)
    		if(pos.grid[i+k][j] != b) return false;
    	return true;
    }
    
    private boolean rightSame(Position p, int i, int j, int n, short b) {
    	GomokuPosition pos = (GomokuPosition)p;
    	for(int k=0; k<=n; k++)
    		if(pos.grid[i][j+k] != b) return false;
    	return true;
    }
    
    private boolean rightDownSame(Position p, int i, int j, int n, short b) {
    	GomokuPosition pos = (GomokuPosition)p;
    	for(int k=0; k<=n; k++)
    		if(pos.grid[i+k][j+k] != b) return false;
    	return true;
    }
    
    private boolean rightUpSame(Position p, int i, int j, int n, short b) {
    	GomokuPosition pos = (GomokuPosition)p;
    	for(int k=0; k<=n; k++)
    		if(pos.grid[i-k][j+k] != b) return false;
    	return true;
    }
    
    
    
    private int distanceToBoundary(int x, int y) {
		int xToUp = x;
		int xToDown = board.grid.length - 1 - x;
		int yToLeft = y;
		int yToRight = board.grid[0].length - 1 - y;
		
		xToUp = Math.min(xToUp, xToDown);
		xToUp = Math.min(xToUp, yToLeft);
		xToUp = Math.min(xToUp, yToRight);
		return xToUp;
	}

	public Position [] possibleMoves(Position p, boolean player){
    	GomokuPosition pos = (GomokuPosition)p;
    	int count = 0;
    	for(int i = 0; i < pos.grid.length; i++)
    		for(int j = 0; j < pos.grid[0].length; j++)
    			if(pos.grid[i][j] == GomokuPosition.BLANK && isInSize(p, i, j)) 
    				count++;
    	
    	Position[] ret = new Position[count];
    	count = 0;
    	for(int i = 0; i < pos.grid.length; i++) {
    		for(int j = 0; j < pos.grid[0].length; j++) {
                    // next move must be in 3 grids away from current grids
    			if(pos.grid[i][j] == GomokuPosition.BLANK && isInSize(p, i, j)) {
    				GomokuPosition pos2 = new GomokuPosition();
    				pos2 = pos.getNewPosition();
    				if(player)
    					pos2.grid[i][j] = GomokuPosition.HUMAN;
    				else
    					pos2.grid[i][j] = GomokuPosition.PROGRAM;
    				ret[count++] = pos2;
    			}
    		}
    	}
    	return ret;
    }
	
	private boolean isInSize(Position p, int x, int y) {
		GomokuPosition pos = (GomokuPosition)p;
		int minX = 0;
		int maxX = 0;
		int minY = 0;
		int maxY = 0;
		
		boolean firstChessman = true;
		for(int i = 0; i < pos.grid.length; i++) {
			for(int j = 0; j < pos.grid[0].length; j++) {
				if(pos.grid[i][j] != GomokuPosition.BLANK) {
					if(firstChessman) {
						maxX = minX = i;
						maxY = minY = j;
						firstChessman = false;
					}
					else {
						//if(size[0] > i) size[0] = i;
                                                if(minX > i) minX = i;
						if(maxX < i) maxX = i;
						if(minY > j) minY = j;
						if(maxX < j) maxX = j;
					}
				}
			}
		}
		
		if(x >= minX-3 && x <= maxX+3 && y >= minY-3 && y <= maxY+3) return true;
		else return false;
	}
    
    public boolean reachedMaxDepth(Position p, int depth){
    	//if(wonPosition(p, true)) return true;
    	//if(wonPosition(p, false)) return true;
    	//if(drawnPosition(p)) return true;
    	if(depth >= maxDepth) return true;
    	return false;
    }
    
    class ChessBoard extends JPanel {
    	private int width;
    	private int height;
    	private int widthStep;
    	private int heightStep;
		
    	private int xStart;
    	private int yStart;
    	private int xEnd;
    	private int yEnd;
        
    	public ChessBoard() {
    		setBackground(Color.GRAY);
    		
    		addMouseListener(new MouseAdapter() {
    			public void mouseClicked(MouseEvent e) {
    				if (gameState && canClick) {
    					int mouseX = 0;
    			    	int mouseY = 0;
    			    	int gridX = 0;
    			    	int gridY = 0;
    			    	
    				    mouseX = e.getX();
    				    mouseY = e.getY();
    				    if (mouseX >= xStart && mouseX <= xEnd && mouseY >= yStart && mouseY <= yEnd) {
    				        gridX = (mouseX - xStart) / widthStep;
    				        gridY = (mouseY - yStart) / heightStep;
    				        
    				       if(board.grid[gridX][gridY] == GomokuPosition.BLANK) {
    				    	   board.grid[gridX][gridY] = GomokuPosition.HUMAN;
    				    	   repaint();
    				    	   //black++;
    				    	   //jblWhite.setText("Black: " + black);
    				    	   if(wonPosition(board, HUMAN)) {
    				    		   JOptionPane.showMessageDialog(null, "Human win!");
    				    		   gameState = false;
    				    		   return;
    				    	   }
    				    	   else if(drawnPosition(board)) {
    				    		   JOptionPane.showMessageDialog(null, "Draw game!");
    				    		   gameState = false;
    				    		   return;
    				    	   }
    				    	   
    				    	   player = PROGRAM;
    				    	   canClick = false;
    				    	   
    				    	   Thread thread2 = new Thread(new Runnable() {
    				    		   public void run() {
    				    			   Vector v = alphaBeta(0, (Position)board, PROGRAM);
    				    			   board = (GomokuPosition)v.elementAt(1);
    				    			   repaint();
    				    			   //white++;
    	    				    	   //jblBlack.setText("White: " + white);
    	    				    	   player = HUMAN;
    	    				    	   canClick = true;
    	    				    	   if(wonPosition(board, PROGRAM)) {
    	    				    		   JOptionPane.showMessageDialog(null, "Computer win!");
    	    				    		   gameState = false;
    	    				    		   return;
    	    				    	   }
    	    				    	   else if(drawnPosition(board)) {
    	    				    		   JOptionPane.showMessageDialog(null, "Draw game!");
    	    				    		   gameState = false;
    	    				    		   return;
    	    				    	   }
                                                }
    				    	   });
    				    	   
    				    	   thread2.setPriority(Thread.MAX_PRIORITY);
    				    	   thread2.start();
    				       }
    				    }
    				}
    			}
    		}); 
    	}
    	
    	protected void paintComponent(Graphics g) {
    		super.paintComponent(g);
    		
    		width = (int)(getWidth() * 0.98);
        	height = (int)(getHeight() * 0.98);
        	widthStep = width / GomokuPosition.CHESSBOARD_SIZE;
        	heightStep = height / GomokuPosition.CHESSBOARD_SIZE;
    		
        	xStart = (int)(width * 0.01);
        	yStart = (int)(height * 0.01);
        	xEnd = xStart + GomokuPosition.CHESSBOARD_SIZE * widthStep;
        	yEnd = yStart + GomokuPosition.CHESSBOARD_SIZE * heightStep;
        	
    		// draw chessboard
    		g.setColor(Color.BLACK);
    		g.drawLine(xStart, yStart, xStart, yEnd);
    		g.drawLine(xStart, yStart, xEnd, yStart);
    		g.drawLine(xEnd, yStart, xEnd, yEnd);
    		g.drawLine(xStart, yEnd, xEnd, yEnd);
    		for (int i = 1; i < GomokuPosition.CHESSBOARD_SIZE; i++) {
    			g.drawLine(xStart + i * widthStep, yStart, xStart + i * widthStep, yEnd);
    			g.drawLine(xStart, yStart + i * heightStep, xEnd, yStart + i * heightStep);
    		}
    		
    		// draw chess
    		int chessRadius = (int)(Math.min(widthStep, heightStep) * 0.9 *0.5);
    		for (int i = 0; i < board.grid.length; i++) {
    			for (int j = 0; j < board.grid[0].length; j++) {
    				if (board.grid[i][j] == GomokuPosition.HUMAN) {
    				    g.setColor(Color.BLACK);
    				    int xCenter = (int)(xStart + (i + 0.5) * widthStep);
    				    int yCenter = (int)(yStart + (j + 0.5) * heightStep);
    				    g.fillOval(xCenter - chessRadius, yCenter - chessRadius, 2 * chessRadius, 2 * chessRadius);
    				}
    				else if (board.grid[i][j] == GomokuPosition.PROGRAM) {
    					g.setColor(Color.WHITE);
    					int xCenter = (int)(xStart + (i + 0.5) * widthStep);
    				    int yCenter = (int)(yStart + (j + 0.5) * heightStep);
    				    g.fillOval(xCenter - chessRadius, yCenter - chessRadius, 2 * chessRadius, 2 * chessRadius);
    				}
    			}
    		}
    	}
    }
}


