package com.jp.checkers;

import java.awt.Point;
import java.io.*; 
import java.applet.*; 
/**
 * 工具类
 * @author Administrator
 *
 */
public class ChessTools {
	
	private static Point[][] p = ChessBoard.p; 
	public static boolean win = false; 

	public ChessTools(){
		
	}

	/**
	 * 西北方向的点
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
	 * 东北方向的点
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
	 * 西南方向的点
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
	 * 东南方向的点
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
	 * 判断是否可以吃子
	 * @param monster
	 * @param CheckerState
	 * @return
	 */
	public static boolean eat(Chess monster,CheckerState CheckerState){//monster是否可以吃子
		int x = 0, y = 0;
		Chess [] red = CheckerState.red;
		Chess [] black = CheckerState.black;

		if(monster==null)
			return false;
		//获取坐标位置
		for(int ii=1;ii<9;ii++)
			for(int jj=1;jj<9;jj++){
				if(p[ii][jj] .equals(monster.getLocation())){
					x = ii;
					y = jj;
				}		
			}
		if(monster.getColor().equals("red")){
			for(int j=0;j<12;j++){//四个角
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
			for(int j=0;j<12;j++){//四个角
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
	 * 判断是否可以吃子
	 * @param monster
	 * @param red
	 * @param black
	 * @return
	 */
	public static boolean eat(Chess monster,Chess[] red,Chess[] black){//是否有可以吃的子
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
			for(int j=0;j<12;j++){//四个角
				//红方，往西北走，及后退，必须为王
				if(x-2>0&&y-2>0&&monster.isKing() && black[j].isVisible()&&black[j].getLocation().equals(p[x-1][y-1])&&hasChess(red,black,p[x-2][y-2]).equals("none"))
					return true;
				//西南方向，前进
				if(x-2>0&&y+2<9&&black[j].getLocation().equals(p[x-1][y+1])&&black[j].isVisible()&&hasChess(red,black,p[x-2][y+2]).equals("none"))
					return true;
				//东北方向，后退，必须为王
				if(x+2<9&&y-2>0&&monster.isKing() && black[j].isVisible()&&black[j].getLocation().equals(p[x+1][y-1])&&hasChess(red,black,p[x+2][y-2]).equals("none"))
					return true;
				//东南方向，前进
				if(x+2<9&&y+2<9&&black[j].getLocation().equals(p[x+1][y+1])&&black[j].isVisible()&&hasChess(red,black,p[x+2][y+2]).equals("none"))
					return true;
			}
			return false;
		}
		else{//黑棋
			for(int j=0;j<12;j++){//四个角
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
					for(int j=0;j<12;j++){//四个角
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
		else{ //黑棋
			for(int i=0;i<12;i++){

				if(black[i].isVisible()){
					for(int ii=1;ii<9;ii++)
						for(int jj=1;jj<9;jj++){
							if(p[ii][jj] .equals(black[i].getLocation())){
								x = ii;
								y = jj;
							}		
						}
					for(int j=0;j<12;j++){//四个角
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
	 * 判断当前位置是否有棋子
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
	 * 判断当前位置是否有棋子
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
