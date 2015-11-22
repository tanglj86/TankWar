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
		setLocation(100, 100);
		setSize(400, 400);
		setVisible(true);
		this.addWindowListener(new WindowAdapter() {

			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
				super.windowClosing(e);
			}
			
		});
	}

}
