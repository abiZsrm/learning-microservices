package microservices.book.multiplication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.service.MultiplicationService;
import microservices.book.multiplication.service.MultiplicationServiceImpl;
import microservices.book.multiplication.service.RandomGeneratorService;


public class MultiplicationServiceImplTest
{
   @Mock
   private RandomGeneratorService m_randomGeneratorService; 
   
   private MultiplicationService m_multiplicationService; 
   
   @BeforeEach
   public void setUp() 
   {
      // With this call to initMocks we tell Mockito to
      // process the annotations
      MockitoAnnotations.initMocks(this);
      this.m_multiplicationService = new MultiplicationServiceImpl(this.m_randomGeneratorService);
   }
   
   @Test
   public void createRandomMultiplicationTest() 
   {
      // given (our mocked Random Generator service will
      // return first 50, then 30)
      given(m_randomGeneratorService.generateRandomFactor()).
      willReturn(50, 30);
      
      // when
      Multiplication multiplication = m_multiplicationService.createRandomMultiplication();
      
      // then
      assertThat(multiplication.getOperand1()).isEqualTo(50);
      assertThat(multiplication.getOperand2()).isEqualTo(30);
      assertThat(multiplication.getResult()).isEqualTo(1500);
   }
   
   @Test
   public void checkCorrectAttemptTest() 
   {
      // given
      Multiplication multiplication = new Multiplication(50, 60);
      User user = new User("john_doe");
      MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3000);
      
      // when
      boolean attemptResult = m_multiplicationService.checkAttempt(attempt);
      
      // assert
      assertThat(attemptResult).isTrue();
   }
   
   @Test
   public void checkWrongAttemptTest() 
   {
      // given
      Multiplication multiplication = new Multiplication(50, 60);
      User user = new User("john_doe");
      MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3010);
      
      // when
      boolean attemptResult = this.m_multiplicationService.checkAttempt(attempt);
      
      // assert
      assertThat(attemptResult).isFalse();
   }
}
