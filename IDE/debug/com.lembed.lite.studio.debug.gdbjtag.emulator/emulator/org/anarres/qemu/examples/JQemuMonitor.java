/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.qemu.examples;

/**
 *
 * @author shevek
 */
public class JQemuMonitor {

    public static void main(String[] args) throws Exception {
    	
    	QEmuExample example = new QEmuRAIDExample();
    	if(example != null) {
    		example.invoke(args);
    	}
    }
}
