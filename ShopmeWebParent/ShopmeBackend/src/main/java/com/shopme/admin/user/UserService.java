package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listAll() {
        return (List<User>) userRepository.findAll();
    }

    public List<Role> listRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    public void save(User user) {
        if(user.getId()!=null){
            if (user.getPassword().isEmpty()) {
                User existingUser = userRepository.findById(user.getId()).get();
                user.setPassword(existingUser.getPassword());
            }
            else{
                encodeUserPassword(user);
            }
        }
        else{
            encodeUserPassword(user);
        }
        userRepository.save(user);
    }

    private void encodeUserPassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isUniqueEmail(Integer id, String email) {
        User user = userRepository.getUserByEmail(email);
        if(user == null){
            return true;
        }
        if(id == null){
            return false;
        }
        // if the email is used by another user. The email is not unique
        else return user.getId().intValue() == id.intValue();
    }

    public User get(Integer id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        } catch(NoSuchElementException e){
            throw new UserNotFoundException("Could not found user with ID: " + id);
        }
    }

    public void deleteById(Integer id) throws UserNotFoundException {
        try {
            User user = userRepository.findById(id).get();
            userRepository.delete(user);
        } catch(NoSuchElementException e){
            throw new UserNotFoundException("Could not found user with ID: " + id);
        }
    }
}
