package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=object-add, returns=null, data={qom-type=str, id=str, *props=any}}</pre>
 */
// QApiCommandDescriptor{name=object-add, returns=null, data={qom-type=str, id=str, *props=any}}
public class ObjectAddCommand extends QApiCommand<ObjectAddCommand.Arguments, ObjectAddCommand.Response> {
	/** Compound arguments to a ObjectAddCommand. */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Arguments {

		
		@JsonProperty("qom-type")
		@Nonnull
		public java.lang.String qomType;
		
		@JsonProperty("id")
		@Nonnull
		public java.lang.String id;
		
		@JsonProperty("props")
		@CheckForNull
		public java.lang.Object props;

		public Arguments() {
		}

		public Arguments(java.lang.String qomType, java.lang.String id, java.lang.Object props) {
			this.qomType = qomType;
			this.id = id;
			this.props = props;
		}
	}

	/** Response to a ObjectAddCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new ObjectAddCommand. */
	public ObjectAddCommand(@Nonnull ObjectAddCommand.Arguments argument) {
		super("object-add", Response.class, argument);
	}

	/** Constructs a new ObjectAddCommand. */
	public ObjectAddCommand(java.lang.String qomType, java.lang.String id, java.lang.Object props) {
		this(new Arguments(qomType, id, props));
	}
}
