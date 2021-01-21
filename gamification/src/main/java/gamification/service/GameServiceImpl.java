package gamification.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gamification.client.dto.MultiplicationResultAttempt;
import gamification.client.dto.MultiplicationResultAttemptClient;
import gamification.domain.Badge;
import gamification.domain.BadgeCard;
import gamification.domain.GameStats;
import gamification.domain.ScoreCard;
import gamification.repository.BadgeCardRepository;
import gamification.repository.ScoreCardRepository;

@Service
public class GameServiceImpl implements GameService
{
   private static final int LUCKY_NUMBER  = 42; 
   private ScoreCardRepository m_scoreCardRepo; 
   private BadgeCardRepository m_badgeCardRepo; 
   private MultiplicationResultAttemptClient m_mraClient; 
   
   @Autowired
   public GameServiceImpl(ScoreCardRepository scoreCardRepo, BadgeCardRepository badgeCardRepo, MultiplicationResultAttemptClient mraClient)
   {
      this.m_scoreCardRepo = scoreCardRepo; 
      this.m_badgeCardRepo = badgeCardRepo; 
      this.m_mraClient = mraClient; 
   }
   
   @Override
   public GameStats newAttemptForUser(int userId, int attemptId, boolean correct)
   {
      if (correct)
      {
         ScoreCard scoreCard = new ScoreCard(userId, attemptId);
         m_scoreCardRepo.save(scoreCard);

         List<BadgeCard> badgeCards = processForBadges(userId, attemptId);
         return new GameStats( userId, 
                               scoreCard.getScore(),
                               badgeCards.stream()
                                         .map(BadgeCard::getBadge)
                                         .collect(Collectors.toList())
                              );
      }
      
      return GameStats.emptyStats(userId);
   }
   
   private List<BadgeCard> processForBadges(final int userId, final int attemptId)
   {
      List<BadgeCard> badgeCards = new ArrayList<BadgeCard>();
      int totalScore = m_scoreCardRepo.getTotalScoreForUser(userId);
      List<ScoreCard> scoreCardList = m_scoreCardRepo.findByUserId(userId);
      List<BadgeCard> badgeCardList = m_badgeCardRepo.findByUserId(userId);
      
      // Badges depending on score
      checkAndGiveBadgeBasedOnScore(badgeCardList, Badge.BRONZE_MULTIPLICATOR, totalScore, 100, userId)
               .ifPresent(badgeCards::add);
      checkAndGiveBadgeBasedOnScore(badgeCardList, Badge.SILVER_MULTIPLICATOR, totalScore, 500, userId)
               .ifPresent(badgeCards::add);
      checkAndGiveBadgeBasedOnScore(badgeCardList, Badge.GOLD_MULTIPLICATOR, totalScore, 999, userId)
               .ifPresent(badgeCards::add);
      
      // First won badge
      if (scoreCardList.size() == 1 && !containsBadge(badgeCardList, Badge.FIRST_WON))
      {
         BadgeCard firstWonBadge = giveBadgeToUser(Badge.FIRST_WON, userId);
         badgeCards.add(firstWonBadge);
      }
      
      // Lucky number badge. 
      MultiplicationResultAttempt attempt = m_mraClient.retrieveMultiplicationResultAttemptbyId(attemptId);
      
      if(
          !containsBadge(badgeCardList, Badge.LUCKY_NUMBER) 
          &&
            (LUCKY_NUMBER == attempt.getOperand1() ||
               LUCKY_NUMBER == attempt.getOperand2())
         ) 
      {
               BadgeCard luckyNumberBadge = giveBadgeToUser(
               Badge.LUCKY_NUMBER, userId);
               badgeCards.add(luckyNumberBadge);
      }
      return badgeCards;
   }
   
   private Optional<BadgeCard> checkAndGiveBadgeBasedOnScore(final List<BadgeCard> badgeCards, final Badge badge,
            final int score, final int scoreThreshold, final int userId)
   {
      if (score >= scoreThreshold && !containsBadge(badgeCards, badge))
      {
         return Optional.of(giveBadgeToUser(badge, userId));
      }
      return Optional.empty();
   }
   
   private boolean containsBadge(final List<BadgeCard> badgeCards, final Badge badge)
   {
      return badgeCards.stream()
                       .anyMatch(b -> b.getBadge().equals(badge));
   }

   private BadgeCard giveBadgeToUser(final Badge badge, final int userId)
   {
      BadgeCard badgeCard = new BadgeCard(userId, badge);
      m_badgeCardRepo.save(badgeCard);
      return badgeCard;
   }
   
   @Override
   public GameStats retrieveStatsForUser(int userId)
   {
      int score = m_scoreCardRepo.getTotalScoreForUser(userId);
      List<BadgeCard> badgeCards = m_badgeCardRepo.findByUserId(userId);
      return new GameStats(userId, score, badgeCards.stream()
                                                    .map(BadgeCard::getBadge)
                                                    .collect(Collectors.toList()));
   }

}
