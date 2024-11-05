package com.assessment.vending.service;

import com.assessment.vending.database.entity.UserEntity;
import com.assessment.vending.database.repository.UserRepository;
import com.assessment.vending.dto.request.UserRequest;
import com.assessment.vending.dto.response.BaseCollectionResponse;
import com.assessment.vending.dto.response.BaseStandardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.assessment.vending.util.AppConstants.EM_SAVING_TO_DATABASE;
import static com.assessment.vending.util.ResponseCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public BaseStandardResponse<UserEntity> createUser(UserRequest request) {
        log.info("Creating new userEntity");
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(request, userEntity);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        BaseStandardResponse<UserEntity> reponse;
        try {
            reponse = new BaseStandardResponse<>(userRepository.save(userEntity));
            log.info("Saved new userEntity to the database");
        } catch (Exception ex) {
            log.error(EM_SAVING_TO_DATABASE, ex);
            reponse = new BaseStandardResponse<>(SAVE_TO_DATABASE_ERROR);
        }
        log.info("Final response :: {}", reponse);
        return reponse;
    }

    public BaseStandardResponse<UserEntity> updateUser(UserRequest request) {
        log.info("Updating existing user with username {}", request.getUsername());
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(request.getUsername());
        if (optionalUserEntity.isEmpty()) {
            log.info("Username is not found in:: {}", request.getUsername());
            return new BaseStandardResponse<>(UNABLE_TO_LOCATE_RECORD);
        }

        UserEntity userEntity = optionalUserEntity.get();
        BeanUtils.copyProperties(request, userEntity);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        BaseStandardResponse<UserEntity> reponse;
        try {
            reponse = new BaseStandardResponse<>(userRepository.save(userEntity));
            log.info("Updated user in the database");
        } catch (Exception ex) {
            log.error(EM_SAVING_TO_DATABASE, ex);
            reponse = new BaseStandardResponse<>(SAVE_TO_DATABASE_ERROR);
        }
        log.info("Final response :: {}", reponse);
        return reponse;
    }

    public BaseCollectionResponse<UserEntity> getUsers() {
        log.info("Get all users");
        return new BaseCollectionResponse<>(userRepository.findAll());
    }

    public BaseStandardResponse<UserEntity> getUser(String username) {
        log.info("Get user with username {}", username);
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);
        return optionalUserEntity
                .map(BaseStandardResponse::new)
                .orElseGet(() -> new BaseStandardResponse<>(UNABLE_TO_LOCATE_RECORD));
    }

    public BaseStandardResponse<UserEntity> deleteUser(String username) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);
        if (optionalUserEntity.isEmpty()) {
            return new BaseStandardResponse<>(UNABLE_TO_LOCATE_RECORD);
        }
        userRepository.deleteById(optionalUserEntity.get().getId());
        log.info("Deleted user with username {}", username);
        return new BaseStandardResponse<>(SUCCESS);
    }

}
