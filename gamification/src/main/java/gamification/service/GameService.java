package gamification.service;

import gamification.domain.GameStats;

public interface GameService
{
   GameStats newAttemptForUser(int userId, int attemptId, boolean correct);
   
   GameStats retrieveStatsForUser(int userId);
}
