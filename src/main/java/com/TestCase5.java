package com;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.HashBiMap;

public class TestCase5 {

	
	public static void main(String[] args) {
		HashBiMap<String, Integer> map1 = HashBiMap.create();
		map1.put("1", 2);
		
		System.out.println(map1.get("1"));
		
		System.out.println(map1.inverse().get(2));
		
//		map1.containsValue(value)
//		map1.
		
		Map<Class<?>,?> map = new HashMap<>();
//		map.put(String.class, "123456");
//		map.put(String.class, "");
		
	}
}
