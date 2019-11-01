select MONTHNAME(buyDAte) as month,count(id) as numPurchases 
from purchase 
group by month;