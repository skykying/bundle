package com.lembed.lite.studio.device.project.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;


	public class BufferPrinter extends StringWriter{
		

		
		public void println(String line) throws IOException{
			BufferedWriter	bw = new BufferedWriter(this);
			bw.write(line);
			bw.newLine();
			bw.flush();
		}
		
		public void print(String line) throws IOException{
			println(line);
		}
		
		public void print() throws IOException{
			BufferedWriter	bw = new BufferedWriter(this);
			bw.newLine();
			bw.flush();
		}
		
		public void println() throws IOException{
			BufferedWriter	bw = new BufferedWriter(this);
			bw.newLine();
			bw.flush();
		}
		
	}

