package chess;

import java.util.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.beans.*;
import java.text.*;
import java.util.concurrent.*;

/**
 * Coded By: Alex Craig
 * Debugged By : Alex Craig, Mark Aronin
 * Images By: abener
 */

/**
 * Binary Board Legend
 * 
 * 0000 = 0 = blank
 * 
 * 0001 = 1 = wp
 * 0011 = 3 = wr
 * 0111 = 7 = wki
 * 0101 = 5 = wq
 * 0100 = 4 = wb
 * 0110 = 6 = wkn
 * 
 * 1001 = 9 = bp
 * 1011 = 12 = br
 * 1111 = 15 = bki
 * 1101 = 13 = bq
 * 1100 = 12 = bb
 * 1110 = 14 = bkn
 * 
 * 0 = white's turn
 * 1 = black's turn
 */

public class Chess {

    private JFrame frame;
    private JPanel[][] squares;
    private JLabel[][] squareLabels;
    private Color[] squareColors = {Color.white, Color.LIGHT_GRAY};
    private int movingFlag;
    private int selectedFlag;
    private int turnFlag;
    private JLabel movingPiece;
    private int[] previousSquare = {0, 0};
    private int[] previousHoverSquare = {0, 0};
    private int[][] binaryBoard;
    
    public Chess() {
        movingPiece = new JLabel();
        movingFlag = 0;
        turnFlag = 0;
        createBoard();
        addListeners();
    }
    
    public void createBoard() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                
        frame = new JFrame();
        squares = new JPanel[8][8];
        squareLabels = new JLabel[8][8];
        binaryBoard = new int[8][8];
        
        frame.setTitle("Chess Game");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLayout(new GridLayout(8, 8));
        frame.setLocation((dim.width-(frame.getSize().width))/2, (dim.height-(frame.getSize().height))/2);
        
