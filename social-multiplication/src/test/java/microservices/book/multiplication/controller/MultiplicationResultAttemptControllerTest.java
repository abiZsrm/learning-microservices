package microservices.book.multiplication.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import microservices.book.multiplication.controller.MultiplicationResultAttemptController.ResultResponse;
import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.service.MultiplicationService;

@WebMvcTest(MultiplicationResultAttemptController.class)
public class MultiplicationResultAttemptControllerTest
{
   @MockBean
   private MultiplicationService multiplicationService;
   
   @Autowired
   private MockMvc mvc;
   
   // This object will be magically initialized by the
   // initFields method below.
   private JacksonTester<MultiplicationResultAttempt> jsonResult;
   private JacksonTester<ResultResponse> jsonResponse;
   
   @BeforeEach
   public void setup()
   {
      ObjectMapper mapper = new ObjectMapper(); 
      JacksonTester.initFields(this, mapper);
   }

   @Test
   public void postResultReturnCorrect() 
      throws Exception
   {
      genericParameterizedTest(true);
   }
   
   public void postResultReturnNotCorrect() 
      throws Exception
   {
      genericParameterizedTest(false);
   }

   void genericParameterizedTest(final boolean correct) 
           throws Exception 
   {
      // given (remember we're not testing here the service
      // itself)
      given(multiplicationService.checkAttempt(any(MultiplicationResultAttempt.class)))
                                 .willReturn(correct);
      User user = new User("john");
      Multiplication multiplication = new Multiplication(50, 70);
      MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3500);
      
      // when
      MockHttpServletResponse response = mvc.perform(post("/results").contentType(MediaType.APPLICATION_JSON)
                                                                     .content(jsonResult.write(attempt).getJson()))
                                                                     .andReturn()
                                                                     .getResponse();
      // then
      assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
      assertThat(response.getContentAsString()).isEqualTo(
      jsonResponse.write(new ResultResponse(correct)).getJson());
    }
}
