package netty2.udp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	
	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("(\\S+ \\S+) (\\S+) (\\S+) (\\S+) (.+)");
		String str = "2006-03-12 20:12:23 gddev001 FID 0 4 75%";
		Matcher matcher = pattern.matcher(str);
		System.out.println(matcher.matches());
		System.out.println(matcher.matches());
		matcher.reset();
		if(matcher.find()) {
//			System.out.println(matcher.find());
			System.out.println(matcher.group(1));
			System.out.println(matcher.group(2));
			System.out.println(matcher.group(3));
			System.out.println(matcher.group(4));
			System.out.println(matcher.group(5));
		}
	}
	
}
