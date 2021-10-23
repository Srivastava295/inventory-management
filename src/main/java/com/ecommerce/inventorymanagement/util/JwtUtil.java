package com.ecommerce.inventorymanagement.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ecommerce.inventorymanagement.constants.Constants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JwtUtil {

	private static final Logger LOG = Logger.getLogger(JwtUtil.class.getName());

	public static String generateToken(Map<String, Object> data, String secret) {
		return generateToken(data, secret, 600000);
	}

	public static String generateToken(final Map<String, Object> data, String secret, long expiryTime) {
		JwtBuilder builder = Jwts.builder().setClaims(data).setIssuedAt(new Date());
		if (expiryTime > 0) {
			long currentTime = Calendar.getInstance(TimeZone.getTimeZone(Constants.TIME_ZONE_UTC)).getTimeInMillis();
			long expiry = currentTime + expiryTime;
			builder.setExpiration(new Date(expiry));
		}
		return builder.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public static Map<String, Object> getClaimsFromJWT(String token, String secret) {
		Claims claims = null;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException | SignatureException e) {
			LOG.log(Level.SEVERE, "Exception while decrypting jwt : ", e.getMessage());
		}
		return claims;
	}

	public static boolean isExpired(Map<String, Object> data) {
		if (data.containsKey(Constants.EXPIRY)) {
			Integer exp = (Integer) data.get(Constants.EXPIRY);
			long expiry = (long) exp;
			long currentTime = Calendar.getInstance(TimeZone.getTimeZone(Constants.TIME_ZONE_UTC)).getTimeInMillis();
			return currentTime > (expiry * 1000);
		}
		return false;
	}
}
