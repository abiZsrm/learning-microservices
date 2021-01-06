package microservices.book.multiplication.service;

import org.springframework.beans.factory.annotation.Autowired;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;

public class MultiplicationServiceImpl implements MultiplicationService
{
   private RandomGeneratorService m_randomGeneratorService; 

   @Autowired
   public MultiplicationServiceImpl(RandomGeneratorService randomService)
   {
      m_randomGeneratorService = randomService; 
   }
   
   @Override
   public Multiplication createRandomMultiplication()
   {
      int operand1 = m_randomGeneratorService.generateRandomFactor(); 
      int operand2 = m_randomGeneratorService.generateRandomFactor(); 
      
      Multiplication m = new Multiplication(operand1, operand2); 
      return m; 
   }

   @Override
   public boolean checkAttempt(MultiplicationResultAttempt resultAttempt)
   {
      return resultAttempt.getResultAttempt() ==
               (resultAttempt.getMultiplication().getOperand1() *
               resultAttempt.getMultiplication().getOperand2());
   }

}
