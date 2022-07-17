package org.infoshare.rekinyfinansjeryweb.service;

import org.infoshare.rekinyfinansjeryweb.dto.user.UserAdminPanelDTO;
import org.infoshare.rekinyfinansjeryweb.dto.user.UserDTO;
import org.infoshare.rekinyfinansjeryweb.entity.user.User;
import org.infoshare.rekinyfinansjeryweb.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminPanelService {

    UserRepository userRepository;
    ModelMapper modelMapper;

    public AdminPanelService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<UserAdminPanelDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserAdminPanelDTO.class))
                .collect(Collectors.toList());
    }

    public boolean deleteUser(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail());
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    public void editUser(UserAdminPanelDTO userAdminPanelDTO) {
        UUID uuid = UUID.fromString(userAdminPanelDTO.getId());
        if (userExists(uuid)) {
            User user = userRepository.findById(uuid);
            saveUser(user, userAdminPanelDTO);
        }
    }

    public boolean userExists(UUID uuid) {
        return userRepository.findById(uuid) != null;
    }

    public boolean usersMailExists(String email, String id) {
        UUID uuid = UUID.fromString(id);
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return userExists(uuid) && !uuid.toString().equals(user.getId().toString());
        }
        return false;
    }

    private void saveUser(User user, UserAdminPanelDTO userAdminPanelDTO) {
        user.setEmail(userAdminPanelDTO.getEmail());
        user.setName(userAdminPanelDTO.getName());
        user.setLastname(userAdminPanelDTO.getLastname());
        user.setBillingCurrency(String.valueOf(userAdminPanelDTO.getBillingCurrency()));
        user.setRole(userAdminPanelDTO.getRole());
        user.setEnabled(userAdminPanelDTO.isEnabled());
        userRepository.save(user);
    }
}
