package fr.polytech.projet.silkchess.ui.window.components;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DraggableJLabel extends JLabel {
	
	public DraggableJLabel() {
		super();
		init();
	}
	public DraggableJLabel(String text) {
		super(text);
		init();
	}
	public DraggableJLabel(Icon image) {
		super(image);
		init();
	}
	
	private void init() {
		this.setTransferHandler(new TransferHandler("text"));
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				
				JComponent comp = (JComponent) e.getSource();
				TransferHandler th = comp.getTransferHandler();
				
				th.exportAsDrag(comp, e, TransferHandler.MOVE);
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				super.mouseDragged(e);
			}
		});
	}
}
