package co.com.pragma.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private static final Logger LOGGER = Logger.getLogger(JwtProvider.class.getName());

  @Value("${jwt.secret}")
  private String secret;
  @Value("${jwt.expiration}")
  private Integer expiration;

  public Claims getClaims(String token) {
    validate(token);
    return Jwts.parser()
        .verifyWith(getKey(secret))
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public String getSubject(String token) {
    return Jwts.parser()
        .verifyWith(getKey(secret))
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public void validate(String token) {
    try {
      Jwts.parser().verifyWith(getKey(secret)).build().parseSignedClaims(token).getPayload()
          .getSubject();
    } catch (ExpiredJwtException e) {
      LOGGER.severe("token expired");
    } catch (UnsupportedJwtException e) {
      LOGGER.severe("token unsupported");
    } catch (MalformedJwtException e) {
      LOGGER.severe("token malformed");
    } catch (SignatureException e) {
      LOGGER.severe("bad signature");
    } catch (IllegalArgumentException e) {
      LOGGER.severe("illegal args");
    }
  }

  private SecretKey getKey(String secret) {
    byte[] secretBytes = Decoders.BASE64URL.decode(secret);
    return Keys.hmacShaKeyFor(secretBytes);
  }
}

