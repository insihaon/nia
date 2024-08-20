package com.kt.ipms.legacy.cmn.typehandler;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.kt.framework.utils.DateUtils;

public class JsonDateSerializer extends JsonSerializer<Date> {

	//private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

	@Override
	public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		jsonGenerator.writeString(DateUtils.parseDate(date, "yyyy-MM-dd HH:mm:ss"));
	}

}
