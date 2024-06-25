package in.astro.service.impl;

import in.astro.entity.User;
import in.astro.exception.ResourceNotFoundException;
import in.astro.helper.AppConstant;
import in.astro.model.UserDTO;
import in.astro.model.UserForm;
import in.astro.repository.UserRepository;
import in.astro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${app.profile.pic}")
    private String profilePic ;

    @Override
    public User saveUser(UserForm user) {
        if (isUserExist(user.getEmail())) return null;
        User user1 =  modelMapper.map(user, User.class);
        user1.setPassword(passwordEncoder.encode(user.getPassword()));
        user1.setRoleList(List.of(AppConstant.USER_ROLE));
        user1.setProfilePic(profilePic);
        return userRepository.save(user1);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    public User updateUser(UserForm user) {
        User user1 = userRepository.findById(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", user.getUserId()));
        BeanUtils.copyProperties(user, user1);
        return userRepository.save(user1);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
    }

    @Override
    public boolean isUserExist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public UserDTO entityToDto(User user) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }
}
