package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;


import model.TivooTimeUtils;

import org.joda.time.DateTime;


import writers.WeeklyCalendarWriter;

import controller.TivooController;

@SuppressWarnings("serial")
public class TivooViewer extends JPanel {
	public static final Dimension SIZE = new Dimension(800, 600);

	private JEditorPane page;
	private JButton mySelect;
	private JButton myFilterByKeywordContain;
	private JButton myFilterByKeywordNotContain;
	private JButton myFilterByTime;
	private JButton myDisplay;

	private TivooController myController;

	public TivooViewer(TivooController controller) {
		myController = controller;
		setLayout(new BorderLayout());
		add(function(), BorderLayout.NORTH);
		add(display(), BorderLayout.CENTER);
		mySelect.setEnabled(true);
		myDisplay.setEnabled(true);
		myFilterByKeywordContain.setEnabled(true);
		myFilterByKeywordNotContain.setEnabled(true);
		myFilterByTime.setEnabled(true);
	}

	private JComponent function() {
		JPanel panel = new JPanel();
		mySelect = new JButton("Load Files");
		mySelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					myController.doRead(file);
				}
			}

		});
		panel.add(mySelect);

		myFilterByKeywordContain = new JButton("Filter By Keyword (contain)");
		myFilterByKeywordContain.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				HashSet<String> set = getKeywords();
				myController.doFilterByKeywordsCommon(set, false);
			}

		});
		panel.add(myFilterByKeywordContain);
		
		myFilterByKeywordNotContain = new JButton("Filter By Keyword (not contain)");
		myFilterByKeywordNotContain.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				HashSet<String> set = getKeywords();
				myController.doFilterByKeywordsCommon(set, true);
			}

		});
		panel.add(myFilterByKeywordNotContain);
		
		myFilterByTime = new JButton("Filter By Time");
		myFilterByTime.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String start = JOptionPane.showInputDialog("Please input start time (YYYYMMDD)");
				String end = JOptionPane.showInputDialog("Please input end time (YYYYMMDD)");
				DateTime startdate = TivooTimeUtils.createTimeUTC(start + "T000000Z");
				DateTime enddate = TivooTimeUtils.createTimeUTC(end + "T000000Z");
				myController.doFilterByTime(startdate, enddate);
			}

		});
		panel.add(myFilterByTime);

		myDisplay = new JButton("Preview Webpage");
		myDisplay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String outputdir = "output";
				myController.doWrite(new WeeklyCalendarWriter(), outputdir);
				showURL();
			}

		});
		panel.add(myDisplay);
		return panel;
	}

	public void showURL() {
		try {
			File file = new File("output/summary.html");
			page.setPage("file://" + file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public HashSet<String> getKeywords() {
		String inputValue = JOptionPane.showInputDialog("Please input keywords (split by spaces)");
		String[] inputs = inputValue.split("\\s+");
		HashSet<String> set = new HashSet<String>();
		for (String s : inputs)
			set.add(s);
		return set;
	}

	private JComponent display() {

		// displays the web page
		page = new JEditorPane();
		page.setPreferredSize(SIZE);
		page.addHyperlinkListener(new LinkFollower());
		page.setEditable(false);
		return new JScrollPane(page);
	}

	private class LinkFollower implements HyperlinkListener {
		public void hyperlinkUpdate(HyperlinkEvent evt) {
			if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				// user clicked a link, load it and show it
				try {
					page.setPage((evt.getURL().toString()));
				} catch (Exception e) {
					String s = evt.getURL().toString();
					JOptionPane.showMessageDialog(TivooViewer.this,
							"loading problem for " + s + " " + e,
							"Load Problem", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}

