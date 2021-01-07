package lmsb.multiplication.repo.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lmsb.multiplication.domain.Multiplication;
import lmsb.multiplication.domain.MultiplicationResultAttempt;
import lmsb.multiplication.domain.User;
import lmsb.multiplication.repo.MultiplicationRepoService;
import lmsb.multiplication.repo.MultiplicationResultAttemptRepoService;
import lmsb.multiplication.repo.UserRepository;

@Repository
public class MultiplicationResultAttemptRepoServiceImpl implements MultiplicationResultAttemptRepoService
{
   public static int m_mraID; 
   
   private NamedParameterJdbcTemplate m_jdbcTemplate; 
   
   private UserRepository m_userRepo; 
   
   private MultiplicationRepoService m_multiplicationRepo; 
   
   @Autowired
   public MultiplicationResultAttemptRepoServiceImpl(NamedParameterJdbcTemplate jdbcTemplate, UserRepository userRepo, MultiplicationRepoService mulRepo)
   {
      this.m_jdbcTemplate = jdbcTemplate; 
      this.m_userRepo = userRepo; 
      this.m_multiplicationRepo = mulRepo; 
   }
   
   @Transactional
   @Override
   public List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(String userAlias)
      throws SQLException
   {
      StringBuilder selectQuery = new StringBuilder(); 
      selectQuery.append("SELECT * FROM MULTIPLICATION_RESULT_ATTEMPT")
                 .append(" WHERE ")
                 .append(" USER_ID = ( ")
                 .append("SELECT USER_ID FROM SOC_MUL_USER WHERE ALIAS = \"?\" )");
      
      RowMapper<MultiplicationResultAttempt> mapper = this::mapRow; 
      
      return m_jdbcTemplate.getJdbcTemplate().query(selectQuery.toString(), mapper, userAlias); 
   }
   
   @Override
   public void saveMultiplicationResultAttempt(MultiplicationResultAttempt attempt)
   {
      String insertQuery = "INSERT INTO MULTIPLICATION_RESULT_ATTEMPT (ID,CORRECT, RESULT_ATTEMPT, MULTIPLICATION_ID, USER_ID) VALUES (:id, :isCorrect, :resultAttempt, :mulID, :userID)"; 
      HashMap<String, String> queryParams = new HashMap<String, String>(); 
      queryParams.put("id", Integer.toString(MultiplicationResultAttemptRepoServiceImpl.m_mraID)); 
      queryParams.put("isCorrect", Boolean.toString(attempt.getCorrect())); 
      queryParams.put("resultAttempt", Integer.toString(attempt.getResultAttempt())); 
      queryParams.put("mulID", Integer.toString(attempt.getMultiplication().getID())); 
      queryParams.put("userID", Integer.toString(attempt.getUser().getID())); 
      
      m_userRepo.saveUser(attempt.getUser());
      m_multiplicationRepo.saveMultiplication(attempt.getMultiplication());
      m_jdbcTemplate.update(insertQuery, queryParams); 
      
      // Update counter in the STATIC_CONFIGURATION table. 
      MultiplicationResultAttemptRepoServiceImpl.m_mraID++; 
      updateID(MultiplicationResultAttemptRepoServiceImpl.m_mraID);
   }
   
   private MultiplicationResultAttempt mapRow(ResultSet rs, int rowNum) 
      throws SQLException
   {
      MultiplicationResultAttempt multiplicationResultAttemptObj = null;
      
      Object[] rawInputData = { rs.getInt("ID"), 
                                rs.getObject("CORRECT"), 
                                rs.getInt("RESULT_ATTEMPT"), 
                                rs.getInt("MULTIPLICATION_ID"), 
                                rs.getInt("USER_ID")
                               };
      
      System.out.println("MRA ID: " + rawInputData[0]);
      System.out.println("CORRECT: " + rawInputData[1]);
      System.out.println("RESULT_ATTEMPT: " + rawInputData[2]);
      System.out.println("MULTIPLICATION_ID: " + rawInputData[3]);
      System.out.println("USER_ID: " + rawInputData[4]);

      Multiplication multiplication = this.m_multiplicationRepo.findMultiplicationByID(((Integer)rawInputData[3]).intValue());
      User user = this.m_userRepo.findByID(((Integer)rawInputData[4]).intValue()); 

      multiplicationResultAttemptObj = new MultiplicationResultAttempt( user, multiplication, 
                                                                        ((Integer)rawInputData[2]).intValue(), 
                                                                        ((Character)rawInputData[1]) == 1?true:false);
      return multiplicationResultAttemptObj; 
   }
   
   private void updateID(int multiplicationID)
   {
      String updateQuery = "UPDATE STATIC_CONFIGURATION SET CONFIGURATION_VALUE = ? WHERE CONFIGURATION_NAME = 'MRA_ID'"; 
      m_jdbcTemplate.getJdbcOperations().update(updateQuery, Integer.toString(multiplicationID)); 
   }
}
