/*******************************************************************************
 * Copyright (c) 2017 LEMBED 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.lembed.lite.studio.device.core.events;

public class LitePairEvent {
	private Object first;
	private Object second;
	
	public LitePairEvent(Object first, Object second){
		this.first = first;
		this.second = second;
	}
	
	public Object getFirst() {
		return first;
	}
	
	public Object getSecond() {
		return second;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LitePairEvent other = (LitePairEvent) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LitePairEvent [first=" + first.toString() + ", second=" + second.toString() + "]";
	}
	
	
}
