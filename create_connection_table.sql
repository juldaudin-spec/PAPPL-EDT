-- Drop existing objects
DROP TRIGGER IF EXISTS cleanup_expired_sessions ON connection CASCADE;
DROP FUNCTION IF EXISTS delete_expired_sessions() CASCADE;
DROP TABLE IF EXISTS connection CASCADE;

-- Create connection table
CREATE TABLE connection (
    connection_code VARCHAR(128) PRIMARY KEY,
    connection_login VARCHAR(256) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for faster lookups
CREATE INDEX idx_connection_login ON connection(connection_login);
CREATE INDEX idx_connection_created_at ON connection(created_at);

-- Create function to delete old sessions
CREATE OR REPLACE FUNCTION delete_expired_sessions()
RETURNS trigger AS $$
DECLARE
    deleted_count INTEGER;
BEGIN
    DELETE FROM connection
    WHERE created_at < NOW() - INTERVAL '2 hours';  -- ← EXPIRATION TIME: 2 hours

    GET DIAGNOSTICS deleted_count = ROW_COUNT;

    IF deleted_count > 0 THEN
        RAISE NOTICE 'Deleted % expired session(s)', deleted_count;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger that runs after INSERT (when someone logs in)
CREATE TRIGGER cleanup_expired_sessions
    AFTER INSERT ON connection
    FOR EACH STATEMENT
    EXECUTE FUNCTION delete_expired_sessions();