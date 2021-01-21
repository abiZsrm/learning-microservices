package gamification.repository;

import java.util.List;

import gamification.domain.BadgeCard;

public interface BadgeCardRepository
{
   List<BadgeCard> findByUserId(final int userId);
   
   public void save(BadgeCard badge); 
}
