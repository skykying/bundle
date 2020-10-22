package com.lembed.lite.studio.qemu.qapi.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;
import com.lembed.lite.studio.qemu.qapi.common.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Autogenerated class.
 *
 * <pre>QApiUnionDescriptor{name=BlockdevCreateOptions, discriminator=driver, data={blkdebug=BlockdevCreateNotSupported, blkverify=BlockdevCreateNotSupported, bochs=BlockdevCreateNotSupported, cloop=BlockdevCreateNotSupported, dmg=BlockdevCreateNotSupported, file=BlockdevCreateOptionsFile, ftp=BlockdevCreateNotSupported, ftps=BlockdevCreateNotSupported, gluster=BlockdevCreateOptionsGluster, host_cdrom=BlockdevCreateNotSupported, host_device=BlockdevCreateNotSupported, http=BlockdevCreateNotSupported, https=BlockdevCreateNotSupported, iscsi=BlockdevCreateNotSupported, luks=BlockdevCreateOptionsLUKS, nbd=BlockdevCreateNotSupported, nfs=BlockdevCreateOptionsNfs, null-aio=BlockdevCreateNotSupported, null-co=BlockdevCreateNotSupported, nvme=BlockdevCreateNotSupported, parallels=BlockdevCreateOptionsParallels, qcow=BlockdevCreateOptionsQcow, qcow2=BlockdevCreateOptionsQcow2, qed=BlockdevCreateOptionsQed, quorum=BlockdevCreateNotSupported, raw=BlockdevCreateNotSupported, rbd=BlockdevCreateOptionsRbd, replication=BlockdevCreateNotSupported, sheepdog=BlockdevCreateOptionsSheepdog, ssh=BlockdevCreateOptionsSsh, throttle=BlockdevCreateNotSupported, vdi=BlockdevCreateOptionsVdi, vhdx=BlockdevCreateOptionsVhdx, vmdk=BlockdevCreateNotSupported, vpc=BlockdevCreateOptionsVpc, vvfat=BlockdevCreateNotSupported, vxhs=BlockdevCreateNotSupported}, innerTypes=null, fields=null, base={driver=BlockdevDriver}, discriminatorField=null}</pre>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BlockdevCreateOptions extends BlockdevCreateOptionsBase implements QApiUnion {

	// union {
	
	@JsonProperty("blkdebug")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported blkdebug;
	
	@JsonProperty("blkverify")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported blkverify;
	
	@JsonProperty("bochs")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported bochs;
	
	@JsonProperty("cloop")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported cloop;
	
	@JsonProperty("dmg")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported dmg;
	
	@JsonProperty("file")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateOptionsFile file;
	
	@JsonProperty("ftp")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported ftp;
	
	@JsonProperty("ftps")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported ftps;
	
	@JsonProperty("gluster")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateOptionsGluster gluster;
	
	@JsonProperty("host_cdrom")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported hostCdrom;
	
	@JsonProperty("host_device")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported hostDevice;
	
	@JsonProperty("http")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported http;
	
	@JsonProperty("https")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported https;
	
	@JsonProperty("iscsi")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported iscsi;
	
	@JsonProperty("luks")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateOptionsLUKS luks;
	
	@JsonProperty("nbd")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported nbd;
	
	@JsonProperty("nfs")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateOptionsNfs nfs;
	
	@JsonProperty("null-aio")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported nullAio;
	
	@JsonProperty("null-co")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported nullCo;
	
	@JsonProperty("nvme")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported nvme;
	
	@JsonProperty("parallels")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateOptionsParallels parallels;
	
	@JsonProperty("qcow")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateOptionsQcow qcow;
	
	@JsonProperty("qcow2")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateOptionsQcow2 qcow2;
	
	@JsonProperty("qed")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateOptionsQed qed;
	
	@JsonProperty("quorum")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported quorum;
	
	@JsonProperty("raw")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported raw;
	
	@JsonProperty("rbd")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateOptionsRbd rbd;
	
	@JsonProperty("replication")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported replication;
	
	@JsonProperty("sheepdog")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateOptionsSheepdog sheepdog;
	
	@JsonProperty("ssh")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateOptionsSsh ssh;
	
	@JsonProperty("throttle")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported throttle;
	
	@JsonProperty("vdi")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateOptionsVdi vdi;
	
	@JsonProperty("vhdx")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateOptionsVhdx vhdx;
	
	@JsonProperty("vmdk")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported vmdk;
	
	@JsonProperty("vpc")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateOptionsVpc vpc;
	
	@JsonProperty("vvfat")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported vvfat;
	
	@JsonProperty("vxhs")
	@JsonUnwrapped
	@CheckForNull
	public BlockdevCreateNotSupported vxhs;
	// }

	@Nonnull
	public static BlockdevCreateOptions blkdebug(@Nonnull BlockdevCreateNotSupported blkdebug) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.blkdebug;
		self.blkdebug = blkdebug;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions blkverify(@Nonnull BlockdevCreateNotSupported blkverify) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.blkverify;
		self.blkverify = blkverify;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions bochs(@Nonnull BlockdevCreateNotSupported bochs) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.bochs;
		self.bochs = bochs;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions cloop(@Nonnull BlockdevCreateNotSupported cloop) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.cloop;
		self.cloop = cloop;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions dmg(@Nonnull BlockdevCreateNotSupported dmg) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.dmg;
		self.dmg = dmg;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions file(@Nonnull BlockdevCreateOptionsFile file) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.file;
		self.file = file;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions ftp(@Nonnull BlockdevCreateNotSupported ftp) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.ftp;
		self.ftp = ftp;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions ftps(@Nonnull BlockdevCreateNotSupported ftps) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.ftps;
		self.ftps = ftps;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions gluster(@Nonnull BlockdevCreateOptionsGluster gluster) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.gluster;
		self.gluster = gluster;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions hostCdrom(@Nonnull BlockdevCreateNotSupported hostCdrom) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.host_cdrom;
		self.hostCdrom = hostCdrom;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions hostDevice(@Nonnull BlockdevCreateNotSupported hostDevice) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.host_device;
		self.hostDevice = hostDevice;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions http(@Nonnull BlockdevCreateNotSupported http) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.http;
		self.http = http;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions https(@Nonnull BlockdevCreateNotSupported https) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.https;
		self.https = https;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions iscsi(@Nonnull BlockdevCreateNotSupported iscsi) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.iscsi;
		self.iscsi = iscsi;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions luks(@Nonnull BlockdevCreateOptionsLUKS luks) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.luks;
		self.luks = luks;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions nbd(@Nonnull BlockdevCreateNotSupported nbd) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.nbd;
		self.nbd = nbd;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions nfs(@Nonnull BlockdevCreateOptionsNfs nfs) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.nfs;
		self.nfs = nfs;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions nullAio(@Nonnull BlockdevCreateNotSupported nullAio) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.null_aio;
		self.nullAio = nullAio;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions nullCo(@Nonnull BlockdevCreateNotSupported nullCo) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.null_co;
		self.nullCo = nullCo;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions nvme(@Nonnull BlockdevCreateNotSupported nvme) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.nvme;
		self.nvme = nvme;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions parallels(@Nonnull BlockdevCreateOptionsParallels parallels) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.parallels;
		self.parallels = parallels;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions qcow(@Nonnull BlockdevCreateOptionsQcow qcow) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.qcow;
		self.qcow = qcow;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions qcow2(@Nonnull BlockdevCreateOptionsQcow2 qcow2) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.qcow2;
		self.qcow2 = qcow2;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions qed(@Nonnull BlockdevCreateOptionsQed qed) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.qed;
		self.qed = qed;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions quorum(@Nonnull BlockdevCreateNotSupported quorum) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.quorum;
		self.quorum = quorum;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions raw(@Nonnull BlockdevCreateNotSupported raw) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.raw;
		self.raw = raw;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions rbd(@Nonnull BlockdevCreateOptionsRbd rbd) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.rbd;
		self.rbd = rbd;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions replication(@Nonnull BlockdevCreateNotSupported replication) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.replication;
		self.replication = replication;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions sheepdog(@Nonnull BlockdevCreateOptionsSheepdog sheepdog) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.sheepdog;
		self.sheepdog = sheepdog;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions ssh(@Nonnull BlockdevCreateOptionsSsh ssh) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.ssh;
		self.ssh = ssh;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions throttle(@Nonnull BlockdevCreateNotSupported throttle) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.throttle;
		self.throttle = throttle;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions vdi(@Nonnull BlockdevCreateOptionsVdi vdi) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.vdi;
		self.vdi = vdi;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions vhdx(@Nonnull BlockdevCreateOptionsVhdx vhdx) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.vhdx;
		self.vhdx = vhdx;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions vmdk(@Nonnull BlockdevCreateNotSupported vmdk) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.vmdk;
		self.vmdk = vmdk;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions vpc(@Nonnull BlockdevCreateOptionsVpc vpc) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.vpc;
		self.vpc = vpc;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions vvfat(@Nonnull BlockdevCreateNotSupported vvfat) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.vvfat;
		self.vvfat = vvfat;
		return self;
	}

	@Nonnull
	public static BlockdevCreateOptions vxhs(@Nonnull BlockdevCreateNotSupported vxhs) {
		BlockdevCreateOptions self = new BlockdevCreateOptions();
		self.driver = BlockdevDriver.vxhs;
		self.vxhs = vxhs;
		return self;
	}

	@Override
	public java.util.List<java.lang.String> getFieldNames() {
		java.util.List<java.lang.String> names = super.getFieldNames();
		names.add("blkdebug");
		names.add("blkverify");
		names.add("bochs");
		names.add("cloop");
		names.add("dmg");
		names.add("file");
		names.add("ftp");
		names.add("ftps");
		names.add("gluster");
		names.add("host_cdrom");
		names.add("host_device");
		names.add("http");
		names.add("https");
		names.add("iscsi");
		names.add("luks");
		names.add("nbd");
		names.add("nfs");
		names.add("null-aio");
		names.add("null-co");
		names.add("nvme");
		names.add("parallels");
		names.add("qcow");
		names.add("qcow2");
		names.add("qed");
		names.add("quorum");
		names.add("raw");
		names.add("rbd");
		names.add("replication");
		names.add("sheepdog");
		names.add("ssh");
		names.add("throttle");
		names.add("vdi");
		names.add("vhdx");
		names.add("vmdk");
		names.add("vpc");
		names.add("vvfat");
		names.add("vxhs");
		return names;
	}

	@JsonIgnore
	@Override
	public Object getFieldByName(@Nonnull java.lang.String name) throws NoSuchFieldException {
		if ("blkdebug".equals(name))
			return blkdebug;
		if ("blkverify".equals(name))
			return blkverify;
		if ("bochs".equals(name))
			return bochs;
		if ("cloop".equals(name))
			return cloop;
		if ("dmg".equals(name))
			return dmg;
		if ("file".equals(name))
			return file;
		if ("ftp".equals(name))
			return ftp;
		if ("ftps".equals(name))
			return ftps;
		if ("gluster".equals(name))
			return gluster;
		if ("host_cdrom".equals(name))
			return hostCdrom;
		if ("host_device".equals(name))
			return hostDevice;
		if ("http".equals(name))
			return http;
		if ("https".equals(name))
			return https;
		if ("iscsi".equals(name))
			return iscsi;
		if ("luks".equals(name))
			return luks;
		if ("nbd".equals(name))
			return nbd;
		if ("nfs".equals(name))
			return nfs;
		if ("null-aio".equals(name))
			return nullAio;
		if ("null-co".equals(name))
			return nullCo;
		if ("nvme".equals(name))
			return nvme;
		if ("parallels".equals(name))
			return parallels;
		if ("qcow".equals(name))
			return qcow;
		if ("qcow2".equals(name))
			return qcow2;
		if ("qed".equals(name))
			return qed;
		if ("quorum".equals(name))
			return quorum;
		if ("raw".equals(name))
			return raw;
		if ("rbd".equals(name))
			return rbd;
		if ("replication".equals(name))
			return replication;
		if ("sheepdog".equals(name))
			return sheepdog;
		if ("ssh".equals(name))
			return ssh;
		if ("throttle".equals(name))
			return throttle;
		if ("vdi".equals(name))
			return vdi;
		if ("vhdx".equals(name))
			return vhdx;
		if ("vmdk".equals(name))
			return vmdk;
		if ("vpc".equals(name))
			return vpc;
		if ("vvfat".equals(name))
			return vvfat;
		if ("vxhs".equals(name))
			return vxhs;
		return super.getFieldByName(name);
	}

	@JsonIgnore
	public boolean isValidUnion() {
		int count = 0;
		if (blkdebug != null)
			count++;
		if (blkverify != null)
			count++;
		if (bochs != null)
			count++;
		if (cloop != null)
			count++;
		if (dmg != null)
			count++;
		if (file != null)
			count++;
		if (ftp != null)
			count++;
		if (ftps != null)
			count++;
		if (gluster != null)
			count++;
		if (hostCdrom != null)
			count++;
		if (hostDevice != null)
			count++;
		if (http != null)
			count++;
		if (https != null)
			count++;
		if (iscsi != null)
			count++;
		if (luks != null)
			count++;
		if (nbd != null)
			count++;
		if (nfs != null)
			count++;
		if (nullAio != null)
			count++;
		if (nullCo != null)
			count++;
		if (nvme != null)
			count++;
		if (parallels != null)
			count++;
		if (qcow != null)
			count++;
		if (qcow2 != null)
			count++;
		if (qed != null)
			count++;
		if (quorum != null)
			count++;
		if (raw != null)
			count++;
		if (rbd != null)
			count++;
		if (replication != null)
			count++;
		if (sheepdog != null)
			count++;
		if (ssh != null)
			count++;
		if (throttle != null)
			count++;
		if (vdi != null)
			count++;
		if (vhdx != null)
			count++;
		if (vmdk != null)
			count++;
		if (vpc != null)
			count++;
		if (vvfat != null)
			count++;
		if (vxhs != null)
			count++;
		return (count == 1);
	}
}