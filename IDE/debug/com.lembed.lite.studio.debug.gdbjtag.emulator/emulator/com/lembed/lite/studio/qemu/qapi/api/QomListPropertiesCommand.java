package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=qom-list-properties, returns=[ObjectPropertyInfo], data={typename=str}}</pre>
 */
// QApiCommandDescriptor{name=qom-list-properties, returns=[ObjectPropertyInfo], data={typename=str}}
public class QomListPropertiesCommand extends QApiCommand<QomListPropertiesCommand.Arguments, QomListPropertiesCommand.Response> {
	/** Compound arguments to a QomListPropertiesCommand. */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Arguments {

		
		@JsonProperty("typename")
		@Nonnull
		public java.lang.String typename;

		public Arguments() {
		}

		public Arguments(java.lang.String typename) {
			this.typename = typename;
		}
	}

	/** Response to a QomListPropertiesCommand. */
	public static class Response extends QApiResponse<java.util.List<ObjectPropertyInfo>> {
	}

	/** Constructs a new QomListPropertiesCommand. */
	public QomListPropertiesCommand(@Nonnull QomListPropertiesCommand.Arguments argument) {
		super("qom-list-properties", Response.class, argument);
	}

	/** Constructs a new QomListPropertiesCommand. */
	public QomListPropertiesCommand(java.lang.String typename) {
		this(new Arguments(typename));
	}
}
