package com.interview.userregister.service;

import com.interview.userregister.exception.UserRegisterException;
import com.interview.userregister.model.request.UserRequest;
import com.interview.userregister.model.response.UserListResponse;
import com.interview.userregister.model.response.UserResponse;
import com.interview.userregister.repository.UserRegisterRepository;
import com.interview.userregister.repository.dao.RegisterUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserRegisterService {

    private UserRegisterRepository userRegisterRepository;
    private JavaMailSender javaMailSender;

    @Autowired
    public UserRegisterService(UserRegisterRepository userRegisterRepository, JavaMailSender javaMailSender) {
        this.userRegisterRepository = userRegisterRepository;
        this.javaMailSender = javaMailSender;
    }


    public UserResponse addUser(UserRequest user) {
        RegisterUser registerUser = RegisterUser.builder()
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .active(Boolean.TRUE)
                .emailSent(Boolean.TRUE)
                .build();
        userRegisterRepository.save(registerUser);

        try {

            MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
                mimeMessageHelper.setFrom("no-reply@interview.com.sg");
                mimeMessageHelper.setTo(user.getEmail());
                mimeMessageHelper.setSubject("User Registration Successful");
                mimeMessageHelper.setText("Welcome Aboard! " + user.getName(), false);
            };

            javaMailSender.send(mimeMessagePreparator);
            log.info("Sending email is successful");

        } catch (Exception e) {
            log.error("Sending email with exception", e);

            registerUser.setEmailSent(Boolean.FALSE);
            userRegisterRepository.saveAndFlush(registerUser);
        }

        return UserResponse.builder()
                .id(registerUser.getId().toString())
                .build();
    }

    public UserResponse editUser(UserRequest user, String id) {
        Optional<RegisterUser> registerUser = userRegisterRepository.findById(UUID.fromString(id));
        if (!registerUser.isPresent()) throw new UserRegisterException("User not found");

        registerUser.ifPresent(u -> {
            u.setName(user.getName());
            u.setAge(user.getAge());
            u.setActive(user.getActive());
        });

        userRegisterRepository.saveAndFlush(registerUser.get());
        return UserResponse.builder()
                .id(registerUser.get().getId().toString())
                .build();
    }

    public UserResponse deleteUser(String id) {
        Optional<RegisterUser> registerUser = userRegisterRepository.findById(UUID.fromString(id));
        if (!registerUser.isPresent()) throw new UserRegisterException("User not found");

        registerUser.ifPresent(u -> {
            u.setActive(Boolean.FALSE);
        });

        userRegisterRepository.saveAndFlush(registerUser.get());
        return UserResponse.builder()
                .id(registerUser.get().getId().toString())
                .build();
    }

    public UserListResponse deleteSelectedUser(List<String> idList) {
        List<RegisterUser> userList = userRegisterRepository.findAllById(idList.stream().map(id -> UUID.fromString(id)).collect(Collectors.toList()));
        if (userList.isEmpty()) throw new UserRegisterException("User not found");

        userList.forEach(u -> {
            u.setActive(Boolean.FALSE);
        });

        userRegisterRepository.saveAllAndFlush(userList);
        return UserListResponse.builder()
                .userList(userList.stream()
                        .map(u -> UserResponse.builder()
                                .id(u.getId().toString())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public UserResponse getUser(String id) {
        Optional<RegisterUser> registerUser = userRegisterRepository.findById(UUID.fromString(id));
        if (!registerUser.isPresent()) throw new UserRegisterException("User not found");

        return UserResponse.builder()
                .id(registerUser.get().getId().toString())
                .name(registerUser.get().getName())
                .email(registerUser.get().getEmail())
                .age(registerUser.get().getAge())
                .active(registerUser.get().getActive())
                .emailSent(registerUser.get().getEmailSent())
                .build();
    }

    public UserListResponse getAllUser() {
        List<RegisterUser> userList = userRegisterRepository.findAll();
        return UserListResponse.builder()
                .userList(userList.stream()
                        .map(u -> UserResponse.builder()
                                .id(u.getId().toString())
                                .name(u.getName())
                                .email(u.getEmail())
                                .age(u.getAge())
                                .active(u.getActive())
                                .emailSent(u.getEmailSent())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
