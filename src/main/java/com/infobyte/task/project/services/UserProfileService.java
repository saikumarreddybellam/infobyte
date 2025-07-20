package com.infobyte.task.project.services;

import com.infobyte.task.project.dtos.ProfileUpdateRequest;
import com.infobyte.task.project.dtos.UserProfileDto;

public interface UserProfileService {
    UserProfileDto getUserProfile(String username);
    void updateUserProfile(String username, ProfileUpdateRequest request);
}