CREATE TABLE main_board (
--     writer_id SERIAL NOT NULL PRIMARY KEY,
    writer_id SERIAL,
    board_number BIGINT,
    user_email VARCHAR(200),
    title VARCHAR(300),
    post VARCHAR(9999),
    admin_reply VARCHAR(2000),
    user_reply VARCHAR(2000),
    replied VARCHAR,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT main_board_pk PRIMARY KEY (writer_id)
);

INSERT INTO main_board (board_number, user_email, title, post, admin_reply, user_reply, replied)
VALUES (1, 'ghdeowls206@gmail.com', 'sampletitle', 'samplepost', 'None', 'None', false);
SELECT * FROM main_board
