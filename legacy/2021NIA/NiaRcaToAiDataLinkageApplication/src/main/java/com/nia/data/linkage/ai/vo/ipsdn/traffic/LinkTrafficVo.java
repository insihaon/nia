package com.nia.data.linkage.ai.vo.ipsdn.traffic;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.sql.Timestamp;
@Component
@Scope(value = "prototype")
@Data
public class LinkTrafficVo implements Serializable {
	private int id;
	private int ifId;
	private int txBitRate;
	private int rxBitRate;
	private int txPktRate;
	private int rxPktRate;
	private Timestamp measuredDatetime;


	@JsonProperty("ID")
	public int getId() {
		return id;
	}

	@JsonProperty("IfId")
	public int getIfId() {
		return ifId;
	}

	@JsonProperty("TxBitRate")
	public int getTxBitRate() {
		return txBitRate;
	}

	@JsonProperty("RxBitRate")
	public int getRxBitRate() {
		return rxBitRate;
	}

	@JsonProperty("TxPktRate")
	public int getTxPktRate() {
		return txPktRate;
	}

	@JsonProperty("RxPktRate")
	public int getRxPktRate() {
		return rxPktRate;
	}

	@JsonProperty("MeasuredDatetime")
	public Timestamp getMeasuredDatetime() {
		return measuredDatetime;
	}
}