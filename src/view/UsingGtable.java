package view;

import java.awt.Button;
import java.awt.Frame;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class UsingGtable extends Frame {


	private static final long serialVersionUID = 1L;
	ArrayList<ArrayList<String>> data= new ArrayList<ArrayList<String>>(); 
	ArrayList<String> headerdata= new ArrayList<String>(); 

	/**
	 * 
	 */
	public UsingGtable(){
		super("test");
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.exit(0);
			} 
		});
		Button b= new Button("Close");
		b.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);

			}

		});
		add(b,"South");
		ArrayList<String> cell1 = new ArrayList<String>();//(ArrayList<String>) Arrays.asList(new String[]{"column1 ", "column2"});
		ArrayList<String> cell = new ArrayList<String>();//(ArrayList<String>) Arrays.asList(new String[]{"column11 ", "column22"});
		cell1.add("column11");
		cell1.add("column22");

		cell.add("column1");
		cell.add("column2");

		headerdata.add("        col1           ");
		headerdata.add("       col2        ");
		data.add(cell);
		data.add(cell1);

		ScrollPane sc= new ScrollPane();
		 

		GTable gt= new GTable( );

		try {
			gt.createGTable(headerdata, data);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		gt.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				System.out.println	("Action of Gtable");

			}

		});
		sc.add(gt);


		add(sc,"North");
		pack();
	}

	public static void main(String[] args) {
		new UsingGtable().setVisible(true);

	}

}
