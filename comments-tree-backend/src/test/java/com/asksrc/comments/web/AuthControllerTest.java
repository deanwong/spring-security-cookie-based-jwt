package com.asksrc.comments.web;

import com.asksrc.comments.BaseTestSuite;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest extends BaseTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @Value("${app.jwtCookieName}")
    private String jwtCookieName;

    final private static String JSESSIONID = "JSESSIONID";

    @Test
    @Order(1)
    public void testLogin_rememberMe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .content("{\"loginId\":\"foobar\",\"password\":\"foobar\", \"rememberMe\":\"true\"}")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().httpOnly(jwtCookieName, true))
                .andExpect(MockMvcResultMatchers.jsonPath("username", Matchers.is("foobar")))
                .andExpect(MockMvcResultMatchers.jsonPath("email", Matchers.is("foo@bar.com")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(20)
    public void testLogout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/logout")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().value(jwtCookieName, Matchers.emptyOrNullString()))
                .andExpect(MockMvcResultMatchers.cookie().value(JSESSIONID, Matchers.emptyOrNullString()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(21)
    public void testLogin_without_rememberMe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .content("{\"loginId\":\"foobar\",\"password\":\"foobar\", \"rememberMe\":\"false\"}")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().doesNotExist(jwtCookieName))
                .andExpect(MockMvcResultMatchers.jsonPath("username", Matchers.is("foobar")))
                .andExpect(MockMvcResultMatchers.jsonPath("email", Matchers.is("foo@bar.com")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(30)
    public void testRegister() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .content("{\"username\":\"tester1\",\"password\":\"AAAbbbccc@123\",\"email\":\"tester1@bar.com\"}")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("username", Matchers.is("tester1")))
                .andExpect(MockMvcResultMatchers.jsonPath("email", Matchers.is("tester1@bar.com")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(40)
    public void testRegister_invalid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .content("{\"username\":\"FOOBAR\",\"password\":\"AAAbbbccc@123\",\"email\":\"FOOBAR@bar.com\"}")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("apierror.errCode", Matchers.is(10004)))
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .content("{\"username\":\"tester2\",\"password\":\"tester1\",\"email\":\"tester2@bar.com\"}")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .content("{\"username\":\"tester2\",\"password\":\"tester1\",\"email\":\"tester2\"}")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }

}
