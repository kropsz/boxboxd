INSERT INTO tb_team (
    name, country, description, logo, banner, url, likes, wins, 
    podiums, titles, first_driver, second_driver
) VALUES
    ('Mercedes', 'Germany', 'Eight-time Constructors Champions', 0, 0, 'http://example.com/mercedes', 
    1000000, 100, 250, 8, 'Lewis Hamilton', 'George Russell'),

    ('Red Bull Racing', 'Austria', 'Four-time Constructors Champions', 0, 0, 'http://example.com/redbull', 
    950000, 85, 200, 4, 'Max Verstappen', 'Sergio Perez'),

    ('Ferrari', 'Italy', 'Most successful team in F1 history', 0, 0, 'http://example.com/ferrari', 
    900000, 240, 500, 16, 'Charles Leclerc', 'Carlos Sainz'),

    ('McLaren', 'United Kingdom', 'Iconic British team', 0, 0, 'http://example.com/mclaren', 
    850000, 180, 400, 8, 'Lando Norris', 'Oscar Piastri'),

    ('Aston Martin', 'United Kingdom', 'Revitalized racing team', 0, 0, 'http://example.com/astonmartin', 
    800000, 10, 50, 0, 'Fernando Alonso', 'Lance Stroll'),

    ('Alpine', 'France', 'French pride in F1', 0, 0, 'http://example.com/alpine', 
    750000, 20, 60, 2, 'Esteban Ocon', 'Pierre Gasly');

INSERT INTO tb_favorite_team (user_id, team_name)
VALUES
    (1, 'Red Bull Racing'),
    (1, 'Ferrari'),
    (1, 'McLaren'),
    (1, 'Aston Martin'),
    (1, 'Alpine');