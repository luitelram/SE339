drop table Friends;
drop table Nofitication;
drop table EventsHistory;
drop table EventsCurrent;
drop table Users;
CREATE TABLE Users(
	Username char(30) not null unique,
	Passwd char(30) not null,
    Fullname char(80) not null,
    DOB char(10) not null,
    Gender char(6) not null,
    Address char(255) not null,
    Phone char(15) not null,
    Email char(50) not null,
    Rating char(3) not null default 0,
	Avatar char (120) not null,
	IsAdmin char (1) not null default 0 comment '0: User - 1:Admin',
	Block char(1) not null default 0 comment '0: Not Block - 1:Block',
	primary key(username)
);
CREATE TABLE Friends(
	FriendID int not null auto_increment,
    Username CHAR(30) not null,
	Friendname char(30) not null,
	primary key(FriendID),
	foreign key (Username) references Users(Username),
	foreign key (Friendname) references Users(Username)
);
CREATE TABLE EventsCurrent(
	EventCurrentID int not null auto_increment,
    EventName char(20) not null,
    EventDescription char(255),
    EventAddress char(80) not null,
    EventTime char(20) not null,
    EventDate char(20) not null,
    HostName	char(30) not null,
	Restriction char(1) not null comment '0:Everyone - 1:21+ - 2:18+',
	EventType char(1) not null default 0 comment '0: Public - 1:Private',
	primary key(EventCurrentID),
	foreign key (HostName) references Users(Username)
);
CREATE TABLE EventsHistory(
	EventHistoryID int not null auto_increment,
	EventID int not null,
	HostUsername char(30) not null,
	primary key (EventHistoryID),
	foreign key (EventID) references EventsCurrent(EventCurrentID),
	foreign key (HostUsername) references Users(Username)
);
CREATE TABLE Nofitication(
	NotificationID char(50) not null,
    FromUser CHAR(30) not null,
	ToUser char(30) not null,
    NofiticationType char(1) not null comment 'F:Friend Request - I:Invitation',
    EventCurrentID int,
	primary key(NotificationID),
	foreign key (FromUser) references Users(Username),
	foreign key (ToUser) references Users(Username)
);
insert into Users value('khbui','11111111','khoa','08/12/1989','Male','1923 64th St,Des Moines,IA 50324','5155885555','khbui@iastate.edu','1.5','avatars/khbui.png','1','0');
insert into Users value('a','11111111','a name','02/15/1993','Male','2058 2nd Ave,Des Moines,IA 50311','5155891585','aemail@email.edu','2','avatars/a.png','0','0');
insert into Users value('b','11111111','b name','05/26/1995','Female','9235 Riverside Lodge,Houston,TX 88923','8324895665','bmaili@gmail.com','1.8','avatars/b.png','0','0');
insert into Users value('Luitel','12345678','Ram','02/13/1992','Male','s Oak','12345678','rluitel@gmail.com','2.1','avatars/Luitel.png','0','0');
insert into EventsCurrent(EventName,EventDescription,EventAddress,EventTime,EventDate,HostName,Restriction,EventType) value ('Birthday','Someone celebrate birthday','123 Ames St,Ames,IA 50332','11:00am','12/22/2017','a','0','1');
insert into EventsCurrent(EventName,EventDescription,EventAddress,EventTime,EventDate,HostName,Restriction,EventType) value ('SingASong','Sing-Sing-Sing','854 University Boul,Rosemead,CA 95332','1:00pm','6/18/2017','b','0','0');
insert into EventsCurrent(EventName,EventDescription,EventAddress,EventTime,EventDate,HostName,Restriction,EventType) value ('Meeting','Impormtant','123 Ames St,Ames,IA 50332','8:00am','4/2/2017','a','0','0');
insert into EventsCurrent(EventName,EventDescription,EventAddress,EventTime,EventDate,HostName,Restriction,EventType) value ('Faizuls Home Party','Home','1027 6th Street, Ames, Iowa','8:00pm','9/22/2017','b','0','1');
insert into EventsCurrent(EventName,EventDescription,EventAddress,EventTime,EventDate,HostName,Restriction,EventType) value ('Performance','Relax','1923 64th st, des moines ia 50324','8:00am','4/2/2017','a','0','0');
insert into EventsCurrent(EventName,EventDescription,EventAddress,EventTime,EventDate,HostName,Restriction,EventType) value ('CA Tour','Vacation','1600 Amphitheatre Parkway, Mountain View, CA','2:00pm','8/1/2017','b','0','0');
insert into EventsCurrent(EventName,EventDescription,EventAddress,EventTime,EventDate,HostName,Restriction,EventType) value ('Home Sweet Home','Visit my country','1D/38 Hem 29,Le Duc Tho, Phuong 7, Go Vap, Ho Chi Minh, Vietnam','2:00pm','7/1/2017','khbui','0','1');
insert into Friends(Username,Friendname) value ('a','khbui');
insert into Friends(Username,Friendname) value ('a','b');
insert into Friends(Username,Friendname) value ('b','a');
insert into Nofitication value ('a~Luitel','a','Luitel','F',-1);