package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TestCase {

	public static void main(String[] args) throws UnknownHostException, IOException {
		String str = "VkwAAAABAAAAADc4QTBCQzA1LTFGNUMtNDMxQi04RUJDLTlCNjYwOUFGQ0EzMwAA"
				+ "ADUAAAABAAAABUtFWUlEAAAAJDg3N0FCNDBELTVFQ0MtNEFCQi1CNEQ4LTRBMTY5"
				+ "ODY3NTVFOQAAAQBHnQF4jdAgtPAEXoCCiTnG+AFElKi0lfbKVNGTW4f+PIWgffeg"
				+ "6CkziM6z38j0d12OeEfdwQKJJKdXX/QxOfL6lzlTnz2oweIGxX5LaAyrqiDStOK/"
				+ "X3F8znQuflaP3ftRNYa3huTNwz1BDZfrKDilM+LJExwxKgGdtCF1M+CSoinKquvW"
				+ "EDGii7OjClvzlRcC4BBeigyfcKybIprh8kuQRpdsmvdEiA7gqjPh3hwlKqJMSqWo"
				+ "yVdRxEA1lq4z0hh6yRGE6N1+sACxL9Luecim9RPhODMXMYQ3PXxpuegTrhHDwLBo"
				+ "/P7KuknvTN8Hdsjm3XokUFspLZz9mYI2+4eBAAAAgIJ+waBKRnX7PisBRS6O8RTI"
				+ "C0jfHOxbDMd86dOudRevquZNmYGPF7KnLGr63KIEA9sMuTx8MSbua5KiTnImlYN7"
				+ "D65kqq2wWVzuD3kZ6G3mZ2SS1YvkCbAjWZGbMi0SwV2GrnIialxMS9XAFPd3y0lF"
				+ "Bic0aGKM8zbsBnUsDzBx";
		
		byte[] data = Base64.getDecoder().decode(str);
		String str1 = new String(data,StandardCharsets.UTF_8);
		System.out.println(str1);
	}
	
}
