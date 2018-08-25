package com.jp.checkers;

import java.awt.Point;
/**
 * 棋子类
 * 位置，颜色，是否可见，是否为王
 * @author Administrator
 *
 */
public class Chess{

	private boolean selected = false;
	private Point location;
	private boolean visible = true;
	private String color;   //颜色
	private boolean king = false;
	int id;
	
	boolean counted = false;

	public Chess(){
	}
	public Chess(String color,int id){
		this.color = color;
		this.id = id;
	}
	public void setLocation(Point p){
		setLocation(p.x,p.y);
	}
	public void setLocation(int x, int y){
		location = new Point(x,y);
		if(color.equals("red") && y/60==7){
			king = true;
		}
		if(color.equals("black") && y/60==0){
			king = true;
		}
	}
	public Point getLocation(){
		return location;
	}
	public void setVisible(boolean b){
		visible = b;
	}
	public boolean isVisible(){
		return visible;
	}
	public void setKing(boolean b){
		king = b;
	}
	public boolean isKing(){
		return king;
	}
	public void setSelected(boolean b){
		selected = b;
	}
	public boolean isSelected(){
		return selected;
	}
	public String getColor(){
		return color;
	}
	public void setColor(String color){
		this.color = color;
	}
	public boolean equals(Object o){
		if(!(o instanceof Chess))
			return false;
		Chess ch = (Chess)o;
		if(this.getColor().equals(ch.getColor()) && this.id==ch.id)
			return true;
		return false;
	}

	public String toString(){
		Point p = this.getLocation();
		if(this.isVisible())
			return "["+(p.x / 60+1) +" "+ (p.y / 60+1)+"]";
		return "dead";
	}
}
