package netty2.xml;

import com.alibaba.fastjson.JSON;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.LastHttpContent;
import netty.http.pojo.Customer;

public class TestCase {

	public static void main(String[] args) {
//		DefaultFullHttpRequest
//		DefaultHttpContent;
//		LastHttpContent
//		DefaultLastHttpContent
		
		Customer c = new Customer();
		c.setCustomerNumber(111L);
		c.setFirstName("zhang");
		c.setLastName("san");
		
		String str = JSON.toJSONString(c);
		Object obj = JSON.parseObject(str);
		
		System.out.println(obj);
		System.out.println(obj.getClass());
		
	}
}
