package gamification.service;

import java.util.List;

import gamification.domain.Leaderboard;

public interface LeaderBoardService
{
   List<Leaderboard> getCurrentLeaderBoard();
}
