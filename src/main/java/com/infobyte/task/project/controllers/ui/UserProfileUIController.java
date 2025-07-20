package com.infobyte.task.project.controllers.ui;

import com.infobyte.task.project.dtos.UserProfileDto;
import com.infobyte.task.project.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserProfileUIController {
    @Autowired private UserProfileService userProfileService;
    @GetMapping("/ui/user/profile")
    public String showProfilePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserProfileDto profile = userProfileService.getUserProfile(userDetails.getUsername());
        model.addAttribute("profile", profile);
        return "user/profile";
    }
}
