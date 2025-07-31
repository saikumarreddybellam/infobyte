package com.infobyte.task.project.controllers.ui;

import com.infobyte.task.project.utility.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/ui/dashboard")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class DashboardController {

    @Autowired private JwtTokenService jwtTokenService;
    @GetMapping
    public String showDashboard(Model model,
                                @CookieValue(value = "AUTH_TOKEN", required = false) String token,
                                Principal principal) {

        if (principal != null) {
            model.addAttribute("username", principal.getName());
            return "dashboard";
        }

        if (token == null || !jwtTokenService.isTokenValid(token)) {
            return "redirect:/ui/auth/login";
        }

        model.addAttribute("username", jwtTokenService.extractUsername(token));
        return "dashboard";
    }
}