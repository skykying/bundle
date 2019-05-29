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
 * <pre>QApiStructDescriptor{name=IOThreadInfo, data={id=str, thread-id=int, poll-max-ns=int, poll-grow=int, poll-shrink=int}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=IOThreadInfo, data={id=str, thread-id=int, poll-max-ns=int, poll-grow=int, poll-shrink=int}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class IOThreadInfo extends QApiType {

	
	@JsonProperty("id")
	@Nonnull
	public java.lang.String id;
	
	@JsonProperty("thread-id")
	@Nonnull
	public long threadId;
	
	@JsonProperty("poll-max-ns")
	@Nonnull
	public long pollMaxNs;
	
	@JsonProperty("poll-grow")
	@Nonnull
	public long pollGrow;
	
	@JsonProperty("poll-shrink")
	@Nonnull
	public long pollShrink;

	@Nonnull
	public IOThreadInfo withId(java.lang.String value) {
		this.id = value;
		return this;
	}

	@Nonnull
	public IOThreadInfo withThreadId(long value) {
		this.threadId = value;
		return this;
	}

	@Nonnull
	public IOThreadInfo withPollMaxNs(long value) {
		this.pollMaxNs = value;
		return this;
	}

	@Nonnull
	public IOThreadInfo withPollGrow(long value) {
		this.pollGrow = value;
		return this;
	}

	@Nonnull
	public IOThreadInfo withPollShrink(long value) {
		this.pollShrink = value;
		return this;
	}

	public IOThreadInfo() {
	}

	public IOThreadInfo(java.lang.String id, long threadId, long pollMaxNs, long pollGrow, long pollShrink) {
		this.id = id;
		this.threadId = threadId;
		this.pollMaxNs = pollMaxNs;
		this.pollGrow = pollGrow;
		this.pollShrink = pollShrink;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("id");
		names.add("thread-id");
		names.add("poll-max-ns");
		names.add("poll-grow");
		names.add("poll-shrink");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("id".equals(name))
			return id;
		if ("thread-id".equals(name))
			return threadId;
		if ("poll-max-ns".equals(name))
			return pollMaxNs;
		if ("poll-grow".equals(name))
			return pollGrow;
		if ("poll-shrink".equals(name))
			return pollShrink;
		return super.getFieldByName(name);
	}
}