package com.vetrikos.inventory.system.service;

import com.vetrikos.inventory.system.entity.User;
import com.vetrikos.inventory.system.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  @NonNull
  public User findById(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException(
            UserService.userNotFoundMessage(userId)));
  }

  @Override
  public Boolean existsById(UUID userId) {
    return userRepository.existsById(userId);
  }

  @Override
  @NonNull
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  @NonNull
  @Transactional
  public User createUser(User userRequest) {
    return userRepository.save(userRequest);
  }

  @Override
  @NonNull
  @Transactional
  public User updateUser(UUID userId, User userRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException(
            UserService.userNotFoundMessage(userId)));

    user.setFullName(userRequest.getFullName());
    user.setRoles(userRequest.getRoles());

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
