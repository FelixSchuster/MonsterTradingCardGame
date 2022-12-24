CREATE TABLE users (
    user_id SERIAL,
    username VARCHAR UNIQUE NOT NULL,
    password VARCHAR NOT NULL,
    name VARCHAR DEFAULT NULL,
    bio VARCHAR DEFAULT NULL,
    image VARCHAR DEFAULT NULL,
    coins INTEGER NOT NULL DEFAULT 20,
    elo INTEGER NOT NULL DEFAULT 100,
    wins INTEGER NOT NULL DEFAULT 0,
    losses INTEGER NOT NULL DEFAULT 0,

    PRIMARY KEY (user_id)
);

CREATE TABLE tokens (
    token_id SERIAL,
    user_id INTEGER NOT NULL,
    token VARCHAR NOT NULL,

    PRIMARY KEY (token_id)
);

CREATE TABLE stacks (
    stack_id SERIAL,
    user_id INTEGER NOT NULL,

    PRIMARY KEY (stack_id)
);

CREATE TABLE stack_card_quantities (
    stack_id INTEGER NOT NULL,
    card_id VARCHAR NOT NULL,
    quantity INTEGER NULL,

    PRIMARY KEY (stack_id, card_id)
);

CREATE TABLE decks (
    deck_id SERIAL,
    user_id INTEGER NOT NULL,

    PRIMARY KEY (deck_id)
);

CREATE TABLE deck_card_quantities (
    deck_id INTEGER NOT NULL,
    card_id VARCHAR NOT NULL,
    quantity INTEGER NULL,

    PRIMARY KEY (deck_id, card_id)
);

CREATE TABLE packages (
    package_id SERIAL,

    PRIMARY KEY (package_id)
);

CREATE TABLE package_card_quantities (
    package_id INTEGER NOT NULL,
    card_id VARCHAR NOT NULL,
    quantity INTEGER NULL,

    PRIMARY KEY (package_id, card_id)
);

CREATE TABLE trading_deal (
    trading_deal_id VARCHAR NOT NULL,
    user_id INTEGER NOT NULL,
    card_id VARCHAR NOT NULL,
    type VARCHAR NOT NULL,
    minimum_damage VARCHAR NOT NULL,

    PRIMARY KEY (trading_deal_id)
);

CREATE TABLE cards (
    card_id VARCHAR NOT NULL,
    user_id INTEGER NULL,
    name VARCHAR NOT NULL,
    damage FLOAT NOT NULL,

    PRIMARY KEY (card_id)
);

ALTER TABLE tokens
    ADD CONSTRAINT user_id_fk_tokens
        FOREIGN KEY (user_id)
            REFERENCES users (user_id);

ALTER TABLE cards
    ADD CONSTRAINT user_id_fk_cards
        FOREIGN KEY (user_id)
            REFERENCES users (user_id);

ALTER TABLE trading_deal
    ADD CONSTRAINT user_id_fk_trading_deal
        FOREIGN KEY (user_id)
            REFERENCES users (user_id);

ALTER TABLE trading_deal
    ADD CONSTRAINT card_id_fk_trading_deal
        FOREIGN KEY (card_id)
            REFERENCES cards (card_id);

ALTER TABLE stacks
    ADD CONSTRAINT user_id_fk_stacks
        FOREIGN KEY (user_id)
            REFERENCES users (user_id);

ALTER TABLE decks
    ADD CONSTRAINT user_id_fk_decks
        FOREIGN KEY (user_id)
            REFERENCES users (user_id);

ALTER TABLE stack_card_quantities
    ADD CONSTRAINT stack_id_fk_stack_card_quantities
        FOREIGN KEY (stack_id)
            REFERENCES stacks (stack_id);

ALTER TABLE stack_card_quantities
    ADD CONSTRAINT card_id_fk_stack_card_quantities
        FOREIGN KEY (card_id)
            REFERENCES cards (card_id);

ALTER TABLE deck_card_quantities
    ADD CONSTRAINT stack_id_fk_deck_card_quantities
        FOREIGN KEY (deck_id)
            REFERENCES decks (deck_id);

ALTER TABLE deck_card_quantities
    ADD CONSTRAINT card_id_fk_deck_card_quantities
        FOREIGN KEY (card_id)
            REFERENCES cards (card_id);

ALTER TABLE package_card_quantities
    ADD CONSTRAINT card_id_fk_package_card_quantities
        FOREIGN KEY (card_id)
            REFERENCES cards (card_id);

-- SELECT * FROM packages LIMIT 1;
-- INSERT INTO packages (package_id) VALUES (DEFAULT);
