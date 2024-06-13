INSERT INTO tb_circuits (
    code_id, name, description, country, url, photo, driver_best_lap, fast_lap, 
    likes, reviews, rating, rating_count
) VALUES
    ('monaco', 'Circuit de Monaco', 'Street circuit in Monte Carlo', 'Monaco', 'http://example.com/monaco', 0, 
    'HAM', '1:10.166', 5000, 2000, 9.8, 10),
    ('silverstone', 'Silverstone Circuit', 'Historic circuit in England', 'United Kingdom', 'http://example.com/silverstone', 0, 
    'VER', '1:27.097', 4500, 1800, 9.7, 9);

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
    40, 80, 25, 2, 9.7, 1800, 950000, 2500);

INSERT INTO tb_rating (
    entity_id, rating, type, user_id
) VALUES
    ('HAM', 5, 'DRIVER', 1),
    ('monaco', 5, 'CIRCUIT', 1);