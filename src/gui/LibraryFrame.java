package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import player.Player;
import songstreams.SongStream;

import library.Library;

public class LibraryFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7178375824315492885L;
	
	private SongTable libraryTable;
	
	public LibraryFrame(Library lib, final Player player, Properties settings, ResourceBundle bundle) throws Exception {
		this.libraryTable = new SongTable(new String[]{"","Artist","Title","Album","Track#","Year","URL"}, settings);
		List<SongStream> songs = lib.getAllSongs();
		for(SongStream song : songs)
			libraryTable.addRow(song);
		JScrollPane sp = new JScrollPane(libraryTable);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(sp);
		Box buttons = Box.createHorizontalBox();
		final JButton play = new JButton("Play");
		final JButton pause = new JButton("Pause");
		final JButton stop = new JButton("Stop");
		final JButton back = new JButton("Back");
		final JButton skip = new JButton("Skip");
		final JButton queue = new JButton("Queue");
		ActionListener playListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource().equals(play))
					player.play();
				if(e.getSource().equals(pause))
					player.pause();
				if(e.getSource().equals(stop))
					player.stop();
				if(e.getSource().equals(skip))
					player.skip();
				if(e.getSource().equals(back))
					player.back();
				if(e.getSource().equals(queue))
					try {
						player.queue(libraryTable.getSelectedSongs());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
			}
		};
		play.addActionListener(playListener);
		pause.addActionListener(playListener);
		stop.addActionListener(playListener);
		queue.addActionListener(playListener);
		skip.addActionListener(playListener);
		back.addActionListener(playListener);
		buttons.add(play);
		buttons.add(pause);
		buttons.add(stop);
		buttons.add(skip);
		buttons.add(back);
		buttons.add(queue);
		panel.add(buttons);
		setContentPane(panel);
		pack();
	}
	
}
