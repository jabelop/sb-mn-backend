ALTER TABLE combats_data ALTER COLUMN creature_class TYPE VARCHAR(80), ALTER COLUMN creature_class SET NOT NULL;
ALTER TABLE combats_data ALTER COLUMN defense TYPE INT, ALTER COLUMN defense SET NOT NULL, ALTER COLUMN defense DROP DEFAULT;
ALTER TABLE combats_data ALTER COLUMN time_to_attack TYPE INT, ALTER COLUMN time_to_attack SET NOT NULL, ALTER COLUMN time_to_attack DROP DEFAULT;
DROP INDEX IF EXISTS indx_combats_data_id_player;
DROP INDEX IF EXISTS indx_combats_data_id_combat;
