package com.infobyte.task.project.controllers.ui;

import com.infobyte.task.project.dtos.LoginRequest;
import com.infobyte.task.project.dtos.LoginResponse;
import com.infobyte.task.project.dtos.RegisterRequest;
import com.infobyte.task.project.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ui/auth")
@RequiredArgsConstructor
public class AuthUiController {

    private final AuthService authService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute LoginRequest request,
                               Model model,
                               RedirectAttributes redirectAttributes,
                               HttpServletResponse response) {
        try {
            LoginResponse loginResponse = authService.login(request);

            // Set cookie
            Cookie cookie = new Cookie("AUTH_TOKEN", loginResponse.getToken());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24); // 1 day
            response.addCookie(cookie);

            redirectAttributes.addFlashAttribute("username", request.getUsername());
            return "redirect:/ui/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid credentials");
            return "auth/login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute RegisterRequest request, Model model) {
        try {
            authService.register(request);
            return "redirect:/ui/auth/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model) {
        try {
            authService.initiatePasswordReset(email);
            model.addAttribute("message", "Reset link sent to your email");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "auth/forgot-password";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // Clear the auth cookie
        Cookie cookie = new Cookie("AUTH_TOKEN", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/ui/auth/login";
    }
}