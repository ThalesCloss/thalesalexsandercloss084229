SELECT 'CREATE DATABASE keycloak'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'keycloak')\gexec

SELECT 'CREATE DATABASE seletivoseplag'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'seletivoseplag')\gexec