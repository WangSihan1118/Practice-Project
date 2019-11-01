DELIMITER \\
CREATE FUNCTION numFlavours(coneNumber INT) returns int(11)
READS SQL DATA
 BEGIN
DECLARE num,cid INT;
SET cid = coneNumber;
SET num = (select count(distinct name) from scoopsincone
			join scoop on scoop.id = scoopsincone.scoopId
			join cone on scoopsincone.coneId = cone.id
            where cone.id = cid);
RETURN num;
END\\
DELIMITER ;