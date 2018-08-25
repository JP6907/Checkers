package com.jp.checkers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * 电脑AI控制类
 * alpha beta剪枝的极大极小值算法来搜索状态并走棋
 * 一直在监听是否轮到电脑下棋
 * @author Administrator
 *
 */
public class Computer extends Thread{
	
	private static int DEPTH = 2; //搜索深度.默认是简单的水平
	private static final int MAX = 99999;
	private static final int MIN = -99999;
	private CheckerState CheckerState;
	private static ArrayList NextCheckerStates; //当前状态所有可能的走法
	private JPanel chessBoard;
	private ChessTools util = new ChessTools();
	public static boolean eaten = false;
	
	public Computer(){
		start(); //启动线程
	}
	/**
	 * 设置棋盘状态
	 * @param CheckerState
	 */
	public void setCheckerState(CheckerState CheckerState){
		this.CheckerState = CheckerState;
	}

	public void run(){
		while(true){
			synchronized(this){ //互斥锁
				try{
//					此时是人在走棋，等待
					wait();
				}
				catch(InterruptedException ie){
					throw new RuntimeException(ie);
				}	
			}
			//锁被释放，轮到电脑下棋
			try{
				Thread.sleep(200); //停顿一下，避免走太快
			}
			catch(Exception e){
				e.printStackTrace();
			}
			alphaBetaSearch(CheckerState); //开始搜索
		}
	}
	/**
	 * alpha beta剪枝的极大极小值算法搜索
	 * @param CheckerState
	 */
	private void alphaBetaSearch(CheckerState CheckerState){
		
		CheckerState.depth = 0;  //初始深度为0
		NextCheckerStates = null;
		
		int value = maxValue(CheckerState,MIN,MAX); //电脑下棋，第一层为max层
		
		if(NextCheckerStates==null){ //如果电脑无路可走
			JOptionPane.showMessageDialog(null, "You win!");
			ChessTools.win = true;  //我方赢
			return ;
		}

//		在NextCheckerStates里面寻找最佳的走法,通过比较权值，寻找最大权值
		CheckerState s = null;
		CheckerState nextCheckerState = null;
		for(Iterator it = NextCheckerStates.iterator();it.hasNext();){
			s = (CheckerState)it.next();
			if(s.value==value){
				nextCheckerState = s;
				break;
			}
		}

//		电脑没棋走了，上面的判断还是有必要的，不然NextCheckerStates.iterator()一句会有异常
		if(nextCheckerState==null){
			JOptionPane.showMessageDialog(null, "You win!");
			ChessTools.win = true;
			return ;
		}

//		寻找当前状态和下一状态之间的路径，因为会有连续吃子的情况，所以还是有点麻烦的，
//		findpath这个方法返回的是一个数组，保存了所有的中间状态和最终状态
		ArrayList steps = findPath(new CheckerState(nextCheckerState.OldRed,nextCheckerState.OldBlack),nextCheckerState);
		for(Iterator it = steps.iterator();it.hasNext();){
			CheckerState st = (CheckerState)it.next();
			ChessBoard.red = st.red;
			ChessBoard.black = st.black;
			chessBoard.repaint();
			checkOver(st); //检查是否结束了
			try{
				Thread.sleep(300); //两步之间间隔一会
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		ChessBoard.myTurn = true;

	}
	/**
	 * max层，取最大值
	 * @param CheckerState
	 * @param a 下限
	 * @param b 上限
	 * @return
	 */
	private int maxValue(CheckerState CheckerState,int a,int b){

//		如果到了最底层，返回评价函数得到的权值
		if(CheckerState.depth==DEPTH) 
			return CheckerState.getValue();

//		先把CheckerState的value设为最小，因为后面要找一个最大值	
		CheckerState.value = MIN;
//		取最大值时对应红子的状态
		ArrayList list = CheckerState.nextStatesOfRed(); //红方下一步可能走的所有情况

//		保存第一层的状态，走棋的时候用
		if(CheckerState.depth==0)
			NextCheckerStates = list; 

		CheckerState s = null;
		int minValue = MIN;
		for(Iterator it = list.iterator();it.hasNext();){
			s = (CheckerState)it.next();
			s.depth = CheckerState.depth + 1; //深度加一

			int temp = minValue(s,a,b);//下一层为min层
			if(minValue < temp)
				minValue = temp;

//			minValue是下一层状态里最小的，它与CheckerState的value相比较，取大的
			CheckerState.value = CheckerState.value>minValue ? CheckerState.value : minValue;

//			CheckerState的value比beta值大，此时没必要再找下去，可以剪枝了  
			if(CheckerState.value>=b)
				return CheckerState.value;

//			如果CheckerState的value比alpha值大，更新alpha值
			a = a>CheckerState.value ? a : CheckerState.value;
		}
		return CheckerState.value;
	}
	/**
	 * min层，取最小值
	 * @param CheckerState
	 * @param a
	 * @param b
	 * @return
	 */
	private int minValue(CheckerState CheckerState,int a,int b){
		if(CheckerState.depth==DEPTH)
			return CheckerState.getValue();
		CheckerState.value = MAX;
		ArrayList list = CheckerState.nextStatesOfBlack();
		CheckerState s = null;
		int maxValue = MAX;
		for(Iterator it = list.iterator();it.hasNext();){
			s = (CheckerState)it.next();
			s.depth = CheckerState.depth + 1;

			int temp = maxValue(s,a,b);
			if(maxValue > temp)
				maxValue = temp;   //下一层最大
			CheckerState.value = CheckerState.value<maxValue ? CheckerState.value : maxValue;
			if(CheckerState.value<=a)
				return CheckerState.value;
			b = b<CheckerState.value ? b : CheckerState.value;
		}
		return CheckerState.value;
	}
	/**
	 * 寻找两个状态之间的路径
	 * @param from
	 * @param to
	 * @return
	 */
	private ArrayList findPath(CheckerState from, CheckerState to){

		ArrayList list = new ArrayList();
		Chess oriRed[] = from.red;
		Chess oriBlack[] = from.black;
		Chess currentBlack[] = to.black;
		Chess currentRed[] = to.red;
		Chess movedRed = null;

		boolean eaten = false;
		for(int i=0;i<12;i++){
			if(!oriRed[i].getLocation().equals(currentRed[i].getLocation()))
				movedRed = oriRed[i];   //移动过的红子
			if(oriBlack[i].isVisible()&& !currentBlack[i].isVisible())
				eaten = true;			//被吃的黑子
		}
		Point start = movedRed.getLocation();

		if(eaten)
			while(!from.equals(to)){
				if(ChessTools.getNorthWest(start)!=null && ChessTools.getNorthWest( ChessTools.getNorthWest(start))!=null && ChessTools.hasChess(from, ChessTools.getNorthWest(start)).equals("black")  &&
						ChessTools.hasChess(to,ChessTools.getNorthWest(start)).equals("none") )
				{
					from.getBlack( ChessTools.getNorthWest(start)).setVisible(false);
					from.getRed(start).setLocation(ChessTools.getNorthWest( ChessTools.getNorthWest(start)));
					list.add(from.copy());
					start = ChessTools.getNorthWest( ChessTools.getNorthWest(start));
				}
				else if(ChessTools.getNorthEast(start)!=null && ChessTools.getNorthEast( ChessTools.getNorthEast(start))!=null && ChessTools.hasChess(from, ChessTools.getNorthEast(start)).equals("black")  &&
						ChessTools.hasChess(to,ChessTools.getNorthEast(start)).equals("none") )
				{
					from.getBlack( ChessTools.getNorthEast(start)).setVisible(false);
					from.getRed(start).setLocation(ChessTools.getNorthEast( ChessTools.getNorthEast(start)));
					list.add(from.copy());
					start = ChessTools.getNorthEast( ChessTools.getNorthEast(start));
				}
				else if(ChessTools.getSouthWest(start)!=null && ChessTools.getSouthWest( ChessTools.getSouthWest(start))!=null && ChessTools.hasChess(from, ChessTools.getSouthWest(start)).equals("black")  &&
						ChessTools.hasChess(to,ChessTools.getSouthWest(start)).equals("none") )
				{
					from.getBlack( ChessTools.getSouthWest(start)).setVisible(false);
					from.getRed(start).setLocation(ChessTools.getSouthWest( ChessTools.getSouthWest(start)));
					list.add(from.copy());
					start = ChessTools.getSouthWest( ChessTools.getSouthWest(start));
				}
				else if(ChessTools.getSouthEast(start)!=null && ChessTools.getSouthEast( ChessTools.getSouthEast(start))!=null && ChessTools.hasChess(from, ChessTools.getSouthEast(start)).equals("black")  &&
						ChessTools.hasChess(to,ChessTools.getSouthEast(start)).equals("none") )
				{
					from.getBlack( ChessTools.getSouthEast(start)).setVisible(false);
					from.getRed( start).setLocation(ChessTools.getSouthEast( ChessTools.getSouthEast(start)));
					list.add(from.copy());
					start = ChessTools.getSouthEast( ChessTools.getSouthEast(start));
				}
			}

		else
			list.add(to);
		return list;
	}

	/**
	 * 电脑下棋后，刷新棋盘
	 * @param jp
	 */
	public void setBorad(JPanel jp){
		chessBoard = jp;
	}

	/**
	 * 设置难度
	 * 设置搜索深度
	 * @param index
	 */
	public void setLevel(int index){
		if(index==0)
			DEPTH = 2;
		else if(index==1)
			DEPTH = 5;
		else if(index==2)
			DEPTH = 8;
		System.out.println("设置难度等级：" + index);
	}
    /**
     * 检查局面是否结束
     * @param CheckerState
     */
	private void checkOver(CheckerState CheckerState){
		Chess[] red = CheckerState.red;
		Chess[] black = CheckerState.black;
		for(int i=0;i<12;i++){
			if(red[i].isVisible())
				break;  //有剩旗子便跳出循环，结束函数
			if(i==11){  //所有红旗都不可见，即没有红旗了
				JOptionPane.showMessageDialog(null, "You win!");
				return ;
			}
		}
		for(int i=0;i<12;i++){
			if(black[i].isVisible())
				break;
			if(i==11){
				JOptionPane.showMessageDialog(null, "You lose!");
				return ;
			}
		}
	}
}
