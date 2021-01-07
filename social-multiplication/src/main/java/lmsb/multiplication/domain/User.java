package lmsb.multiplication.domain;

import lmsb.multiplication.repo.impl.UserRepoServiceImpl;

public final class User 
{
   private final String alias; 
   
   private int m_id; 
   
   public User() 
   {
      this.alias = null;
      this.m_id = UserRepoServiceImpl.m_userID; 
   }
   
   public User(String alias, int ID)
   {
      this.alias = alias; 
      this.m_id = ID;  
   }
   
   public String getAlias()
   {
      return this.alias; 
   }
   
   public int getID()
   {
      return this.m_id; 
   }
}
