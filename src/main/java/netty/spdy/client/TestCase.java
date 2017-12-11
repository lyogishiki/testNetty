package netty.spdy.client;

import java.net.URI;
import java.net.URISyntaxException;

import netty.Consts;

public class TestCase {
	

	public static void main(String[] args) throws URISyntaxException {
		
		URI uri = new URI(Consts.URL);
		System.out.println(uri.getRawPath());
	}
}
