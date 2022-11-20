CREATE TABLE users (
    user_id SERIAL,
    name VARCHAR(32) UNIQUE NOT NULL,
    password VARCHAR(64) NOT NULL,
    coins INTEGER NOT NULL DEFAULT 20,
    elo INTEGER NOT NULL DEFAULT 100,

    PRIMARY KEY (user_id)
);

CREATE TABLE tokens (
    token_id SERIAL,
    user_id INTEGER NOT NULL,
    token VARCHAR(32) NOT NULL,

    PRIMARY KEY (token_id)
);

CREATE TABLE stacks (
    stack_id SERIAL,
    user_id INTEGER NOT NULL,

    PRIMARY KEY (stack_id)
);

CREATE TABLE stack_card_quantities (
    stack_id INTEGER NOT NULL,
    card_id INTEGER NOT NULL,
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
    card_id INTEGER NOT NULL,
    quantity INTEGER NULL,

    PRIMARY KEY (deck_id, card_id)
);

CREATE TABLE store (
    store_id SERIAL,
    user_id INTEGER NOT NULL,
    card_id INTEGER NOT NULL,
    quantity INTEGER NULL,
    requirement VARCHAR(128) NULL,

    PRIMARY KEY (store_id)
);

CREATE TABLE cards (
    card_id SERIAL,
    name VARCHAR(32) NOT NULL,
    damage INTEGER NOT NULL,
    category VARCHAR(1) NOT NULL,   /* s... spell card, m... monster card */
    element_type VARCHAR(1) NOT NULL,   /* w... water, f... fire, n... normal */

    PRIMARY KEY (card_id)
);

ALTER TABLE tokens
    ADD CONSTRAINT user_id_fk_tokens
        FOREIGN KEY (user_id)
            REFERENCES users (user_id);

ALTER TABLE store
    ADD CONSTRAINT user_id_fk_store
        FOREIGN KEY (user_id)
            REFERENCES users (user_id);

ALTER TABLE store
    ADD CONSTRAINT card_id_fk_store
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
