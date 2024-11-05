package com.assessment.vending.config;

import com.assessment.vending.database.entity.UserEntity;
import com.assessment.vending.dto.response.BaseStandardResponse;
import com.assessment.vending.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.assessment.vending.util.ResponseCode.SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendingUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BaseStandardResponse<UserEntity> userEntityBaseStandardResponse
                = userService.getUser(username);
        if (!SUCCESS.getCode().equals(userEntityBaseStandardResponse.getResponseCode())) {
            throw new UsernameNotFoundException(username);
        }
        UserEntity userEntity = userEntityBaseStandardResponse.getData();
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userEntity.getRole()));
        return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
    }
}
