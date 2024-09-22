package efub.session.blog.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Test
    public void 사용자_생성() throws Exception{
        // given
        String name="fubby";
        UserRequestDTO userRequestDTO = new UserRequestDTO(name);
        String requestBody = objectMapper.writeValueAsString(userRequestDTO);

        given(userService.save(any(UserRequestDTO.class)))
                .willReturn(User.builder().name(name)
                        .type(UserType.MEMBER).build());


        // when & then
        mvc.perform(post("/test/user")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value(name));

    }


    @Test
    public void 사용자없음_예외처리() throws Exception{
        //given
        given(userService.findById(anyLong())).willThrow(new IllegalArgumentException());

        //when & then
        mvc.perform(get("/test/user/1"))
                .andExpect(status().isNotFound());

    }

}
