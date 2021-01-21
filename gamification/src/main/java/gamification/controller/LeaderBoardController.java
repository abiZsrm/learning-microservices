package gamification.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamification.domain.Leaderboard;
import gamification.service.LeaderBoardService;

@RestController
@RequestMapping("/leaders")
class LeaderBoardController
{
   private final LeaderBoardService leaderBoardService;

   public LeaderBoardController( final LeaderBoardService leaderBoardService )
   {
      this.leaderBoardService = leaderBoardService;
   }

   @GetMapping
   public List<Leaderboard> getLeaderBoard()
   {
      return leaderBoardService.getCurrentLeaderBoard();
   }
}