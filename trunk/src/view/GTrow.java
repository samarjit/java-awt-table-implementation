package view;

import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Formatter;

public class GTrow{
	private int height;
	private int numcells;
	private ArrayList<GTcell> cells;
	private GTrow headingRow;
	//private int wordwidth=0; //contains number characters in that row
	private int currentCellId=0; //denotes column number
	private int x;
	private int y;
	private int curX;
	private int curY;
	private Panel component;
	private GTable parentElement;
	private int maxNLines;
	private boolean isHedingRow;
	private boolean selectedRow;
	
	public int getHeight() {
		return height;
	}

	public GTable getParentElement() {
		return parentElement;
	}

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
	public boolean isHeadingRow(){
			return isHedingRow;
	}
	 
	public ArrayList<GTcell> getCells() {
		return cells;
	}
	public GTrow(GTable gt,Panel component){
		parentElement = gt;
		y = this.curY = gt.getCurY();
		x = this.curX = gt.getX();
		this.component = component;
		cells=new ArrayList<GTcell>();
		isHedingRow = true;
	}
	public GTrow(GTable gt,GTrow headingRow, Panel component){
		parentElement = gt;
		y = this.curY = gt.getCurY();
		x = this.curX = gt.getX();
		cells=new ArrayList<GTcell>();
		this.headingRow = headingRow;
		this.component = component;
	}
	public void trStart(){
		
	}
	public void actionHappened(String cmd){
		//System.out.println("Action happened now"+cmd);
	}
	public void addHeadingCell(int width, String text) throws Exception{
		GTcell tc=new GTcell(this,curX,curY,width,text);
		this.component.add(tc);
		tc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				actionHappened(e.getActionCommand());
				parentElement.actionOccured(e);
			}});
		cells.add(tc);currentCellId++;curX += width;
	}
	public void addCell( String text ) throws Exception{
		if(headingRow == null){throw new Exception("Bad Table heading reference");}
		GTcell tc=new GTcell(this,curX,curY,headingRow.cells.get(currentCellId).getWidth(),text);
		tc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				actionHappened(e.getActionCommand());
				parentElement.actionOccured(e);
			}});
		cells.add(tc);
		curX += headingRow.cells.get(currentCellId).getWidth();
		currentCellId++;
		this.component.add(tc);
		
	}
	public void trowResize(int rowsize, ArrayList<GTrow> extraCellTrow) throws Exception {
		numcells = rowsize;
		
		int ncells = cells.size();
		while(rowsize > ncells){
			cells.add(new GTcell(this,x,y, extraCellTrow.get(0).cells.get(ncells).getWidth()," "));ncells++;
		}
	}
	public void trEnd(){
		resizeCells();
		//System.out.println("trEnd()"+height);
		getParentElement().setCurY(height + getParentElement().getCurY());
	}
	public int getMaxTrHeight(){ //returns max(nlines)
		int maxHeight=cells.get(0).getNlines();
		for(int i=1;i<cells.size();i++){
			if(cells.get(i).getNlines() > maxHeight){
				maxHeight = cells.get(i).getNlines();
			} 
		}
		return maxHeight;
	}
	private void resizeCells(){
		 maxNLines = getMaxTrHeight();   //max(nlines)
		
		for(GTcell c:cells){
			c.cellResize(maxNLines);
		}
	}
	public String toString(){
		StringBuffer tmp = new StringBuffer(50);
		Formatter fmt=new Formatter();
		for(int i=0;i<maxNLines;i++){
			for(GTcell tc:cells ){
				//tmp.append(tc.getCellTexti(i)).append("|");
				fmt.format("|%"+((tc.isMultiline())?"-":"")+tc.getCharwidth()+"s%s (%s,%s)",tc.getCellTexti(i) ,"|",tc.getX(),tc.getY());
			}fmt.format("%s","\r\n");
			//tmp.append("\r\n");
		}tmp.append(fmt.toString());fmt.flush();fmt.close();
		return tmp.toString();
	}
	public String drawSeparator(){
		StringBuffer tmp = new StringBuffer(50);
		Formatter fmt=new Formatter();
		
		for(GTcell tc:cells ){
			//tmp.append(tc.getCellTexti(i)).append("|");
			fmt.format("%s","+");
			for (int i=0;i<tc.getCharwidth();i++){
				fmt.format("%s","-");
			}
			fmt.format("%s","+");
		}fmt.format("%s","\r\n");
		tmp.append(fmt.toString());fmt.flush();fmt.close();
		return tmp.toString();
	}

	public void setHeight(int height) { //this is maintained by Tcell class after trEnd on the basis of max(nlines)
		this.height = height;
	}

	public void setSelectedRow(boolean b) {
		selectedRow = b;
		parentElement.refresh();
		
	}
	public boolean getSelectedRow() {
		return selectedRow;
	}
}