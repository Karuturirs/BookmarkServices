# BookmarkServices
restapi service to handle my bookmarks developed using springs and hibernates
 
Creating tables in mysql

create table bookmark_favourites (
 id INT NOT NULL AUTO_INCREMENT,
 name varchar(50) NOT NULL,
 type varchar(10) NOT NULL,
 path varchar(500) NOT NUll,
 url varchar(500),
 description varchar(500),
 `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , 
 PRIMARY KEY (path),
 KEY (id)
);

insert into bookmark_favourites ( name, type, path) values ("2016", "FOLDER", "2016");
insert into bookmark_favourites ( name, type, path) values ("06", "FOLDER", "2016/06");
insert into bookmark_favourites ( name, type, path) values ("2015", "FOLDER", "2015");
insert into bookmark_favourites ( name, type, path) values ("01", "FOLDER", "2016/06/01");
insert into bookmark_favourites ( name, type, path) values ("02", "FOLDER", "2016/06/02");
insert into bookmark_favourites ( name, type, path) values ("05", "FOLDER", "2016/05");
insert into bookmark_favourites ( name, type, path) values ("21", "FOLDER", "2016/05/21");
insert into bookmark_favourites ( name, type, path) values ("22", "FOLDER", "2016/05/22");
insert into bookmark_favourites ( name, type, path) values ("22", "FOLDER", "2016/0555/22");
insert into bookmark_favourites ( name, type, path) values ("01", "FOLDER", "2015/01");
insert into bookmark_favourites ( name, type, path) values ("02", "FOLDER", "2015/01/02");
select * from bookmark_favourites;

delete from bookmark_favourites where name="ravi";
commit;
select * from bookmark_favourites where path LIKE "2016/05/%" or path ="2016/05";

select * from bookmark_favourites where path LIKE "2015/01%";

select name,type,path from bookmark_favourites where path like "2016/01%";

UPDATE bookmark_favourites SET path= "2016/07/21" where path = "2016/05/21"



