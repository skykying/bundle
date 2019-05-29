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
 * <pre>QApiUnionDescriptor{name=SshHostKeyCheck, discriminator=mode, data={none=SshHostKeyDummy, hash=SshHostKeyHash, known_hosts=SshHostKeyDummy}, innerTypes=null, fields=null, base={mode=SshHostKeyCheckMode}, discriminatorField=null}</pre>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SshHostKeyCheck extends SshHostKeyCheckBase implements QApiUnion {

	// union {
	
	@JsonProperty("none")
	@JsonUnwrapped
	@CheckForNull
	public SshHostKeyDummy none;
	
	@JsonProperty("hash")
	@JsonUnwrapped
	@CheckForNull
	public SshHostKeyHash hash;
	
	@JsonProperty("known_hosts")
	@JsonUnwrapped
	@CheckForNull
	public SshHostKeyDummy knownHosts;
	// }

	@Nonnull
	public static SshHostKeyCheck none(@Nonnull SshHostKeyDummy none) {
		SshHostKeyCheck self = new SshHostKeyCheck();
		self.mode = SshHostKeyCheckMode.none;
		self.none = none;
		return self;
	}

	@Nonnull
	public static SshHostKeyCheck hash(@Nonnull SshHostKeyHash hash) {
		SshHostKeyCheck self = new SshHostKeyCheck();
		self.mode = SshHostKeyCheckMode.hash;
		self.hash = hash;
		return self;
	}

	@Nonnull
	public static SshHostKeyCheck knownHosts(@Nonnull SshHostKeyDummy knownHosts) {
		SshHostKeyCheck self = new SshHostKeyCheck();
		self.mode = SshHostKeyCheckMode.known_hosts;
		self.knownHosts = knownHosts;
		return self;
	}

	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("none");
		names.add("hash");
		names.add("known_hosts");
		return names;
	}

	@JsonIgnore
	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("none".equals(name))
			return none;
		if ("hash".equals(name))
			return hash;
		if ("known_hosts".equals(name))
			return knownHosts;
		return super.getFieldByName(name);
	}

	@JsonIgnore
	public boolean isValidUnion() {
		int count = 0;
		if (none != null)
			count++;
		if (hash != null)
			count++;
		if (knownHosts != null)
			count++;
		return (count == 1);
	}
}
