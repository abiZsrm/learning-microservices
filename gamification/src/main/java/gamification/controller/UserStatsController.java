package gamification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gamification.domain.GameStats;
import gamification.service.GameService;

@RestController
@RequestMapping("/stats")
class UserStatsController
{
   private final GameService gameService;

   public UserStatsController( final GameService gameService )
   {
      this.gameService = gameService;
   }

   @GetMapping
   public GameStats getStatsForUser(@RequestParam("userId") final int userId)
   {
      return gameService.retrieveStatsForUser(userId);
   }
}