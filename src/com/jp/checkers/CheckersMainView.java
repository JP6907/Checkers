package com.jp.checkers;

import javax.swing.*;
import java.awt.event.*;
/**
 * 主类
 * @author Administrator
 *
 */
public class CheckersMainView extends JFrame implements ActionListener, ItemListener{

	
	public static JPanel beginPanel = new JPanel();
	public static JPanel quitPanel = new JPanel();
	public JButton start = new JButton("新游戏");
	public JLabel level = new JLabel("难度: ");
	public JButton restart = new JButton("重新开始");
	public JRadioButton easy = new JRadioButton("简单");
	public JRadioButton middle = new JRadioButton("中等");
	public JRadioButton difficult = new JRadioButton("困难");
	public ButtonGroup group = new ButtonGroup();
	public static boolean isHomePage = true;
	public ChessBoard chessBoard; 	//棋盘
	public Computer computer;	//AI

	public static void main(String[] args){
		new CheckersMainView();
	}
	
	public CheckersMainView(){
		computer = new Computer();
		chessBoard = new ChessBoard(computer);
		computer.setBorad(chessBoard);
		this.setLayout(null);
		chessBoard.setLocation(0,35);

		beginPanel.setBounds(0,0,480-100,35);
		beginPanel.setLayout(null);
		
		quitPanel.setBounds(480-100,0,100,35);
		quitPanel.setLayout(null);
		
		easy.setBounds(130,0,70,35);
		middle.setBounds(200,0,70,35);
		difficult.setBounds(270,0,70,35);
		
		easy.setSelected(true);
		easy.addItemListener(this);
		middle.addItemListener(this);
		difficult.addItemListener(this);
		group.add(easy);
		group.add(middle);
		group.add(difficult);
		
		beginPanel.add(start);
		beginPanel.add(level);
		beginPanel.add(easy);
		beginPanel.add(middle);
		beginPanel.add(difficult);
		quitPanel.add(restart);

		start.setBounds(0,0,80,30);
		start.addActionListener(this);

		level.setBounds(90,0,40,30);
		
		restart.setBounds(0,0,100,30);
		restart.addActionListener(this);
		

		this.add(beginPanel);
		this.add(quitPanel);
		this.add(chessBoard);
		this.setBounds(100,100,490,545);
		this.setTitle("西洋跳棋");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource() instanceof JButton){
			JButton jb = (JButton)e.getSource();
			if(jb.equals(start)){ //新游戏
				ChessTools.win = false;
				chessBoard.init();
				chessBoard.setVisible(true);			
				chessBoard.repaint();				
//				ChessBoard.myTurn = false;
//				computer.notify();
			}else if(jb.equals(restart)){
				int i = JOptionPane.showConfirmDialog(null, "确定要重新开始吗?");
				if(i==0){
					//System.exit(0);
					computer = new Computer();
					chessBoard = new ChessBoard(computer);
					computer.setBorad(chessBoard);
					chessBoard.repaint();
				}
				
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==easy){
			computer.setLevel(0);
		}else if(e.getSource()==middle){
			computer.setLevel(1);
		}else if(e.getSource()==difficult){
			computer.setLevel(2);
		}
	}
	
}