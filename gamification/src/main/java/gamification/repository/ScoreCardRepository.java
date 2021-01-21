package gamification.repository;

import java.util.List;

import gamification.domain.Leaderboard;
import gamification.domain.ScoreCard;

public interface ScoreCardRepository
{
   public void save(ScoreCard scoreCard); 
   
   int getTotalScoreForUser(final int userId);
   
   List<Leaderboard> findFirst10();
   
   List<ScoreCard> findByUserId(final int userId);
}
