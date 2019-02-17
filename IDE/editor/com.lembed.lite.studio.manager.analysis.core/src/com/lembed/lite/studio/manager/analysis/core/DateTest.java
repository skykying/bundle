package com.lembed.lite.studio.manager.analysis.core;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.lembed.lite.studio.manager.analysis.core.datetime.datetimeLexer;
import com.lembed.lite.studio.manager.analysis.core.datetime.datetimeParser;

public class DateTest {
	
	public static String tesxxt = "Wed, 02 Oct 82 08:00:00 EST";

	public static String run(String expr) throws Exception {

		// ��ÿһ��������ַ���������һ�� ANTLRStringStream �� in
		ANTLRInputStream in = new ANTLRInputStream(expr);

		// �� in ����ʷ������� lexer���ʷ������������ǲ����Ǻ�
		datetimeLexer lexer = new datetimeLexer(in);

		// �ôʷ������� lexer ����һ���Ǻ��� tokens
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		// ��ʹ�� tokens �����﷨������ parser,�����Ѿ���ɴʷ��������﷨������׼������
		datetimeParser parser = new datetimeParser(tokens);

		// ���յ����﷨�������Ĺ��� prog����ɶԱ��ʽ����֤
		ParseTree tree = parser.day();
		
		StringVisitor vis = new StringVisitor();
		
		return vis.visit(tree);
	}


}