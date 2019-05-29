package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiEnumDescriptor{name=DisplayType, data=[default, none, gtk, sdl, egl-headless, curses, cocoa], fields=null}</pre>
 */
// QApiEnumDescriptor{name=DisplayType, data=[default, none, gtk, sdl, egl-headless, curses, cocoa], fields=null}
public enum DisplayType {
	_default("default"),
	none("none"),
	gtk("gtk"),
	sdl("sdl"),
	egl_headless("egl-headless"),
	curses("curses"),
	cocoa("cocoa"),
	__UNKNOWN("<unknown>");

	private final java.lang.String jsonValue;

	/* pp */ DisplayType(@Nonnull java.lang.String jsonValue) {
		this.jsonValue = jsonValue;
	}

	@JsonValue
	public java.lang.String getJsonValue() {
		return jsonValue;
	}
}
