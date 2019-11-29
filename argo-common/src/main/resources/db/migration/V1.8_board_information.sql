CREATE TABLE main_board (
    board_id SERIAL,
    user_email VARCHAR(200),
    title VARCHAR(300),
    post VARCHAR(9999),
    parent BIGINT DEFAULT null,
    admin_reply boolean DEFAULT false,
    deleted boolean DEFAULT false,
    replied boolean DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT main_board_pk PRIMARY KEY (board_id)
);
ALTER TABLE main_board owner to argo;

INSERT INTO main_board (user_email, title, post)
VALUES ('ghdeowls206@gmail.com', 'sampletitle', 'samplepost');

SELECT * FROM main_board;
