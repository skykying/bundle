package org.anarres.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.qemu.qapi.common.*;

/**
 * Autogenerated class.
 *
 * <pre>QApiUnionDescriptor{name=KeyValue, discriminator=null, data={number=int, qcode=QKeyCode}, innerTypes=null, fields=null, base=null, discriminatorField=null}</pre>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class KeyValue extends QApiType implements QApiUnion {
	public static enum Discriminator {
		number,
		qcode,
		__NONE;
	}

	
	@Nonnull
	@JsonProperty("type")
	public Discriminator type;

	@Nonnull
	public final Discriminator getType() {
		return type;
	}

	// union {
	
	@JsonProperty("number")
	@JsonUnwrapped
	@CheckForNull
	public java.lang.Long number;
	
	@JsonProperty("qcode")
	@JsonUnwrapped
	@CheckForNull
	public QKeyCode qcode;
	// }

	@Nonnull
	public static KeyValue number(@Nonnull java.lang.Long number) {
		KeyValue self = new KeyValue();
		self.type = Discriminator.number;
		self.number = number;
		return self;
	}

	@Nonnull
	public static KeyValue qcode(@Nonnull QKeyCode qcode) {
		KeyValue self = new KeyValue();
		self.type = Discriminator.qcode;
		self.qcode = qcode;
		return self;
	}

	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("number");
		names.add("qcode");
		return names;
	}

	@JsonIgnore
	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("number".equals(name))
			return number;
		if ("qcode".equals(name))
			return qcode;
		return super.getFieldByName(name);
	}

	@JsonIgnore
	public boolean isValidUnion() {
		int count = 0;
		if (number != null)
			count++;
		if (qcode != null)
			count++;
		return (count == 1);
	}
}
