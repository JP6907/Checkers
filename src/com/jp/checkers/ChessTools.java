package com.jp.checkers;

import java.awt.Point;
import java.io.*; 
import java.applet.*; 
/**
 * ������
 * @author Administrator
 *
 */
public class ChessTools {
	
	private static Point[][] p = ChessBoard.p; 
	public static boolean win = false; 

	public ChessTools(){
		
	}

	/**
	 * ��������ĵ�
	 * @param pp
	 * @return
	 */
	public static Point getNorthWest(Point pp){
		int x = 0;
		int y = 0;
		for(int ii=1;ii<9;ii++)
			for(int jj=1;jj<9;jj++){
				if(p[ii][jj] .equals(pp)){
					x = ii;
					y = jj;
				}		
			}
		if(x-1>0 && y-1>0)
			return p[x-1] [y-1];
		return null;
	}
	/**
	 * ��������ĵ�
	 * @param pp
	 * @return
	 */
	public static Point getNorthEast(Point pp){
		int x = 0;
		int y = 0;
		for(int ii=1;ii<9;ii++)
			for(int jj=1;jj<9;jj++){
				if(p[ii][jj] .equals(pp)){
					x = ii;
					y = jj;
				}		
			}
		if(x+1<9 && y-1>0)
			return p[x+1] [y-1];
		return null;
	}
	/**
	 * ���Ϸ���ĵ�
	 * @param pp
	 * @return
	 */
	public static Point getSouthWest(Point pp){
		int x = 0;
		int y = 0;
		for(int ii=1;ii<9;ii++)
			for(int jj=1;jj<9;jj++){
				if(p[ii][jj] .equals(pp)){
					x = ii;
					y = jj;
				}		
			}
		if(x-1>0 && y+1<9)
			return p[x-1] [y+1];
		return null;
	}
	/**
	 * ���Ϸ���ĵ�
	 * @param pp
	 * @return
	 */
	public static Point getSouthEast(Point pp){
		int x = 0;
		int y = 0;
		for(int ii=1;ii<9;ii++)
			for(int jj=1;jj<9;jj++){
				if(p[ii][jj] .equals(pp)){
					x = ii;
					y = jj;
				}		
			}
		if(x+1<9 && y+1<9)
			return p[x+1] [y+1];
		return null;
	}
	/**
	 * �ж��Ƿ���Գ���
	 * @param monster
	 * @param CheckerState
	 * @return
	 */
	public static boolean eat(Chess monster,CheckerState CheckerState){//monster�Ƿ���Գ���
		int x = 0, y = 0;
		Chess [] red = CheckerState.red;
		Chess [] black = CheckerState.black;

		if(monster==null)
			return false;
		//��ȡ����λ��
		for(int ii=1;ii<9;ii++)
			for(int jj=1;jj<9;jj++){
				if(p[ii][jj] .equals(monster.getLocation())){
					x = ii;
					y = jj;
				}		
			}
		if(monster.getColor().equals("red")){
			for(int j=0;j<12;j++){//�ĸ���
				if(x-2>0&&y-2>0&&monster.isKing() && black[j].isVisible()&&black[j].getLocation().equals(p[x-1][y-1])&&hasChess(red,black,p[x-2][y-2]).equals("none"))
					return true;
				if(x-2>0&&y+2<9&&black[j].getLocation().equals(p[x-1][y+1])&&black[j].isVisible()&&hasChess(red,black,p[x-2][y+2]).equals("none"))
					return true;
				if(x+2<9&&y-2>0&&monster.isKing() && black[j].isVisible()&&black[j].getLocation().equals(p[x+1][y-1])&&hasChess(red,black,p[x+2][y-2]).equals("none"))
					return true;
				if(x+2<9&&y+2<9&&black[j].getLocation().equals(p[x+1][y+1])&&black[j].isVisible()&&hasChess(red,black,p[x+2][y+2]).equals("none"))
					return true;
			}
			return false;
		}
		else{
			for(int j=0;j<12;j++){//�ĸ���
				if(x-2>0&&y-2>0&&red[j].isVisible()&&red[j].getLocation().equals(p[x-1][y-1])&&hasChess(red,black,p[x-2][y-2]).equals("none"))
					return true;
				if(x-2>0&&y+2<9&&monster.isKing()&&red[j].getLocation().equals(p[x-1][y+1])&&red[j].isVisible()&&hasChess(red,black,p[x-2][y+2]).equals("none"))
					return true;
				if(x+2<9&&y-2>0&&red[j].isVisible()&&red[j].getLocation().equals(p[x+1][y-1])&&hasChess(red,black,p[x+2][y-2]).equals("none"))
					return true;
				if(x+2<9&&y+2<9&&monster.isKing()&&red[j].getLocation().equals(p[x+1][y+1])&&red[j].isVisible()&&hasChess(red,black,p[x+2][y+2]).equals("none"))
					return true;
			}
			return false;
		}
	}
	/**
	 * �ж��Ƿ���Գ���
	 * @param monster
	 * @param red
	 * @param black
	 * @return
	 */
	public static boolean eat(Chess monster,Chess[] red,Chess[] black){//�Ƿ��п��ԳԵ���
		int x = 0, y = 0;
		if(monster==null)
			return false;
		for(int ii=1;ii<9;ii++)
			for(int jj=1;jj<9;jj++){
				if(p[ii][jj] .equals(monster.getLocation())){
					x = ii;
					y = jj;
				}		
			}
		if(monster.getColor().equals("red")){
			for(int j=0;j<12;j++){//�ĸ���
				//�췽���������ߣ������ˣ�����Ϊ��
				if(x-2>0&&y-2>0&&monster.isKing() && black[j].isVisible()&&black[j].getLocation().equals(p[x-1][y-1])&&hasChess(red,black,p[x-2][y-2]).equals("none"))
					return true;
				//���Ϸ���ǰ��
				if(x-2>0&&y+2<9&&black[j].getLocation().equals(p[x-1][y+1])&&black[j].isVisible()&&hasChess(red,black,p[x-2][y+2]).equals("none"))
					return true;
				//�������򣬺��ˣ�����Ϊ��
				if(x+2<9&&y-2>0&&monster.isKing() && black[j].isVisible()&&black[j].getLocation().equals(p[x+1][y-1])&&hasChess(red,black,p[x+2][y-2]).equals("none"))
					return true;
				//���Ϸ���ǰ��
				if(x+2<9&&y+2<9&&black[j].getLocation().equals(p[x+1][y+1])&&black[j].isVisible()&&hasChess(red,black,p[x+2][y+2]).equals("none"))
					return true;
			}
			return false;
		}
		else{//����
			for(int j=0;j<12;j++){//�ĸ���
				if(x-2>0&&y-2>0&&red[j].isVisible()&&red[j].getLocation().equals(p[x-1][y-1])&&hasChess(red,black,p[x-2][y-2]).equals("none"))
					return true;
				if(x-2>0&&y+2<9&&monster.isKing()&&red[j].getLocation().equals(p[x-1][y+1])&&red[j].isVisible()&&hasChess(red,black,p[x-2][y+2]).equals("none"))
					return true;
				if(x+2<9&&y-2>0&&red[j].isVisible()&&red[j].getLocation().equals(p[x+1][y-1])&&hasChess(red,black,p[x+2][y-2]).equals("none"))
					return true;
				if(x+2<9&&y+2<9&&monster.isKing()&&red[j].getLocation().equals(p[x+1][y+1])&&red[j].isVisible()&&hasChess(red,black,p[x+2][y+2]).equals("none"))
					return true;
			}
			return false;
		}
	}

