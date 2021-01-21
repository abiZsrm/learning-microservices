package lmsb.multiplication.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import lmsb.multiplication.domain.Multiplication;
import lmsb.multiplication.domain.MultiplicationResultAttempt;
import lmsb.multiplication.event.EventDispatcher;
import lmsb.multiplication.event.MultiplicationSolvedEvent;
import lmsb.multiplication.repo.MultiplicationResultAttemptRepoService;
import lmsb.multiplication.repo.impl.MultiplicationRepoServiceImpl;
import lmsb.multiplication.service.MultiplicationService;
import lmsb.multiplication.service.RandomGeneratorService;

@Service
public class MultiplicationServiceImpl implements MultiplicationService
{
   private RandomGeneratorService m_randomGeneratorService; 
   
   @Autowired
   private MultiplicationResultAttemptRepoService m_raRepo; 

   private EventDispatcher m_eventDispatcher; 
   
   @Autowired
   public MultiplicationServiceImpl(RandomGeneratorService randomService, EventDispatcher eventDispatcher)
   {
      m_randomGeneratorService = randomService; 
      this.m_eventDispatcher = eventDispatcher; 
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
      // TODO: Check if the user already exists for that alias
      boolean correct =  resultAttempt.getResultAttempt() == 
                         (resultAttempt.getMultiplication().getOperand1() * resultAttempt.getMultiplication().getOperand2());
      
      Assert.isTrue(!resultAttempt.getCorrect(), "You can't send an attempt marked as correct!!");
      
      MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt( resultAttempt.getUser(), 
                                                                                    resultAttempt.getMultiplication(), 
                                                                                    resultAttempt.getResultAttempt(), 
                                                                                    correct, 
                                                                                    MultiplicationRepoServiceImpl.mulID );
      
      //1. check for the user when creating the multiplicationresultattempt object. 
      //2. store the multiplicationresultattempt into the database. 
      m_raRepo.saveMultiplicationResultAttempt(checkedAttempt);
      //3. mark this method as transactional. 
      
      // Communicates the result via Event
      m_eventDispatcher.send( new MultiplicationSolvedEvent( checkedAttempt.getId(),
                                                             checkedAttempt.getUser().getID(),
                                                             checkedAttempt.getCorrect()) 
                            );
      return correct; 
   }

   public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) 
      throws SQLException 
   {
      return m_raRepo.findTop5ByUserAliasOrderByIdDesc(userAlias);
   }

   @Override
   public MultiplicationResultAttempt findMRAById(int mraId)
   {
      // TODO Auto-generated method stub
      return m_raRepo.findMRAById(mraId); 
   }
}
