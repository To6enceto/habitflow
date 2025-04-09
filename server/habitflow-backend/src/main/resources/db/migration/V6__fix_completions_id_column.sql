-- Change completions.id from INTEGER to BIGINT
ALTER TABLE completions ALTER COLUMN id TYPE BIGINT;

-- Change habit_id to match FK type (also BIGINT)
ALTER TABLE completions ALTER COLUMN habit_id TYPE BIGINT;

-- Drop old unique constraint if it exists (Flyway may warn about duplicate names)
ALTER TABLE completions DROP CONSTRAINT IF EXISTS UK96149neh6ekknq4av1qjrtcgi;

-- Re-add unique constraint with standard naming
ALTER TABLE completions ADD CONSTRAINT completions_unique_date UNIQUE (habit_id, date);

-- Update tasks table as well
ALTER TABLE tasks ALTER COLUMN id TYPE BIGINT;
ALTER TABLE tasks ALTER COLUMN user_id TYPE BIGINT;
ALTER TABLE tasks ALTER COLUMN title TYPE VARCHAR(255);
ALTER TABLE tasks ALTER COLUMN description TYPE VARCHAR(255);
ALTER TABLE tasks ALTER COLUMN created_at TYPE DATE;
