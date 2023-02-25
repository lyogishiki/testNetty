package com.test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Test {
	
	public static void main(String[] args) {
		double d = Math.toRadians(90);
		System.out.println(d);
		System.out.println(Math.sin(30));
		
		double d2 = Math.sin(Math.toRadians(30));
		System.out.println(d2);
		BigDecimal b = new BigDecimal(d2).setScale(8, RoundingMode.HALF_UP);
		System.out.println(b);
	}
	
}
