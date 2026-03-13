-- CAS Authentication Database Setup
-- Run this script in your PostgreSQL database

-- Create connection table
CREATE TABLE IF NOT EXISTS connection (
    connection_code VARCHAR(128) PRIMARY KEY,
    connection_login VARCHAR(256) NOT NULL
);

-- Create index for faster lookups
CREATE INDEX IF NOT EXISTS idx_connection_login ON connection(connection_login);
