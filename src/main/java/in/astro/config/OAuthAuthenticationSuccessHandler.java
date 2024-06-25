package in.astro.config;

import in.astro.entity.Providers;
import in.astro.entity.User;
import in.astro.helper.AppConstant;
import in.astro.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserRepository userRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var oauth2AuthenticationToken =  (OAuth2AuthenticationToken)authentication;
        String clientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User)authentication.getPrincipal();
        User user = User.builder()
                .enabled(true)
                .emailVerified(true)
                .roleList(new ArrayList<>(List.of(AppConstant.USER_ROLE)))
                .build();
        if (clientRegistrationId.equalsIgnoreCase("google")){
            user.setEmail(oAuth2User.getAttribute("email")!=null?oAuth2User.getAttribute("email"):"");
            user.setFirstName(oAuth2User.getAttribute("name")!=null?oAuth2User.getAttribute("name"):"");
            user.setProfilePic(oAuth2User.getAttribute("picture")!=null?oAuth2User.getAttribute("picture"):"");
            user.setProvider(Providers.GOOGLE);
            user.setAbout("Account Created using Google OAuth2");
        } else if (clientRegistrationId.equalsIgnoreCase("github")){
            user.setEmail(oAuth2User.getAttribute("email")!=null?oAuth2User.getAttribute("email"):oAuth2User.getAttribute("login")+ "@github.com");
            user.setFirstName(oAuth2User.getAttribute("login")!=null?oAuth2User.getAttribute("login"):"");
            user.setProfilePic(oAuth2User.getAttribute("avatar_url")!=null?oAuth2User.getAttribute("avatar_url"):"");
            user.setProvider(Providers.GITHUB);
            user.setAbout("Account Created using Github OAuth2");
        }
        userRepository.findByEmail(user.getEmail()).ifPresentOrElse(
                user1 -> {
                    user.setUserId(user1.getUserId());
                    userRepository.save(user);
                },
                ()->{
                    userRepository.save(user);
                }
        );
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }
}
