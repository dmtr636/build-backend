package com.kydas.build.account;

import com.kydas.build.security.SecurityTestUtils;
import com.kydas.build.users.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;

import static com.kydas.build.AssertUtils.assertDTO;
import static com.kydas.build.core.endpoints.Endpoints.ACCOUNT_ENDPOINT;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SecurityTestUtils securityTestUtils;

    @BeforeEach
    void authenticateRestTemplate() {
        securityTestUtils.authenticateRestTemplateAsRootUser(restTemplate);
    }

    @Test
    void getCurrentUserInfo() {
        var response = restTemplate.getForEntity(
            ACCOUNT_ENDPOINT,
            UserDTO.class
        );
        assertDTO(response, securityTestUtils.getRootUserDTO());
    }
}

