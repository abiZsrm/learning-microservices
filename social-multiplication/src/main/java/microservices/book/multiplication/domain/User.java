package microservices.book.multiplication.domain;

public final class User 
{
   private final String m_alias; 
   
   public User() 
   {
      this.m_alias = null;
   }
   
   public User(String alias)
   {
      this.m_alias = alias; 
   }
   
   public String getAlias()
   {
      return this.m_alias; 
   }
}