        for (int j=0; j<8; j++) {         
            for (int i=0; i<8; i++) {            
                squares[j][i] = new JPanel();
                squareLabels[j][i] = new JLabel();
                squares[j][i].setBackground(squareColors[(i+j)%2]);
                squares[j][i].setBorder(BorderFactory.createLineBorder(Color.black));               
                frame.add(squares[j][i]);
                squares[j][i].add(squareLabels[j][i]);
                squareLabels[j][i].setIcon(loadPiece(j, i));
                
                //TEMP binaryBoard DISPLAY
                System.out.print(binaryBoard[j][i]+" ");
            }
            System.out.print("\n");
        }
    }
    
    public ImageIcon loadPiece(int row, int column) {
        ImageIcon piece;
        
        //Black Pieces
        if (row==0 && (column==0 || column==7)) {
            piece = new ImageIcon(getClass().getResource("/pieces/br.png"));
            binaryBoard[row][column] = 11;
        }
        
        else if (row==0 && (column==1 || column==6)) {
            piece = new ImageIcon(getClass().getResource("/pieces/bkn.png"));
            binaryBoard[row][column] = 14;
        }
        
        else if (row==0 && (column==2 || column==5)) {
            piece = new ImageIcon(getClass().getResource("/pieces/bb.png"));
            binaryBoard[row][column] = 12;
        }
        
        else if (row==0 && column==3) {
            piece = new ImageIcon(getClass().getResource("/pieces/bq.png"));
            binaryBoard[row][column] = 13;
        }
        
        else if (row==0 && column==4) {
            piece = new ImageIcon(getClass().getResource("/pieces/bki.png"));
            binaryBoard[row][column] = 15;
        }
        
        else if (row==1) {
            piece = new ImageIcon(getClass().getResource("/pieces/bp.png"));
            binaryBoard[row][column] = 9;
        }
        
        //White Pieces
        else if (row==6) {
            piece = new ImageIcon(getClass().getResource("/pieces/wp.png"));
            binaryBoard[row][column] = 1;
        }
        
        else if (row==7 && (column==0 || column==7)) {
            piece = new ImageIcon(getClass().getResource("/pieces/wr.png"));
            binaryBoard[row][column] = 3;
        }
        
        else if (row==7 && (column==1 || column==6)) {
            piece = new ImageIcon(getClass().getResource("/pieces/wkn.png"));
            binaryBoard[row][column] = 6;
        }
        
        else if (row==7 && (column==2 || column==5)) {
            piece = new ImageIcon(getClass().getResource("/pieces/wb.png"));
            binaryBoard[row][column] = 4;
        }
        
        else if (row==7 && column==3) {
            piece = new ImageIcon(getClass().getResource("/pieces/wq.png"));
            binaryBoard[row][column] = 5;
        }
        
        else if (row==7 && column==4) {
            piece = new ImageIcon(getClass().getResource("/pieces/wki.png"));
            binaryBoard[row][column] = 7;
        }
        
        else {
            piece = new ImageIcon(getClass().getResource("/pieces/blank.png"));
            binaryBoard[row][column] = 0;
        }
        
        return piece;
    }
    
    public void addListeners() {
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                squareLabels[i][j].addMouseListener(new MouseAdapter() {
                    public void mousePressed (MouseEvent e) {
                        JLabel tempLabel = new JLabel();
                        tempLabel = (JLabel)e.getSource();
                                           
                        for (int i=0; i<8; i++) {
                            for (int j=0; j<8; j++) {
                                if (movingFlag==0) {
                                    if (squareLabels[i][j]==tempLabel) {
                                        movingPiece.setIcon(tempLabel.getIcon());
                                        squares[i][j].setBackground(Color.GREEN);                                      
                                        previousSquare[0]=i;
                                        previousSquare[1]=j;
                                        selectedFlag++;
                                        movingFlag++;
                                    }
                                }
                                    
                                else {
                                    if (squareLabels[i][j]==tempLabel) {
                                        if (squareLabels[i][j]!=squareLabels[previousSquare[0]][previousSquare[1]]) {
                                            squareLabels[i][j].setIcon(movingPiece.getIcon());
                                            ImageIcon tempIcon = new ImageIcon(getClass().getResource("/pieces/blank.png"));
                                            squareLabels[previousSquare[0]][previousSquare[1]].setIcon(tempIcon);                                      
                                            squares[previousSquare[0]][previousSquare[1]].setBackground(squareColors[(previousSquare[0]+previousSquare[1])%2]);
                                            binaryBoard[i][j] = binaryBoard[previousSquare[0]][previousSquare[1]];
                                            binaryBoard[previousSquare[0]][previousSquare[1]] = 0;                                           
                                            selectedFlag--;
                                            movingFlag--;
                                                    //TEMP binaryBoard DISPLAY
                                                    for (int b=0; b<8; b++) {         
                                                    for (int a=0; a<8; a++) {            
                                                    System.out.print(binaryBoard[b][a]+" ");
                                                    }
                                                    System.out.print("\n");
                                                    }
                                        }
                                        
                                        else {
                                            squares[previousSquare[0]][previousSquare[1]].setBackground(squareColors[(previousSquare[0]+previousSquare[1])%2]);
                                            selectedFlag--;
                                            movingFlag--;
                                        }
                                    }                                                                     
                                }
                            }
                        }           
                    }
                    
                    public void mouseEntered (MouseEvent e) {
                        JLabel transferLabel = new JLabel();
                        transferLabel = (JLabel)e.getSource();
                        
                        JPanel tempPanel = new JPanel();
                        tempPanel = (JPanel)transferLabel.getParent();

                        for (int i=0; i<8; i++) {
                            for (int j=0; j<8; j++) {
                                if (squares[i][j]==tempPanel) {
                                    if (selectedFlag==0) {
                                        squares[i][j].setBackground(Color.CYAN);
                                        previousHoverSquare[0]=i;
                                        previousHoverSquare[1]=j;
                                    }
                                    
                                    else if (squares[i][j]!=squares[previousSquare[0]][previousSquare[1]]) {
                                        squares[i][j].setBackground(Color.CYAN);
                                        previousHoverSquare[0]=i;
                                        previousHoverSquare[1]=j;
                                    }
                                }
                            }
                        }
                        
                    }
                    
                    public void mouseExited (MouseEvent e) {
                        JLabel transferLabel = new JLabel();
                        transferLabel = (JLabel)e.getSource();
                        
                        JPanel tempPanel = new JPanel();
                        tempPanel = (JPanel)transferLabel.getParent();
                        
                        for (int i=0; i<8; i++) {
                            for (int j=0; j<8; j++) {
                                if (squares[i][j]==tempPanel) {
                                    if (selectedFlag==0) {
                                        squares[previousHoverSquare[0]][previousHoverSquare[1]].setBackground(squareColors[(i+j)%2]);
                                    }
                                    
                                    else if (squares[i][j]!=squares[previousSquare[0]][previousSquare[1]]) {
                                        squares[previousHoverSquare[0]][previousHoverSquare[1]].setBackground(squareColors[(i+j)%2]);
                                    }
                                }
                            }
                        }
                    }
                });
                
                squares[i][j].addMouseListener(new MouseAdapter() {
                    public void mouseEntered (MouseEvent e) {
                        JPanel tempPanel = new JPanel();
                        tempPanel = (JPanel)e.getSource();

                        for (int i=0; i<8; i++) {
                            for (int j=0; j<8; j++) {
                                if (squares[i][j]==tempPanel) {
                                    if (selectedFlag==0) {
                                        squares[i][j].setBackground(Color.CYAN);
                                        previousHoverSquare[0]=i;
                                        previousHoverSquare[1]=j;
                                    }
                                    
                                    else if (squares[i][j]!=squares[previousSquare[0]][previousSquare[1]]) {
                                        squares[i][j].setBackground(Color.CYAN);
                                        previousHoverSquare[0]=i;
                                        previousHoverSquare[1]=j;
                                    }
                                }
                            }
                        }
                    }
                    
                    public void mouseExited (MouseEvent e) {
                        JPanel tempPanel = new JPanel();
                        tempPanel = (JPanel)e.getSource();
                        
                        for (int i=0; i<8; i++) {
                            for (int j=0; j<8; j++) {
                                if (squares[i][j]==tempPanel) {
                                    if (selectedFlag==0) {
                                        squares[previousHoverSquare[0]][previousHoverSquare[1]].setBackground(squareColors[(i+j)%2]);
                                    }
                                    
                                    else if (squares[i][j]!=squares[previousSquare[0]][previousSquare[1]]) {
                                        squares[previousHoverSquare[0]][previousHoverSquare[1]].setBackground(squareColors[(i+j)%2]);
                                    }
                                }
                            }
                        }
                    }
                });
            }              
        }
    }
    
    public static void main(String[] args) {
       Chess c = new Chess();
    }
}