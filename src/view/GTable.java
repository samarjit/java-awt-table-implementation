package view;

/**
* @Author			: 	samrjit
* @Creation Date	: 	14/03/2009
* @Description		:   USS Main Window file
*  
* @          -------------------------------------------------
* @Revision:  Revision Date    Name     Change Description      
* @          -------------------------------------------------
* @           1.1    22-sep-09 Samarjit "Made GTable a truely panel component
* @										added function to create table just by 
* @										gt.createGTable(headerdata, data);"	
* @                      
*/




import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Formatter;



/**
 * Usage:
 * ScrollPane sc= new ScrollPane();
 * GTable gt= new GTable( );
 * gt.createGTable(headerdata, data);
 * gt.addActionListener(new ActionListener(){
  			public void actionPerformed(ActionEvent e) {
				System.out.println	("Action of Gtable");
			}
		});
		sc.add(gt);
 */

public class GTable extends Panel{
	int width; //px
	int height; //px
	ArrayList<GTrow> tr;
	private ArrayList<GTrow> extraCellTrow; //only used to resize
	private int x;
	private int y;
	private int curX;
	private int curY;
	
	private Panel component = this;//this variable should be !deprecated
	private ActionListener actionListener = null;
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getCurX() {
		return curX;
	}

	public void setCurX(int curX) {
		this.curX = curX;
	}

	public int getCurY() {
		return curY;
	}

	public void setCurY(int curY) {
		this.curY = curY;
	}
	public void addActionListener(ActionListener l) {
		    actionListener  = 
		       AWTEventMulticaster.add(actionListener,l);
	}
	public void removeActionListener(ActionListener l) {
		    actionListener =
		       AWTEventMulticaster.remove(actionListener, l);
	}
	public void actionOccured(ActionEvent ae){
		if (actionListener != null) {
	          actionListener.actionPerformed(ae);
	          }
	}
	public GTable(){
		super(null);
		tr=new ArrayList<GTrow>();
		//component = comp; //this assignment is unnecessary as table itself is now a component
		this.x=this.getX();
		this.y=this.getY();
		System.out.println(this.x+ " "+this.y);
	}
	
	@ Deprecated
	public GTable(Panel comp){
		tr=new ArrayList<GTrow>();
		component = comp; //this assignment is unnecessary as table itself is now a component
		this.x=comp.getX();
		this.y=comp.getY();
		System.out.println(this.x+ " "+this.y);
	}
	public GTrow addRow(GTrow headingTr) {
		GTrow newRow=new GTrow(this,headingTr,component); //component commented
		tr.add(newRow);
		return newRow;
	}
	public GTrow addHeadingRow(){
		GTrow newRow=new GTrow(this,component); //component commented
		tr.add(newRow);
		return newRow;
	}
	public int getHeight(){
		int tmpHeight = 0;
		for(GTrow t:tr){
			tmpHeight += t.getHeight();
		}
		height = tmpHeight;
		return height;
	}
	public void tableEnd() throws Exception{
		resizeTrow();
		getHeight();
		for(GTcell t:tr.get(0).getCells())
		width += t.getWidth();
	}
	 
	public int getWidth() {
		return width;
	}

	public int getNumCharacterHeight(){
		int tmpHeight = 0;
		for(GTrow t:tr){
			tmpHeight += t.getMaxTrHeight();
		}
		return tmpHeight;
	}
	
	private int getMaxRowSize(){//returns max number of columns 
		int maxCols=tr.get(0).getCells().size();
		extraCellTrow=new ArrayList<GTrow>();
		extraCellTrow.add(new GTrow(this,component)); //blank row to fit in extra cells only //component commented
		for(int i=1;i<tr.size();i++){
			if(tr.get(i-1).getCells().size() > tr.get(i).getCells().size()){
				maxCols = tr.get(i-1).getCells().size();
				extraCellTrow.set(0,tr.get(i-1));
			}else{
				maxCols=  tr.get(i).getCells().size();
				extraCellTrow.set(0,tr.get(i));
			}
		}
		return maxCols;
	}
	private void resizeTrow() throws Exception{ //introduce extra cells if missing
		int rowsize = getMaxRowSize();
		for(GTrow t:tr){
			t.trowResize(rowsize,extraCellTrow);
			
		}
	}
	public String toString(){
		Formatter fmt=new Formatter();
		StringBuffer stb=new StringBuffer(100);
		for(GTrow trow:tr){
			fmt.format("%s", "");
			stb.append(trow.toString());
		}
		return stb.toString();
	}

	public void refresh() {
		 for(GTrow gtr:tr){
			 if(gtr.getSelectedRow() == true && !gtr.isHeadingRow()){
			 gtr.setSelectedRow(false);	
			 for(GTcell gtc : gtr.getCells()){
						gtc.setBackground(new Color(200,200,200));
						gtc.repaint();
				 }
				}else{
					for(GTcell gtc : gtr.getCells()){
						gtc.setBackground(new Color(255,255,255));
						gtc.repaint();
				 }
					 
				}
				
			
		 }
		
	}
	
	
	
	public void createGTable(  ArrayList<String> heading2, ArrayList<ArrayList<String>> arData1) throws Exception{
		try {  Panel px= this;
				for(Component cmp:px.getComponents())
				{
					 px.remove(cmp);
				}
			//calculate column Length
			ArrayList<Integer> colMdata=new ArrayList<Integer>();
			int i1=0;
		 
			int totalLength=0;
			px.repaint();
			 
			ArrayList<ArrayList<String>> arData2 = new ArrayList<ArrayList<String>>();
			arData2.add(heading2);
			arData2.addAll(arData1);
			//for(ArrayList<String> arLst:arData2)
			//{ 
				for(i1=0;i1<heading2.size();i1++) {
					if(colMdata.size()> i1)
					colMdata.set(i1,heading2.get(i1).length()+colMdata.get(i1));
					else
						colMdata.add(heading2.get(i1).length());
				}
				
			//}
			
			// i1++;
			//calculate column Length from average
			for(int j=0;j<colMdata.size();j++) {
				//colMdata.set(j,((colMdata.get(j)/i1)<maxLen)?(colMdata.get(j)/i1)+7:maxLen);
				totalLength+=colMdata.get(j);
			}
			totalLength+=colMdata.size()+1;//for accounting for '||' at the end of each column
			//calculate column Length
			
			
			 
			FontMetrics fm = getFontMetrics ( new Font ("Monospaced",Font.BOLD,16));
			int charLen = fm.charWidth('A');
			for(int i=0;i<colMdata.size();i++){
				colMdata.set(i, charLen * colMdata.get(i));
			}
				
			
			
			//GTable table = new GTable(px);
			
			GTrow htr =  addHeadingRow();
			 
			for(int i=0;i<colMdata.size();i++){
				htr.addHeadingCell(colMdata.get(i), heading2.get(i));
			}
			htr.trEnd();
			
		 
		 
			GTrow tr2 =null;
			for(int j=1; j<arData2.size();j++){
				tr2 =  addRow(htr);
				for(int i=0;i<arData2.get(j).size();i++){
					tr2.addCell(arData2.get(j).get(i));
					 
				}
				tr2.trEnd();
			}
			
			
		 tableEnd();
				
				
			
			 setSize( getWidth(), getHeight());
			 
			
		} catch (Exception e) {
			 System.out.println("Bad Grid Table creation exception"+e);
			 e.printStackTrace();
		}
	}
 
}


