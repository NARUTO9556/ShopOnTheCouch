--liquibase formatted sql

-- changeset by_qMargo:create_users_table
CREATE TABLE users (
          id BIGSERIAL PRIMARY KEY,
          email VARCHAR(255),
          password VARCHAR(255),
          first_name VARCHAR(255),
          last_name VARCHAR(255),
          phone VARCHAR(255),
          role VARCHAR(255)
    );

 -- changeset by_qMargo:add_ADMIN_into_users_table
INSERT INTO users (email,password,role,first_name,last_name,phone)
VALUES('user@gmail.com',
        '$2a$10$TtcSj737PmMoWLS2QpvA2.LfD8DZqOS/Ozv./74FJK3t8aBXjX4uu',
        'ADMIN',
        'ADMIN',
        '!!!не забудь поменять пароль!!!',
        '!!!требуй повышения!!!')

