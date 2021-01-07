package lmsb.multiplication.repo.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lmsb.multiplication.domain.Multiplication;
import lmsb.multiplication.repo.MultiplicationRepoService;

@Repository
public class MultiplicationRepoServiceImpl implements MultiplicationRepoService
{
   private NamedParameterJdbcTemplate m_jdbcTemplate; 

   public static int mulID; 
   
   @Autowired
   public MultiplicationRepoServiceImpl(NamedParameterJdbcTemplate jdbcTemplate)
   {
      this.m_jdbcTemplate = jdbcTemplate; 
   }
   
   @Override
   public Multiplication findMultiplicationByID(int mulID)
   {
      String selectQuery = "SELECT OPERAND_1, OPERAND_2 FROM MULTIPLICATION WHERE MULTIPLICATION_ID = ?"; 
      ResultSetExtractor<Multiplication> ms = this::mapMultiplication; 
      
      Multiplication mul = m_jdbcTemplate.getJdbcTemplate().query(selectQuery, ms, mulID); 
      return mul; 
   }

   @Override
   public void saveMultiplication(Multiplication multiplication)
   {
      String insertQuery = "INSERT INTO MULTIPLICATION (MULTIPLICATION_ID, OPERAND_1, OPERAND_2) VALUES (:multiplicationID, :operand1, :operand2)"; 
      HashMap<String, String> keyValMap = new HashMap<String, String>(); 
      keyValMap.put("multiplicationID", Integer.toString(MultiplicationRepoServiceImpl.mulID)); 
      keyValMap.put("operand1", Integer.toString(multiplication.getOperand1())); 
      keyValMap.put("operand2", Integer.toString(multiplication.getOperand2()));
      
      this.m_jdbcTemplate.update(insertQuery, keyValMap); 
      
      // Increment counter and update the STATIC_CONFIGURATION_TABLE
      MultiplicationRepoServiceImpl.mulID++; 
      updateID(MultiplicationRepoServiceImpl.mulID);
   }
   
   private Multiplication mapMultiplication(ResultSet rs) 
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
      m_jdbcTemplate.getJdbcOperations().update(updateQuery, Integer.toString(multiplicationID)); 
   }
}
