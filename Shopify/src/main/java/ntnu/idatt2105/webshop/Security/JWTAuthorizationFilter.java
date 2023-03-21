package ntnu.idatt2105.webshop.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import ntnu.idatt2105.webshop.controller.RestApiController;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    /**
     * The header name where the JWT token is expected to be present
     */

    private final String HEADER = "Authorization";


    /**
     * Overridden method which performs necessary checks for authorization
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param filterChain FilterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // try to extract JWT from the header
        try {
            // generate key from the key string
            Key key = Keys.hmacShaKeyFor(RestApiController.keyStr.getBytes("UTF-8"));

            // retrieve the authorization header
            String authenticationHeader = request.getHeader(HEADER);
            final String PREFIX = "Bearer ";

            // check if the header exists and starts with the expected prefix
            if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX)){
                // clear the security context if the header is not present
                SecurityContextHolder.clearContext();
            } else {
                // extract the JWT token from the header
                String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");

                // parse the claims from the JWT
                Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken);

                // check if the token has the necessary authorities claim
                if (claims.getBody().get("authorities") != null) {
                    // extract the authorities from the claims
                    List<String> authorities = (List) claims.getBody().get("authorities");

                    // create an authentication token with the subject and authorities
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getBody().getSubject(), null,
                            authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

                    // set the authentication in the security context
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    // clear the security context if the authorities claim is not present
                    SecurityContextHolder.clearContext();
                }
            }

            // continue with the filter chain
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            // return a forbidden status and send an error message
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            return;
        }

    }
}