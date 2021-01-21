package lmsb.multiplication.repo.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lmsb.multiplication.domain.Multiplication;
import lmsb.multiplication.domain.MultiplicationResultAttempt;
import lmsb.multiplication.domain.User;
import lmsb.multiplication.repo.MultiplicationRepo;
import lmsb.multiplication.repo.MultiplicationResultAttemptRepoService;
import lmsb.multiplication.repo.UserRepo;

@Repository
public class MultiplicationResultAttemptRepoServiceImpl implements MultiplicationResultAttemptRepoService
{
   public static int m_mraID; 
   
   private NamedParameterJdbcTemplate m_namedJdbcTemplate; 
   
   private UserRepo m_userRepo; 
   
   private MultiplicationRepo m_multiplicationRepo; 
   
   @Autowired
   public MultiplicationResultAttemptRepoServiceImpl(NamedParameterJdbcTemplate jdbcTemplate, UserRepo userRepo, MultiplicationRepo mulRepo)
   {
      this.m_namedJdbcTemplate = jdbcTemplate; 
      this.m_userRepo = userRepo; 
      this.m_multiplicationRepo = mulRepo; 
   }
   
   @PostConstruct
   private void retrieveStaticCounterForMRA()
   {
      StringBuilder selectQuery = new StringBuilder(); 
      selectQuery.append("SELECT CONFIGURATION_VALUE FROM "); 
      selectQuery.append("STATIC_CONFIGURATION "); 
      selectQuery.append("WHERE CONFIGURATION_NAME = ");
      selectQuery.append("'MRA_ID'"); 

      String output = null; 
      output = m_namedJdbcTemplate.getJdbcTemplate().queryForObject(selectQuery.toString(), String.class); 
      
      // Retrieving seed ID for 'MultiplicationResultAttempt' Object. 
      MultiplicationResultAttemptRepoServiceImpl.m_mraID = Integer.parseInt(output); 
   }
   
   @Override
   public List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(String userAlias)
      throws SQLException
   {
      StringBuilder selectQuery = new StringBuilder(); 
      selectQuery.append("SELECT * FROM "); 
      selectQuery.append("MULTIPLICATION_RESULT_ATTEMPT "); 
      selectQuery.append("WHERE USER_ID = "); 
      selectQuery.append("( SELECT USER_ID FROM "); 
      selectQuery.append("SOC_MUL_USER "); 
      selectQuery.append("WHERE ALIAS ="); 
      selectQuery.append("? ) "); 
      
      RowMapper<MultiplicationResultAttempt> mapper = this::mapRow; 
      return m_namedJdbcTemplate.getJdbcTemplate().query(selectQuery.toString(), mapper, userAlias); 
   }
   
   public MultiplicationResultAttempt findMRAById(int mraId)
   {
      String selectQuery = "SELECT * FROM MULTIPLICATION_RESULT_ATTEMPT WHERE ID = ?"; 
      
      return m_namedJdbcTemplate.getJdbcTemplate().queryForObject(selectQuery, this::mapRow, mraId); 
   }
   
   @Override
   public void saveMultiplicationResultAttempt(MultiplicationResultAttempt attempt)
   {
      // Retrieve userID if user exists, otherwise persist the user record into the database. 
      String userId = null; 
      Optional<User> user = m_userRepo.findByAlias(attempt.getUser().getAlias()); 
      if(!user.isPresent())
      {
         // Persist user if not present in the database. 
         m_userRepo.saveUser(attempt.getUser());
         userId = Integer.toString(attempt.getUser().getID()); 
      }
      else
      {
         userId = Integer.toString(user.get().getID());
      }
      
      StringBuilder insertQuery = new StringBuilder(); 
      insertQuery.append("INSERT INTO "); 
      insertQuery.append("MULTIPLICATION_RESULT_ATTEMPT "); 
      insertQuery.append("(ID, CORRECT, RESULT_ATTEMPT, "); 
      insertQuery.append(" MULTIPLICATION_ID, USER_ID ) "); 
      insertQuery.append(" VALUES ("); 
      insertQuery.append(":id, :isCorrect, "); 
      insertQuery.append(":resultAttempt, :mulID, "); 
      insertQuery.append(":userID )"); 
      
      HashMap<String, String> queryParams = new HashMap<String, String>(); 
      queryParams.put("id", Integer.toString(MultiplicationResultAttemptRepoServiceImpl.m_mraID)); 
      queryParams.put("isCorrect", Boolean.toString(attempt.getCorrect())); 
      queryParams.put("resultAttempt", Integer.toString(attempt.getResultAttempt())); 
      queryParams.put("mulID", Integer.toString(attempt.getMultiplication().getID())); 
      queryParams.put("userID", userId); 
      
      // Persist multiplication record in the database. 
      m_multiplicationRepo.saveMultiplication(attempt.getMultiplication());
      m_namedJdbcTemplate.update(insertQuery.toString(), queryParams); 
      
      // Update static counter and corresponding CONFIGURATION_VALUE in the STATIC_CONFIGURATION table. 
      MultiplicationResultAttemptRepoServiceImpl.m_mraID++; 
      updateID(MultiplicationResultAttemptRepoServiceImpl.m_mraID);
   }
   
   private MultiplicationResultAttempt mapRow(ResultSet rs, int rowNum) 
      throws SQLException
   {
      MultiplicationResultAttempt multiplicationResultAttemptObj = null;
      Object[] rawInputData = { 
                                rs.getInt("ID"), 
                                rs.getObject("CORRECT"), 
                                rs.getInt("RESULT_ATTEMPT"), 
                                rs.getInt("MULTIPLICATION_ID"), 
                                rs.getInt("USER_ID")
                              };
      
      // Retrieve the associated attributes for creating 'User' and 'Multiplication' object. 
      User user = this.m_userRepo.findByID(((Integer)rawInputData[4]).intValue()); 
      Multiplication multiplication = this.m_multiplicationRepo.findMultiplicationByID(((Integer)rawInputData[3]).intValue());

      multiplicationResultAttemptObj = new MultiplicationResultAttempt( user, multiplication, 
                                                                        ((Integer)rawInputData[2]).intValue(), 
                                                                        ((Boolean)rawInputData[1]).booleanValue(), 
                                                                        ((Integer)rawInputData[0]).intValue() );
      return multiplicationResultAttemptObj; 
   }
   
   private void updateID(int multiplicationID)
   {
      StringBuilder updateQuery = new StringBuilder(); 
      updateQuery.append("UPDATE STATIC_CONFIGURATION "); 
      updateQuery.append("SET CONFIGURATION_VALUE = ? "); 
      updateQuery.append("WHERE CONFIGURATION_NAME = 'MRA_ID'"); 
      
      m_namedJdbcTemplate.getJdbcOperations().update(updateQuery.toString(), Integer.toString(multiplicationID)); 
   }
}
