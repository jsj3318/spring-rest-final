package com.nhnacademy.springrestfinal.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails,
            @AuthenticationPrincipal OAuth2User oAuth2User
    ) {
        String name = null;

        if(userDetails != null) {
            name = userDetails.getUsername();
        } else if(oAuth2User != null) {
            log.info(oAuth2User.toString());
            name = oAuth2User.getAttribute("name");
        }

        model.addAttribute("loginName", name);
        return "home";
    }

}
