package com.codej.base.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GsonFactory {
//    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static Gson build() {
        return GsonFactory.build(null, null);
    }

    public static Gson build(final List<String> fieldExclusions, final List<Class<?>> classExclusions) {
        GsonBuilder b = new GsonBuilder();
        b.addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return fieldExclusions != null && fieldExclusions.contains(f.getName())
                        || f.getAnnotation(JsonIgnore.class) != null;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return classExclusions != null && classExclusions.contains(clazz);
            }
        });
        b.setDateFormat("yyyy-MM-dd");
        b.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter());
        b.registerTypeAdapter(Date.class, new GsonDateFormatAdapter());

        return b.create();
    }

    static class GsonLocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT);
            try {
                return json == null ? null : simpleDateFormat.parse(json.getAsString()).toInstant() // Date -> Instant
                        .atZone(JsonUtil.TimeZoneDefault().toZoneId()) // Instant -> ZonedDateTime
                        .toLocalDateTime(); // ZonedDateTime -> LocalDateTime
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT);
            return src == null ? null : new JsonPrimitive(simpleDateFormat.format(Date.from(src.atZone(JsonUtil.TimeZoneDefault().toZoneId()).toInstant())));
        }
    }

    static class GsonDateFormatAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

        private final DateFormat dateFormat;

        public GsonDateFormatAdapter() {
            dateFormat = new SimpleDateFormat(FORMAT, Locale.KOREA);
            dateFormat.setTimeZone(JsonUtil.TimeZoneDefault());
        }

        @Override
        public synchronized JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(dateFormat.format(date));
        }

        @Override
        public synchronized Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
            try {
                return dateFormat.parse(jsonElement.getAsString());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }
}
