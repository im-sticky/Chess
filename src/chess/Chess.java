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
 *
 * @author
 * Coded By: Alex Craig, Mark Aronin
 * Images By: abener
 */
public class Chess {

    private JFrame frame;
    private JPanel[][] squares;
    private JLabel[][] squareLabels;
    private Color[] squareColors = {Color.white, Color.LIGHT_GRAY};
    private int movingFlag;
    private JLabel movingPiece;
    private int[] previousSquare = {0, 0};
    private int[] previousHoverSquare = {0, 0};
    
    public Chess() {
        movingPiece = new JLabel();
        movingFlag=0;
        createBoard();
        addListeners();
    }
    
    public void createBoard() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                
        frame = new JFrame();
        squares = new JPanel[8][8];
        squareLabels = new JLabel[8][8];
             
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
            }
        }
   
    }
    
    public ImageIcon loadPiece(int row, int column) {
        ImageIcon piece;
        
        //Black Pieces
        if (row==0 && (column==0 || column==7)) {
            piece = new ImageIcon(getClass().getResource("/pieces/br.png"));
        }
        
        else if (row==0 && (column==1 || column==6)) {
            piece = new ImageIcon(getClass().getResource("/pieces/bkn.png"));
        }
        
        else if (row==0 && (column==2 || column==5)) {
            piece = new ImageIcon(getClass().getResource("/pieces/bb.png"));
        }
        
        else if (row==0 && column==3) {
            piece = new ImageIcon(getClass().getResource("/pieces/bq.png"));
        }
        
        else if (row==0 && column==4) {
            piece = new ImageIcon(getClass().getResource("/pieces/bki.png"));
        }
        
        else if (row==1) {
            piece = new ImageIcon(getClass().getResource("/pieces/bp.png"));
        }
        
        //White Pieces
        else if (row==6) {
            piece = new ImageIcon(getClass().getResource("/pieces/wp.png"));
        }
        
        else if (row==7 && (column==0 || column==7)) {
            piece = new ImageIcon(getClass().getResource("/pieces/wr.png"));
        }
        
        else if (row==7 && (column==1 || column==6)) {
            piece = new ImageIcon(getClass().getResource("/pieces/wkn.png"));
        }
        
        else if (row==7 && (column==2 || column==5)) {
            piece = new ImageIcon(getClass().getResource("/pieces/wb.png"));
        }
        
        else if (row==7 && column==3) {
            piece = new ImageIcon(getClass().getResource("/pieces/wq.png"));
        }
        
        else if (row==7 && column==4) {
            piece = new ImageIcon(getClass().getResource("/pieces/wki.png"));
        }
        
        else {
            piece = new ImageIcon(getClass().getResource("/pieces/blank.png"));
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
                                        previousSquare[0]=i;
                                        previousSquare[1]=j;   
                                        movingFlag++;
                                    }
                                }
                                    
                                else {
                                    if (squareLabels[i][j]==tempLabel) {
                                        squareLabels[i][j].setIcon(movingPiece.getIcon());
                                        ImageIcon tempIcon = new ImageIcon(getClass().getResource("/pieces/blank.png"));
                                        squareLabels[previousSquare[0]][previousSquare[1]].setIcon(tempIcon);
                                        movingFlag--;
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
                                    squares[i][j].setBackground(Color.CYAN);
                                    previousHoverSquare[0]=i;
                                    previousHoverSquare[1]=j;
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
                                    squares[previousHoverSquare[0]][previousHoverSquare[1]].setBackground(squareColors[(i+j)%2]);
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
                                    squares[i][j].setBackground(Color.CYAN);
                                    previousHoverSquare[0]=i;
                                    previousHoverSquare[1]=j;
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
                                    squares[previousHoverSquare[0]][previousHoverSquare[1]].setBackground(squareColors[(i+j)%2]);
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