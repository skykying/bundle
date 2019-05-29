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
 * <pre>QApiStructDescriptor{name=DriveMirror, data={*job-id=str, device=str, target=str, *format=str, *node-name=str, *replaces=str, sync=MirrorSyncMode, *mode=NewImageMode, *speed=int, *granularity=uint32, *buf-size=int, *on-source-error=BlockdevOnError, *on-target-error=BlockdevOnError, *unmap=bool}, innerTypes=null, fields=null, base=null}</pre>
 */
// QApiStructDescriptor{name=DriveMirror, data={*job-id=str, device=str, target=str, *format=str, *node-name=str, *replaces=str, sync=MirrorSyncMode, *mode=NewImageMode, *speed=int, *granularity=uint32, *buf-size=int, *on-source-error=BlockdevOnError, *on-target-error=BlockdevOnError, *unmap=bool}, innerTypes=null, fields=null, base=null}
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DriveMirror extends QApiType {

	
	@JsonProperty("job-id")
	@CheckForNull
	public java.lang.String jobId;
	
	@JsonProperty("device")
	@Nonnull
	public java.lang.String device;
	
	@JsonProperty("target")
	@Nonnull
	public java.lang.String target;
	
	@JsonProperty("format")
	@CheckForNull
	public java.lang.String format;
	
	@JsonProperty("node-name")
	@CheckForNull
	public java.lang.String nodeName;
	
	@JsonProperty("replaces")
	@CheckForNull
	public java.lang.String replaces;
	
	@JsonProperty("sync")
	@Nonnull
	public MirrorSyncMode sync;
	
	@JsonProperty("mode")
	@CheckForNull
	public NewImageMode mode;
	
	@JsonProperty("speed")
	@CheckForNull
	public java.lang.Long speed;
	
	@JsonProperty("granularity")
	@CheckForNull
	public java.lang.Long granularity;
	
	@JsonProperty("buf-size")
	@CheckForNull
	public java.lang.Long bufSize;
	
	@JsonProperty("on-source-error")
	@CheckForNull
	public BlockdevOnError onSourceError;
	
	@JsonProperty("on-target-error")
	@CheckForNull
	public BlockdevOnError onTargetError;
	
	@JsonProperty("unmap")
	@CheckForNull
	public java.lang.Boolean unmap;

	@Nonnull
	public DriveMirror withJobId(java.lang.String value) {
		this.jobId = value;
		return this;
	}

	@Nonnull
	public DriveMirror withDevice(java.lang.String value) {
		this.device = value;
		return this;
	}

	@Nonnull
	public DriveMirror withTarget(java.lang.String value) {
		this.target = value;
		return this;
	}

	@Nonnull
	public DriveMirror withFormat(java.lang.String value) {
		this.format = value;
		return this;
	}

	@Nonnull
	public DriveMirror withNodeName(java.lang.String value) {
		this.nodeName = value;
		return this;
	}

	@Nonnull
	public DriveMirror withReplaces(java.lang.String value) {
		this.replaces = value;
		return this;
	}

	@Nonnull
	public DriveMirror withSync(MirrorSyncMode value) {
		this.sync = value;
		return this;
	}

	@Nonnull
	public DriveMirror withMode(NewImageMode value) {
		this.mode = value;
		return this;
	}

	@Nonnull
	public DriveMirror withSpeed(java.lang.Long value) {
		this.speed = value;
		return this;
	}

	@Nonnull
	public DriveMirror withGranularity(java.lang.Long value) {
		this.granularity = value;
		return this;
	}

	@Nonnull
	public DriveMirror withBufSize(java.lang.Long value) {
		this.bufSize = value;
		return this;
	}

	@Nonnull
	public DriveMirror withOnSourceError(BlockdevOnError value) {
		this.onSourceError = value;
		return this;
	}

	@Nonnull
	public DriveMirror withOnTargetError(BlockdevOnError value) {
		this.onTargetError = value;
		return this;
	}

	@Nonnull
	public DriveMirror withUnmap(java.lang.Boolean value) {
		this.unmap = value;
		return this;
	}

	public DriveMirror() {
	}

	public DriveMirror(java.lang.String jobId, java.lang.String device, java.lang.String target, java.lang.String format, java.lang.String nodeName, java.lang.String replaces, MirrorSyncMode sync, NewImageMode mode, java.lang.Long speed, java.lang.Long granularity, java.lang.Long bufSize, BlockdevOnError onSourceError, BlockdevOnError onTargetError, java.lang.Boolean unmap) {
		this.jobId = jobId;
		this.device = device;
		this.target = target;
		this.format = format;
		this.nodeName = nodeName;
		this.replaces = replaces;
		this.sync = sync;
		this.mode = mode;
		this.speed = speed;
		this.granularity = granularity;
		this.bufSize = bufSize;
		this.onSourceError = onSourceError;
		this.onTargetError = onTargetError;
		this.unmap = unmap;
	}

	@JsonIgnore
	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("job-id");
		names.add("device");
		names.add("target");
		names.add("format");
		names.add("node-name");
		names.add("replaces");
		names.add("sync");
		names.add("mode");
		names.add("speed");
		names.add("granularity");
		names.add("buf-size");
		names.add("on-source-error");
		names.add("on-target-error");
		names.add("unmap");
		return names;
	}

	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("job-id".equals(name))
			return jobId;
		if ("device".equals(name))
			return device;
		if ("target".equals(name))
			return target;
		if ("format".equals(name))
			return format;
		if ("node-name".equals(name))
			return nodeName;
		if ("replaces".equals(name))
			return replaces;
		if ("sync".equals(name))
			return sync;
		if ("mode".equals(name))
			return mode;
		if ("speed".equals(name))
			return speed;
		if ("granularity".equals(name))
			return granularity;
		if ("buf-size".equals(name))
			return bufSize;
		if ("on-source-error".equals(name))
			return onSourceError;
		if ("on-target-error".equals(name))
			return onTargetError;
		if ("unmap".equals(name))
			return unmap;
		return super.getFieldByName(name);
	}
}
