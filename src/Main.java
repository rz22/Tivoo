import javax.swing.JFrame;

import view.TivooViewer;
import controller.*;

public class Main {

//	public static void main(String[] args) {
//		TivooController controller = new TivooController();
//		controller.initialize();
//	}
	
	public static void main(String[] args) {
		TivooController controller = new TivooController();
		TivooViewer display = new TivooViewer(controller);
		JFrame frame = new JFrame("Tivoo");
		frame.getContentPane().add(display);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}