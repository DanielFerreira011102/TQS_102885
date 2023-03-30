CREATE TABLE game (
    id BIGSERIAL PRIMARY KEY,
    title varchar(255) not null unique,
    release_year int not null,
    rating int not null
);