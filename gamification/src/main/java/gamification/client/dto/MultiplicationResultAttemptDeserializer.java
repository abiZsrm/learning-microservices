package gamification.client.dto;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
public class MultiplicationResultAttemptDeserializer extends JsonDeserializer<MultiplicationResultAttempt>
{
   // TODO - Change factorA, factorB to operand1, operand2. 
   @Override
   public MultiplicationResultAttempt deserialize(JsonParser jsonParser,
                                                  DeserializationContext deserializationContext)
           throws IOException, JsonProcessingException {
       ObjectCodec oc = jsonParser.getCodec();
       JsonNode node = oc.readTree(jsonParser);
       return new MultiplicationResultAttempt(node.get("user").get("alias").asText(),
               node.get("multiplication").get("operand1").asInt(),
               node.get("multiplication").get("operand2").asInt(),
               node.get("resultAttempt").asInt(),
               node.get("correct").asBoolean());
   }

}
