package com.nia.engine.common;

import javax.xml.bind.DatatypeConverter;
import java.math.BigDecimal;
import java.util.*;

public class Data implements ModelData {
    public static final String DATA_LIST = "DATA_LIST";

    protected Map<String, Object> map;
    private boolean blobTobase64;

    public Data() {
    }

    public Data(Map<String, Object> properties) {
        this(properties, false);
    }

    public Data(Map<String, Object> properties, boolean blobTobase64) {
        super();
        this.blobTobase64 = blobTobase64;
        setProperties(properties);
    }

    public <X> X get(String property, X defaultValue) {
        X obj = get(property);
        return obj != null ? obj : defaultValue;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <X> X get(String property) {
        if (map == null) {
            return null;
        }
        int start = property.indexOf("[");
        int end = property.indexOf("]");
        X obj = null;
        if (start > -1 && end > -1) {
            Object o = map.get(property.substring(0, start));
            String p = property.substring(start + 1, end);
            if (o instanceof Object[]) {
                obj = (X) ((Object[]) o)[Integer.valueOf(p)];
            } else if (o instanceof List) {
                obj = (X) ((List) o).get(Integer.valueOf(p));
            } else if (o instanceof Map) {
                obj = (X) ((Map) o).get(p);
            }
        } else {
            obj = (X) map.get(property);

            if (obj instanceof BigDecimal) {
                obj = (X) (Float) ((BigDecimal) obj).floatValue();
            }
        }
        return obj;
    }

    public Map<String, Object> getProperties() {
        Map<String, Object> newMap = new HashMap<String, Object>();
        if (map != null) {
            newMap.putAll(map);
        }
        return newMap;
    }

    public Collection<String> getPropertyNames() {
        Set<String> set = new HashSet<String>();
        if (map != null) {
            set.addAll(map.keySet());
        }
        return set;
    }

    @SuppressWarnings("unchecked")
    public <X> X remove(String property) {
        try {
            return map == null ? null : (X) map.remove(property);
        } catch (Exception e) {
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <X> X set(String property, X value) {
        if (map == null) {
            map = new HashMap<String, Object>();
        }

        int start = property.indexOf("[");
        int end = property.indexOf("]");

        if (start > -1 && end > -1) {
            Object o = get(property.substring(0, start));
            String p = property.substring(start + 1, end);
            if (o instanceof Object[]) {
                int i = Integer.valueOf(p);
                Object[] oa = (Object[]) o;
                X old = (X) oa[i];
                oa[i] = value;
                return old;
            } else if (o instanceof List) {
                int i = Integer.valueOf(p);
                List list = (List) o;
                return (X) list.set(i, value);
            } else if (o instanceof Map) {
                Map map = (Map) o;
                return (X) map.put(p, value);
            } else {
                // not supported
                return null;
            }
        } else if (blobTobase64 && value instanceof byte[]) {
            return (X) map.put(property, DatatypeConverter.printBase64Binary((byte[]) value));
        } else {
            return (X) map.put(property, value);
        }
    }

    public void setProperties(Map<String, Object> properties) {
        if (properties == null) {
            return;
        }
        for (String property : properties.keySet()) {
            set(property, properties.get(property));
        }
    }

    public Map<String, Object> getMap() {
        return map == null ? null : map;
    }

    public Data clone() {
        return new Data(getProperties());
    }

    public boolean containsKey(String key) {
        return map == null ? false : map.containsKey(key);
    }

    public String getString(String key) {
        return this.get(key);
    }

    public int getInt(String key) {
        return this.get(key);
    }

    public int getIntParse(String key) {
        return Integer.parseInt(get(key, "0"));
    }

    public long getLong(String key) {
        return this.get(key);
    }

    public long getLongParse(String key) {
        return Integer.parseInt(get(key, "0"));
    }

    public boolean getBoolean(String key) {
        return this.get(key);
    }

    public String toString() {
        String str = "\n";
        Map<String, Object> properties = getProperties();
        Iterator<String> iterator = properties.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = properties.get(key);
            String strValue = "";
            if (value == null) {
                strValue = "Null";
            } else if (value instanceof Data[]) {
                strValue = "{";
                Data[] parameters = (Data[]) value;
                for (int i = 0; i < parameters.length; i++) {
                    strValue += parameters[i] + ", ";
                }
                strValue += "}";
            } else if (value instanceof String[]) {
                String[] array = (String[]) value;
                for (int i = 0; i < array.length; i++) {
                    strValue += array[i];
                    if (i < array.length - 1) {
                        strValue += ", ";
                    }
                }
            } else {
                strValue = value.toString();
            }
            str += "[" + key + ": " + strValue + "]\n";
        }
        return str;
    }

    public static void putDataValue(Data data, String property, Object value) {
        if (value != null && !(value instanceof String && ((String) value).length() == 0)) {
            data.set(property, value);
        }
    }

    public static void putDataValue(Data parameter, String property, String key, Data data) {
        Object value = data.get(key);
        if (value != null && !(value instanceof String && ((String) value).length() == 0)) {
            parameter.set(property, value);
        }
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Data) {
            Data temp = (Data) obj;
            return this.toString().equals(temp.toString());
        }
        return false;
    }
}
