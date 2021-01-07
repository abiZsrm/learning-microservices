package lmsb.multiplication.domain;

import lmsb.multiplication.repo.impl.MultiplicationRepoServiceImpl;

public class MultiplicationResultAttempt
{
   private final User user;
   private final Multiplication multiplication;
   private final int resultAttempt;
   private boolean correct; 
   private int id; 
   
   public MultiplicationResultAttempt() 
   {
      user = null;
      multiplication = null;
      resultAttempt = -1;
      correct = false; 
      id = MultiplicationRepoServiceImpl.mulID; 
   }
   
   public MultiplicationResultAttempt(User user, Multiplication multiplication, int resultAttempt, boolean correct, int id)
   {
      this.user = user; 
      this.multiplication = multiplication; 
      this.resultAttempt = resultAttempt; 
      this.correct = correct; 
      this.id = id; 
   }
   
   public int getId()
   {
      return this.id; 
   }
   
   public User getUser()
   {
      return user;
   }

   public Multiplication getMultiplication()
   {
      return multiplication;
   }

   public int getResultAttempt()
   {
      return resultAttempt;
   }
   
   public boolean getCorrect()
   {
      return this.correct; 
   }
}
