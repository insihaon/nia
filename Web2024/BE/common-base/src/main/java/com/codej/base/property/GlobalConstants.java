package com.codej.base.property;
public class GlobalConstants {
    // mybatis 결과 resultMap 에서 키를 camelcase 로 변경 여부 (CREATE_TIME > createTime)
    // public static final Boolean CONVERT_CAMELCASE_KEY = false;

    public static class CustomCacheKey {

        public static final int DEFAULT_EXPIRE_SEC = 60; // 1 minutes

        public static final String USER = "user";
        public static final int USER_EXPIRE_SEC = 60 * 5; // 5 minutes
    }

    public static class KEY {
        public static final String RSA_KEY = "RsaKey";
        public static final String AES_KEY = "celsis";
    }

    public static class Common {
        public static final String DATA = "data";
        public static final String N_LANG = "N_LANG";
        public static final String ENCRYPT = "encrypt";
        public static final String UID = "uid";
        public static final String ROLES = "roles";
        public static final String RESULT = "result";
    }
}
