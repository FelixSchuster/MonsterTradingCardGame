package at.fhtw.mtcg.service.battle;

import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.*;
import at.fhtw.mtcg.exception.DataAccessException;
import at.fhtw.mtcg.exception.DataNotFoundException;
import at.fhtw.mtcg.exception.InvalidTokenException;
import at.fhtw.mtcg.exception.NoRunningBattleException;
import at.fhtw.mtcg.model.Card;
import at.fhtw.mtcg.model.UserData;
import at.fhtw.server.http.ContentType;
import at.fhtw.server.http.HttpStatus;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BattleController extends Controller {
    public synchronized Response battle(Request request) {
        UnitOfWork unitOfWork = new UnitOfWork();
        BattleRepository battleRepository = new BattleRepository(unitOfWork);
        SessionRepository sessionRepository = new SessionRepository(unitOfWork);
        DeckRepository deckRepository = new DeckRepository(unitOfWork);
        CardRepository cardRepository = new CardRepository(unitOfWork);
        UserRepository userRepository = new UserRepository(unitOfWork);
        BattlelogRepository battlelogRepository = new BattlelogRepository(unitOfWork);
        String token = request.getHeaderMap().getAuthorizationTokenHeader();

        try {
            int userId = sessionRepository.checkForValidToken(token); // check for valid token
            int deckId = deckRepository.getDeckIdByUserId(userId);

            try { // assuming a battle already exists
                int deck2Id = deckId;
                int battleId = battleRepository.getRunningBattle();
                int deck1Id = battleRepository.getDeck1IdByBattleId(battleId);

                int user1Id = cardRepository.getUserIdByDeckId(deck1Id);
                int user2Id = userId;

                UserData user1 = userRepository.getUserDataByUserId(user1Id);
                UserData user2 = userRepository.getUserDataByUserId(user2Id);

                battleRepository.updateDeck2IdByBattleId(battleId, deckId);

                if(deck1Id == deck2Id) {
                    throw new NoRunningBattleException("User is battling himself.");
                }

                List<String> deck1CardIds = cardRepository.getCardIdsByDeckId(deck1Id);
                List<String> deck2CardIds = cardRepository.getCardIdsByDeckId(deck2Id);

                List<Card> deck1Cards = new ArrayList<>();
                List<Card> deck2Cards = new ArrayList<>();

                for(String cardId : deck1CardIds) {
                    deck1Cards.add(cardRepository.getCardByCardId(cardId));
                }

                for(String cardId : deck2CardIds) {
                    deck2Cards.add(cardRepository.getCardByCardId(cardId));
                }

                Collections.shuffle(deck1Cards);
                Collections.shuffle(deck2Cards);

                int user1Wins = 0;
                int user2Wins = 0;

                String battleLog = "# Battle: " + user1.getName() + " vs " + user2.getName() + "\n\r";

                for(int i = 0; i < deck1Cards.size(); ++i) {
                    Card card1 = deck1Cards.get(i);
                    Card card2 = deck2Cards.get(i);

                    float card1Damage = card1.calculateDamage(card2);
                    float card2Damage = card2.calculateDamage(card1);

                    battleLog += "# Round: " + (i + 1) + "\n\r";
                    battleLog += user1.getName() + ": " + card1.getName() + " - " + card1.getDamage() + "\n\r";
                    battleLog += user2.getName() + ": " + card2.getName() + " - " + card2.getDamage() + "\n\r";

                    if(card1Damage > card2Damage) {
                        battleLog += user1.getName() + " wins\n\r\n\r";
                        user1Wins += 1;
                    }
                    if(card1Damage < card2Damage) {
                        battleLog += user2.getName() + " wins\n\r\n\r";
                        user2Wins += 1;
                    }
                    if(card1Damage == card2Damage) {
                        battleLog += "Draw\n\r\n\r";
                    }
                }

                battleLog += "# Final result:\n\r";
                battleLog += user1.getName() + ": " + user1Wins + " wins\n\r";
                battleLog += user2.getName() + ": " + user2Wins + " wins\n\r";

                if(user1Wins > user2Wins) {
                    battleLog += "Final result: " + user1.getName() + " wins!";
                }
                if(user1Wins < user2Wins) {
                    battleLog += "Final result: " + user2.getName() + " wins!";
                }
                if(user1Wins == user2Wins) {
                    battleLog += "Final result: draw!";
                }

                battlelogRepository.createBattlelog(battleId, battleLog);

                unitOfWork.commitTransaction();
                return new Response(HttpStatus.OK, ContentType.JSON, "{\"message\":\"The battle has been carried out successfully.\"}");

            } catch(NoRunningBattleException e) { // no battle exists
                battleRepository.createBattle(deckId);

                unitOfWork.commitTransaction();
                return new Response(HttpStatus.OK, ContentType.JSON, "{\"message\":\"The battle has been carried out successfully.\"}");
            }

        } catch(InvalidTokenException e) {
            // e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"message\":\"Authentication information is missing or invalid\"}");

        } catch(DataNotFoundException e) {
            // e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{\"message\":\"The request was fine, but the user doesn't have any cards\"}");

        } catch(DataAccessException e) {
            // e.printStackTrace();

        } catch(Exception e) {
            // e.printStackTrace();
        }

        unitOfWork.rollbackTransaction();
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\":\"Internal Server Error\"}");
    }
}
