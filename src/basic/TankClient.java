package basic;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class TankClient extends Frame{

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.lauchFrame("TankWar");
	}
	
	public void lauchFrame(String name){
		setTitle(name);
		setLocation(50, 50);
		setSize(800, 600);
		setResizable(false);
		setVisible(true);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
	}

}
