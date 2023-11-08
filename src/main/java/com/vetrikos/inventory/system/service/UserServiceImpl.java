package com.vetrikos.inventory.system.service;

import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.model.UserRestDTO;
import com.vetrikos.inventory.system.repository.UserRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    @NonNull
    public User findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        UserService.userNotFoundMessage(userId)));
    }

    @Override
    @NonNull
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @NonNull
    @Transactional
    public User createUser(UserRestDTO requestRestDTO) {

        User user = User.builder()
                .username(requestRestDTO.getUsername())
                .fullName(requestRestDTO.getFullName())
                .build();
        return userRepository.save(user);
    }

    @Override
    @NonNull
    @Transactional
    public User updateUser(UUID userId, UserRestDTO updateRequestRestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        UserService.userNotFoundMessage(userId)));

        user.setUsername(updateRequestRestDTO.getUsername());
        user.setFullName(updateRequestRestDTO.getFullName());

        return user;
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        UserService.userNotFoundMessage(userId)));
        userRepository.delete(user);
    }
}
