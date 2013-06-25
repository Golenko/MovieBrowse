package com.app.parsjson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class Resource {
	private final static int PORT = 8080;
	private final static String HOST_NAME = "proxy.softservecom.com";
	
	public Bitmap getImage(String link, Long id, File dir) throws IOException {
		Bitmap bmp = null;
		/*--- this method downloads an Image from the given URL, 
		 *  then decodes and returns a Bitmap object
		 ---*/
		
		File image = new File(dir, id + ".jpg");
		if (image.exists()) {
			 System.out.println(image.getAbsolutePath() + "   find");
			try {
				FileInputStream fis = new FileInputStream(image);
				bmp = BitmapFactory.decodeStream(fis);
				System.out.println(bmp.getHeight());

			} catch (FileNotFoundException e) {
				e.printStackTrace();
		
			}

		} else {
			try {
				URL url = new URL(link);
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(HOST_NAME, PORT));
				URLConnection yc = url.openConnection(proxy);

				yc.setDoInput(true);
				yc.connect();
				InputStream input = yc.getInputStream();
				bmp = BitmapFactory.decodeStream(input);

				FileOutputStream fOut = new FileOutputStream(image);
				bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bmp;
	}
	
	
}
