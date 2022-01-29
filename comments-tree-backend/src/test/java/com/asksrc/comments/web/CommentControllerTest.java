package com.asksrc.comments.web;

import com.asksrc.comments.BaseTestSuite;
import com.asksrc.comments.entity.Comment;
import com.asksrc.comments.repo.CommentRepository;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.StopWatch;

import javax.servlet.http.Cookie;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentControllerTest extends BaseTestSuite {

    @Value("${app.jwtCookieName}")
    private String jwtCookieName;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @Order(1)
    public void testList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/comments")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(2)
    public void testAdd_without_login() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comments/0")
                        .content("{\"pid\":6,\"content\":\"level 0-1-1-1-1-2\"}")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(20)
    public void testAdd() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .content("{\"loginId\":\"foobar\",\"password\":\"foobar\", \"rememberMe\":\"true\"}")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().httpOnly(jwtCookieName, true))
                .andExpect(MockMvcResultMatchers.jsonPath("username", Matchers.is("foobar")))
                .andExpect(MockMvcResultMatchers.jsonPath("email", Matchers.is("foo@bar.com")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        Cookie cookie = new Cookie(jwtCookieName, Objects.requireNonNull(result.getResponse().getCookie(jwtCookieName)).getValue());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comments/0")
                        .content("{\"pid\":6,\"content\":\"level 0-1-1-1-1-2\"}")
                        .contentType(APPLICATION_JSON).cookie(cookie))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("content", Matchers.is("level 0-1-1-1-1-2")))
                .andExpect(MockMvcResultMatchers.jsonPath("aid", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("depth", Matchers.is(5)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Order(30)
    public void testLoopPerf() throws Exception {
        // List<String> sql = new ArrayList<>();
        Comment parent = commentRepository.findById(3L).orElseThrow(IllegalArgumentException::new);
        for (int i = 0; i < 100; i++) {
            Comment entity = new Comment();
            entity.setContent(parent.getContent() + "-1");
            entity.setAid(1L);
            entity.setPid(parent.getId());
            entity.setDepth(parent.getDepth() + 1);
            // sql.add(String.format("(1, %d, '%s', '%d', 1, 1, now(), now())", entity.getPid(), entity.getContent(), entity.getDepth()));
            commentRepository.create(entity);
            parent = commentRepository.findById(entity.getId()).orElseThrow(IllegalArgumentException::new);
        }
        // System.out.println(StringUtils.join(sql, ","));
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/comments")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        stopWatch.stop();
        assertTrue(stopWatch.getTotalTimeMillis() < 500);
        int count = StringUtils.countMatches(result.getResponse().getContentAsString(), "depth");
        assertEquals(110, count);
    }

}
