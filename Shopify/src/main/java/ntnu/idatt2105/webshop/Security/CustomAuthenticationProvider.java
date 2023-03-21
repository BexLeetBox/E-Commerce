package ntnu.idatt2105.webshop.Security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**

 CustomAuthenticationProvider is a class that implements the AuthenticationProvider interface.

 This class is used to provide custom authentication for the application.

 @author

 @version 1.0

 @since 2023-03-20
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    /**
     Constructor for CustomAuthenticationProvider class.
     */
    public CustomAuthenticationProvider() {
        super();
    }

    /**

     Overridden method from the AuthenticationProvider interface. This method is used to authenticate

     the user and password provided in the request.

     @param authentication the authentication object containing user and password information.

     @return the authenticated user information.

     @throws AuthenticationException if an error occurs during authentication.
     */
    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        // get user and password info from the request
        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();

        // check if the user and password are correct
        if (name.equals("admin") && password.equals("password")) {
            // create a list of granted authorities
            final List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("USER"));
            final UserDetails principal = new User(name, password, grantedAuths);
            // return the authenticated user information
            return new UsernamePasswordAuthenticationToken(principal, password, grantedAuths);
        } else {
            // return null if the user and password are incorrect
            return null;
        }
    }

    /**
     Overridden method from the AuthenticationProvider interface. This method is used to check if
     the authentication provider supports the authentication class.
     @param authentication the authentication class to check support for.
     @return true if the authentication provider supports the authentication class, false otherwise.
     */
    @Override
    public boolean supports(final Class<?> authentication) {
        // check if the authentication provider supports the UsernamePasswordAuthenticationToken class
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}