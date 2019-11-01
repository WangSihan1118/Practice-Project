delimiter $$
create procedure createMonthlyWinners()
begin

declare Abuydate, Bbuydate varchar(255);
declare Apurchase, Bpurchase int;
declare Astore, Bstore varchar(255);


declare cursorFinished int default false;
declare mycursor cursor for select concat("  ", monthname(buydate)," ",year(buydate)) as date,count(id) as amount ,store from purchase group by year(buydate),month(buydate),store;

declare continue handler for not found
set cursorfinished = True;

open mycursor;
fetch mycursor into Abuydate, Apurchase, Astore;
set Bbuydate=Abuydate;
set Bpurchase=Apurchase;
set Bstore=Astore;

drop table if exists MonthlyWinners ;
create table MonthlyWinners(
`Date` varchar(255),
`Winningstore` varchar(255),
`NumofPurchases` int);

read_loop:loop
fetch mycursor into Abuydate, Apurchase, Astore;
if cursorfinished then leave read_loop; 
end if;

if Abuydate = Bbuydate then
if Apurchase > Bpurchase then
set Bpurchase = Apurchase;
set Bstore = Astore;
end if;
end if;

if Abuydate <> Bbuydate then
insert into MonthlyWinners values(Bbuydate, Bstore, Bpurchase);
set Bbuydate = Abuydate;
set Bpurchase = Apurchase;
set Bstore = Astore;
end if;
end loop;
close mycursor;
end $$
delimiter ;

call createMonthlyWinners();
select * from MonthlyWinners;