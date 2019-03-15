package _02_Pixel_Art;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;

public class PixelArtMaker implements MouseListener, ActionListener {
	private JFrame window;
	private GridInputPanel gip;
	private GridPanel gp;
	ColorSelectionPanel csp;
	
	boolean loaded = false;
	private static final String DATA_FILE = "src/_02_Pixel_Art/saved.dat";
	
	JButton saveButton = new JButton();
	JButton deleteFile = new JButton();
	
	public void start() {
		
		window = new JFrame("Pixel Art");
		window.setLayout(new FlowLayout());
		window.setResizable(false);
		
		try {
			
			FileReader fr = new FileReader(DATA_FILE);
			fr.close();
			
			loaded = true;
			submitGridData(0, 0, 0, 0);
			
		} catch (FileNotFoundException e) {

			gip = new GridInputPanel(this);	
			window.add(gip);
			window.pack();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	public void submitGridData(int w, int h, int r, int c) {
		if (!loaded) {
			window.remove(gip);
			gp = new GridPanel(w, h, r, c);
		} else {
			gp = load();
		}
		
		csp = new ColorSelectionPanel();
		window.add(gp);
		window.add(csp);
		
		saveButton.setText("save");
		saveButton.addActionListener(this);
		deleteFile.setText("delete forever");
		deleteFile.addActionListener(this);
		
		window.add(saveButton);
		window.add(deleteFile);
		gp.repaint();
		gp.addMouseListener(this);
		window.pack();
	}
	
	private static void save(GridPanel data) {
		try (FileOutputStream fos = new FileOutputStream(new File(DATA_FILE)); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static GridPanel load() {
		try (FileInputStream fis = new FileInputStream(new File(DATA_FILE)); ObjectInputStream ois = new ObjectInputStream(fis)) {
			return (GridPanel) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			// This can occur if the object we read from the file is not
			// an instance of any recognized class
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		new PixelArtMaker().start();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		gp.setColor(csp.getSelectedColor());
	//	System.out.println(csp.getSelectedColor());
		gp.clickPixel(e.getX(), e.getY());
		gp.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveButton) {
			save(gp);
		} else if (e.getSource() == deleteFile) {
			File thing = new File(DATA_FILE);
			thing.delete();
		}
	}
}
