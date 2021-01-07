package lmsb.multiplication.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import lmsb.multiplication.domain.Multiplication;
import lmsb.multiplication.domain.MultiplicationResultAttempt;
import lmsb.multiplication.repo.impl.MultiplicationRepoServiceImpl;
import lmsb.multiplication.repo.impl.MultiplicationResultAttemptRepoServiceImpl;
import lmsb.multiplication.service.MultiplicationService;
import lmsb.multiplication.service.RandomGeneratorService;

public class MultiplicationServiceImpl implements MultiplicationService
{
   private RandomGeneratorService m_randomGeneratorService; 
   
   @Autowired
   private MultiplicationResultAttemptRepoServiceImpl m_raRepo; 

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
      
      Multiplication m = new Multiplication(operand1, operand2, MultiplicationRepoServiceImpl.mulID); 
      return m; 
   }

   @Override
   public boolean checkAttempt(MultiplicationResultAttempt resultAttempt)
   {
      //TODO: Check if the user already exists for that alias
      
      boolean correct =  resultAttempt.getResultAttempt() ==
               (resultAttempt.getMultiplication().getOperand1() *
               resultAttempt.getMultiplication().getOperand2());
      
      Assert.isTrue(!resultAttempt.getCorrect(), "You can't send an attempt marked as correct!!");
      
      MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt
                                                   (resultAttempt.getUser(), resultAttempt.getMultiplication(), 
                                                    resultAttempt.getResultAttempt(), correct); 
      //1. check for the user when creating the multiplicationresultattempt object. 
      //2. store the multiplicationresultattempt into the database. 
      m_raRepo.saveMultiplicationResultAttempt(checkedAttempt);
      //3. mark this method as transactional. 
      return correct; 
   }

}
