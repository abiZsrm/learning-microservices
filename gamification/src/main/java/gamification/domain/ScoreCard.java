package gamification.domain;

public class ScoreCard
{
   // The default score assigned to this card, if not specified.
   public static final int DEFAULT_SCORE = 10;

   private final int cardId;
   private final int userId;
   private final int attemptId;
   private final int score;

   // Empty constructor for JSON / JPA
   public ScoreCard()
   {
      this(0, 0, 0, 0);
   }

   public ScoreCard(int cardId, int userId, int attemptId, int score)
   {
      this.cardId = cardId; 
      this.userId = userId; 
      this.attemptId = attemptId; 
      this.score = score; 
   }
   
   public ScoreCard( final int userId, final int attemptId )
   {
      this(0, userId, attemptId, DEFAULT_SCORE);
   }
   
   public int getCardId()
   {
      return this.cardId; 
   }
   
   public int getUserId()
   {
      return this.userId; 
   }
   
   public int getAttemptId()
   {
      return this.attemptId; 
   }
   
   public int getScore()
   {
      return this.score; 
   }
}
