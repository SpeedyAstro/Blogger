package in.astro.service;

import in.astro.entity.User;
import in.astro.model.UserForm;

import java.util.List;

public interface UserService {
    User saveUser(UserForm user);
    User getUserById(Long id);
    User updateUser(UserForm user);
    void deleteUser(Long id);
    boolean isUserExist(String email);
    List<User> getAllUsers();
    User getUserByEmail(String email);
}
