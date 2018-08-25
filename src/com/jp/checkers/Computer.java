package com.jp.checkers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * ����AI������
 * alpha beta��֦�ļ���Сֵ�㷨������״̬������
 * һֱ�ڼ����Ƿ��ֵ���������
 * @author Administrator
 *
 */
public class Computer extends Thread{
	
	private static int DEPTH = 2; //�������.Ĭ���Ǽ򵥵�ˮƽ
	private static final int MAX = 99999;
	private static final int MIN = -99999;
	private CheckerState CheckerState;
	private static ArrayList NextCheckerStates; //��ǰ״̬���п��ܵ��߷�
	private JPanel chessBoard;
	private ChessTools util = new ChessTools();
	public static boolean eaten = false;
	
	public Computer(){
		start(); //�����߳�
	}
	/**
	 * ��������״̬
	 * @param CheckerState
	 */
	public void setCheckerState(CheckerState CheckerState){
		this.CheckerState = CheckerState;
	}

	public void run(){
		while(true){
			synchronized(this){ //������
				try{
//					��ʱ���������壬�ȴ�
					wait();
				}
				catch(InterruptedException ie){
					throw new RuntimeException(ie);
				}	
			}
			//�����ͷţ��ֵ���������
			try{
				Thread.sleep(200); //ͣ��һ�£�������̫��
			}
			catch(Exception e){
				e.printStackTrace();
			}
			alphaBetaSearch(CheckerState); //��ʼ����
		}
	}
	/**
	 * alpha beta��֦�ļ���Сֵ�㷨����
	 * @param CheckerState
	 */
	private void alphaBetaSearch(CheckerState CheckerState){
		
		CheckerState.depth = 0;  //��ʼ���Ϊ0
		NextCheckerStates = null;
		
		int value = maxValue(CheckerState,MIN,MAX); //�������壬��һ��Ϊmax��
		
		if(NextCheckerStates==null){ //���������·����
			JOptionPane.showMessageDialog(null, "You win!");
			ChessTools.win = true;  //�ҷ�Ӯ
			return ;
		}

//		��NextCheckerStates����Ѱ����ѵ��߷�,ͨ���Ƚ�Ȩֵ��Ѱ�����Ȩֵ
		CheckerState s = null;
		CheckerState nextCheckerState = null;
		for(Iterator it = NextCheckerStates.iterator();it.hasNext();){
			s = (CheckerState)it.next();
			if(s.value==value){
				nextCheckerState = s;
				break;
			}
		}

//		����û�����ˣ�������жϻ����б�Ҫ�ģ���ȻNextCheckerStates.iterator()һ������쳣
		if(nextCheckerState==null){
			JOptionPane.showMessageDialog(null, "You win!");
			ChessTools.win = true;
			return ;
		}

//		Ѱ�ҵ�ǰ״̬����һ״̬֮���·������Ϊ�����������ӵ���������Ի����е��鷳�ģ�
//		findpath����������ص���һ�����飬���������е��м�״̬������״̬
		ArrayList steps = findPath(new CheckerState(nextCheckerState.OldRed,nextCheckerState.OldBlack),nextCheckerState);
		for(Iterator it = steps.iterator();it.hasNext();){
			CheckerState st = (CheckerState)it.next();
			ChessBoard.red = st.red;
			ChessBoard.black = st.black;
			chessBoard.repaint();
			checkOver(st); //����Ƿ������
			try{
				Thread.sleep(300); //����֮����һ��
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		ChessBoard.myTurn = true;

	}
	/**
	 * max�㣬ȡ���ֵ
	 * @param CheckerState
	 * @param a ����
	 * @param b ����
	 * @return
	 */
	private int maxValue(CheckerState CheckerState,int a,int b){

//		���������ײ㣬�������ۺ����õ���Ȩֵ
		if(CheckerState.depth==DEPTH) 
			return CheckerState.getValue();

//		�Ȱ�CheckerState��value��Ϊ��С����Ϊ����Ҫ��һ�����ֵ	
		CheckerState.value = MIN;
//		ȡ���ֵʱ��Ӧ���ӵ�״̬
		ArrayList list = CheckerState.nextStatesOfRed(); //�췽��һ�������ߵ��������

//		�����һ���״̬�������ʱ����
		if(CheckerState.depth==0)
			NextCheckerStates = list; 

		CheckerState s = null;
		int minValue = MIN;
		for(Iterator it = list.iterator();it.hasNext();){
			s = (CheckerState)it.next();
			s.depth = CheckerState.depth + 1; //��ȼ�һ

			int temp = minValue(s,a,b);//��һ��Ϊmin��
			if(minValue < temp)
				minValue = temp;

//			minValue����һ��״̬����С�ģ�����CheckerState��value��Ƚϣ�ȡ���
			CheckerState.value = CheckerState.value>minValue ? CheckerState.value : minValue;

//			CheckerState��value��betaֵ�󣬴�ʱû��Ҫ������ȥ�����Լ�֦��  
			if(CheckerState.value>=b)
				return CheckerState.value;

//			���CheckerState��value��alphaֵ�󣬸���alphaֵ
			a = a>CheckerState.value ? a : CheckerState.value;
		}
		return CheckerState.value;
	}
	/**
	 * min�㣬ȡ��Сֵ
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
				maxValue = temp;   //��һ�����
			CheckerState.value = CheckerState.value<maxValue ? CheckerState.value : maxValue;
			if(CheckerState.value<=a)
				return CheckerState.value;
			b = b<CheckerState.value ? b : CheckerState.value;
		}
		return CheckerState.value;
	}
	/**
	 * Ѱ������״̬֮���·��
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
				movedRed = oriRed[i];   //�ƶ����ĺ���
			if(oriBlack[i].isVisible()&& !currentBlack[i].isVisible())
				eaten = true;			//���Եĺ���
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
	 * ���������ˢ������
	 * @param jp
	 */
	public void setBorad(JPanel jp){
		chessBoard = jp;
	}

	/**
	 * �����Ѷ�
	 * �����������
	 * @param index
	 */
	public void setLevel(int index){
		if(index==0)
			DEPTH = 2;
		else if(index==1)
			DEPTH = 5;
		else if(index==2)
			DEPTH = 8;
		System.out.println("�����Ѷȵȼ���" + index);
	}
    /**
     * �������Ƿ����
     * @param CheckerState
     */
	private void checkOver(CheckerState CheckerState){
		Chess[] red = CheckerState.red;
		Chess[] black = CheckerState.black;
		for(int i=0;i<12;i++){
			if(red[i].isVisible())
				break;  //��ʣ���ӱ�����ѭ������������
			if(i==11){  //���к��춼���ɼ�����û�к�����
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
