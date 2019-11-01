DELIMITER //
CREATE FUNCTION  isNutFree(coneNumber INT) 
	RETURNS BOOLEAN
	READS SQL DATA
	BEGIN
		DECLARE nutFree,cid int;
        DECLARE status boolean;
        
        SET cid = coneNumber;
		SET nutFree = (select count(*) from scoopsincone
						join cone on cone.id = coneId
						join scoop on scoop.id = scoopId
						where coneId = cid
						AND (conetype IN
							(select distinct conetype from cone where conetype = "Waffle")
						or name IN
							(select distinct name from scoop where name = 'Coconut' or name = 'Macadamia')));
		IF nutFree = 0
        Then set status = TRUE;
        
        ELSE set status = FALSE;
        end if;
RETURN status;
END//
DELIMITER ;