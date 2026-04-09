package com.kt.ipms.legacy.cmn.typehandler;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class JsonBigIntegerSerializer extends JsonSerializer<BigInteger> {

	private final DecimalFormat decimalFormat = new DecimalFormat("#,##0");
	@Override
	public void serialize(BigInteger bigInteger, JsonGenerator jsonGenerator,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jsonGenerator.writeString(decimalFormat.format(bigInteger));
	}

}
