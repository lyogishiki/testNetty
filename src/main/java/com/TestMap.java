package com;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

public class TestMap {
	
	public static void main(String[] args) {
		
		Map<String, Integer> left = ImmutableMap.of("a", 1, "b", 2, "d", 3);
		
		Map<String, Integer> right = ImmutableMap.of("a", 1, "b", 2, "c", 3);
		
		MapDifference<String, Integer> diff = Maps.difference(left, right);
		
		 
		
		System.out.println(diff.entriesInCommon()); // {"b" => 2}
		System.out.println(diff.entriesInCommon()); // {"b" => 2}
		System.out.println(diff.entriesOnlyOnLeft()); // {"a" => 1}
		System.out.println(diff.entriesOnlyOnRight()); // {"d" => 5}
	}

}
