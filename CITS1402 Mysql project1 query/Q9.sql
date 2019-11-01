DELIMITER \\
CREATE FUNCTION numScoops(coneNumber INT) returns int(11)
READS SQL DATA
 BEGIN
DECLARE num,cid INT;
SET cid = coneNumber;
SET num = (select count(scoopId) from scoopsincone where coneId = cid);
RETURN num;
END\\
DELIMITER ;

DELIMITER \\
CREATE PROCEDURE purchaseSummary (purchaseNum INT, OUT oneScoop INT, OUT twoScoop INT, OUT threeScoop INT)
BEGIN
#Procedure Varibles
DECLARE CID INT;
DECLARE numOfOne, numOfTwo, numOfThree INT DEFAULT 0;#OUTPUT value
DECLARE numScoop INT;

#Cursor Varibles
DECLARE cursorFinished INT DEFAULT FALSE;

DECLARE myCursor CURSOR FOR
	select coneId from conesinpurchase
		join cone on cone.id = coneId
		join purchase on purchase.id = purchaseID
    where purchaseID = purchaseNum;

DECLARE CONTINUE HANDLER FOR NOT FOUND
	SET cursorFinished = TRUE;

#Open cursor
OPEN myCursor;
#First row
FETCH myCursor INTO CID;
SET numScoop = numScoops(CID);
	IF numScoop = 1 THEN
		SET numOfOne = numOfOne + 1;
	ELSEIF numScoop = 2 THEN
		SET numOfTwo = numOfTwo + 1;
	ELSE SET numOfThree = numOfThree + 1;
	END IF;
    
#Loop though rows
read_loop: LOOP
	FETCH myCursor INTO CID;
	IF cursorFinished THEN
		LEAVE read_loop;
	ELSE
    
		SET numScoop = numScoops(CID);
    
		IF numScoop = 1 THEN
			SET numOfOne = numOfOne + 1;
		ELSEIF numScoop = 2 THEN
			SET numOfTwo = numOfTwo + 1;
		ELSE 
			SET numOfThree = numOfThree + 1;
		END IF;
	END IF;
    
END LOOP;
#Close cursor
CLOSE myCursor;
#final output
Select numOfOne INTO oneScoop;
Select numOfTwo INTO twoScoop;
Select numOfThree INTO threeScoop;
END\\
DELIMITER ;