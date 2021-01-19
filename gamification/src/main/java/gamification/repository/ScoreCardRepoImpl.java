package gamification.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import gamification.domain.Leaderboard;
import gamification.domain.ScoreCard;

@Repository
public class ScoreCardRepoImpl implements ScoreCardRepository
{
   private NamedParameterJdbcTemplate m_namedJdbcTemplate; 
   
   public static int scoreCardID; 
   
   @Autowired
   public ScoreCardRepoImpl(NamedParameterJdbcTemplate namedJdbcTemplate)
   {
      this.m_namedJdbcTemplate = namedJdbcTemplate; 
   }
   
   @PostConstruct
   private void retrieveStaticID()
   {
      String selectQuery = "SELECT CONFIGURATION_VALUE FROM STATIC_CONFIGURATION WHERE CONFIGURATION_NAME = 'SCORE_CARD_ID_COUNT'"; 
      String output = null; 
      output = m_namedJdbcTemplate.getJdbcTemplate().queryForObject(selectQuery.toString(), String.class); 
      
      // Retrieving static counter from the STATIC_CONFIGURATION table. 
      ScoreCardRepoImpl.scoreCardID = Integer.parseInt(output); 
   }
   
   public void save(ScoreCard scoreCard)
   {
      StringBuilder insertSQL = new StringBuilder(); 
      insertSQL.append("INSERT INTO SCORE_CARD "); 
      insertSQL.append("( CARD_ID, USER_ID, ATTEMPT_ID, SCORE) "); 
      insertSQL.append("VALUES "); 
      insertSQL.append("(:card_id, :user_id, :attempt_id, :score) "); 
      
      HashMap<String, Integer> insertSQLValues = new HashMap<String, Integer>(); 
      insertSQLValues.put("card_id", scoreCard.getCardId()); 
      insertSQLValues.put("user_id", scoreCard.getUserId()); 
      insertSQLValues.put("attempt_id", scoreCard.getAttemptId()); 
      insertSQLValues.put("score", scoreCard.getScore());
      
      m_namedJdbcTemplate.update(insertSQL.toString(), insertSQLValues);
      
      // Increment counter and update the STATIC_CONFIGURATION_TABLE
      ScoreCardRepoImpl.scoreCardID++; 
      updateID(ScoreCardRepoImpl.scoreCardID);
   }

   public ScoreCard findScoreCardByID(int scoreCardID)
   {
      String selectQuery = "SELECT * FROM SCORE_CARD WHERE CARD_ID = ?"; 
      ScoreCard sc = m_namedJdbcTemplate.getJdbcTemplate().queryForObject(selectQuery, this::mapScoreCard, scoreCardID); 
      return sc; 
   }
   
   @Override
   public int getTotalScoreForUser(int userId)
   {
      String totalScore = "SELECT SUM(SCORE) FROM SCORE_CARD WHERE USER_ID = ?";
      return m_namedJdbcTemplate.getJdbcTemplate().queryForObject(totalScore, Integer.class, userId); 
   }

   @Override
   public List<Leaderboard> findFirst10()
   {
      String findFirst10 = "SELECT SUM(SCORE) AS TOTAL_SCORE, USER_ID FROM SCORE_CARD GROUP BY (USER_ID)";
      return m_namedJdbcTemplate.getJdbcTemplate().query(findFirst10, this::mapLb); 
   }

   @Override
   public List<ScoreCard> findByUserId(Long userId)
   {
      String selectQueryForScoreCard = "SELECT * FROM SCORE_CARD WHERE USER_ID = ?";
      
      return m_namedJdbcTemplate.getJdbcTemplate().query(selectQueryForScoreCard, this::mapScoreCard); 
   }
   
   // Utility Methods // 
   private ScoreCard mapScoreCard(ResultSet rs, int rowNum) throws SQLException
   {
      int cardID = rs.getInt("CARD_ID");
      int userID = rs.getInt("USER_ID");
      int attemptID = rs.getInt("ATTEMPT_ID");
      int score = rs.getInt("SCORE");

      return new ScoreCard(cardID, userID, attemptID, score);
   }

   private Leaderboard mapLb(ResultSet rs, int rowNum) throws SQLException
   {
      long totalScore = rs.getInt("TOTAL_SCORE"); 
      int userID = rs.getInt("USER_ID"); 
      
      return new Leaderboard(userID, totalScore); 
   }
   private void updateID(int scoreCardID)
   {
      String updateQuery = "UPDATE STATIC_CONFIGURATION SET CONFIGURATION_VALUE = ? WHERE CONFIGURATION_NAME = 'SCORE_CARD_ID_COUNT'";
      m_namedJdbcTemplate.getJdbcOperations().update(updateQuery, Integer.toString(scoreCardID));
   }
}
