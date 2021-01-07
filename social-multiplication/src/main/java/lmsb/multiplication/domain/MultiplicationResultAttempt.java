package lmsb.multiplication.domain;

public class MultiplicationResultAttempt
{
   private final User user;
   private final Multiplication multiplication;
   private final int resultAttempt;
   private boolean correct; 

   public MultiplicationResultAttempt() 
   {
      user = null;
      multiplication = null;
      resultAttempt = -1;
      correct = false; 
   }
   
   public MultiplicationResultAttempt(User user, Multiplication multiplication, int resultAttempt, boolean correct)
   {
      this.user = user; 
      this.multiplication = multiplication; 
      this.resultAttempt = resultAttempt; 
      this.correct = correct; 
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
