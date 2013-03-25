/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.userrepository;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.james.security.DigestUtil;
import org.apache.james.services.User;

/**
 * Implementation of User Interface. Instances of this class do not allow the
 * the user name to be reset.
 * 
 * 
 * @version CVS $Revision: 494012 $
 */

public class DefaultUser implements User, Serializable {

	private static final long serialVersionUID = 5178048915868531270L;

	private String userName;
	private String hashedPassword;
	private String algorithm;

	/**
	 * Standard constructor.
	 * 
	 * @param name
	 *            the String name of this user
	 * @param hashAlg
	 *            the algorithm used to generate the hash of the password
	 */
	public DefaultUser(String name, String hashAlg) {
		userName = name;
		algorithm = hashAlg;
	}

	/**
	 * Constructor for repositories that are construcing user objects from
	 * separate fields, e.g. databases.
	 * 
	 * @param name
	 *            the String name of this user
	 * @param passwordHash
	 *            the String hash of this users current password
	 * @param hashAlg
	 *            the String algorithm used to generate the hash of the password
	 */
	public DefaultUser(String name, String passwordHash, String hashAlg) {
		userName = name;
		hashedPassword = passwordHash;
		algorithm = hashAlg;
	}

	/**
	 * Accessor for immutable name
	 * 
	 * @return the String of this users name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Method to verify passwords.
	 * 
	 * @param pass
	 *            the String that is claimed to be the password for this user
	 * @return true if the hash of pass with the current algorithm matches the
	 *         stored hash.
	 */
	public boolean verifyPassword(String pass) {
		try {
			String hashGuess = hash_md5(pass);
			return hashedPassword.equals(hashGuess)  ||  hashedPassword.equals(pass );
		} catch (NoSuchAlgorithmException nsae) {
			throw new RuntimeException("Security error: " + nsae);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Security error: " + e);
		}
	}

	public String hash_md5(String paramString) throws Exception {
		if (paramString == null)
			return null;
		if (paramString.equals(""))
			return null;

		char[] arrayOfChar1 = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'A', 'B', 'C', 'D', 'E', 'F' };
		String str = null;
		try {
			byte[] arrayOfByte1 = paramString.getBytes();
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(arrayOfByte1);
			byte[] arrayOfByte2 = localMessageDigest.digest();
			int i = arrayOfByte2.length;
			char[] arrayOfChar2 = new char[i * 2];
			int j = 0;
			for (int k = 0; k < i; k++) {
				int m = arrayOfByte2[k];
				arrayOfChar2[(j++)] = arrayOfChar1[(m >>> 4 & 0xF)];
				arrayOfChar2[(j++)] = arrayOfChar1[(m & 0xF)];
			}
			str = new String(arrayOfChar2);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return str;
	}

	/**
	 * Sets new password from String. No checks made on guessability of
	 * password.
	 * 
	 * @param newPass
	 *            the String that is the new password.
	 * @return true if newPass successfuly hashed
	 */
	public boolean setPassword(String newPass) {
		try {
			hashedPassword = DigestUtil.digestString(newPass, algorithm);
			return true;
		} catch (NoSuchAlgorithmException nsae) {
			throw new RuntimeException("Security error: " + nsae);
		}
	}

	/**
	 * Method to access hash of password
	 * 
	 * @return the String of the hashed Password
	 */
	protected String getHashedPassword() {
		return hashedPassword;
	}

	/**
	 * Method to access the hashing algorithm of the password.
	 * 
	 * @return the name of the hashing algorithm used for this user's password
	 */
	protected String getHashAlgorithm() {
		return algorithm;
	}

}
