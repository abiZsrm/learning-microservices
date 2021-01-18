package gamification.service;

import org.springframework.beans.factory.annotation.Autowired;

import gamification.domain.GameStats;
import gamification.repository.BadgeCardRepository;
import gamification.repository.ScoreCardRepository;

public class GameServiceImpl implements GameService
{
   private ScoreCardRepository m_scoreCardRepo; 
   private BadgeCardRepository m_badgeCardRepo; 
   
   @Autowired
   public GameServiceImpl(ScoreCardRepository scoreCardRepo, BadgeCardRepository badgeCardRepo)
   {
      this.m_scoreCardRepo = scoreCardRepo; 
      this.m_badgeCardRepo = badgeCardRepo; 
   }
   
   @Override
   public GameStats newAttemptForUser(int userId, int attemptId, boolean correct)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public GameStats retrieveStatsForUser(int userId)
   {
      // TODO Auto-generated method stub
      return null;
   }

}
