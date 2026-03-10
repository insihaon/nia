DROP TABLE IF EXISTS "USER";
DROP TABLE IF EXISTS USER_ROLES;

CREATE TABLE "USER" ( uid        VARCHAR(50) NOT NULL, password   VARCHAR(100), name       VARCHAR(100) NOT NULL, ip_address VARCHAR(255), connect_time TIMESTAMP, PRIMARY KEY (uid) );
CREATE TABLE USER_ROLES ( user_uid VARCHAR(50) NOT NULL, roles    VARCHAR(255) );

INSERT INTO    "USER" ( name, password, uid ) VALUES ( '개발자', '{bcrypt}$2a$10$4RyY782xnmR/3uJ3MZ0/EeSxza4PJnQAGmi/yqB4UAyfOiRP7akAe', 'dev' );
INSERT INTO    USER_ROLES ( user_uid, roles ) VALUES ( 'dev', 'ROLE_USER' );

INSERT INTO    "USER" ( name, password, uid ) VALUES ( '딤비', '{bcrypt}$2a$10$Tm7Z2nDCIF4NXNKMj1g4BORL.Hq5MWci9O09hYIhe29DoS66EGULu', 'dimmby' );
INSERT INTO    USER_ROLES ( user_uid, roles ) VALUES ( 'dimmby', 'ROLE_USER' );
INSERT INTO    USER_ROLES ( user_uid, roles ) VALUES ( 'dimmby', 'ROLE_ADMIN' );

INSERT INTO    "USER" ( name, password, uid ) VALUES ( '이창재', '{bcrypt}$2a$10$FYZvXtl7Lt8yUI4t/8X7uOmHQcEMv27WRa8EVfSexydCWHajrbhZS', '91187283' );
INSERT INTO    USER_ROLES ( user_uid, roles ) VALUES ( '91187283', 'ROLE_USER' );
INSERT INTO    USER_ROLES ( user_uid, roles ) VALUES ( '91187283', 'ROLE_ADMIN' );

-- used for oauth2
DROP TABLE IF EXISTS oauth_client_details;
create table oauth_client_details ( client_id VARCHAR(256) PRIMARY KEY, resource_ids VARCHAR(256), client_secret VARCHAR(256), scope VARCHAR(256), authorized_grant_types VARCHAR(256), web_server_redirect_uri VARCHAR(256), authorities VARCHAR(256), access_token_validity INTEGER, refresh_token_validity INTEGER, additional_information VARCHAR(4096), autoapprove VARCHAR(256) );
INSERT INTO    oauth_client_details ( client_id, client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove ) VALUES ( 'auth_id', '{noop}auth_secret', NULL, 'read,write', 'authorization_code,password,client_credentials,implicit,refresh_token', NULL, 'ROLE_MY_CLIENT', 36000, 2592000, NULL, NULL );


-- ALIAS
-- DROP alias if exists TO_CHAR;
-- CREATE alias TO_CHAR as '
-- String toChar(String date, String pattern) throws Exception {
--     pattern = pattern.replaceAll("YY","yy");
--     pattern = pattern.replaceAll("DD","dd");
--     pattern = pattern.replaceAll("HH24|hh24","HH");
--     pattern = pattern.replaceAll("HH?!24|hh?!24","KK");
--     pattern = pattern.replaceAll("MON|mon","MMM");
--     pattern = pattern.replaceAll("MI|mi","mm");
--     pattern = pattern.replaceAll("SS|ss","ss");
--     pattern = pattern.replaceAll("AM|PM","aa");
--     pattern = pattern.replaceAll("T","");
--     java.text.SimpleDateFormat sm = new java.text.SimpleDateFormat(pattern);
--     java.util.Date dt;
--     if(date.length() > 10) {
--         date = date.toString().substring(0, 19);
--         dt = java.sql.Timestamp.valueOf(date);
--     } else {
--         dt = java.sql.Date.valueOf(date);
--     }
--     return sm.format(dt);
-- }';