package com;

import java.io.IOError;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multisets;
import com.google.common.collect.TreeMultimap;

public class TestCase {

	static void run() throws IllegalArgumentException,
	IOException{
		
	}
	
	public static void main(String[] args) {
		/*try {
			run();
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}*/
		List<String> list1 = Arrays.asList("1","2","3","4");
		ImmutableList<String> list2 = ImmutableList.copyOf(list1);
		System.out.println(list1.getClass());
		System.out.println(list2.getClass());
		
//		ImmutableSet.copyOf(elements)
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		
//		map.
		
		list1.stream().mapToInt(s -> 1).average();
		
//		map.getOrDefault("1", 0);
		
		HashMultimap<String, Integer> map2 = HashMultimap.create();
		map2.put("a", 3);
		map2.put("a", 1);
		map2.put("a", 2);
		map2.put("a", 17);
		map2.put("a", 9);
		
		map2.put("b", 5);
		map2.put("b", 4);
		
		map2.put("c", 6);
		
		System.out.println(map2);
		System.out.println(map2.get("a").getClass());
		
		TreeMultimap<String, Integer> map3 = TreeMultimap.create();
		map3.put("a", 3);
		map3.put("a", 1);
		map3.put("a", 2);
		map3.put("a", 17);
		map3.put("a", 9);
		
		map3.put("b", 5);
		map3.put("b", 4);
		
		map3.put("c", 6);
		
		System.out.println(map3);
		System.out.println(map3.get("a").getClass());
		
		System.out.println(map2.asMap().getClass());
		System.out.println(map3.asMap().getClass());
		
		System.out.println(hash(1));
		System.out.println(hash(2));
		System.out.println(hash(3));
		System.out.println(hash(17));
		System.out.println(hash(9));
		
		System.out.println(hash(1) & 15);
		System.out.println(hash(2) & 15);
		System.out.println(hash(3) & 15);
		System.out.println(hash(17) & 15);
		System.out.println(hash(9) & 15);
		
//		HashBasedTable<String, String, Integer> table = HashBasedTable.create();
		
		
	}
	
	 static final int hash(Object key) {
	        int h;
	        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
	    }
	
}
