select email, store, count(purchase.id)
from customerpurchases
    join purchase on purchase.id = customerpurchases.purchaseId
    join customer on customer.id = customerpurchases.customerId
    group by email, store with rollup;