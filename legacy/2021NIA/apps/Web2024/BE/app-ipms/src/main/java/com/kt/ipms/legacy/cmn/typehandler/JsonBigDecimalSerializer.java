package com.kt.ipms.legacy.cmn.typehandler;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class JsonBigDecimalSerializer extends JsonSerializer<BigDecimal> {

	private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00########");
	@Override
	public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		decimalFormat.setParseBigDecimal(true);
		jsonGenerator.writeString(decimalFormat.format(bigDecimal));
	}

}
