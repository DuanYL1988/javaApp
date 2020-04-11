package com.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
	
	public long getUseTime (Date start,Date end){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		long starttime = calendar.getTimeInMillis();
		calendar.setTime(end);
		long endtime = calendar.getTimeInMillis();
		return endtime-starttime;
	}  
	
	public void getMs(Date start,Date end){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
		System.out.println("START:"+formatter.format(start));
		System.out.println("END:"+formatter.format(end));
	}
}
