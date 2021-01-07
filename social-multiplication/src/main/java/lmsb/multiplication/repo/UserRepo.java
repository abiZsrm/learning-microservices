package lmsb.multiplication.repo;

import java.util.Optional;

import lmsb.multiplication.domain.User;

public interface UserRepo
{
   public Optional<User> findByAlias(final String alias);
   
   public void saveUser(User user); 
   
   public User findByID(int userID);
}
