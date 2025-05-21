package org.example.fourchak.domain.user.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.fourchak.config.TestSecurityConfig;
import org.example.fourchak.config.security.CustomUserPrincipal;
import org.example.fourchak.domain.user.dto.request.UserPasswordRequest;
import org.example.fourchak.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private CustomUserPrincipal testPrincipal;

    @BeforeEach
    void setUp() {
        // 100만 번째 사용자에 대한 가짜 principal 생성
        // 실제 DB에는 이 유저가 존재해야 함 (id: 1000000, email: user1000000@example.com)

        User fakeUser = new User(
            "email1000",
            "username1000",
            "01012345678",
            passwordEncoder.encode("1234"), // 실제 비밀번호가 암호화되어 있어야 함
            "USER"
        );
        fakeUser.setId(1000L); // ID 기반으로 조회할 수 있도록 설정
        testPrincipal = new CustomUserPrincipal(fakeUser);
    }

    @Test
    void getMyInfo_기존형식데이터읽기() throws Exception {
        UserPasswordRequest request = new UserPasswordRequest("1234");

        long start = System.currentTimeMillis();

        mockMvc.perform(get("/users")
                .with(user(testPrincipal))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isOk());

        long end = System.currentTimeMillis();
        System.out.println("이메일 기반 조회 시간: " + (end - start) + " ms");
    }

    @Test
    void getMyInfoFindIndex_인덱스형식데이터읽기() throws Exception {
        UserPasswordRequest request = new UserPasswordRequest("1234");

        long start = System.currentTimeMillis();

        mockMvc.perform(get("/users/indexing")
                .with(user(testPrincipal))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isOk());

        long end = System.currentTimeMillis();
        System.out.println("ID 기반 조회 시간: " + (end - start) + " ms");
    }
}