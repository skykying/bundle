package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiEventDescriptor{name=QUORUM_REPORT_BAD, data={type=QuorumOpType, *error=str, node-name=str, sector-num=int, sectors-count=int}}</pre>
 */
// QApiEventDescriptor{name=QUORUM_REPORT_BAD, data={type=QuorumOpType, *error=str, node-name=str, sector-num=int, sectors-count=int}}
public class QuorumReportBadEvent extends QApiEvent {

	public static class Data {
		
		@JsonProperty("type")
		@Nonnull
		public QuorumOpType type;
		
		@JsonProperty("error")
		@CheckForNull
		public java.lang.String error;
		
		@JsonProperty("node-name")
		@Nonnull
		public java.lang.String nodeName;
		
		@JsonProperty("sector-num")
		@Nonnull
		public long sectorNum;
		
		@JsonProperty("sectors-count")
		@Nonnull
		public long sectorsCount;
	}

	@JsonProperty("data")
	public Data data;
}
