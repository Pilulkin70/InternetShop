INSERT INTO users (id, email, username, password, role)
VALUES (1, 'mail@mail.ru', 'admin', '$2a$10$yVmWxvra8Rw2OvsOuVDdou91Zj1YsbUAliewHhXTdyDxhS9G4MCGm', 'ADMIN');

ALTER SEQUENCE user_seq RESTART WITH 2;