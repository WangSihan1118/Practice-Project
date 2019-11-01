DELIMITER \\
CREATE FUNCTION costOfPurchase(purchaseNumber INT) RETURNS INT(11)
READS SQL DATA
BEGIN
DECLARE base_cost, cone_cost, scoopNumber, scoop_cost,multiscoop_discount, customer_discount, cost INT;
DECLARE day VARCHAR(64);

#SET BASECOST
select dayname(buydate) INTO day from purchase where id = purchaseNumber;
	IF day LIKE 'Saturday' THEN
			SET base_cost = 100;
	ELSEIF day LIKE 'Sunday' THEN
			SET base_cost = 150;
		ELSE 
			SET base_cost = 50;
	END IF;
    
#SET CONECOST
select sum(conecost) INTO cone_cost
from conesinpurchase
	join cone on cone.id = coneId
	join purchase on purchase.id = purchaseID
where purchase.id = purchaseNumber;


#SET SCOOP_COST
select sum(costInCents) INTO scoop_cost
from conesinpurchase
	join scoopsincone on conesinpurchase.coneId = scoopsincone.coneId
	join scoop on scoop.id = scoopsincone.scoopId
where purchaseID = purchaseNumber;

#SET MULTISCOOPE_DISCOUNT
select count(scoopId) INTO scoopNumber from conesinpurchase
						join scoopsincone on conesinpurchase.coneId = scoopsincone.coneId
						join scoop on scoop.id = scoopsincone.coneId
				   where purchaseID = purchaseNumber;
                   
	CASE scoopNumber
		WHEN scoopNumber = 2 THEN
			SET multiscoop_discount = 50;
		WHEN scoopNumber = 3 THEN
			SET multiscoop_discount = 150;
		ELSE
			SET  multiscoop_discount = 0;
	END CASE;
            
#SET CUSTOMER_DISCOUNT
select discountApplied  INTO customer_discount
from customerpurchases 
where purchaseId = purchaseNumber;

#CALCULATE COST
SELECT round((base_cost + cone_cost + scoop_cost - multiscoop_discount)*((100 - customer_discount)/100))
INTO cost;

RETURN cost;
END\\
DELIMITER ;