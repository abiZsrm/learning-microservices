package lmsb.multiplication.repo.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lmsb.multiplication.domain.Multiplication;
import lmsb.multiplication.repo.MultiplicationRepo;

@Repository
public class MultiplicationRepoServiceImpl implements MultiplicationRepo
{
   private NamedParameterJdbcTemplate m_namedJdbcTemplate; 

   public static int mulID; 
   
   @Autowired
   public MultiplicationRepoServiceImpl(NamedParameterJdbcTemplate jdbcTemplate)
   {
      this.m_namedJdbcTemplate = jdbcTemplate; 
   }
   
   @PostConstruct
   private void retrieveStaticID()
   {
      String selectQuery = "SELECT CONFIGURATION_VALUE FROM STATIC_CONFIGURATION WHERE CONFIGURATION_NAME = 'MULTIPLICATION_ID_COUNT'"; 
      String output = null; 
      output = m_namedJdbcTemplate.getJdbcTemplate().queryForObject(selectQuery.toString(), String.class); 
      
      // Retrieving static counter from the STATIC_CONFIGURATION table. 
      MultiplicationRepoServiceImpl.mulID = Integer.parseInt(output); 
   }
   
   @Override
   public Multiplication findMultiplicationByID(int mulID)
   {
      String selectQuery = "SELECT * FROM MULTIPLICATION WHERE MULTIPLICATION_ID = ?";  
      
      Multiplication mul = m_namedJdbcTemplate.getJdbcTemplate().queryForObject(selectQuery, this::mapMultiplication, mulID); 
      return mul; 
   }

   @Override
   public void saveMultiplication(Multiplication multiplication)
   {
      StringBuilder insertQuery = new StringBuilder(); 
      insertQuery.append("INSERT INTO MULTIPLICATION "); 
      insertQuery.append("(MULTIPLICATION_ID, OPERAND_1, "); 
      insertQuery.append("OPERAND_2) "); 
      insertQuery.append("VALUES "); 
      insertQuery.append("(:multiplicationID, :operand1, "); 
      insertQuery.append(":operand2)"); 
      
      HashMap<String, String> keyValMap = new HashMap<String, String>(); 
      keyValMap.put("multiplicationID", Integer.toString(MultiplicationRepoServiceImpl.mulID)); 
      keyValMap.put("operand1", Integer.toString(multiplication.getOperand1())); 
      keyValMap.put("operand2", Integer.toString(multiplication.getOperand2()));
      
      this.m_namedJdbcTemplate.update(insertQuery.toString(), keyValMap); 
      
      // Increment counter and update the STATIC_CONFIGURATION_TABLE
      MultiplicationRepoServiceImpl.mulID++; 
      updateID(MultiplicationRepoServiceImpl.mulID);
   }
   
   private Multiplication mapMultiplication(ResultSet rs, int rowNum) 
      throws SQLException
   {
      int operand1 = rs.getInt("OPERAND_1");
      int operand2 = rs.getInt("OPERAND_2");
      int id = rs.getInt("MULTIPLICATION_ID"); 

      return new Multiplication(operand1, operand2, id);
   }
   
   private void updateID(int multiplicationID)
   {
      String updateQuery = "UPDATE STATIC_CONFIGURATION SET CONFIGURATION_VALUE = ? WHERE CONFIGURATION_NAME = 'MULTIPLICATION_ID_COUNT'"; 
      m_namedJdbcTemplate.getJdbcOperations().update(updateQuery, Integer.toString(multiplicationID)); 
   }
}
