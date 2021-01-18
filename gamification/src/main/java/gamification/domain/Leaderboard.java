package gamification.domain;

public class Leaderboard
{
   private final int userId;
   private final Long totalScore;

   // Empty constructor for JSON / JPA
   public Leaderboard() 
   {
       this(0, 0L);
   }
   
   public Leaderboard(int userId, Long totalScore)
   {
      this.userId = userId; 
      this.totalScore = totalScore;
   }
   
   public int getUserId()
   {
      return this.userId; 
   }
   
   public Long getTotalScore()
   {
      return this.totalScore; 
   }
}
