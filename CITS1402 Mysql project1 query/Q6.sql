select name from scoop
where costInCents = (select MAX(costInCents) from scoop);