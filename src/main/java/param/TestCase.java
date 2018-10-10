package param;

import javax.activation.MimetypesFileTypeMap;

import io.netty.handler.codec.rtsp.RtspHeaderNames;

public class TestCase {

	
	public static void main(String[] args) {
		MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
//		System.out.println(mimeTypesMap.getContentType(file.getPath()));
//		MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType("")
		
		System.out.println(mimeTypesMap);
		System.out.println(MimetypesFileTypeMap.getDefaultFileTypeMap());
		System.out.println(MimetypesFileTypeMap.getDefaultFileTypeMap());
		System.out.println(MimetypesFileTypeMap.getDefaultFileTypeMap());
		System.out.println(MimetypesFileTypeMap.getDefaultFileTypeMap());
	
		String type = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType("D:\\Program Files (x86)\\UltraISO\\unins000.exe");
		System.out.println(type);
	}
}
