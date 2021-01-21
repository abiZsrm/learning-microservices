package lmsb.multiplication.event;

import java.io.Serializable;

public class MultiplicationSolvedEvent implements Serializable
{
   private final int multiplicationResultAttemptId;
   private final int userId;
   private final boolean correct;
   
   public MultiplicationSolvedEvent()
   {
      this.multiplicationResultAttemptId = 0; 
      this.userId = 0; 
      this.correct = false; 
   }
   
   public MultiplicationSolvedEvent(int mraId, int userId, boolean correct)
   {
      this.multiplicationResultAttemptId = mraId; 
      this.userId = userId; 
      this.correct = correct; 
   }
   
   public int getMultiplicationResultAttemptID()
   {
      return this.multiplicationResultAttemptId; 
   }
   
   public int getUserId()
   {
      return this.userId; 
   }
   
   public boolean isCorrect()
   {
      return this.correct; 
   }
}
