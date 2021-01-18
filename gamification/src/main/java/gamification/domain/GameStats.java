package gamification.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameStats
{
   private final int userId;
   private final int score;
   private final List<Badge> badges;

   // Empty constructor for JSON / JPA
   public GameStats() 
   {
      this.userId = 0;
      this.score = 0;
      this.badges = new ArrayList<>();
   }

   public GameStats(int userId, int score, List<Badge> badges)
   {
      this.userId = userId; 
      this.score = score; 
      this.badges = badges; 
   }
   
   public int getUserID()
   {
      return this.userId; 
   }
   
   public int getScore()
   {
      return this.score; 
   }
   
   public List<Badge> getBadges() 
   {
       return Collections.unmodifiableList(badges);
   }
   
   /**
    * Factory method to build an empty instance (zero points and no badges)
    * @param userId the user's id
    * @return a {@link GameStats} object with zero score and no badges
    */
   public static GameStats emptyStats(final int userId) 
   {
       return new GameStats(userId, 0, Collections.emptyList());
   }
}
