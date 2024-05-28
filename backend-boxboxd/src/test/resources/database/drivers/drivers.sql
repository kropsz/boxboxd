INSERT INTO tb_drivers (
    code, number, name, last_name, photo, team_id, teammate_id, birh_date, 
    nationality, biography, url, first_places, podiums, poles, ranking, 
    rating, rating_count, likes, reviews
) VALUES
    ('HAM', '44', 'Lewis', 'Hamilton', 0, 'MER', 'BOT', '1985-01-07', 
    'British', 'Seven-time World Champion', 'http://example.com/hamilton', 
    100, 182, 101, 1, 9.8, 2500, 1000000, 3000),

    ('VER', '33', 'Max', 'Verstappen', 0, 'RED', 'PER', '1997-09-30', 
    'Dutch', 'Two-time World Champion', 'http://example.com/verstappen', 
    40, 80, 25, 2, 9.7, 1800, 950000, 2500),

    ('LEC', '16', 'Charles', 'Leclerc', 0, 'FER', 'SAI', '1997-10-16', 
    'Monegasque', 'Future World Champion', 'http://example.com/leclerc', 10, 40, 15, 3, 9.3, 1200, 800000, 1500),

    ('SAI', '55', 'Carlos', 'Sainz', 0, 'FER', 'LEC', '1994-09-01', 
    'Spanish', 'Son of Rally Legend', 'http://example.com/sainz', 
    2, 20, 5, 4, 8.9, 800, 700000, 1000),

    ('RIC', '3', 'Daniel', 'Ricciardo', 0, 'MCL', 'NOR', '1989-07-01', 
    'Australian', 'Smiling Assassin', 'http://example.com/ricciardo', 
    8, 32, 3, 5, 9.0, 1000, 850000, 1100),

    ('NOR', '4', 'Lando', 'Norris', 0, 'MCL', 'RIC', '1999-11-13', 
    'British', 'Rising Star', 'http://example.com/norris', 
    0, 15, 1, 6, 8.8, 600, 750000, 900);


INSERT INTO tb_favorite_driver (user_id, driver_code)
VALUES (1, 'LEC'), (1, 'VER'), (1, 'NOR');