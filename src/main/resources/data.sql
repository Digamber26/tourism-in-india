INSERT INTO destinations (name, state, description, category)
SELECT * FROM (SELECT 'Goa' AS name, 'Goa' AS state, 'Beaches, nightlife, and Portuguese heritage.' AS description, 'Beach' AS category) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM destinations WHERE name = 'Goa');

INSERT INTO destinations (name, state, description, category)
SELECT * FROM (SELECT 'Manali', 'Himachal Pradesh', 'Snow-capped mountains, adventure sports, and scenic valleys.', 'Hill Station') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM destinations WHERE name = 'Manali');

INSERT INTO destinations (name, state, description, category)
SELECT * FROM (SELECT 'Jaipur', 'Rajasthan', 'Forts, palaces, and vibrant local markets.', 'Heritage') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM destinations WHERE name = 'Jaipur');

INSERT INTO travel_packages (name, destination_id, price, duration_days, inclusions)
SELECT * FROM (
    SELECT 'Goa Beach Getaway' AS name, d.id AS destination_id, 12999.00 AS price, 4 AS duration_days,
           'Hotel, Breakfast, Airport Transfer' AS inclusions
    FROM destinations d WHERE d.name = 'Goa'
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM travel_packages WHERE name = 'Goa Beach Getaway');

INSERT INTO travel_packages (name, destination_id, price, duration_days, inclusions)
SELECT * FROM (
    SELECT 'Manali Adventure Trip', d.id, 15999.00, 5, 'Hotel, All Meals, Trekking, Bonfire'
    FROM destinations d WHERE d.name = 'Manali'
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM travel_packages WHERE name = 'Manali Adventure Trip');
