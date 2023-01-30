package com.nia.data.linkage.ipsdn.vo.ipsdn.sflow;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.sql.Timestamp;
@Component
@Data
@Scope(value = "prototype")
public class SflowDataVo implements Serializable {
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


	@JsonProperty("CollectSeq")
	public long getCollectSeq() {
		return collectSeq;
	}

	@JsonProperty("CollectHost")
	public String getCollectHost() {
		return collectHost;
	}

	@JsonProperty("AgentAddress")
	public String getAgentAddress() {
		return agentAddress;
	}

	@JsonProperty("EtherType")
	public String getEtherType() {
		return etherType;
	}

	@JsonProperty("HeaderProtocol")
	public String getHeaderProtocol() {
		return headerProtocol;
	}

	@JsonProperty("InputIfindex")
	public int getInputIfindex() {
		return inputIfindex;
	}

	@JsonProperty("OutputIfindex")
	public int getOutputIfindex() {
		return outputIfindex;
	}

	@JsonProperty("SampleDirection")
	public String getSampleDirection() {
		return sampleDirection;
	}

	@JsonProperty("SrcIp")
	public String getSrcIp() {
		return srcIp;
	}

	@JsonProperty("SrcMac")
	public String getSrcMac() {
		return srcMac;
	}

	@JsonProperty("SrcPort")
	public String getSrcPort() {
		return srcPort;
	}

	@JsonProperty("SourceIdType")
	public String getSourceIdType() {
		return sourceIdType;
	}

	@JsonProperty("SourceIdIndex")
	public String getSourceIdIndex() {
		return sourceIdIndex;
	}

	@JsonProperty("DstIp")
	public String getDstIp() {
		return dstIp;
	}

	@JsonProperty("DstMac")
	public String getDstMac() {
		return dstMac;
	}

	@JsonProperty("DstPort")
	public String getDstPort() {
		return dstPort;
	}

	@JsonProperty("PacketBytes")
	public int getPacketBytes() {
		return packetBytes;
	}

	@JsonProperty("PacketDrops")
	public int getPacketDrops() {
		return packetDrops;
	}

	@JsonProperty("FrameLength")
	public int getFrameLength() {
		return frameLength;
	}

	@JsonProperty("HeaderLength")
	public int getHeaderLength() {
		return headerLength;
	}

	@JsonProperty("IpDscp")
	public String getIpDscp() {
		return ipDscp;
	}

	@JsonProperty("IpEcn")
	public String getIpEcn() {
		return ipEcn;
	}

	@JsonProperty("IpFlags")
	public int getIpFlags() {
		return ipFlags;
	}

	@JsonProperty("IpFragmentOffset")
	public int getIpFragmentOffset() {
		return ipFragmentOffset;
	}

	@JsonProperty("IpTotalLength")
	public int getIpTotalLength() {
		return ipTotalLength;
	}

	@JsonProperty("IpTtl")
	public int getIpTtl() {
		return ipTtl;
	}

	@JsonProperty("SamplingRate")
	public int getSamplingRate() {
		return samplingRate;
	}

	@JsonProperty("TcpHeaderLength")
	public int getTcpHeaderLength() {
		return tcpHeaderLength;
	}

	@JsonProperty("TcpUrgentPointer")
	public int getTcpUrgentPointer() {
		return tcpUrgentPointer;
	}

	@JsonProperty("TcpWindowSize")
	public int getTcpWindowSize() {
		return tcpWindowSize;
	}

	@JsonProperty("CollectTimestamp")
	public Timestamp getCollectTimestamp() {
		return collectTimestamp;
	}

}