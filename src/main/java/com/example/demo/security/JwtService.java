package com.example.demo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
  private final String secretKey = "code-secret-pour-la-gestion-de-rdv-fait-par-dorian-florian-herve-et-christian";

  public String generateToken(UserDetails user) {
    return Jwts.builder()
      .setSubject(user.getUsername())
      .claim("roles", user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority).toList())
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60))
      .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
      .compact();
  }

  public String extractUsername(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(secretKey.getBytes())
      .build()
      .parseClaimsJws(token)
      .getBody()
      .getSubject();
  }
}
