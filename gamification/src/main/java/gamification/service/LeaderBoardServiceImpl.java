package gamification.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gamification.domain.Leaderboard;
import gamification.repository.ScoreCardRepository;

@Service
public class LeaderBoardServiceImpl implements LeaderBoardService
{
   private ScoreCardRepository m_scoreCardRepo; 
   
   @Autowired
   public LeaderBoardServiceImpl(ScoreCardRepository scoreCardRepo)
   {
      m_scoreCardRepo = scoreCardRepo; 
   }
   
   @Override
   public List<Leaderboard> getCurrentLeaderBoard()
   {
      return m_scoreCardRepo.findFirst10();
   }
}
