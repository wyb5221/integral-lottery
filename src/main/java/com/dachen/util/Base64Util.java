package com.dachen.util;

import java.io.IOException;


public class Base64Util {
	
	public static byte[] base64ToImgByteArray(String base64) {
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        //因为参数base64来源不一样，需要将附件数据替换清空掉。如果此入参来自canvas.toDataURL("image/png");
        base64 = base64.replaceAll("data:image/png;base64,", "");
        //base64解码并转为二进制数组
        byte[] bytes = null;
		try {
			 bytes = decoder.decodeBuffer(base64);
			 for (int i = 0; i < bytes.length; ++i) {  
		            if (bytes[i] < 0) {// 调整异常数据  
		                bytes[i] += 256;  
		            }
		        }
		} catch (IOException e) {
			e.printStackTrace();
		}
       
        return bytes;
    }
}
