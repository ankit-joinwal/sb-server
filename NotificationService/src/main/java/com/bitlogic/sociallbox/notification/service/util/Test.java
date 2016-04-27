package com.bitlogic.sociallbox.notification.service.util;

import java.util.Calendar;

public class Test {

	public static void main(String[] args) throws Exception{

		
	}

	public static String getDate(Calendar cal){
		cal.set(Calendar.DAY_OF_MONTH, 1);
        return "" + cal.get(Calendar.DAY_OF_MONTH) +"/" +
                (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR);
    }

}
