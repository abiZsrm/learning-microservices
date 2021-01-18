package gamification.domain;

public class BadgeCard
{
   private final int badgeId;
   private final int userId;
   private final Badge badge;

   // Empty constructor for JSON / JPA
   public BadgeCard()
   {
      this(0, 0, null);
   }

   public BadgeCard( final int userId, final Badge badge )
   {
      this(0, userId, badge);
   }
   
   public BadgeCard(int badgeId, int userId, Badge badge)
   {
      this.badgeId = badgeId; 
      this.userId = userId; 
      this.badge = badge; 
   }
   
   public int getBadgeId()
   {
      return this.badgeId; 
   }
   
   public int getUserId()
   {
      return this.userId; 
   }
   
   public Badge getBadge()
   {
      return this.badge; 
   }
}
