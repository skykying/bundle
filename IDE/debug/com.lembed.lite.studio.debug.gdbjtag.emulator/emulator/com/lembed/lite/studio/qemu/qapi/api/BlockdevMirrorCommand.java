package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiCommandDescriptor{name=blockdev-mirror, returns=null, data={*job-id=str, device=str, target=str, *replaces=str, sync=MirrorSyncMode, *speed=int, *granularity=uint32, *buf-size=int, *on-source-error=BlockdevOnError, *on-target-error=BlockdevOnError, *filter-node-name=str}}</pre>
 */
// QApiCommandDescriptor{name=blockdev-mirror, returns=null, data={*job-id=str, device=str, target=str, *replaces=str, sync=MirrorSyncMode, *speed=int, *granularity=uint32, *buf-size=int, *on-source-error=BlockdevOnError, *on-target-error=BlockdevOnError, *filter-node-name=str}}
public class BlockdevMirrorCommand extends QApiCommand<BlockdevMirrorCommand.Arguments, BlockdevMirrorCommand.Response> {
	/** Compound arguments to a BlockdevMirrorCommand. */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public static class Arguments {

		
		@JsonProperty("job-id")
		@CheckForNull
		public java.lang.String jobId;
		
		@JsonProperty("device")
		@Nonnull
		public java.lang.String device;
		
		@JsonProperty("target")
		@Nonnull
		public java.lang.String target;
		
		@JsonProperty("replaces")
		@CheckForNull
		public java.lang.String replaces;
		
		@JsonProperty("sync")
		@Nonnull
		public MirrorSyncMode sync;
		
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
		
		@JsonProperty("filter-node-name")
		@CheckForNull
		public java.lang.String filterNodeName;

		public Arguments() {
		}

		public Arguments(java.lang.String jobId, java.lang.String device, java.lang.String target, java.lang.String replaces, MirrorSyncMode sync, java.lang.Long speed, java.lang.Long granularity, java.lang.Long bufSize, BlockdevOnError onSourceError, BlockdevOnError onTargetError, java.lang.String filterNodeName) {
			this.jobId = jobId;
			this.device = device;
			this.target = target;
			this.replaces = replaces;
			this.sync = sync;
			this.speed = speed;
			this.granularity = granularity;
			this.bufSize = bufSize;
			this.onSourceError = onSourceError;
			this.onTargetError = onTargetError;
			this.filterNodeName = filterNodeName;
		}
	}

	/** Response to a BlockdevMirrorCommand. */
	public static class Response extends QApiResponse<java.lang.Void> {
	}

	/** Constructs a new BlockdevMirrorCommand. */
	public BlockdevMirrorCommand(@Nonnull BlockdevMirrorCommand.Arguments argument) {
		super("blockdev-mirror", Response.class, argument);
	}

	/** Constructs a new BlockdevMirrorCommand. */
	public BlockdevMirrorCommand(java.lang.String jobId, java.lang.String device, java.lang.String target, java.lang.String replaces, MirrorSyncMode sync, java.lang.Long speed, java.lang.Long granularity, java.lang.Long bufSize, BlockdevOnError onSourceError, BlockdevOnError onTargetError, java.lang.String filterNodeName) {
		this(new Arguments(jobId, device, target, replaces, sync, speed, granularity, bufSize, onSourceError, onTargetError, filterNodeName));
	}
}
