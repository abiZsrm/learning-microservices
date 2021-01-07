package microservices.book.multiplication.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import com.fasterxml.jackson.databind.ObjectMapper;

import lmsb.multiplication.controller.MultiplicationController;
import lmsb.multiplication.domain.Multiplication;
import lmsb.multiplication.repo.impl.MultiplicationRepoServiceImpl;
import lmsb.multiplication.service.MultiplicationService;

@WebMvcTest(MultiplicationController.class)
public class MultiplicationControllerTest
{
   @MockBean
   private MultiplicationService multiplicationService;
   
   @Autowired
   private MockMvc mvc;
   
   // This object will be magically initialized by the
   // initFields method below.
   private JacksonTester<Multiplication> json;
   
   @BeforeEach
   public void setup() 
   {
      JacksonTester.initFields(this, new ObjectMapper());
   }
   
   @Test
   public void getRandomMultiplicationTest() throws Exception 
   {
      // Given
      given(multiplicationService.createRandomMultiplication())
                                 .willReturn(new Multiplication(70, 20, MultiplicationRepoServiceImpl.mulID));
   
      // When
      MockHttpServletResponse response = mvc.perform(get("/multiplications/random")
                                                      .accept(MediaType.APPLICATION_JSON))
                                                      .andReturn().getResponse();
      
      // Then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
      assertThat(response.getContentAsString()).isEqualTo(json.write(new Multiplication(70, 20, MultiplicationRepoServiceImpl.mulID)).getJson());
   }
}
