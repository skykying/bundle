/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lembed.lite.studio.qemu.view;


public class JQemuMonitor {

    public static void main(String[] args) throws Exception {
    	
    	AbstractQEmu example = new QEmuRAID();
    	if(example != null) {
    		example.invoke(args);
    	}
    }
}
