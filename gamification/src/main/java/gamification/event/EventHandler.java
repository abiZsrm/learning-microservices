package gamification.event;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import gamification.service.GameService;

@Component
class EventHandler
{
   private GameService gameService;

   EventHandler( final GameService gameService )
   {
      this.gameService = gameService;
   }

   @RabbitListener(queues = "${multiplication.queue}")
   void handleMultiplicationSolved(final MultiplicationSolvedEvent event)
   {
      try
      {
         System.out.println("handling event");
         gameService.newAttemptForUser(event.getUserId(), event.getMultiplicationResultAttemptID(), event.isCorrect());
      }
      catch (final Exception e)
      {
         // Avoids the event to be re-queued and reprocessed.
         throw new AmqpRejectAndDontRequeueException(e);
      }
   }
}