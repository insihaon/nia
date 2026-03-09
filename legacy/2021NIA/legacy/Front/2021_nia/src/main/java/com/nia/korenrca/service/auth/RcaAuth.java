package com.nia.korenrca.service.auth;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.jdbc.JDBCHashStrategy;


/**
 * Factory interface for creating {@link io.vertx.ext.auth.AuthProvider} instances that use the
 * Vert.x JDBC client
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public interface RcaAuth extends AuthProvider {

  /**
   * The default query to be used for authentication
   */
  String DEFAULT_AUTHENTICATE_QUERY = "SELECT PW FROM tb_user WHERE ID = ?";

  /**
   * Set the authentication query to use. Use this if you want to override the default
   * authentication query.
   * 
   * @param authenticationQuery the authentication query
   * @return a reference to this for fluency
   */
  RcaAuth setAuthenticationQuery(String authenticationQuery);

  /**
   * Set the roles query to use. Use this if you want to override the default roles query.
   * 
   * @param rolesQuery the roles query
   * @return a reference to this for fluency
   */
  RcaAuth setRolesQuery(String rolesQuery);

  /**
   * Set the permissions query to use. Use this if you want to override the default permissions
   * query.
   * 
   * @param permissionsQuery the permissions query
   * @return a reference to this for fluency
   */
  RcaAuth setPermissionsQuery(String permissionsQuery);

  /**
   * Set the role prefix to distinguish from permissions when checking for isPermitted requests.
   * 
   * @param rolePrefix a Prefix e.g.: "role:"
   * @return a reference to this for fluency
   */
  RcaAuth setRolePrefix(String rolePrefix);

  /**
   * Set the hash strategy to use. Use this if you want override the default hash strategy
   * 
   * @param strategy the strategy
   * @return a reference to this for fluency
   */
  RcaAuth setHashStrategy(JDBCHashStrategy strategy);

  /**
   * Compute the hashed password given the unhashed password and the salt without nonce
   *
   * The implementation relays to the JDBCHashStrategy provided.
   *
   * @param password the unhashed password
   * @param salt the salt
   * @return the hashed password
   */
  default String computeHash(String password, String salt) {
    return computeHash(password, salt, -1);
  }

  /**
   * Compute the hashed password given the unhashed password and the salt
   *
   * The implementation relays to the JDBCHashStrategy provided.
   *
   * @param password the unhashed password
   * @param salt the salt
   * @param version the nonce version to use
   * @return the hashed password
   */
  String computeHash(String password, String salt, int version);

  /**
   * Compute a salt string.
   *
   * The implementation relays to the JDBCHashStrategy provided.
   *
   * @return a non null salt value
   */
  String generateSalt();

  /**
   * Provide a application configuration level on hash nonce's as a ordered list of nonces where
   * each position corresponds to a version.
   *
   * The nonces are supposed not to be stored in the underlying jdbc storage but to be provided as a
   * application configuration. The idea is to add one extra variable to the hash function in order
   * to make breaking the passwords using rainbow tables or precomputed hashes harder. Leaving the
   * attacker only with the brute force approach.
   *
   * The implementation relays to the JDBCHashStrategy provided.
   *
   * @param nonces a List of non null Strings.
   * @return a reference to this for fluency
   */
  RcaAuth setNonces(JsonArray nonces);
}
