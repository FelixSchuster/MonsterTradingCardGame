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

    PRIMARY KEY (stack_id)
);

CREATE TABLE decks (
    deck_id SERIAL,

    PRIMARY KEY (deck_id)
);

CREATE TABLE packages (
    package_id SERIAL,

    PRIMARY KEY (package_id)
);

CREATE TABLE trading_deals (
    trading_deal_id VARCHAR NOT NULL,
    type VARCHAR NOT NULL,
    minimum_damage FLOAT NOT NULL,

    PRIMARY KEY (trading_deal_id)
);

CREATE TABLE cards (
    card_id VARCHAR NOT NULL,
    user_id INTEGER NULL,
    trading_deal_id VARCHAR NULL,
    package_id INTEGER NULL,
    deck_id INTEGER NULL,
    stack_id INTEGER NULL,
    name VARCHAR NOT NULL,
    damage FLOAT NOT NULL,

    PRIMARY KEY (card_id)
);

CREATE TABLE battles (
    battle_id SERIAL,
    user_1_id INTEGER NULL,
    user_2_id INTEGER NULL,
    deck_1_id INTEGER NULL,
    deck_2_id INTEGER NULL,

    PRIMARY KEY (battle_id)
);

CREATE TABLE battle_logs (
    battle_log_id SERIAL,
    battle_id INTEGER NOT NULL,
    log VARCHAR NOT NULL,

    PRIMARY KEY (battle_log_id)
);

ALTER TABLE tokens
    ADD CONSTRAINT user_id_fk_tokens
        FOREIGN KEY (user_id)
            REFERENCES users (user_id);

ALTER TABLE cards
    ADD CONSTRAINT user_id_fk_cards
        FOREIGN KEY (user_id)
            REFERENCES users (user_id);

ALTER TABLE cards
    ADD CONSTRAINT trading_deal_id_fk_cards
        FOREIGN KEY (trading_deal_id)
            REFERENCES trading_deals (trading_deal_id);

ALTER TABLE cards
    ADD CONSTRAINT package_id_fk_cards
        FOREIGN KEY (package_id)
            REFERENCES packages (package_id);

ALTER TABLE cards
    ADD CONSTRAINT deck_id_fk_cards
        FOREIGN KEY (deck_id)
            REFERENCES decks (deck_id);

ALTER TABLE cards
    ADD CONSTRAINT stack_id_fk_cards
        FOREIGN KEY (stack_id)
            REFERENCES stacks (stack_id);

ALTER TABLE battles
    ADD CONSTRAINT user_1_id_fk_battles
        FOREIGN KEY (user_1_id)
            REFERENCES users (user_id);

ALTER TABLE battles
    ADD CONSTRAINT user_2_id_fk_battles
        FOREIGN KEY (user_2_id)
            REFERENCES users (user_id);

ALTER TABLE battles
    ADD CONSTRAINT deck_1_id_fk_battles
        FOREIGN KEY (deck_1_id)
            REFERENCES decks (deck_id);

ALTER TABLE battles
    ADD CONSTRAINT deck_2_id_fk_battles
        FOREIGN KEY (deck_2_id)
            REFERENCES decks (deck_id);

ALTER TABLE battle_logs
    ADD CONSTRAINT battle_id_fk_battle_logs
        FOREIGN KEY (battle_id)
            REFERENCES battles (battle_id);
