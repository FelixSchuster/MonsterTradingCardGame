package at.fhtw;

import at.fhtw.mtcg.service._package.PackageService;
import at.fhtw.mtcg.service.battle.BattleService;
import at.fhtw.mtcg.service.battlelog.BattlelogService;
import at.fhtw.mtcg.service.card.CardService;
import at.fhtw.mtcg.service.deck.DeckService;
import at.fhtw.mtcg.service.scoreboard.ScoreboardService;
import at.fhtw.mtcg.service.session.SessionService;
import at.fhtw.mtcg.service.stats.StatsService;
import at.fhtw.mtcg.service.trading.TradingService;
import at.fhtw.mtcg.service.transaction.TransactionService;
import at.fhtw.mtcg.service.user.UserService;
import at.fhtw.server.server.Server;
import at.fhtw.server.utils.Router;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(10001, configureRouter());
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static Router configureRouter() {
        Router router = new Router();

        router.addService("/users", new UserService());
        router.addService("/sessions", new SessionService());
        router.addService("/packages", new PackageService());
        router.addService("/transactions", new TransactionService());
        router.addService("/cards", new CardService());
        router.addService("/deck", new DeckService());
        router.addService("/stats", new StatsService());
        router.addService("/score", new ScoreboardService());
        router.addService("/battles", new BattleService());
        router.addService("/battlelogs", new BattlelogService());
        router.addService("/tradings", new TradingService());

        return router;
    }
}
