package com;

import java.util.Spliterators;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class TestCase6 {
	
	public static final Splitter COMMA_SPLITTER = Splitter.on(',')
			.trimResults()
			.omitEmptyStrings();

	public static final Splitter BLANK_SPLITTER = Splitter.onPattern("\\s")
			.trimResults().omitEmptyStrings();
	
	public static void main(String[] args) {
		String[] strs = ",a,,b,".split(",");
		
		System.out.println(Lists.newArrayList(strs));
		
		Iterable<String> it = COMMA_SPLITTER
				.split("foo,bar,,   qux,");
		
		it.forEach((e) -> System.out.println(e));
		
		System.out.println(it);
		
		System.out.println(BLANK_SPLITTER.split("foo bar   qux "));
		
		
		String str = "strs";
		
		str.getBytes(Charsets.UTF_8);
	}
	
}
