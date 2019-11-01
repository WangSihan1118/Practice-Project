DELIMITER \\
CREATE PROCEDURE createMonthlyWinners()
BEGIN

#Procedure Varibles
DECLARE newmonthyear, localmonthyear VARCHAR(64);
DECLARE newnumOfPurchase, localnumOfPurchase INT;
DECLARE newstorename, localstorename, backupstorename VARCHAR(64);

#Cursor Varibles
DECLARE cursorFinished INT DEFAULT FALSE;

DECLARE myCursor CURSOR FOR
	select concat("  ", monthname(buydate)," ",year(buydate)) as date,count(id) as amount ,store 
	from purchase
	group by year(buydate),month(buydate),store;

DECLARE CONTINUE HANDLER FOR NOT FOUND
	SET cursorFinished = TRUE;

#Open cursor
OPEN myCursor;
#First row
FETCH myCursor INTO newmonthyear, newnumOfPurchase, newstorename;
SET localmonthyear = newmonthyear;
SET localnumOfPurchase = newnumOfPurchase;
SET localstorename = newstorename;
SET backupstorename = null;

#CREATE TABLE
DROP TABLE IF EXISTS MonthlyWinners; 
CREATE TABLE MonthlyWinners(
month_year VARCHAR(64), 
winingstore VARCHAR(64), 
puchasesAmount INT);


#Loop though rows
read_loop: LOOP
	FETCH myCursor INTO newmonthyear,newnumOfPurchase,newstorename;
	IF cursorFinished THEN
		LEAVE read_loop;
	END IF;
    
                
	IF newmonthyear = localmonthyear  THEN
		IF newnumOfPurchase = localnumOfPurchase THEN
            SET newnumOfPurchase = localnumOfPurchase;
			SET localnumOfPurchase = newnumOfPurchase;
			SET backupstorename = newstorename;
		END IF;
	END IF;
    
	IF newmonthyear = localmonthyear  THEN
		IF	newnumOfPurchase > localnumOfPurchase THEN 
			SET localnumOfPurchase = newnumOfPurchase;
            SET localstorename = newstorename;
            SET backupstorename = null;
		END IF;
	END IF;
        
	IF newmonthyear <> localmonthyear THEN
		IF backupstorename IS NOT NULL THEN
			INSERT INTO MonthlyWinners VALUES(localmonthyear,localstorename,localnumOfPurchase),(localmonthyear,backupstorename,localnumOfPurchase);
			SET localmonthyear = newmonthyear;
			SET localnumOfPurchase = newnumOfPurchase;
			SET localstorename = newstorename;
			SET backupstorename = null;
		ELSE INSERT INTO MonthlyWinners VALUES(localmonthyear,localstorename,localnumOfPurchase);
			SET localmonthyear = newmonthyear;
			SET localnumOfPurchase = newnumOfPurchase;
			SET localstorename = newstorename;
			SET backupstorename = null;
		END IF;
	END IF;
    
END LOOP;
#Close cursor
CLOSE myCursor;    
END\\
DELIMITER ;