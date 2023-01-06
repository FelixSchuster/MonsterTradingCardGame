package at.fhtw.mtcg;

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
import at.fhtw.server.http.ContentType;
import at.fhtw.server.http.Method;
import at.fhtw.server.server.HeaderMap;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MtcgTests {
    @Test
    @Order(1)
    void testCreateNewUser() {
        UserService userService = new UserService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/users");
        request.setBody("{\"Username\": \"kienboec\",\"Password\": \"daniel\"}");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = userService.handleRequest(request);

        assertEquals( "{\"message\":\"User successfully created\"}", response.getContent());
        assertEquals(201, response.getStatus());
    }
    @Test
    @Order(2)
    void testCreateAlreadyExistingUser() {
        UserService userService = new UserService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/users");
        request.setBody("{\"Username\": \"kienboec\",\"Password\": \"daniel\"}");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = userService.handleRequest(request);

        assertEquals( "{\"message\":\"User with same username already registered\"}", response.getContent());
        assertEquals(409, response.getStatus());
    }
    @Test
    @Order(3)
    void testGetUserDataWithInvalidToken() {
        UserService userService = new UserService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/users/kienboec");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = userService.handleRequest(request);

        assertEquals("{\"message\":\"Authentication information is missing or invalid\"}", response.getContent());
        assertEquals(401, response.getStatus());
    }
    @Test
    @Order(4)
    void testLoginWithInvalidCredentials() {
        SessionService sessionService = new SessionService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/sessions");
        request.setBody("{\"Username\":\"kienboec\",\"Password\":\"wrongpass\"}");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = sessionService.handleRequest(request);

        assertEquals("{\"message\":\"Invalid username/password provided\"}", response.getContent());
        assertEquals(401, response.getStatus());
    }
    @Test
    @Order(5)
    void testLoginWithValidCredentials() {
        SessionService sessionService = new SessionService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/sessions");
        request.setBody("{\"Username\":\"kienboec\",\"Password\":\"daniel\"}");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = sessionService.handleRequest(request);

        assertEquals("{\"token\":\"kienboec-mtcgToken\"}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(6)
    void testGetEmptyUserDataWithValidToken() {
        UserService userService = new UserService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/users/kienboec");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = userService.handleRequest(request);

        assertEquals("{\"name\":null,\"bio\":null,\"image\":null}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(7)
    void testLogout() {
        SessionService sessionService = new SessionService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.DELETE);
        request.setPathname("/sessions");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = sessionService.handleRequest(request);

        assertEquals("{\"message\":\"Logged out successfully\"}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(8)
    void testSetUserDataWithoutToken() {
        UserService userService = new UserService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.PUT);
        request.setPathname("/users/kienboec");
        // request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");
        request.setBody("{\"Name\":\"Hoax\",\"Bio\":\"me playin...\",\"Image\":\":-)\"}");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = userService.handleRequest(request);

        assertEquals("{\"message\":\"Authentication information is missing or invalid\"}", response.getContent());
        assertEquals(401, response.getStatus());
    }
    @Test
    @Order(9)
    void testReLoginWithValidCredentials() {
        SessionService sessionService = new SessionService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/sessions");
        request.setBody("{\"Username\":\"kienboec\",\"Password\":\"daniel\"}");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = sessionService.handleRequest(request);

        assertEquals("{\"token\":\"kienboec-mtcgToken\"}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(10)
    void testSetUserDataWithValidToken() {
        UserService userService = new UserService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.PUT);
        request.setPathname("/users/kienboec");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");
        request.setBody("{\"name\":\"Hoax\",\"bio\":\"me playin...\",\"image\":\":-)\"}");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = userService.handleRequest(request);

        assertEquals("{\"message\":\"User successfully updated.\"}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(11)
    void testGetUserDataWithValidToken() {
        UserService userService = new UserService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/users/kienboec");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = userService.handleRequest(request);

        assertEquals("{\"name\":\"Hoax\",\"bio\":\"me playin...\",\"image\":\":-)\"}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(12)
    void testCreateAdministratorAccount() {
        UserService userService = new UserService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/users");
        request.setBody("{\"Username\": \"admin\",\"Password\": \"istrator\"}");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = userService.handleRequest(request);

        assertEquals( "{\"message\":\"User successfully created\"}", response.getContent());
        assertEquals(201, response.getStatus());
    }
    @Test
    @Order(13)
    void testLoginAsAdministrator() {
        SessionService sessionService = new SessionService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/sessions");
        request.setBody("{\"Username\": \"admin\",\"Password\": \"istrator\"}");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = sessionService.handleRequest(request);

        assertEquals("{\"token\":\"admin-mtcgToken\"}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(14)
    void testSetUserDataAsAdministrator() {
        UserService userService = new UserService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.PUT);
        request.setPathname("/users/admin");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic admin-mtcgToken");
        request.setBody("{\"name\":\"Admin\",\"bio\":\"Hey there!\",\"image\":\":D\"}");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = userService.handleRequest(request);

        assertEquals("{\"message\":\"User successfully updated.\"}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(15)
    void testCreatePackageAsNonAdministrator() {
        PackageService packageService = new PackageService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/packages");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");
        request.setBody("[{\"Id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"Name\":\"WaterGoblin\",\"Damage\":10.0},"+
                "{\"Id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"Name\":\"Dragon\",\"Damage\":50.0},"+
                "{\"Id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\",\"Name\":\"WaterSpell\",\"Damage\":20.0},"+
                "{\"Id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"Name\":\"Ork\",\"Damage\":45.0},"+
                "{\"Id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\",\"Name\":\"FireSpell\",\"Damage\":25.0}]");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = packageService.handleRequest(request);

        assertEquals("{\"message\":\"Provided user is not 'admin'\"}", response.getContent());
        assertEquals(403, response.getStatus());
    }
    @Test
    @Order(16)
    void testCreatePackageAsAdministrator() {
        PackageService packageService = new PackageService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/packages");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic admin-mtcgToken");
        request.setBody("[{\"Id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"Name\":\"WaterGoblin\",\"Damage\":10.0},"+
                "{\"Id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"Name\":\"Dragon\",\"Damage\":50.0},"+
                "{\"Id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\",\"Name\":\"WaterSpell\",\"Damage\":20.0},"+
                "{\"Id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"Name\":\"Ork\",\"Damage\":45.0},"+
                "{\"Id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\",\"Name\":\"FireSpell\",\"Damage\":25.0}]");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = packageService.handleRequest(request);

        assertEquals("{\"message\":\"Package and cards successfully created\"}", response.getContent());
        assertEquals(201, response.getStatus());
    }
    @Test
    @Order(17)
    void testCreatePackageWithExistingCardAsAdministrator() {
        PackageService packageService = new PackageService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/packages");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic admin-mtcgToken");
        request.setBody("[{\"Id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"Name\":\"WaterGoblin\",\"Damage\":10.0},"+
                "{\"Id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"Name\":\"Dragon\",\"Damage\":50.0},"+
                "{\"Id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\",\"Name\":\"WaterSpell\",\"Damage\":20.0},"+
                "{\"Id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"Name\":\"Ork\",\"Damage\":45.0},"+
                "{\"Id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\",\"Name\":\"FireSpell\",\"Damage\":25.0}]");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = packageService.handleRequest(request);

        assertEquals("{\"message\":\"At least one card in the packages already exists\"}", response.getContent());
        assertEquals(409, response.getStatus());
    }
    @Test
    @Order(18)
    void testAcquireAnExistingPackage() {
        TransactionService transactionService = new TransactionService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/transactions/packages");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = transactionService.handleRequest(request);

        assertEquals("{\"message\":\"A package has been successfully bought\"}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(19)
    void testAcquireANonExistingPackage() {
        TransactionService transactionService = new TransactionService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/transactions/packages");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = transactionService.handleRequest(request);

        assertEquals("{\"message\":\"No card package available for buying\"}", response.getContent());
        assertEquals(404, response.getStatus());
    }
    @Test
    @Order(20)
    void testCreateMorePackagesAsAdministrator() {
        PackageService packageService = new PackageService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/packages");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic admin-mtcgToken");
        request.setBody("[{\"Id\":\"644808c2-f87a-4600-b313-122b02322fd5\",\"Name\":\"WaterGoblin\",\"Damage\":9.0},"+
                "{\"Id\":\"4a2757d6-b1c3-47ac-b9a3-91deab093531\",\"Name\":\"Dragon\",\"Damage\":55.0},"+
                "{\"Id\":\"91a6471b-1426-43f6-ad65-6fc473e16f9f\",\"Name\":\"WaterSpell\",\"Damage\":21.0},"+
                "{\"Id\":\"4ec8b269-0dfa-4f97-809a-2c63fe2a0025\",\"Name\":\"Ork\",\"Damage\":55.0},"+
                "{\"Id\":\"f8043c23-1534-4487-b66b-238e0c3c39b5\",\"Name\":\"WaterSpell\",\"Damage\":23.0}]");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = packageService.handleRequest(request);

        assertEquals("{\"message\":\"Package and cards successfully created\"}", response.getContent());
        assertEquals(201, response.getStatus());

        request.setBody("[{\"Id\":\"b017ee50-1c14-44e2-bfd6-2c0c5653a37c\",\"Name\":\"WaterGoblin\",\"Damage\":11.0},"+
                        "{\"Id\":\"d04b736a-e874-4137-b191-638e0ff3b4e7\",\"Name\":\"Dragon\",\"Damage\":70.0},"+
                        "{\"Id\":\"88221cfe-1f84-41b9-8152-8e36c6a354de\",\"Name\":\"WaterSpell\",\"Damage\":22.0},"+
                        "{\"Id\":\"1d3f175b-c067-4359-989d-96562bfa382c\",\"Name\":\"Ork\",\"Damage\":40.0},"+
                        "{\"Id\":\"171f6076-4eb5-4a7d-b3f2-2d650cc3d237\",\"Name\":\"RegularSpell\",\"Damage\":28.0}]");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        response = packageService.handleRequest(request);

        assertEquals("{\"message\":\"Package and cards successfully created\"}", response.getContent());
        assertEquals(201, response.getStatus());

        request.setBody("[{\"Id\":\"ed1dc1bc-f0aa-4a0c-8d43-1402189b33c8\",\"Name\":\"WaterGoblin\",\"Damage\":10.0},"+
                "{\"Id\":\"65ff5f23-1e70-4b79-b3bd-f6eb679dd3b5\",\"Name\":\"Dragon\",\"Damage\":50.0},"+
                "{\"Id\":\"55ef46c4-016c-4168-bc43-6b9b1e86414f\",\"Name\":\"WaterSpell\",\"Damage\":20.0},"+
                "{\"Id\":\"f3fad0f2-a1af-45df-b80d-2e48825773d9\",\"Name\":\"Ork\",\"Damage\":45.0},"+
                "{\"Id\":\"8c20639d-6400-4534-bd0f-ae563f11f57a\",\"Name\":\"WaterSpell\",\"Damage\":25.0}]");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        response = packageService.handleRequest(request);

        assertEquals("{\"message\":\"Package and cards successfully created\"}", response.getContent());
        assertEquals(201, response.getStatus());

        request.setBody("[{\"Id\":\"d7d0cb94-2cbf-4f97-8ccf-9933dc5354b8\",\"Name\":\"WaterGoblin\",\"Damage\":0}," +
                "{\"Id\":\"44c82fbc-ef6d-44ab-8c7a-9fb19a0e7c6e\",\"Name\":\"Dragon\",\"Damage\":0}," +
                "{\"Id\":\"2c98cd06-518b-464c-b911-8d787216cddd\",\"Name\":\"WaterSpell\",\"Damage\":0}," +
                "{\"Id\":\"951e886a-0fbf-425d-8df5-af2ee4830d85\",\"Name\":\"Ork\",\"Damage\":0}," +
                "{\"Id\":\"dcd93250-25a7-4dca-85da-cad2789f7198\",\"Name\":\"FireSpell\",\"Damage\":0}]");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        response = packageService.handleRequest(request);

        assertEquals("{\"message\":\"Package and cards successfully created\"}", response.getContent());
        assertEquals(201, response.getStatus());

        request.setBody("[{\"Id\":\"b2237eca-0271-43bd-87f6-b22f70d42ca4\",\"Name\":\"WaterGoblin\",\"Damage\":0}," +
                "{\"Id\":\"9e8238a4-8a7a-487f-9f7d-a8c97899eb48\",\"Name\":\"Dragon\",\"Damage\":0}," +
                "{\"Id\":\"d60e23cf-2238-4d49-844f-c7589ee5342e\",\"Name\":\"WaterSpell\",\"Damage\":0}," +
                "{\"Id\":\"fc305a7a-36f7-4d30-ad27-462ca0445649\",\"Name\":\"Ork\",\"Damage\":0}," +
                "{\"Id\":\"84d276ee-21ec-4171-a509-c1b88162831c\",\"Name\":\"RegularSpell\",\"Damage\":0}]");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        response = packageService.handleRequest(request);

        assertEquals("{\"message\":\"Package and cards successfully created\"}", response.getContent());
        assertEquals(201, response.getStatus());
    }
    @Test
    @Order(21)
    void testAcquireMoreExistingPackages() {
        TransactionService transactionService = new TransactionService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/transactions/packages");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = transactionService.handleRequest(request);

        assertEquals("{\"message\":\"A package has been successfully bought\"}", response.getContent());
        assertEquals(200, response.getStatus());

        response = transactionService.handleRequest(request);

        assertEquals("{\"message\":\"A package has been successfully bought\"}", response.getContent());
        assertEquals(200, response.getStatus());

        response = transactionService.handleRequest(request);

        assertEquals("{\"message\":\"A package has been successfully bought\"}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(22)
    void testAcquireAPackageWithoutCoins() {
        TransactionService transactionService = new TransactionService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/transactions/packages");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = transactionService.handleRequest(request);

        assertEquals("{\"message\":\"Not enough money for buying a card package\"}", response.getContent());
        assertEquals(403, response.getStatus());
    }
    @Test
    @Order(23)
    void testShowCardsButUserHasNoCards() {
        CardService cardService = new CardService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/cards");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic admin-mtcgToken");

        Response response = cardService.handleRequest(request);

        assertEquals("{\"message\":\"The request was fine, but the user doesn't have any cards\"}", response.getContent());
        assertEquals(204, response.getStatus());
    }
    @Test
    @Order(24)
    void testAcquirePackagesAsAdmin() {
        TransactionService transactionService = new TransactionService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/transactions/packages");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic admin-mtcgToken");

        Response response = transactionService.handleRequest(request);

        assertEquals("{\"message\":\"A package has been successfully bought\"}", response.getContent());
        assertEquals(200, response.getStatus());

        response = transactionService.handleRequest(request);

        assertEquals("{\"message\":\"A package has been successfully bought\"}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(25)
    void testShowCards() {
        CardService cardService = new CardService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/cards");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = cardService.handleRequest(request);

        assertEquals("[{\"id\":\"f8043c23-1534-4487-b66b-238e0c3c39b5\",\"name\":\"WaterSpell\",\"damage\":23.0}," +
                "{\"id\":\"f3fad0f2-a1af-45df-b80d-2e48825773d9\",\"name\":\"Ork\",\"damage\":45.0}," +
                "{\"id\":\"ed1dc1bc-f0aa-4a0c-8d43-1402189b33c8\",\"name\":\"WaterGoblin\",\"damage\":10.0}," +
                "{\"id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\",\"name\":\"WaterSpell\",\"damage\":20.0}," +
                "{\"id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\",\"name\":\"FireSpell\",\"damage\":25.0}," +
                "{\"id\":\"d04b736a-e874-4137-b191-638e0ff3b4e7\",\"name\":\"Dragon\",\"damage\":70.0}," +
                "{\"id\":\"b017ee50-1c14-44e2-bfd6-2c0c5653a37c\",\"name\":\"WaterGoblin\",\"damage\":11.0}," +
                "{\"id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"name\":\"Dragon\",\"damage\":50.0}," +
                "{\"id\":\"91a6471b-1426-43f6-ad65-6fc473e16f9f\",\"name\":\"WaterSpell\",\"damage\":21.0}," +
                "{\"id\":\"8c20639d-6400-4534-bd0f-ae563f11f57a\",\"name\":\"WaterSpell\",\"damage\":25.0}," +
                "{\"id\":\"88221cfe-1f84-41b9-8152-8e36c6a354de\",\"name\":\"WaterSpell\",\"damage\":22.0}," +
                "{\"id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"name\":\"WaterGoblin\",\"damage\":10.0}," +
                "{\"id\":\"65ff5f23-1e70-4b79-b3bd-f6eb679dd3b5\",\"name\":\"Dragon\",\"damage\":50.0}," +
                "{\"id\":\"644808c2-f87a-4600-b313-122b02322fd5\",\"name\":\"WaterGoblin\",\"damage\":9.0}," +
                "{\"id\":\"55ef46c4-016c-4168-bc43-6b9b1e86414f\",\"name\":\"WaterSpell\",\"damage\":20.0}," +
                "{\"id\":\"4ec8b269-0dfa-4f97-809a-2c63fe2a0025\",\"name\":\"Ork\",\"damage\":55.0}," +
                "{\"id\":\"4a2757d6-b1c3-47ac-b9a3-91deab093531\",\"name\":\"Dragon\",\"damage\":55.0}," +
                "{\"id\":\"1d3f175b-c067-4359-989d-96562bfa382c\",\"name\":\"Ork\",\"damage\":40.0}," +
                "{\"id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"name\":\"Ork\",\"damage\":45.0}," +
                "{\"id\":\"171f6076-4eb5-4a7d-b3f2-2d650cc3d237\",\"name\":\"RegularSpell\",\"damage\":28.0}]", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(26)
    void testShowDeckButUserHasNoCardsInDeck() {
        DeckService deckService = new DeckService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/deck");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = deckService.handleRequest(request);

        assertEquals("{\"message\":\"The request was fine, but the user doesn't have any cards\"}", response.getContent());
        assertEquals(204, response.getStatus());
    }
    @Test
    @Order(27)
    void testConfigureDeckButProvidedCardDoesNotBelongToUser() {
        DeckService deckService = new DeckService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.PUT);
        request.setPathname("/deck");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic admin-mtcgToken");
        request.setBody("[\"845f0dc7-37d0-426e-994e-43fc3ac83c08\"," +
                "\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\"," +
                "\"e85e3976-7c86-4d06-9a80-641c2019a79f\"," +
                "\"171f6076-4eb5-4a7d-b3f2-2d650cc3d237\"]");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = deckService.handleRequest(request);

        assertEquals("{\"message\":\"At least one of the provided cards does not belong to the user or is not available.\"}", response.getContent());
        assertEquals(403, response.getStatus());
    }
    @Test
    @Order(28)
    void testConfigureDeck() {
        DeckService deckService = new DeckService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.PUT);
        request.setPathname("/deck");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");
        request.setBody("[\"845f0dc7-37d0-426e-994e-43fc3ac83c08\"," +
                "\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\"," +
                "\"e85e3976-7c86-4d06-9a80-641c2019a79f\"," +
                "\"171f6076-4eb5-4a7d-b3f2-2d650cc3d237\"]");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = deckService.handleRequest(request);

        assertEquals("{\"message\":\"The deck has been successfully configured\"}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(29)
    void testShowDeckJson() {
        DeckService deckService = new DeckService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/deck");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = deckService.handleRequest(request);

        assertEquals("[{\"id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"name\":\"WaterGoblin\",\"damage\":10.0}," +
                "{\"id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"name\":\"Dragon\",\"damage\":50.0}," +
                "{\"id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\",\"name\":\"WaterSpell\",\"damage\":20.0}," +
                "{\"id\":\"171f6076-4eb5-4a7d-b3f2-2d650cc3d237\",\"name\":\"RegularSpell\",\"damage\":28.0}]", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(30)
    void testShowDeckTextPlain() {
        DeckService deckService = new DeckService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/deck?format=plain");
        request.setParams("format=plain");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = deckService.handleRequest(request);

        assertEquals("text/plain", response.getContentType());
        assertEquals("845f0dc7-37d0-426e-994e-43fc3ac83c08 - WaterGoblin - 10.0\n" +
                "99f8f8dc-e25e-4a95-aa2c-782823f36e2a - Dragon - 50.0\n" +
                "e85e3976-7c86-4d06-9a80-641c2019a79f - WaterSpell - 20.0\n" +
                "171f6076-4eb5-4a7d-b3f2-2d650cc3d237 - RegularSpell - 28.0\n", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(31)
    void testConfigureDeckInvalidCardAmount() {
        DeckService deckService = new DeckService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.PUT);
        request.setPathname("/deck");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");
        request.setBody("[\"845f0dc7-37d0-426e-994e-43fc3ac83c08\"," +
                "\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\"]");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = deckService.handleRequest(request);

        assertEquals("{\"message\":\"The provided deck did not include the required amount of cards\"}", response.getContent());
        assertEquals(400, response.getStatus());
    }
    @Test
    @Order(32)
    void testConfigureDeckAsAdministrator() {
        DeckService deckService = new DeckService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.PUT);
        request.setPathname("/deck");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic admin-mtcgToken");
        request.setBody("[\"44c82fbc-ef6d-44ab-8c7a-9fb19a0e7c6e\"," +
                "\"2c98cd06-518b-464c-b911-8d787216cddd\"," +
                "\"951e886a-0fbf-425d-8df5-af2ee4830d85\"," +
                "\"dcd93250-25a7-4dca-85da-cad2789f7198\"]");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = deckService.handleRequest(request);

        assertEquals("{\"message\":\"The deck has been successfully configured\"}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(33)
    void testShowStatsBeforeBattle() {
        StatsService statsService = new StatsService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/stats");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = statsService.handleRequest(request);

        assertEquals("{\"name\":\"Hoax\",\"elo\":100,\"wins\":0,\"losses\":0}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(34)
    void testShowStatsInvalidToken() {
        StatsService statsService = new StatsService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/stats");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic invalid-mtcgToken");

        Response response = statsService.handleRequest(request);

        assertEquals("{\"message\":\"Authentication information is missing or invalid\"}", response.getContent());
        assertEquals(401, response.getStatus());
    }
    @Test
    @Order(35)
    void testShowScoreboardBeforeBattle() {
        ScoreboardService scoreboardService = new ScoreboardService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/scoreboard");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = scoreboardService.handleRequest(request);

        assertEquals("[{\"name\":\"Hoax\",\"elo\":100,\"wins\":0,\"losses\":0}," +
                "{\"name\":\"Admin\",\"elo\":100,\"wins\":0,\"losses\":0}]", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(36)
    void testShowScoreboardInvalidToken() {
        ScoreboardService scoreboardService = new ScoreboardService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/scoreboard");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic invalid-mtcgToken");

        Response response = scoreboardService.handleRequest(request);

        assertEquals("{\"message\":\"Authentication information is missing or invalid\"}", response.getContent());
        assertEquals(401, response.getStatus());
    }
    @Test
    @Order(37)
    void testGetBattlelogWithoutBattle() {
        BattlelogService battlelogService = new BattlelogService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/battlelogs");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = battlelogService.handleRequest(request);

        assertEquals("{\"message\":\"The request was fine, but the user has not battled yet\"}", response.getContent());
        assertEquals(204, response.getStatus());
    }
    @Test
    @Order(38)
    void testBattleAsFirstPlayer() {
        BattleService battleService = new BattleService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/battles");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = battleService.handleRequest(request);

        assertEquals("{\"message\":\"Waiting for another Player.\"}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(39)
    void testBattleAsSecondPlayer() {
        BattleService battleService = new BattleService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/battles");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic admin-mtcgToken");

        Response response = battleService.handleRequest(request);

        assertEquals("{\"message\":\"The battle has been carried out successfully.\"}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(40)
    void testGetBattlelogAsFirstPlayer() {
        BattlelogService battlelogService = new BattlelogService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/battlelogs");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = battlelogService.handleRequest(request);

        assertTrue(response.getContent().startsWith("# Battle: Hoax vs Admin"));
        assertTrue(response.getContent().endsWith("Final result: Hoax wins!"));
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(41)
    void testGetBattlelogAsSecondPlayer() {
        BattlelogService battlelogService = new BattlelogService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/battlelogs");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic admin-mtcgToken");

        Response response = battlelogService.handleRequest(request);

        assertTrue(response.getContent().startsWith("# Battle: Hoax vs Admin"));
        assertTrue(response.getContent().endsWith("Final result: Hoax wins!"));
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(42)
    void testGetBattlelogInvalidToken() {
        BattlelogService battlelogService = new BattlelogService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/battlelogs");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic invalid-mtcgToken");

        Response response = battlelogService.handleRequest(request);

        assertEquals("{\"message\":\"Authentication information is missing or invalid\"}", response.getContent());
        assertEquals(401, response.getStatus());
    }
    @Test
    @Order(43)
    void testShowCardsAfterBattle() {
        CardService cardService = new CardService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/cards");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = cardService.handleRequest(request);

        assertEquals("[{\"id\":\"f8043c23-1534-4487-b66b-238e0c3c39b5\",\"name\":\"WaterSpell\",\"damage\":23.0}," +
                "{\"id\":\"f3fad0f2-a1af-45df-b80d-2e48825773d9\",\"name\":\"Ork\",\"damage\":45.0}," +
                "{\"id\":\"ed1dc1bc-f0aa-4a0c-8d43-1402189b33c8\",\"name\":\"WaterGoblin\",\"damage\":10.0}," +
                "{\"id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\",\"name\":\"WaterSpell\",\"damage\":20.0}," +
                "{\"id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\",\"name\":\"FireSpell\",\"damage\":25.0}," +
                "{\"id\":\"dcd93250-25a7-4dca-85da-cad2789f7198\",\"name\":\"FireSpell\",\"damage\":0.0}," +
                "{\"id\":\"d04b736a-e874-4137-b191-638e0ff3b4e7\",\"name\":\"Dragon\",\"damage\":70.0}," +
                "{\"id\":\"b017ee50-1c14-44e2-bfd6-2c0c5653a37c\",\"name\":\"WaterGoblin\",\"damage\":11.0}," +
                "{\"id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"name\":\"Dragon\",\"damage\":50.0}," +
                "{\"id\":\"951e886a-0fbf-425d-8df5-af2ee4830d85\",\"name\":\"Ork\",\"damage\":0.0}," +
                "{\"id\":\"91a6471b-1426-43f6-ad65-6fc473e16f9f\",\"name\":\"WaterSpell\",\"damage\":21.0}," +
                "{\"id\":\"8c20639d-6400-4534-bd0f-ae563f11f57a\",\"name\":\"WaterSpell\",\"damage\":25.0}," +
                "{\"id\":\"88221cfe-1f84-41b9-8152-8e36c6a354de\",\"name\":\"WaterSpell\",\"damage\":22.0}," +
                "{\"id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"name\":\"WaterGoblin\",\"damage\":10.0}," +
                "{\"id\":\"65ff5f23-1e70-4b79-b3bd-f6eb679dd3b5\",\"name\":\"Dragon\",\"damage\":50.0}," +
                "{\"id\":\"644808c2-f87a-4600-b313-122b02322fd5\",\"name\":\"WaterGoblin\",\"damage\":9.0}," +
                "{\"id\":\"55ef46c4-016c-4168-bc43-6b9b1e86414f\",\"name\":\"WaterSpell\",\"damage\":20.0}," +
                "{\"id\":\"4ec8b269-0dfa-4f97-809a-2c63fe2a0025\",\"name\":\"Ork\",\"damage\":55.0}," +
                "{\"id\":\"4a2757d6-b1c3-47ac-b9a3-91deab093531\",\"name\":\"Dragon\",\"damage\":55.0}," +
                "{\"id\":\"44c82fbc-ef6d-44ab-8c7a-9fb19a0e7c6e\",\"name\":\"Dragon\",\"damage\":0.0}," +
                "{\"id\":\"2c98cd06-518b-464c-b911-8d787216cddd\",\"name\":\"WaterSpell\",\"damage\":0.0}," +
                "{\"id\":\"1d3f175b-c067-4359-989d-96562bfa382c\",\"name\":\"Ork\",\"damage\":40.0}," +
                "{\"id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"name\":\"Ork\",\"damage\":45.0}," +
                "{\"id\":\"171f6076-4eb5-4a7d-b3f2-2d650cc3d237\",\"name\":\"RegularSpell\",\"damage\":28.0}]", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(44)
    void testGetTradingDealsButNoTradingDealAvailable() {
        TradingService tradingService = new TradingService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/tradings");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = tradingService.handleRequest(request);

        assertEquals("{\"message\":\"The request was fine, but there are no trading deals available\"}", response.getContent());
        assertEquals(204, response.getStatus());
    }
    @Test
    @Order(45)
    void testGetTradingDealsInvalidToken() {
        TradingService tradingService = new TradingService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/tradings");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic invalid-mtcgToken");

        Response response = tradingService.handleRequest(request);

        assertEquals("{\"message\":\"Authentication information is missing or invalid\"}", response.getContent());
        assertEquals(401, response.getStatus());
    }
    @Test
    @Order(46)
    void testCreateTradingDealCardNotOwnedByUser() {
        TradingService tradingService = new TradingService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/tradings");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");
        request.setBody("{\"Id\":\"6cd85277-4590-49d4-b0cf-ba0a921faad0\"," +
                "\"CardToTrade\":\"84d276ee-21ec-4171-a509-c1b88162831c\"," +
                "\"Type\":\"monster\"," +
                "\"MinimumDamage\":15}");

        Response response = tradingService.handleRequest(request);

        assertEquals("{\"message\":\"The deal contains a card that is not owned by the user or locked in the deck.\"}", response.getContent());
        assertEquals(403, response.getStatus());
    }
    @Test
    @Order(47)
    void testCreateTradingDealCardLockedInDeck() {
        TradingService tradingService = new TradingService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/tradings");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");
        request.setBody("{\"Id\":\"6cd85277-4590-49d4-b0cf-ba0a921faad0\"," +
                "\"CardToTrade\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\"," +
                "\"Type\":\"monster\"," +
                "\"MinimumDamage\":15}");

        Response response = tradingService.handleRequest(request);

        assertEquals("{\"message\":\"The deal contains a card that is not owned by the user or locked in the deck.\"}", response.getContent());
        assertEquals(403, response.getStatus());
    }
    @Test
    @Order(48)
    void testCreateTradingDealInvalidToken() {
        TradingService tradingService = new TradingService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/tradings");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic invalid-mtcgToken");
        request.setBody("{\"Id\":\"6cd85277-4590-49d4-b0cf-ba0a921faad0\"," +
                "\"CardToTrade\":\"84d276ee-21ec-4171-a509-c1b88162831c\"," +
                "\"Type\":\"monster\"," +
                "\"MinimumDamage\":15}");

        Response response = tradingService.handleRequest(request);

        assertEquals("{\"message\":\"Authentication information is missing or invalid\"}", response.getContent());
        assertEquals(401, response.getStatus());
    }
    @Test
    @Order(49)
    void testCreateTradingDeal() {
        TradingService tradingService = new TradingService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/tradings");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic admin-mtcgToken");
        request.setBody("{\"Id\":\"6cd85277-4590-49d4-b0cf-ba0a921faad0\"," +
                "\"CardToTrade\":\"84d276ee-21ec-4171-a509-c1b88162831c\"," +
                "\"Type\":\"monster\"," +
                "\"MinimumDamage\":15}");

        Response response = tradingService.handleRequest(request);

        assertEquals("{\"message\":\"Trading deal successfully created\"}", response.getContent());
        assertEquals(201, response.getStatus());
    }
    @Test
    @Order(50)
    void testCreateTradingDealWithExistingId() {
        TradingService tradingService = new TradingService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/tradings");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic admin-mtcgToken");
        request.setBody("{\"Id\":\"6cd85277-4590-49d4-b0cf-ba0a921faad0\"," +
                "\"CardToTrade\":\"84d276ee-21ec-4171-a509-c1b88162831c\"," +
                "\"Type\":\"monster\"," +
                "\"MinimumDamage\":15}");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = tradingService.handleRequest(request);

        assertEquals("{\"message\":\"A deal with this deal ID already exists.\"}", response.getContent());
        assertEquals(409, response.getStatus());
    }
    @Test
    @Order(51)
    void testDeleteTradingDealInvalidToken() {
        TradingService tradingService = new TradingService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.DELETE);
        request.setPathname("/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic invalid-mtcgToken");

        Response response = tradingService.handleRequest(request);

        assertEquals("{\"message\":\"Authentication information is missing or invalid\"}", response.getContent());
        assertEquals(401, response.getStatus());
    }
    @Test
    @Order(52)
    void testDeleteTradingDealByOtherUser() {
        TradingService tradingService = new TradingService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.DELETE);
        request.setPathname("/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = tradingService.handleRequest(request);

        assertEquals("{\"message\":\"The deal contains a card that is not owned by the user.\"}", response.getContent());
        assertEquals(403, response.getStatus());
    }
    @Test
    @Order(53)
    void testDeleteTradingDealInvalidId() {
        TradingService tradingService = new TradingService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.DELETE);
        request.setPathname("/tradings/invalid");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic admin-mtcgToken");

        Response response = tradingService.handleRequest(request);

        assertEquals("{\"message\":\"The provided deal ID was not found.\"}", response.getContent());
        assertEquals(404, response.getStatus());
    }
    @Test
    @Order(54)
    void testDeleteTradingDeal() {
        TradingService tradingService = new TradingService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.DELETE);
        request.setPathname("/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic admin-mtcgToken");

        Response response = tradingService.handleRequest(request);

        assertEquals("{\"message\":\"Trading deal successfully deleted\"}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(55)
    void testReCreateTradingDeal() {
        TradingService tradingService = new TradingService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.setPathname("/tradings");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic admin-mtcgToken");
        request.setBody("{\"Id\":\"6cd85277-4590-49d4-b0cf-ba0a921faad0\"," +
                "\"CardToTrade\":\"84d276ee-21ec-4171-a509-c1b88162831c\"," +
                "\"Type\":\"monster\"," +
                "\"MinimumDamage\":15}");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.JSON));

        Response response = tradingService.handleRequest(request);

        assertEquals("{\"message\":\"Trading deal successfully created\"}", response.getContent());
        assertEquals(201, response.getStatus());
    }
    @Test
    @Order(56)
    void testTradeInvalidToken() {
        TradingService tradingService = new TradingService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.getHeaderMap().setAuthorizationTokenHeader("Basic invalid-mtcgToken");
        request.setPathname("/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0");
        request.setBody("\"d04b736a-e874-4137-b191-638e0ff3b4e7\"");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.PLAIN_TEXT));

        Response response = tradingService.handleRequest(request);

        assertEquals("{\"message\":\"Authentication information is missing or invalid\"}", response.getContent());
        assertEquals(401, response.getStatus());
    }
    @Test
    @Order(57)
    void testTradeCardNotOwnedByUser() {
        TradingService tradingService = new TradingService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");
        request.setPathname("/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0");
        request.setBody("\"84d276ee-21ec-4171-a509-c1b88162831c\"");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.PLAIN_TEXT));

        Response response = tradingService.handleRequest(request);

        assertEquals("{\"message\":\"The offered card is not owned by the user," +
                " or the requirements are not met (Type, MinimumDamage)," +
                " or the offered card is locked in the deck.\"}", response.getContent());
        assertEquals(403, response.getStatus());
    }
    @Test
    @Order(58)
    void testTradeCardLockedInDeck() {
        TradingService tradingService = new TradingService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");
        request.setPathname("/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0");
        request.setBody("\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\"");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.PLAIN_TEXT));

        Response response = tradingService.handleRequest(request);

        assertEquals("{\"message\":\"The offered card is not owned by the user," +
                " or the requirements are not met (Type, MinimumDamage)," +
                " or the offered card is locked in the deck.\"}", response.getContent());
        assertEquals(403, response.getStatus());
    }
    @Test
    @Order(59)
    void testTradeInvalidTradingDealId() {
        TradingService tradingService = new TradingService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");
        request.setPathname("/tradings/invalid");
        request.setBody("\"d04b736a-e874-4137-b191-638e0ff3b4e7\"");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.PLAIN_TEXT));

        Response response = tradingService.handleRequest(request);

        assertEquals("{\"message\":\"The provided deal ID was not found.\"}", response.getContent());
        assertEquals(404, response.getStatus());
    }
    @Test
    @Order(60)
    void testShowCardsBeforeTradeUser1() {
        CardService cardService = new CardService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/cards");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = cardService.handleRequest(request);

        assertEquals("[{\"id\":\"f8043c23-1534-4487-b66b-238e0c3c39b5\",\"name\":\"WaterSpell\",\"damage\":23.0}," +
                "{\"id\":\"f3fad0f2-a1af-45df-b80d-2e48825773d9\",\"name\":\"Ork\",\"damage\":45.0}," +
                "{\"id\":\"ed1dc1bc-f0aa-4a0c-8d43-1402189b33c8\",\"name\":\"WaterGoblin\",\"damage\":10.0}," +
                "{\"id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\",\"name\":\"WaterSpell\",\"damage\":20.0}," +
                "{\"id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\",\"name\":\"FireSpell\",\"damage\":25.0}," +
                "{\"id\":\"dcd93250-25a7-4dca-85da-cad2789f7198\",\"name\":\"FireSpell\",\"damage\":0.0}," +
                "{\"id\":\"d04b736a-e874-4137-b191-638e0ff3b4e7\",\"name\":\"Dragon\",\"damage\":70.0}," +
                "{\"id\":\"b017ee50-1c14-44e2-bfd6-2c0c5653a37c\",\"name\":\"WaterGoblin\",\"damage\":11.0}," +
                "{\"id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"name\":\"Dragon\",\"damage\":50.0}," +
                "{\"id\":\"951e886a-0fbf-425d-8df5-af2ee4830d85\",\"name\":\"Ork\",\"damage\":0.0}," +
                "{\"id\":\"91a6471b-1426-43f6-ad65-6fc473e16f9f\",\"name\":\"WaterSpell\",\"damage\":21.0}," +
                "{\"id\":\"8c20639d-6400-4534-bd0f-ae563f11f57a\",\"name\":\"WaterSpell\",\"damage\":25.0}," +
                "{\"id\":\"88221cfe-1f84-41b9-8152-8e36c6a354de\",\"name\":\"WaterSpell\",\"damage\":22.0}," +
                "{\"id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"name\":\"WaterGoblin\",\"damage\":10.0}," +
                "{\"id\":\"65ff5f23-1e70-4b79-b3bd-f6eb679dd3b5\",\"name\":\"Dragon\",\"damage\":50.0}," +
                "{\"id\":\"644808c2-f87a-4600-b313-122b02322fd5\",\"name\":\"WaterGoblin\",\"damage\":9.0}," +
                "{\"id\":\"55ef46c4-016c-4168-bc43-6b9b1e86414f\",\"name\":\"WaterSpell\",\"damage\":20.0}," +
                "{\"id\":\"4ec8b269-0dfa-4f97-809a-2c63fe2a0025\",\"name\":\"Ork\",\"damage\":55.0}," +
                "{\"id\":\"4a2757d6-b1c3-47ac-b9a3-91deab093531\",\"name\":\"Dragon\",\"damage\":55.0}," +
                "{\"id\":\"44c82fbc-ef6d-44ab-8c7a-9fb19a0e7c6e\",\"name\":\"Dragon\",\"damage\":0.0}," +
                "{\"id\":\"2c98cd06-518b-464c-b911-8d787216cddd\",\"name\":\"WaterSpell\",\"damage\":0.0}," +
                "{\"id\":\"1d3f175b-c067-4359-989d-96562bfa382c\",\"name\":\"Ork\",\"damage\":40.0}," +
                "{\"id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"name\":\"Ork\",\"damage\":45.0}," +
                "{\"id\":\"171f6076-4eb5-4a7d-b3f2-2d650cc3d237\",\"name\":\"RegularSpell\",\"damage\":28.0}]", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(61)
    void testShowCardsBeforeTradeUser2() {
        CardService cardService = new CardService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/cards");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic admin-mtcgToken");

        Response response = cardService.handleRequest(request);

        assertEquals("[{\"id\":\"fc305a7a-36f7-4d30-ad27-462ca0445649\",\"name\":\"Ork\",\"damage\":0.0}," +
                "{\"id\":\"d7d0cb94-2cbf-4f97-8ccf-9933dc5354b8\",\"name\":\"WaterGoblin\",\"damage\":0.0}," +
                "{\"id\":\"d60e23cf-2238-4d49-844f-c7589ee5342e\",\"name\":\"WaterSpell\",\"damage\":0.0}," +
                "{\"id\":\"b2237eca-0271-43bd-87f6-b22f70d42ca4\",\"name\":\"WaterGoblin\",\"damage\":0.0}," +
                "{\"id\":\"9e8238a4-8a7a-487f-9f7d-a8c97899eb48\",\"name\":\"Dragon\",\"damage\":0.0}," +
                "{\"id\":\"84d276ee-21ec-4171-a509-c1b88162831c\",\"name\":\"RegularSpell\",\"damage\":0.0}]", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(62)
    void testTrade() {
        TradingService tradingService = new TradingService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.POST);
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");
        request.setPathname("/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0");
        request.setBody("\"d04b736a-e874-4137-b191-638e0ff3b4e7\"");
        request.getHeaderMap().setContentLengthHeader(String.valueOf(request.getBody().length()));
        request.getHeaderMap().setContentTypeHeader(String.valueOf(ContentType.PLAIN_TEXT));

        Response response = tradingService.handleRequest(request);

        assertEquals("{\"message\":\"Trading deal successfully executed.\"}", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(63)
    void testShowCardsAfterTradeUser1() {
        CardService cardService = new CardService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/cards");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic kienboec-mtcgToken");

        Response response = cardService.handleRequest(request);

        assertEquals("[{\"id\":\"f8043c23-1534-4487-b66b-238e0c3c39b5\",\"name\":\"WaterSpell\",\"damage\":23.0}," +
                "{\"id\":\"f3fad0f2-a1af-45df-b80d-2e48825773d9\",\"name\":\"Ork\",\"damage\":45.0}," +
                "{\"id\":\"ed1dc1bc-f0aa-4a0c-8d43-1402189b33c8\",\"name\":\"WaterGoblin\",\"damage\":10.0}," +
                "{\"id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\",\"name\":\"WaterSpell\",\"damage\":20.0}," +
                "{\"id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\",\"name\":\"FireSpell\",\"damage\":25.0}," +
                "{\"id\":\"dcd93250-25a7-4dca-85da-cad2789f7198\",\"name\":\"FireSpell\",\"damage\":0.0}," +
                "{\"id\":\"b017ee50-1c14-44e2-bfd6-2c0c5653a37c\",\"name\":\"WaterGoblin\",\"damage\":11.0}," +
                "{\"id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"name\":\"Dragon\",\"damage\":50.0}," +
                "{\"id\":\"951e886a-0fbf-425d-8df5-af2ee4830d85\",\"name\":\"Ork\",\"damage\":0.0}," +
                "{\"id\":\"91a6471b-1426-43f6-ad65-6fc473e16f9f\",\"name\":\"WaterSpell\",\"damage\":21.0}," +
                "{\"id\":\"8c20639d-6400-4534-bd0f-ae563f11f57a\",\"name\":\"WaterSpell\",\"damage\":25.0}," +
                "{\"id\":\"88221cfe-1f84-41b9-8152-8e36c6a354de\",\"name\":\"WaterSpell\",\"damage\":22.0}," +
                "{\"id\":\"84d276ee-21ec-4171-a509-c1b88162831c\",\"name\":\"RegularSpell\",\"damage\":0.0}," +
                "{\"id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"name\":\"WaterGoblin\",\"damage\":10.0}," +
                "{\"id\":\"65ff5f23-1e70-4b79-b3bd-f6eb679dd3b5\",\"name\":\"Dragon\",\"damage\":50.0}," +
                "{\"id\":\"644808c2-f87a-4600-b313-122b02322fd5\",\"name\":\"WaterGoblin\",\"damage\":9.0}," +
                "{\"id\":\"55ef46c4-016c-4168-bc43-6b9b1e86414f\",\"name\":\"WaterSpell\",\"damage\":20.0}," +
                "{\"id\":\"4ec8b269-0dfa-4f97-809a-2c63fe2a0025\",\"name\":\"Ork\",\"damage\":55.0}," +
                "{\"id\":\"4a2757d6-b1c3-47ac-b9a3-91deab093531\",\"name\":\"Dragon\",\"damage\":55.0}," +
                "{\"id\":\"44c82fbc-ef6d-44ab-8c7a-9fb19a0e7c6e\",\"name\":\"Dragon\",\"damage\":0.0}," +
                "{\"id\":\"2c98cd06-518b-464c-b911-8d787216cddd\",\"name\":\"WaterSpell\",\"damage\":0.0}," +
                "{\"id\":\"1d3f175b-c067-4359-989d-96562bfa382c\",\"name\":\"Ork\",\"damage\":40.0}," +
                "{\"id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"name\":\"Ork\",\"damage\":45.0}," +
                "{\"id\":\"171f6076-4eb5-4a7d-b3f2-2d650cc3d237\",\"name\":\"RegularSpell\",\"damage\":28.0}]", response.getContent());
        assertEquals(200, response.getStatus());
    }
    @Test
    @Order(64)
    void testShowCardsAfterTradeUser2() {
        CardService cardService = new CardService();
        Request request = new Request();

        request.setHeaderMap(new HeaderMap());
        request.setMethod(Method.GET);
        request.setPathname("/cards");
        request.getHeaderMap().setAuthorizationTokenHeader("Basic admin-mtcgToken");

        Response response = cardService.handleRequest(request);

        assertEquals("[{\"id\":\"fc305a7a-36f7-4d30-ad27-462ca0445649\",\"name\":\"Ork\",\"damage\":0.0}," +
                "{\"id\":\"d7d0cb94-2cbf-4f97-8ccf-9933dc5354b8\",\"name\":\"WaterGoblin\",\"damage\":0.0}," +
                "{\"id\":\"d60e23cf-2238-4d49-844f-c7589ee5342e\",\"name\":\"WaterSpell\",\"damage\":0.0}," +
                "{\"id\":\"d04b736a-e874-4137-b191-638e0ff3b4e7\",\"name\":\"Dragon\",\"damage\":70.0}," +
                "{\"id\":\"b2237eca-0271-43bd-87f6-b22f70d42ca4\",\"name\":\"WaterGoblin\",\"damage\":0.0}," +
                "{\"id\":\"9e8238a4-8a7a-487f-9f7d-a8c97899eb48\",\"name\":\"Dragon\",\"damage\":0.0}]", response.getContent());
        assertEquals(200, response.getStatus());
    }
}
