package lmsb.multiplication.repo.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lmsb.multiplication.domain.User;
import lmsb.multiplication.repo.UserRepo;

@Repository
public class UserRepoServiceImpl implements UserRepo
{
   public static int m_userID; 
   
   @Autowired
   private NamedParameterJdbcTemplate m_jdbcTemplate; 
   
   @PostConstruct
   public void startUpConfiguration()
   {
      String retrieveIDQuery = "SELECT CONFIGURATION_VALUE FROM STATIC_CONFIGURATION WHERE CONFIGURATION_NAME = 'USER_ID_COUNT'"; 
      
      UserRepoServiceImpl.m_userID = Integer.parseInt(this.m_jdbcTemplate.getJdbcOperations().queryForObject(retrieveIDQuery, String.class)); 
   }
   
   public Optional<User> findByAlias(String alias)
   {
      String retrievalQuery = "SELECT USER_ID FROM SOC_MUL_USER WHERE ALIAS = '" + alias + "'"; 
      Integer result = null; 
      User user = null;
      
      try
      {
         result = m_jdbcTemplate.getJdbcOperations().queryForObject(retrievalQuery, Integer.class);
         user = new User(alias, result);
      }
      catch(EmptyResultDataAccessException ede)
      {
         user = null;
      }

       return Optional.ofNullable(user); 
   }

   public void saveUser(User user)
   {
      String insertQuery = "INSERT INTO SOC_MUL_USER (USER_ID, ALIAS) VALUES (:user_id, :alias)"; 
      HashMap<String, String> keyValMap= new HashMap<String, String>(); 
      keyValMap.put("user_id", Integer.toString(UserRepoServiceImpl.m_userID)); 
      keyValMap.put("alias", user.getAlias()); 
      
      this.m_jdbcTemplate.update(insertQuery, keyValMap); 
      
      // Increment counter and update the STATIC_CONFIGURATION table. 
      UserRepoServiceImpl.m_userID++;
      updateID(UserRepoServiceImpl.m_userID);
   }
   
   public User findByID(int userID)
   {
      String retrieveUserQuery = "SELECT * FROM SOC_MUL_USER WHERE USER_ID = '" + userID + "'";  
      System.out.println("Query: " + retrieveUserQuery);
      return m_jdbcTemplate.getJdbcTemplate().queryForObject(retrieveUserQuery, this::mapRowUser); 
   }
   
   private void updateID(int userID)
   {
      String updateQuery = "UPDATE STATIC_CONFIGURATION SET CONFIGURATION_VALUE = ? WHERE CONFIGURATION_NAME = 'USER_ID_COUNT'"; 
      m_jdbcTemplate.getJdbcOperations().update(updateQuery, Integer.toString(userID)); 
   }

   private User mapRowUser(ResultSet rs, int rowNum) throws SQLException
   {
      User user = null;
      String alias = rs.getString("ALIAS");
      Integer id = rs.getInt("USER_ID"); 
      user = new User(alias, id); 
      return user; 
   }
}
