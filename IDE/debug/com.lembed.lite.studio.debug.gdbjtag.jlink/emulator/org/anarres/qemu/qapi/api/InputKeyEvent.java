package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.qemu.qapi.common.*;

/**
 * Autogenerated class.
 *
 * <pre>QApiStructDescriptor{name=InputKeyEvent, data={key=KeyValue, down=bool}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=InputKeyEvent, data={key=KeyValue, down=bool}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InputKeyEvent extends QApiType {

	
	@JsonProperty("key")
	@Nonnull
	public KeyValue key;
	
	@JsonProperty("down")
	@Nonnull
	public boolean down;

	@Nonnull
	public InputKeyEvent withKey(KeyValue value) {
		this.key = value;
		return this;
	}

	@Nonnull
	public InputKeyEvent withDown(boolean value) {
		this.down = value;
		return this;
	}

	public InputKeyEvent() {
	}

	public InputKeyEvent(KeyValue key, boolean down) {
		this.key = key;
		this.down = down;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("key");
		names.add("down");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("key".equals(name))
			return key;
		if ("down".equals(name))
			return down;
		return super.getFieldByName(name);
	}
}
