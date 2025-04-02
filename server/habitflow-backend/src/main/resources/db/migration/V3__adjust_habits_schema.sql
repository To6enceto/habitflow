ALTER TABLE habits ALTER COLUMN id TYPE BIGINT;
ALTER TABLE habits ALTER COLUMN user_id TYPE BIGINT;
ALTER TABLE habits RENAME COLUMN title TO name;
ALTER TABLE habits ALTER COLUMN name TYPE VARCHAR(255);

-- Optional: remove description/frequency/start_date if not in the entity

-- Add the sequence for Panache
CREATE SEQUENCE habits_SEQ START 1 INCREMENT 50;

-- Also adjust users.email if needed
ALTER TABLE users ALTER COLUMN email TYPE VARCHAR(255);
