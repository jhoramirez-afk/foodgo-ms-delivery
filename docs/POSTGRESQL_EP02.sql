-- Abrir Query Tool sobre foodgo_delivery.
SELECT current_database() AS base, current_user AS usuario, version() AS version_postgresql;
SELECT table_name AS tabla FROM information_schema.tables
WHERE table_schema='public' AND table_type='BASE TABLE' ORDER BY table_name;
SELECT tc.table_name AS tabla_hija, kcu.column_name AS clave_foranea,
       ccu.table_name AS tabla_padre, ccu.column_name AS clave_primaria
FROM information_schema.table_constraints tc
JOIN information_schema.key_column_usage kcu
  ON tc.constraint_name=kcu.constraint_name AND tc.constraint_schema=kcu.constraint_schema
JOIN information_schema.constraint_column_usage ccu
  ON tc.constraint_name=ccu.constraint_name AND tc.constraint_schema=ccu.constraint_schema
WHERE tc.constraint_type='FOREIGN KEY' AND tc.table_schema='public'
ORDER BY tc.table_name, kcu.column_name;
