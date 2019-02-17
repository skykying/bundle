package com.lembed.unit.test.ext.code;

public class UTestCode {
	private String code;
	public UTestCode(){
		code = "";
	}
	public UTestCode(String str){
		code = str;
	}
	public UTestCode append(UTestCode emptyStub) {
		code += emptyStub.toString();
		return this;
	}
	public void append(String string) {
		code += string;
	}
	@Override
	public String toString() {
		return code;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		UTestCode other = (UTestCode) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	public boolean isEmpty() {
		return code.isEmpty();
	}
}
