package gamification.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import gamification.domain.Badge;
import gamification.domain.BadgeCard;

@Repository
public class BadgeCardRepoImpl implements BadgeCardRepository
{
   private NamedParameterJdbcTemplate m_namedJdbcTemplate; 
   
   public static int badgeCardID; 
   
   @Autowired
   public BadgeCardRepoImpl(NamedParameterJdbcTemplate namedJdbcTemplate)
   {
      this.m_namedJdbcTemplate = namedJdbcTemplate; 
   }
   
   @PostConstruct
   private void retrieveStaticID()
   {
      String selectQuery = "SELECT CONFIGURATION_VALUE FROM STATIC_CONFIGURATION WHERE CONFIGURATION_NAME = 'BADGE_CARD_ID_COUNT'"; 
      String output = null; 
      output = m_namedJdbcTemplate.getJdbcTemplate().queryForObject(selectQuery.toString(), String.class); 
      
      // Retrieving static counter from the STATIC_CONFIGURATION table. 
      BadgeCardRepoImpl.badgeCardID = Integer.parseInt(output); 
   }
   
   public void save(BadgeCard badge)
   {
      StringBuilder insertSQL = new StringBuilder(); 
      insertSQL.append("INSERT INTO BADGE_CARD "); 
      insertSQL.append("(BADGE_ID, USER_ID, BADGE) "); 
      insertSQL.append(" VALUES "); 
      insertSQL.append(" (:badge_id, :user_id, :badge) "); 
      
      HashMap<String, Object> sqlValues = new HashMap<String, Object>(); 
      sqlValues.put("badge_id", badge.getBadgeId()); 
      sqlValues.put("user_id", badge.getUserId()); 
      sqlValues.put("badge", badge.getBadge().toString()); 
      
      m_namedJdbcTemplate.update(insertSQL.toString(), sqlValues); 
      
      // Increment counter and update the STATIC_CONFIGURATION_TABLE
      BadgeCardRepoImpl.badgeCardID++; 
      updateID(BadgeCardRepoImpl.badgeCardID);
   }
   
   @Override
   public List<BadgeCard> findByUserId(int userId)
   {
      String selectQuery = "SELECT * FROM BADGE_CARD WHERE USER_ID = ?";
      
      List<BadgeCard> list = m_namedJdbcTemplate.getJdbcTemplate().query(selectQuery, this::mapRow, userId); 
      return list;
   }
   
   private BadgeCard mapRow(ResultSet rs, int rowNum) 
      throws SQLException 
   {
      Object[] rawInputData = { 
                                rs.getInt("BADGE_ID"),
                                rs.getInt("USER_ID"),                                
                                rs.getString("BADGE")
                              }; 

      return new BadgeCard( (Integer)rawInputData[0], 
                            (Integer)rawInputData[1], 
                            Badge.valueOf((String)rawInputData[2])); 
   }
   
   private void updateID(int scoreCardID)
   {
      String updateQuery = "UPDATE STATIC_CONFIGURATION SET CONFIGURATION_VALUE = ? WHERE CONFIGURATION_NAME = 'BADGE_CARD_ID_COUNT'";
      m_namedJdbcTemplate.getJdbcOperations().update(updateQuery, Integer.toString(scoreCardID));
   }
}
