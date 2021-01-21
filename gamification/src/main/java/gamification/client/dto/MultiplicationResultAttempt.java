package gamification.client.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = MultiplicationResultAttemptDeserializer.class)
public class MultiplicationResultAttempt
{
   private final String userAlias;

   //TODO: Convert factor a-b to operand1-2.
   private final int multiplicationFactorA;
   private final int multiplicationFactorB;
   private final int resultAttempt;

   private final boolean correct;

   // Empty constructor for JSON/JPA
   MultiplicationResultAttempt() 
   {
       userAlias = null;
       multiplicationFactorA = -1;
       multiplicationFactorB = -1;
       resultAttempt = -1;
       correct = false;
   }
   
   public MultiplicationResultAttempt( String userAlias, int operand1, int operand2, int resultAttempt, boolean correct)
   {
      this.userAlias = userAlias; 
      this.multiplicationFactorA = operand1; 
      this.multiplicationFactorB = operand2;
      this.resultAttempt = resultAttempt; 
      this.correct = correct; 
   }
   
   public String getUserAlias()
   {
      return this.userAlias; 
   }
   
   public int getOperand1()
   {
      return this.multiplicationFactorA; 
   }
   
   public int getOperand2()
   {
      return this.multiplicationFactorB; 
   }
   
   public int getResultAttempt()
   {
      return this.resultAttempt; 
   }
   
   public boolean isCorrect()
   {
      return this.correct; 
   }
}
