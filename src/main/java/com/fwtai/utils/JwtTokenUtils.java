package com.fwtai.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtils {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String SECRET = "jwtsecretdemo";
    private static final String ISS = "echisan";

    // 角色的key
    private static final String ROLE_CLAIMS = "rol";
    
    public static final String SUBJECT = "congge";

    // 过期时间是3600秒，既是1个小时
    private static final long EXPIRATION = 3600L;

    // 选择了记住我之后的过期时间为7天
    private static final long EXPIRATION_REMEMBER = 604800L;

    // 创建token
    public static String createToken(String username,String role) {
    	
        //long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
    	
    	long expiration = EXPIRATION_REMEMBER ;
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(ROLE_CLAIMS, role);
        
        return 
        		Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setClaims(map)
                .setIssuer(ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
        		
        		/*Jwts
        		.builder()
        		.setSubject(SUBJECT)
        		.claim("name", username)
                .setClaims(map)
                //.setIssuer(ISS)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                //.signWith(SignatureAlgorithm.HS256, SECRET)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();*/
        
    }

    // 从token中获取用户名
    public static String getUsername(String token){
        //return getTokenBody(token).getSubject();
    	
    	Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    	return claims.get("name").toString();
    	
    }

    // 获取用户角色
    public static String getUserRole(String token){
        return (String) getTokenBody(token).get(ROLE_CLAIMS);
    }

    // 是否已过期
    public static boolean isExpiration(String token){
        return getTokenBody(token).getExpiration().before(new Date());
    }

    private static Claims getTokenBody(String token){
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}