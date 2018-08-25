package com.jp.checkers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
/**
 * 棋盘状态类，24个棋子
 * @author Administrator
 *
 */
public class CheckerState{
		
	public int depth; //搜索时所在深度
	public int value; //权值
	public static Chess OldRed[];
	public static Chess OldBlack[];
	public static Point p[][] = ChessBoard.p;
	public Chess red[];
	public Chess black[] ;
	private Chess redKiller; //刚吃过子的红子
	private Chess blackKiller; //刚吃过子的黑子
	public static int[] w = new int[]{8,6,-6,8,-8,-4,4};//评价函数的系数
	
	/**
	 * 构造函数
	 * @param red
	 * @param black
	 */
	public CheckerState(Chess red[], Chess[] black){
		
		Chess red0[] = new Chess[12];
		Chess black0[] = new Chess[12];
		for(int i=0;i<12;i++){
			red0[i] = new Chess(); 
			red0[i].id = red[i].id;
			red0[i].setColor("red");
			red0[i].setLocation(red[i].getLocation());
			red0[i].setVisible(red[i].isVisible());
			red0[i].setKing(red[i].isKing());

			black0[i] = new Chess();
			black0[i].id = black[i].id;
			black0[i].setColor("black");
			black0[i].setLocation(black[i].getLocation());
			black0[i].setVisible(black[i].isVisible());
			black0[i].setKing(black[i].isKing());

		}
		this.red = red0;
		this.black = black0;
	}
	public CheckerState(Chess red[], Chess[] black, boolean b){
		
		Chess red0[] = new Chess[12];
		Chess black0[] = new Chess[12];
		
//		棋手走完后第一次创建新状态时要包初始状态保存起来，这也是为以后走棋用的
		OldRed = red;
		OldBlack = black;
		
		for(int i=0;i<12;i++){
			red0[i] = new Chess(); 
			red0[i].id = red[i].id;
			red0[i].setColor("red");
			red0[i].setLocation(red[i].getLocation());
			red0[i].setVisible(red[i].isVisible());
			red0[i].setKing(red[i].isKing());

			black0[i] = new Chess();
			black0[i].id = black[i].id;
			black0[i].setColor("black");
			black0[i].setLocation(black[i].getLocation());
			black0[i].setVisible(black[i].isVisible());
			black0[i].setKing(black[i].isKing());

		}
		this.red = red0;
		this.black = black0;
	}
	/**
	 * 红方，即电脑方的 估值函数
	 * @return
	 */
	public int getValue(){
		int redNumber = this.getRedNumber(); //红子个数
		int blackNumber = this.getBlackNumber(); //黑子个数
		int redKingNumber = this.getRedKingNumber();//红王个数
		int blackKingNumber = this.getBlackKingNumber();//黑王个数
		int redBeingKilledNumber = this.getRedBeingKilledNumber();//红子将要被吃掉的个数
		int blackBeingKilledNumber = this.getBlackBeingKilledNumber();//黑子将要被吃掉的个数
		
		if(redNumber+redKingNumber==0) //红方输掉的状态，估值最小
			return -99999;
		if(blackNumber+blackKingNumber==0) //红方赢棋的状态，估值最大
			return 99999;	
		//估值函数
		return w[0] + w[1] * redNumber + w[2] * blackNumber + w[3] * redKingNumber+
		w[4] * blackKingNumber + w[5] * redBeingKilledNumber + w[6] * blackBeingKilledNumber;
	}
	/**
	 * 判断当前位置是否有棋子
	 * @param p
	 * @return "red" "black" or "none" 
	 */
	private String hasChess(Point p){
		if(p==null)
			return null;

		for(int i=0;i<12;i++){
			if(red[i].isVisible()&&red[i].getLocation().equals(p))
				return "red";
			if(black[i].isVisible()&&black[i].getLocation().equals(p))
				return "black";
		}
		return "none";
	}
	/**
	 * 获取当前位置的黑子
	 * @param p
	 * @return
	 */
	public Chess getBlack(Point p){
		for(int i=0;i<12;i++)
			if(black[i].isVisible() && black[i].getLocation().equals(p))
				return black[i];
		return null;
	}
	/**
	 * 获取当前位置的红子
	 * @param p
	 * @return
	 */
	public Chess getRed(Point p){
		for(int i=0;i<12;i++)
			if(red[i].isVisible() && red[i].getLocation().equals(p))
				return red[i];
		return null;
	}
	/**
	 * 获取红方非king的棋子数量
	 * @return
	 */
	public int getRedNumber(){
		int count = 0;
		for(int i=0;i<12;i++){
			if(red[i].isVisible() && !red[i].isKing())
				count++;
		}
		return count;
	}
	/**
	 * 获取黑方非king的棋子数量
	 * @return
	 */
	public int getBlackNumber(){
		int count = 0;
		for(int i=0;i<12;i++){
			if(black[i].isVisible() && !black[i].isKing())
				count++;
		}
		return count;
	}
	/**
	 * 获取红方king的数量
	 * @return
	 */
	public int getRedKingNumber(){
		int count = 0;
		for(int i=0;i<12;i++){
			if(red[i].isVisible() && red[i].isKing())
				count++;
		}
		return count;
	}
	/**
	 * 获取黑方king的数量
	 * @return
	 */
	public int getBlackKingNumber(){
		int count = 0;
		for(int i=0;i<12;i++){
			if(black[i].isVisible() && black[i].isKing())
				count++;
		}
		return count;
	}
	/**
	 * 获取红方即将被吃的棋子的数量
	 * @return
	 */
	public int getRedBeingKilledNumber(){
		for(int i=0;i<12;i++){
			red[i].counted = false;
		}

		int x = 0, y = 0, count = 0;
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
					if(x-2>0&&y+2<9&&black[i].isKing() && red[j].isVisible()&&red[j].getLocation().equals(p[x-1][y+1])&&hasChess(p[x-2][y+2]).equals("none") && !red[j].counted)
					{
						count++;
						red[j].counted = true;
					}
					if(x-2>0&&y-2>0&&red[j].getLocation().equals(p[x-1][y-1])&&red[j].isVisible()&&hasChess(p[x-2][y-2]).equals("none")&& !red[j].counted)
					{
						count++;
						red[j].counted = true;
					}
					if(x+2<9&&y+2<9&&black[i].isKing() &&red[j].isVisible()&& red[j].getLocation().equals(p[x+1][y+1])&&y+2<9&&hasChess(p[x+2][y+2]).equals("none")&& !red[j].counted)
					{
						count++;
						red[j].counted = true;
					}
					if(x+2<9&&y-2>0&&red[j].getLocation().equals(p[x+1][y-1])&&red[j].isVisible()&&hasChess(p[x+2][y-2]).equals("none")&& !red[j].counted)
					{
						count++;
						red[j].counted = true;
					}
				}
			}	
		}
		return count;
	}
	/**
	 * 获取黑方即将被吃的棋子的数量
	 * @return
	 */
	public int getBlackBeingKilledNumber(){
		for(int i=0;i<12;i++){
			black[i].counted = false;
		}
		int x = 0, y = 0, count = 0;
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
					if(x-2>0&&y-2>0&&red[i].isKing() && black[j].isVisible()&&black[j].getLocation().equals(p[x-1][y-1])&&hasChess(p[x-2][y-2]).equals("none")&&!black[j].counted)
					{
						count++;
						black[j].counted = true;
					}
					if(x-2>0&&y+2<9&&black[j].getLocation().equals(p[x-1][y+1])&&black[j].isVisible()&&hasChess(p[x-2][y+2]).equals("none")&&!black[j].counted)
					{
						count++;
						black[j].counted = true;
					}
					if(x+2<9&&y-2>0&&red[i].isKing() && black[j].isVisible()&&black[j].getLocation().equals(p[x+1][y-1])&&hasChess(p[x+2][y-2]).equals("none")&&!black[j].counted)
					{
						count++;
						black[j].counted = true;
					}
					if(x+2<9&&y+2<9&&black[j].getLocation().equals(p[x+1][y+1])&&black[j].isVisible()&&hasChess(p[x+2][y+2]).equals("none")&&!black[j].counted)
					{
						count++;
						black[j].counted = true;
					}
				}
			}	
		}
		return count;
	}
	/**
	 * 红方下一步可能走的所有状态
	 * @return
	 */
	public ArrayList nextStatesOfRed(){
		ArrayList list = new ArrayList();
		for(int i=0;i<12;i++){//首先寻找吃子的状态
			if(!red[i].isVisible())
				continue;
			list.addAll(nextEatingStates(red[i],this,true));
		}
		if(list.size()==0) //没有吃子的情况再找其他状态
			for(int i=0;i<12;i++){
				if(!red[i].isVisible())
					continue;
				list.addAll(nextStates(red[i],this));
			}
		return list;
	}
	/**
	 * 黑方下一步可能走的所有状态
	 * @return
	 */
	public ArrayList nextStatesOfBlack(){//同上
		ArrayList list = new ArrayList();
		for(int i=0;i<12;i++){
			if(!black[i].isVisible())
				continue;
			list.addAll(nextEatingStates(black[i],this,true));
		}
		if(list.size()==0)
			for(int i=0;i<12;i++){
				if(!black[i].isVisible())
					continue;
				list.addAll(nextStates(black[i],this));
			}
		return list;
	}
	/**
	 * 下一步可以吃子的状态
	 * @param chess
	 * @param st
	 * @param first 判断是否为第一层
	 * @return
	 */
	private ArrayList nextEatingStates(Chess chess, CheckerState st,boolean first){
		ArrayList list = new ArrayList();
		Point pp = chess.getLocation();
		int x = pp.x/60 +1;
		int y = pp.y/60 +1;
		Chess red[] = st.red;
		Chess black[] = st.black;
		
		if(chess.getColor().equals("red")){
			if(ChessTools.eat(chess,st,true)){ //可以吃子，必须吃。返回的状态只是吃子的情况
				if(chess.isVisible()){	
					for(int j=0;j<12;j++){//检查四个角
						if(x-2>0&&y-2>0&&chess.isKing() && black[j].isVisible()&&black[j].getLocation().equals(p[x-1][y-1])&&hasChess(p[x-2][y-2]).equals("none"))
						{
							CheckerState state = new CheckerState(red ,black);
							state.getRed(p[x][y]).setLocation(p[x-2][y-2]);
							state.redKiller = state.getRed(p[x-2][y-2]);
							state.getBlack(p[x-1][y-1]).setVisible(false);
							list.add(state);
						}
						if(y+2<9&&x-2>0&&black[j].getLocation().equals(p[x-1][y+1])&&black[j].isVisible()&&hasChess(p[x-2][y+2]).equals("none"))
						{
							CheckerState state = new CheckerState(red ,black);
							state.getRed(p[x][y]).setLocation(p[x-2][y+2]);
							state.redKiller = state.getRed(p[x-2][y+2]);
							state.getBlack(p[x-1][y+1]).setVisible(false);
							list.add(state);
						}
						if(x+2<9&&y-2>0&&chess.isKing() && black[j].isVisible()&&black[j].getLocation().equals(p[x+1][y-1])&&hasChess(p[x+2][y-2]).equals("none"))
						{
							CheckerState state = new CheckerState(red ,black);
							state.getRed(p[x][y]).setLocation(p[x+2][y-2]);
							state.redKiller = state.getRed(p[x+2][y-2]);
							state.getBlack(p[x+1][y-1]).setVisible(false);
							list.add(state);
						}
						if(x+2<9&&y+2<9&&black[j].getLocation().equals(p[x+1][y+1])&&black[j].isVisible()&&hasChess(p[x+2][y+2]).equals("none"))
						{
							CheckerState state = new CheckerState(red ,black);
							state.getRed(p[x][y]).setLocation(p[x+2][y+2]);
							state.redKiller = state.getRed(p[x+2][y+2]);
							state.getBlack(p[x+1][y+1]).setVisible(false);
							list.add(state);
						}
					}
				}	
			}
			ArrayList temp = new ArrayList();
			for(int i=0;i<list.size();i++){
				temp.add(null);
			}
//			将list深拷贝到temp
			Collections.copy(temp, list);
			ArrayList nextLevelList = new ArrayList();
			for(Iterator it = temp.iterator();it.hasNext();){
				CheckerState s = (CheckerState)it.next();
				if(ChessTools.eat(s.redKiller, s)){  //刚吃过旗子的棋能否继续吃，第二层
					nextLevelList.addAll(nextEatingStates(s.redKiller,s,false));
				}
			}
			if(nextLevelList.size()>0)//下一层还有就返回下一层，可以继续吃棋子就一定要继续吃
				return nextLevelList;
			if(list.size()>0) //没有可以继续吃棋子就寻找其它可以走的情况
				return list;
		}
		else{ //黑棋
			if(ChessTools.eat(chess,st,true)){ //可以吃子，必须吃。返回的状态只是吃子的情况
				if(chess.isVisible()){	
					for(int j=0;j<12;j++){//四个角
						if(x-2>0&&y-2>0 && red[j].isVisible()&&red[j].getLocation().equals(p[x-1][y-1])&&hasChess(p[x-2][y-2]).equals("none"))
						{
							CheckerState state = new CheckerState(red ,black);
							state.getBlack(p[x][y]).setLocation(p[x-2][y-2]);
							state.blackKiller = state.getBlack(p[x-2][y-2]);
							state.getRed(p[x-1][y-1]).setVisible(false);
							list.add(state);
						}
						if(y+2<9&&x-2>0&&chess.isKing()&&red[j].getLocation().equals(p[x-1][y+1])&&red[j].isVisible()&&hasChess(p[x-2][y+2]).equals("none"))
						{
							CheckerState state = new CheckerState(red ,black);
							state.getBlack(p[x][y]).setLocation(p[x-2][y+2]);
							state.blackKiller = state.getBlack(p[x-2][y+2]);
							state.getRed(p[x-1][y+1]).setVisible(false);
							list.add(state);
						}
						if(x+2<9&&y-2>0 && red[j].isVisible()&&red[j].getLocation().equals(p[x+1][y-1])&&hasChess(p[x+2][y-2]).equals("none"))
						{
							CheckerState state = new CheckerState(red ,black);
							state.getBlack(p[x][y]).setLocation(p[x+2][y-2]);
							state.blackKiller = state.getBlack(p[x+2][y-2]);
							state.getRed(p[x+1][y-1]).setVisible(false);
							list.add(state);
						}
						if(x+2<9&&y+2<9&&chess.isKing()&&red[j].getLocation().equals(p[x+1][y+1])&&red[j].isVisible()&&hasChess(p[x+2][y+2]).equals("none"))
						{
							CheckerState state = new CheckerState(red ,black);
							state.getBlack(p[x][y]).setLocation(p[x+2][y+2]);
							state.blackKiller = state.getBlack(p[x+2][y+2]);
							state.getRed(p[x+1][y+1]).setVisible(false);
							list.add(state);
						}
					}
				}	
			}

			ArrayList temp = new ArrayList();
			for(int i=0;i<list.size();i++){
				temp.add(null);
			}
			Collections.copy(temp, list);
			ArrayList nextLevelList = new ArrayList();
			for(Iterator it = temp.iterator();it.hasNext();){
				CheckerState s = (CheckerState)it.next();
				if(ChessTools.eat(s.blackKiller, s)){
					nextLevelList.addAll(nextEatingStates(s.blackKiller,s,false));
				}
			}
			
			if(nextLevelList.size()>0)
				return nextLevelList;
			if(list.size()>0)
				return list;
		}
		
		return list;
	}
	/**
	 * 不吃子的下一步状态
	 * @param chess
	 * @param st
	 * @return
	 */
	private ArrayList nextStates(Chess chess,CheckerState st){
		ArrayList list = new ArrayList();
		Point pp = chess.getLocation();
	
		Point southEast = ChessTools.getSouthEast(pp);
		Point southWest = ChessTools.getSouthWest(pp);
		Point northWest = ChessTools.getNorthWest(pp);
		Point northEast = ChessTools.getNorthEast(pp);

		Chess red[] = st.red;
		Chess black[] = st.black;

		if(chess.getColor().equals("red")){

			if(southEast!=null && hasChess(southEast).equals("none")){
				CheckerState state = new CheckerState(red ,black);
				state.getRed(pp).setLocation(southEast);
				list.add(state);
			}
			if(southWest!=null && hasChess(southWest).equals("none")){
				CheckerState state = new CheckerState(red ,black);
				state.getRed(pp).setLocation(southWest);
				list.add(state);
			}
			if(northEast!=null && hasChess(northEast).equals("none") && chess.isKing()){
				CheckerState state = new CheckerState(red ,black);
				state.getRed(pp).setLocation(northEast);
				list.add(state);
			}
			if(northWest!=null && hasChess(northWest).equals("none") && chess.isKing()){
				CheckerState state = new CheckerState(red ,black);
				state.getRed(pp).setLocation(northWest);

				list.add(state);
			}
			if(southEast!=null){
				Point ssEast = ChessTools.getSouthEast(southEast);
				if(ssEast!=null && hasChess(southEast).equals("black") && hasChess(ssEast).equals("none")){
					CheckerState state = new CheckerState(red,black);
					state.getRed(pp).setLocation(ssEast);
					state.getBlack(southEast).setVisible(false);
					list.add(state);
				}
			}
			if(southWest!=null){
				Point ssWest = ChessTools.getSouthWest(southWest);
				if(ssWest!=null && hasChess(southWest).equals("black") && hasChess(ssWest).equals("none")){
					CheckerState state = new CheckerState(red,black);
					state.getRed(pp).setLocation(ssWest);
					state.getBlack(southWest).setVisible(false);
					list.add(state);
				}
			}
			if(northWest!=null){
				Point nnWest = ChessTools.getNorthWest(northWest);
				if(chess.isKing() && nnWest!=null && hasChess(northWest).equals("black") && hasChess(nnWest).equals("none")){
					CheckerState state = new CheckerState(red,black);
					state.getRed(pp).setLocation(nnWest);
					state.getBlack(northWest).setVisible(false);
					list.add(state);
				}
			}
			if(northEast!=null){
				Point nnEast =ChessTools.getNorthEast(northEast);
				if(chess.isKing() && nnEast!=null && hasChess(northEast).equals("black") && hasChess(nnEast).equals("none")){
					CheckerState state = new CheckerState(red,black);
					state.getRed(pp).setLocation(nnEast);
					state.getBlack(northEast).setVisible(false);
					list.add(state);
				}
			}
			return list;	
		}
		else{ //black
		
			if(southEast!=null && chess.isKing() && hasChess(southEast).equals("none")){
				CheckerState state = new CheckerState(red ,black);
				state.getBlack(pp).setLocation(southEast);
				list.add(state);
			}
			if(southWest!=null && chess.isKing() && hasChess(southWest).equals("none")){
				CheckerState state = new CheckerState(red ,black);
				state.getBlack(pp).setLocation(southWest);
				list.add(state);
			}
			if(northEast!=null && hasChess(northEast).equals("none")){
				CheckerState state = new CheckerState(red ,black);
				state.getBlack(pp).setLocation(northEast);
				list.add(state);
			}
			if(northWest!=null && hasChess(northWest).equals("none")){
				CheckerState state = new CheckerState(red ,black);
				state.getBlack(pp).setLocation(northWest);
				list.add(state);
			}
			if(southEast!=null){
				Point ssEast = ChessTools.getSouthEast(southEast);
				if(chess.isKing() && ssEast!=null && hasChess(southEast).equals("red") && hasChess(ssEast).equals("none")){
					CheckerState state = new CheckerState(red,black);
					state.getBlack(pp).setLocation(ssEast);
					state.getRed(southEast).setVisible(false);
					list.add(state);
				}
			}
			if(southWest!=null){
				Point ssWest = ChessTools.getSouthWest(southWest);
				if(chess.isKing() && ssWest!=null && hasChess(southWest).equals("red") && hasChess(ssWest).equals("none")){
					CheckerState state = new CheckerState(red,black);
					state.getBlack(pp).setLocation(ssWest);
					state.getRed(southWest).setVisible(false);
					list.add(state);
				}
			}
			if(northWest!=null){
				Point nnWest = ChessTools.getNorthWest(northWest);
				if(nnWest!=null && hasChess(northWest).equals("red") && hasChess(nnWest).equals("none")){
					CheckerState state = new CheckerState(red,black);
					state.getBlack(pp).setLocation(nnWest);
					state.getRed(northWest).setVisible(false);
					list.add(state);
				}
			}
			if(northEast!=null){
				Point nnEast = ChessTools.getNorthEast(northEast);
				if(nnEast!=null && hasChess(northEast).equals("red") && hasChess(nnEast).equals("none")){
					CheckerState state = new CheckerState(red,black);
					state.getBlack(pp).setLocation(nnEast);
					state.getRed(northEast).setVisible(false);
					list.add(state);
				}	
			}
			return list;
		}
	}
	public boolean equals(Object o){
		if(!(o instanceof CheckerState))
			return false;
		CheckerState s = (CheckerState)o;
		Chess redO[] = s.red;
		Chess blackO[] = s.black;
		for(int i=0;i<12;i++){
			if(!(red[i].getLocation().equals(redO[i].getLocation()) && 
					red[i].isVisible()==redO[i].isVisible() && red[i].isKing()==redO[i].isKing()))
				return false;
			if(!(black[i].getLocation().equals(blackO[i].getLocation()) && 
					black[i].isVisible()==blackO[i].isVisible() && black[i].isKing()==blackO[i].isKing()))
				return false;
		}
		return true;
	}

	public String toString(){
		String s = "red: ";
		for(int i=0;i<12;i++){
			s+= i+":"+red[i].toString()+" ";
		}
		s += ";  black: ";
		for(int i=0;i<12;i++){
			s+= i+":"+black[i].toString()+" ";
		}
		return s;
	}
	public CheckerState copy(){
		CheckerState st = new CheckerState(red,black);
		return st;
		
	}
}
