package com.lembed.lite.studio.manager.analysis.core;

import com.lembed.lite.studio.manager.analysis.core.datetime.datetimeBaseVisitor;
import com.lembed.lite.studio.manager.analysis.core.datetime.datetimeParser.MonthContext;

public class StringVisitor extends datetimeBaseVisitor<String> {

	
	

	@Override
	public String visitMonth(MonthContext ctx) {
	
		log(ctx.getStart().getLine()+"StringVisitor");
		log(ctx.getText());
		log(ctx.toString());
		
		ctx.start.getLine();
		
		return super.visitMonth(ctx);
	}
	
	private void log(String msg){
		System.out.println(msg);
	}
	
}
