package controller;

import java.io.*;
import java.util.*;
import org.dom4j.DocumentException;
import org.joda.time.*;
import writers.*;
import model.*;

public class TivooController {

	private TivooModel myModel;

	public TivooController() {
		myModel = new TivooModel();
	}

	public void initialize() {
		String input = "dukecal.xml", outputdir = "output";
		// DateTime startdate =
		// TivooTimeHandler.createTimeUTC("20110601T000000Z");
		// DateTime enddate = startdate.plusDays(180);
		doRead(new File(input));
		// doFilterByTime(startdate, enddate);
		// doWrite(new SortedListWriter(), outputdir);
		// doWrite(new DailyCalendarWriter(), outputdir);
		 doWrite(new WeeklyCalendarWriter(), outputdir);
//		doWrite(new MonthlyCalendarWriter(), outputdir);
		// doWrite(new ConflictingEventsWriter(), outputdir);

	}
	
	public void doRead(File input) {
		try {
			System.out.println("reading...");
			myModel.read(input);
			System.out.println("read complete!");
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public void doFilterByTime(DateTime startdate, DateTime enddate) {
		System.out.println("filtering...");
		myModel.filterByTime(startdate, enddate);
		System.out.println("filter complete!");
	}

	public void doFilterByKeywordsCommon(Set<String> keywords, boolean retain) {
		System.out.println("filtering...");
		myModel.filterCommon(keywords, retain);
		System.out.println("filter complete!");
	}

	public void doFilterByKeywordsSpecial(Set<String> keywords,
			TivooEventType eventtype, String attr, boolean retain) {
		myModel.filterSpecial(keywords, eventtype, attr, retain);
	}

	public void doSort(Comparator<TivooEvent> comp, boolean reverse) {
		myModel.sort(comp, reverse);
	}

	public void doWrite(TivooWriter writer, String outputdir) {
		try {
			writer.doWriteSummary(myModel.getFilteredList(), outputdir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doClearFilter() {
		myModel.clearFilter();
	}

	public void doReset() {
		myModel.reset();
	}

}