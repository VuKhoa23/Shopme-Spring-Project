package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {
    public static final int PAGE_SIZE = 4;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listAll() {
        return (List<User>) userRepository.findAll();
    }

    public Page<User> listByPage(int pageNumber, String sortField, String sortOrder, String keyWord){
        Sort sort = Sort.by(sortField);
        sort = sortOrder.equals("asc") ? sort.ascending() : sort.descending();
        // list number is 0-based
        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE, sort);

        if(keyWord != null){
            return userRepository.findAllByKeyWord(keyWord, pageable);
        }

        return userRepository.findAll(pageable);
    }

    public List<Role> listRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    public User save(User user) {
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
        return userRepository.save(user);
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
        // if the user keep the old password. Return true
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

    public void updateUserEnabledStatus(Integer id, boolean enabled){
        userRepository.updateUserEnabledStatus(id, enabled);
    }

    public User getUserByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

    // update information for logged in user
    public User updateAccount(User userInForm){
        User userInDb = userRepository.findById(userInForm.getId()).get();
        if(!userInForm.getPassword().isEmpty()){
            userInDb.setPassword(userInForm.getPassword());
            encodeUserPassword(userInDb);
        }
        if(userInForm.getPhotos() != null){
            userInDb.setPhotos(userInForm.getPhotos());
        }
        userInDb.setFirstName(userInForm.getFirstName());
        userInDb.setLastName(userInForm.getLastName());

        return userRepository.save(userInDb);
    }

}
