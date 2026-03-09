package com.nia.ip.sdn.sflow.linkage.vo.sflow;

import com.nia.ip.sdn.sflow.linkage.common.UtlDateHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Component
@Scope(value = "prototype")
public class SflowCollectVo implements Serializable {

	private long collectSeq;
	private String collectHost;
	private String agentAddress;
	private String etherType;
	private String headerProtocol;
	private int inputIfindex;
	private int outputIfindex;
	private String sampleDirection;
	private String srcIp;
	private String srcMac;
	private String srcPort;
	private String sourceIdType;
	private String sourceIdIndex;
	private String dstIp;
	private String dstMac;
	private String dstPort;
	private int packetBytes;
	private int packetDrops;
	private int frameLength;
	private int headerLength;
	private String ipDscp;
	private String ipEcn;
	private int ipFlags;
	private int ipFragmentOffset;
	private int ipTotalLength;
	private int ipTtl;
	private int samplingRate;
	private int tcpHeaderLength;
	private int tcpUrgentPointer;
	private int tcpWindowSize;
	private Timestamp collectTimestamp;
	private String partitionKey;

	public void setSflowCollectVo(SflowDataVo sflowDataVo) {
		this.setCollectSeq(sflowDataVo.getCollectSeq());
		this.setCollectHost(sflowDataVo.getTags().getHost());
		this.setAgentAddress(sflowDataVo.getTags().getAgentAddress());
		this.setEtherType(sflowDataVo.getTags().getEtherType());
		this.setHeaderProtocol(sflowDataVo.getTags().getHeaderProtocol());

		if(StringUtils.isNotEmpty(sflowDataVo.getTags().getInputIfindex())){
			this.setInputIfindex(Integer.parseInt(sflowDataVo.getTags().getInputIfindex()));
		}

		if(StringUtils.isNotEmpty(sflowDataVo.getTags().getOutputIfindex())){
			this.setOutputIfindex(Integer.parseInt(sflowDataVo.getTags().getOutputIfindex()));
		}

		this.setSampleDirection(sflowDataVo.getTags().getSampleDirection());
		this.setSrcIp(sflowDataVo.getTags().getSrcIp());
		this.setSrcMac(sflowDataVo.getTags().getSrcMac());
		this.setSrcPort(sflowDataVo.getTags().getSrcPort());
		this.setSourceIdType(sflowDataVo.getTags().getSourceIdType());
		this.setSourceIdIndex(sflowDataVo.getTags().getSourceIdIndex());
		this.setDstIp(sflowDataVo.getTags().getDstIp());
		this.setDstMac(sflowDataVo.getTags().getDstMac());
		this.setDstPort(sflowDataVo.getTags().getDstPort());
		this.setPacketBytes(sflowDataVo.getFields().getBytes());
		this.setPacketDrops(sflowDataVo.getFields().getDrops());
		this.setFrameLength(sflowDataVo.getFields().getFrameLength());
		this.setHeaderLength(sflowDataVo.getFields().getHeaderLength());
		this.setIpDscp(sflowDataVo.getFields().getIpDscp());
		this.setIpEcn(sflowDataVo.getFields().getIpEcn());
		this.setIpFlags(sflowDataVo.getFields().getIpFlags());
		this.setIpFragmentOffset(sflowDataVo.getFields().getIpFragmentOffset());
		this.setIpTotalLength(sflowDataVo.getFields().getIpTotalLength());
		this.setIpTtl(sflowDataVo.getFields().getIpTtl());
		this.setSamplingRate(sflowDataVo.getFields().getSamplingRate());
		this.setTcpHeaderLength(sflowDataVo.getFields().getTcpHeaderLength());
		this.setTcpUrgentPointer(sflowDataVo.getFields().getTcpUrgentPointer());
		this.setTcpWindowSize(sflowDataVo.getFields().getTcpWindowSize());
		this.setCollectTimestamp(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(sflowDataVo.getTimestamp() * 1000L))));
	}
}
