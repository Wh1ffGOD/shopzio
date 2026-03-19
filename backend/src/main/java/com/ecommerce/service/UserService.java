package com.ecommerce.service;

import com.ecommerce.dto.ChangePasswordRequest;
import com.ecommerce.dto.ProfileResponse;
import com.ecommerce.dto.UpdateProfileRequest;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ── Get own profile ───────────────────────────────────────────────────────
    public ProfileResponse getProfile() {
        return ProfileResponse.from(getCurrentUser());
    }

    // ── Update own profile ────────────────────────────────────────────────────
    public ProfileResponse updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUser();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        return ProfileResponse.from(userRepository.save(user));
    }

    // ── Change password ───────────────────────────────────────────────────────
    public void changePassword(ChangePasswordRequest request) {
        User user = getCurrentUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    // ── Admin: get all users ──────────────────────────────────────────────────
    public List<ProfileResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(ProfileResponse::from)
                .toList();
    }

    // ── Admin: get user by ID ─────────────────────────────────────────────────
    public ProfileResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return ProfileResponse.from(user);
    }

    // ── Admin: deactivate a user (set role to a locked state) ────────────────
    // We don't hard-delete users to preserve order history
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));

        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user.getEmail().equals(currentEmail)) {
            throw new RuntimeException("You cannot deactivate your own account");
        }

        // Anonymise the user instead of hard delete — preserves order history
        user.setFirstName("Deactivated");
        user.setLastName("User");
        user.setPhone(null);
        user.setAddress(null);
        userRepository.save(user);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