	public static boolean eat(Chess chess,CheckerState CheckerState,boolean b){
		int x = 0, y = 0;
		Chess [] red = CheckerState.red;
		Chess [] black = CheckerState.black;
		if(chess.getColor()=="red"){
			for(int i=0;i<12;i++){
				if(red[i].isVisible()){	
					for(int ii=1;ii<9;ii++)
						for(int jj=1;jj<9;jj++){
							if(p[ii][jj] .equals(red[i].getLocation())){
								x = ii;
								y = jj;
							}		
						}
					for(int j=0;j<12;j++){//�ĸ���
						if(x-2>0&&y-2>0&&red[i].isKing() && black[j].isVisible()&&black[j].getLocation().equals(p[x-1][y-1])&&hasChess(red,black,p[x-2][y-2]).equals("none"))
							return true;
						if(x-2>0&&y+2<9&&black[j].getLocation().equals(p[x-1][y+1])&&black[j].isVisible()&&hasChess(red,black,p[x-2][y+2]).equals("none"))
							return true;
						if(x+2<9&&y-2>0&&red[i].isKing() && black[j].isVisible()&&black[j].getLocation().equals(p[x+1][y-1])&&hasChess(red,black,p[x+2][y-2]).equals("none"))
							return true;
						if(x+2<9&&y+2<9&&black[j].getLocation().equals(p[x+1][y+1])&&black[j].isVisible()&&hasChess(red,black,p[x+2][y+2]).equals("none"))
							return true;
					}
				}	
			}
			return false;
		}
		else{ //����
			for(int i=0;i<12;i++){

				if(black[i].isVisible()){
					for(int ii=1;ii<9;ii++)
						for(int jj=1;jj<9;jj++){
							if(p[ii][jj] .equals(black[i].getLocation())){
								x = ii;
								y = jj;
							}		
						}
					for(int j=0;j<12;j++){//�ĸ���
						if(y+2<9&&x-2>0&&black[i].isKing() && red[j].isVisible()&&red[j].getLocation().equals(p[x-1][y+1])&&x-2>0&&hasChess(red,black,p[x-2][y+2]).equals("none"))
							return true;
						if(x-2>0&&y-2>0&&red[j].getLocation().equals(p[x-1][y-1])&&red[j].isVisible()&&x-2>0&&y-2>0&&hasChess(red,black,p[x-2][y-2]).equals("none"))
							return true;
						if(x+2<9&&y+2<9&&black[i].isKing() &&red[j].isVisible()&& red[j].getLocation().equals(p[x+1][y+1])&&y+2<9&&hasChess(red,black,p[x+2][y+2]).equals("none"))
							return true;
						if(x+2<9&&y-2>0&&red[j].getLocation().equals(p[x+1][y-1])&&y-2>0&&red[j].isVisible()&&hasChess(red,black,p[x+2][y-2]).equals("none"))
							return true;
					}
				}	
			}
			return false;
		}
	}
	/**
	 * �жϵ�ǰλ���Ƿ�������
	 * @param red
	 * @param black
	 * @param p
	 * @return "red" "black" or "none" 
	 */
	public static String hasChess(Chess[] red,Chess[] black,Point p){
		for(int i=0;i<12;i++){
			if(red[i].isVisible()&&red[i].getLocation().equals(p))
				return "red";
			if(black[i].isVisible()&&black[i].getLocation().equals(p))
				return "black";
		}
		return "none";
	}
	/**
	 * �жϵ�ǰλ���Ƿ�������
	 * @param st
	 * @param p
	 * @return  "red" "black" or "none" 
	 */
	public static String hasChess(CheckerState st,Point p){
		Chess[] red = st.red;
		Chess[] black = st.black;
		return hasChess(red,black,p);
	}
}
