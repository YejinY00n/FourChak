package org.example.fourchak.domain.user.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.fourchak.config.TestSecurityConfig;
import org.example.fourchak.config.security.CustomUserPrincipal;
import org.example.fourchak.domain.user.dto.request.UserPasswordRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String PASSWORD = "1234";

    CustomUserPrincipal mockPrincipal = Mockito.mock(CustomUserPrincipal.class);

    @BeforeEach
    void setUp() {
        // Mock 설정 - 원하는 ID나 이메일 설정
        Mockito.when(mockPrincipal.getId()).thenReturn(1_000_000L);
        Mockito.when(mockPrincipal.getUsername()).thenReturn("email1000000");
        Mockito.when(mockPrincipal.getPassword()).thenReturn(PASSWORD);
    }

    @Test
    void getMyInfo_기존형식데이터읽기() throws Exception {
        UserPasswordRequest request = new UserPasswordRequest(PASSWORD);
        String json = objectMapper.writeValueAsString(request);

        long start = System.currentTimeMillis();

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .principal(mockPrincipal))
            .andExpect(status().isOk());

        long end = System.currentTimeMillis();
        System.out.println("이메일 기반 조회 시간: " + (end - start) + "ms");
    }

    @Test
    void getMyInfoFindIndex_인덱스형식데이터읽기() throws Exception {
        UserPasswordRequest request = new UserPasswordRequest(PASSWORD);
        String json = objectMapper.writeValueAsString(request);

        long start = System.currentTimeMillis();

        mockMvc.perform(MockMvcRequestBuilders.get("/users/indexing")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .principal(mockPrincipal))
            .andExpect(status().isOk());

        long end = System.currentTimeMillis();
        System.out.println("ID 인덱스 기반 조회 시간: " + (end - start) + "ms");
    }
}