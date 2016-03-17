package com.bitlogic.sociallbox.service.utils;

import java.util.HashMap;
import java.util.Map;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.service.exception.ClientException;
import com.bitlogic.sociallbox.service.exception.RestErrorCodes;

public class GeoUtils implements Constants {

	public static Map<String, Double> getCoordinatesFromLocation(String location) {
		String[] locArray = location.split(",");
		if (locArray == null || locArray.length != 2) {
			throw new ClientException(RestErrorCodes.ERR_001,
					ERROR_LOCATION_INVALID_FORMAT);
		}
		Map<String, Double> locationCordinates = new HashMap<String, Double>(2);
		try {
			Double lattitude = Double.parseDouble(locArray[0]);
			Double longitude = Double.parseDouble(locArray[1]);

			locationCordinates.put(LATTITUDE_KEY, lattitude);
			locationCordinates.put(LONGITUDE_KEY, longitude);
		} catch (NumberFormatException exception) {
			throw new ClientException(RestErrorCodes.ERR_001,
					ERROR_LOCATION_INVALID_FORMAT);
		}
		return locationCordinates;
	}

	public static String calculateDistance(Double lattitude1,
			Double longitude1, Double lattitude2, Double longitude2) {
		Double theta = longitude1 - longitude1;
		Double dist = Math.sin(deg2rad(lattitude1))
				* Math.sin(deg2rad(lattitude2)) + Math.cos(deg2rad(lattitude1))
				* Math.cos(deg2rad(lattitude2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		String distanceString = Integer.toString(dist.intValue())+Constants.KILOMETRES;
		return distanceString;
	}

	

	/* :: This function converts decimal degrees to radians : */
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/* :: This function converts radians to decimal degrees : */
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
	
	public static void main(String[] args) {
		System.out.println(calculateDistance(28.5401956, 77.1786472, 28.5442613885, 77.1782181412));
	}
}
