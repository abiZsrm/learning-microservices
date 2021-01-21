package microservices.book.multiplication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import lmsb.multiplication.domain.Multiplication;
import lmsb.multiplication.domain.MultiplicationResultAttempt;
import lmsb.multiplication.domain.User;
import lmsb.multiplication.repo.impl.MultiplicationRepoServiceImpl;
import lmsb.multiplication.repo.impl.UserRepoServiceImpl;
import lmsb.multiplication.service.MultiplicationService;
import lmsb.multiplication.service.RandomGeneratorService;
import lmsb.multiplication.service.impl.MultiplicationServiceImpl;


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
      
      //TODO: Mock other values as well. 
//      this.m_multiplicationService = new MultiplicationServiceImpl(this.m);
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
//   
//   @Test
//   public void checkCorrectAttemptTest() 
//   {
//      // given
//      Multiplication multiplication = new Multiplication(50, 60, MultiplicationRepoServiceImpl.mulID);
//      User user = new User("john_doe", UserRepoServiceImpl.m_userID);
//      MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3000, false);
//      
//      // when
//      boolean attemptResult = m_multiplicationService.checkAttempt(attempt);
//      
//      // assert
//      assertThat(attemptResult).isTrue();
//   }
//   
//   @Test
//   public void checkWrongAttemptTest() 
//   {
//      // given
//      Multiplication multiplication = new Multiplication(50, 60, MultiplicationRepoServiceImpl.mulID);
//      User user = new User("john_doe", UserRepoServiceImpl.m_userID);
//      MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3010, false);
//      
//      // when
//      boolean attemptResult = this.m_multiplicationService.checkAttempt(attempt);
//      
//      // assert
//      assertThat(attemptResult).isFalse();
//   }
}
