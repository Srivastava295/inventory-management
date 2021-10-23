-- Creating database, this name should be used for provide db url in application.properties
create database inventory;

use inventory;

-- Creating action table
CREATE TABLE action (
     id BigInt AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(255) NOT NULL
);

-- Inserting pre-defined actions
insert into action(name) values("create");
insert into action(name) values("read");
insert into action(name) values("update");
insert into action(name) values("delete");

-- Creating user groups
CREATE TABLE user_group (
     id BigInt AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(255) NOT NULL
);

-- Adding user groups data
insert into user_group(name) values("superadmin");
insert into user_group(name) values("merchantXView");
insert into user_group(name) values("merchantXAdmin");
insert into user_group(name) values("merchantYView");
insert into user_group(name) values("merchantYAdmin");

-- Creating a join table that contains mapping between actions and groups
CREATE TABLE group_actions (
     group_id BigInt,
     action_id BigInt,
     foreign key (group_id) references user_group(id),
     foreign key(action_id) references action(id)
);

-- Giving all permissions to Super admin
insert into group_actions(group_id, action_id) values(1,1);
insert into group_actions(group_id, action_id) values(1,2);
insert into group_actions(group_id, action_id) values(1,3);
insert into group_actions(group_id, action_id) values(1,4);
-- Giving read-only permission to merchantXView and merchantYView
insert into group_actions(group_id, action_id) values(2,1);
insert into group_actions(group_id, action_id) values(4,1);
-- Giving all permissions to merchantXAdmin and merchantYAdmin
insert into group_actions(group_id, action_id) values(3,1);
insert into group_actions(group_id, action_id) values(5,1);
insert into group_actions(group_id, action_id) values(3,2);
insert into group_actions(group_id, action_id) values(5,2);
insert into group_actions(group_id, action_id) values(3,3);
insert into group_actions(group_id, action_id) values(5,3);
insert into group_actions(group_id, action_id) values(3,4);
insert into group_actions(group_id, action_id) values(5,4);

-- Creating user
CREATE TABLE user (
     id BigInt AUTO_INCREMENT PRIMARY KEY,
     username VARCHAR(255) NOT NULL,
     password VARCHAR(255) NOT NULL,
     email VARCHAR(255) NOT NULL UNIQUE
);

-- Creating a join table that contains mapping between users and groups
CREATE TABLE user_group_map (
     user_id BigInt,
     group_id BigInt,
     foreign key (user_id) references user(id),
     foreign key(group_id) references user_group(id)
);

-- Creating category table which references to itself for having indefinite depth
CREATE TABLE category (
     id BigInt AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     parent_id BigInt,
     foreign key (parent_id) references category(id)
);

-- Adding some pre-defined categories
insert into category(name) values("uncategorized");
insert into category(name) values("men");
insert into category(name) values("women");
insert into category(name,parent_id) values("shirtMen", 2);
insert into category(name,parent_id) values("shirtWomen", 3);
insert into category(name,parent_id) values("pantMen", 2);
insert into category(name,parent_id) values("pantWomen", 3);
insert into category(name,parent_id) values("casualShirtMen", 4);
insert into category(name,parent_id) values("casualShirtWomen", 5);
insert into category(name,parent_id) values("formalShirtMen", 4);
insert into category(name,parent_id) values("formalShirtWomen", 5);
insert into category(name,parent_id) values("jeansMen", 6);
insert into category(name,parent_id) values("jeansWomen", 7);
insert into category(name,parent_id) values("trousersMen", 6);
insert into category(name,parent_id) values("trousersWomen", 7);

/*
Creating product table with references to Groups(so that members of that group can access this)
and references to category(so that it can have significant most sub category tagged)
*/
CREATE TABLE product (
     id BigInt AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     description VARCHAR(255) NOT NULL,
     stock BigInt NOT NULL,
     size VARCHAR(255) NOT NULL,
     color VARCHAR(255) NOT NULL,
     category_id BigInt,
     group_id Bigint,
	 is_active boolean,
     foreign key (category_id) references category(id),
     foreign key (group_id) references user_group(id)
);
