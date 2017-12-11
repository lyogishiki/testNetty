package com;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.google.common.collect.Sets;

public class TestCase2 {

	public static void main(String[] args) {
		HashSet<Integer> s = new HashSet<Integer>(32);
		s.add(1);
		s.add(2);
		s.add(3);
		s.add(17);
		s.add(16);
		s.add(18);
		s.add(9);
		System.out.println(((Integer)1).hashCode());
		
		EnumSet<Planet> es = EnumSet.allOf(Planet.class);
		System.out.println(es);
		
//		Sets.intersection(set1, set2)
		
//		FileUtils.copyFile(srcFile, destFile);
	}
}
