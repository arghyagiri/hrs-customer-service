package com.tcs.training.customer.util;

import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class Constants {

	public static Map<String, String> HOTEL_TYPE_MAP = Map.of("SINGLE", "1 Single Bed", "SINGLEX", "2 Single Beds",
			"DOUBLE", "1 Double Bed", "DOUBLEX", "2 Double Beds", "DULUX", "2 Queen Beds");

}
