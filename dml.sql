DELETE FROM DERIVATIVES;

DROP FUNCTION IF EXISTS insertData();

CREATE FUNCTION insertData()
  RETURNS INTEGER AS $$
DECLARE
  counter INTEGER := 1000000; -- 1 million
BEGIN
  LOOP
    EXIT WHEN counter = 1100000; -- 1 million + 100k , so we inserting 100k of products
    counter := counter + 1;
    INSERT INTO derivatives VALUES (''CH000'' || counter, ''PRODUCT '' || to_char(counter, ''9999999''));
  END LOOP;
  RETURN 0;
END;
$$ LANGUAGE plpgsql;


SELECT insertData();