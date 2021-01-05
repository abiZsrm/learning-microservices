package microservices.book.multiplication.domain;

public final class User
{
   private final String m_alias; 
   
   protected User() 
   {
      this.m_alias = null;
   }
   
   public User(String alias)
   {
      this.m_alias = alias; 
   }
}
