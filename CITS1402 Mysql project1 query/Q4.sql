select customerId, count(coneId)
from  purchase p
	join customerpurchases cu on cu.purchaseId = p.id
	join conesinpurchase  co on co.purchaseID = p.id
group by customerId
order by count(coneId) DESC;