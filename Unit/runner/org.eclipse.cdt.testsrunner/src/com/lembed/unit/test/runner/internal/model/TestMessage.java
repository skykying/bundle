/*******************************************************************************
 * Copyright (c) 2011, 2012 Anton Gorenkov 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Anton Gorenkov - initial API and implementation
 *******************************************************************************/
package com.lembed.unit.test.runner.internal.model;

import com.lembed.unit.test.runner.model.IModelVisitor;
import com.lembed.unit.test.runner.model.ITestMessage;

/**
 * Represents the message that was produced during the testing process.
 */
public class TestMessage implements ITestMessage {

	/** Test message location. */
	private TestLocation location;

	/** Test message level */
	private Level level;

	/** Test message text */
	private String text;

	
	public TestMessage(TestLocation location, Level level, String text) {
		this.location = location;
		this.level = level;
		this.text = text;
	}

	@Override
	public TestLocation getLocation() {
		return location;
	}

	@Override
	public Level getLevel() {
		return level;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void visit(IModelVisitor visitor) {
		visitor.visit(this);
		visitor.leave(this);
	}
}
