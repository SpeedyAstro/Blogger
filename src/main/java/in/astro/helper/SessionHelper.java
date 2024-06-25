package in.astro.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

import java.security.Principal;

@Component
public class SessionHelper {

    public static void removeMessage() {
        try {
            System.out.println("removing message from session");
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                    .getSession();
            session.removeAttribute("message");
        } catch (Exception e) {
            System.out.println("Error in SessionHelper: " + e);
            e.printStackTrace();
        }
    }

    public static String getEmailOfLoggedInUser(Authentication authentication){
        if (authentication instanceof OAuth2AuthenticationToken) {
            var oauth = (OAuth2AuthenticationToken) authentication;
            var clientRegistrationId = oauth.getAuthorizedClientRegistrationId();
            var oauth2User = (OAuth2User) oauth.getPrincipal();
            if (clientRegistrationId.equalsIgnoreCase("google")) {
                return oauth2User.getAttribute("email");
            } else if (clientRegistrationId.equalsIgnoreCase("github")) {
                return oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email")
                        : oauth2User.getAttribute("login")+ "@github.com";
            }
        }
        return authentication.getName();
    }

}