package it.corso.jwt;

import java.io.IOException;
import java.security.Key;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.corso.service.Blacklist;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@JWTTokenNeeded
@Provider
public class JWTTokenNeededFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;
    
    @Autowired
	private Blacklist blacklist;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Secured annotationRole = resourceInfo.getResourceMethod().getAnnotation(Secured.class);

        if (annotationRole == null) {
            annotationRole = resourceInfo.getResourceClass().getAnnotation(Secured.class);
        }

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        String token = authorizationHeader.substring("Bearer".length()).trim();
        
        if (blacklist.isTokenRevoked(token)) {
        	throw new NotAuthorizedException("Blacklisted token");
        }

        try {
            byte[] secretKey = "efhsrehfsehhfiosejfijseiofjiosejfios".getBytes();
            Key key = Keys.hmacShaKeyFor(secretKey);

            // Decodifica e verifica il token JWT
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            Claims body = claims.getBody();

            List<String> rolesToken = body.get("ruoli", List.class);

            Boolean hasRole = false;
            for (String role : rolesToken) {
                if (role.equals(annotationRole.role())) {
                    hasRole = true;
                    break;
                }
            }

            if (!hasRole) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }

        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
