DROP TABLE IF EXISTS User;

CREATE TABLE User (UserID LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
UserName VARCHAR(30) NOT NULL,EmailAddress VARCHAR(30) NOT NULL,IsActive BOOLEAN NOT NULL);

CREATE UNIQUE INDEX idx_user_unique on User(UserName,EmailAddress);

INSERT INTO User (UserName, EmailAddress, IsActive) VALUES ('jon saunders','saunders.jon@gmail.com',true);
INSERT INTO User (UserName, EmailAddress, IsActive) VALUES ('david rodilla','rodilla.david@gmail.com',true);
INSERT INTO User (UserName, EmailAddress, IsActive) VALUES ('ogun bilge','bilge.ogun@gmail.com',true);
INSERT INTO User (UserName, EmailAddress, IsActive) VALUES ('mehdi taj','mehdi.taj@gmail.com',true);


DROP TABLE IF EXISTS AccountType;

CREATE TABLE AccountType (AccountTypeID INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
AccountTypeName VARCHAR(50));
INSERT INTO AccountType(AccountTypeName) VALUES ('Current');
INSERT INTO AccountType(AccountTypeName) VALUES ('Savings');

DROP TABLE IF EXISTS Account;

CREATE TABLE Account (AccountID LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
AccountTypeID INT ,Balance DECIMAL(20,3),
Currency VARCHAR(30),
UserID Long,IsActive BOOLEAN NOT NULL);

ALTER TABLE Account 
ADD FOREIGN KEY (AccountTypeID ) 
REFERENCES AccountType (AccountTypeID);

ALTER TABLE Account 
ADD FOREIGN KEY (UserID ) 
REFERENCES User(UserID);

CREATE UNIQUE INDEX idx_acc on Account(UserID,AccountTypeID,Currency);

INSERT INTO Account (AccountTypeID,Balance,Currency,UserID,IsActive) SELECT 1,5000.000,'EUR',UserID,1 FROM User WHERE UserName='jon saunders' AND EmailAddress='saunders.jon@gmail.com';
INSERT INTO Account (AccountTypeID,Balance,Currency,UserID,IsActive) SELECT 1,4000.000,'EUR',UserID,1 FROM User WHERE UserName='david rodilla' AND EmailAddress='rodilla.david@gmail.com';
INSERT INTO Account (AccountTypeID,Balance,Currency,UserID,IsActive) SELECT 1,700.000,'EUR',UserID,1 FROM User WHERE UserName='ogun bilge' AND EmailAddress='bilge.ogun@gmail.com';
INSERT INTO Account (AccountTypeID,Balance,Currency,UserID,IsActive) SELECT 2,8000.000,'EUR',UserID,1 FROM User WHERE UserName='ogun bilge' AND EmailAddress='bilge.ogun@gmail.com';
INSERT INTO Account (AccountTypeID,Balance,Currency,UserID,IsActive) SELECT 1,735.000,'EUR',UserID,1 FROM User WHERE UserName='mehdi taj' AND EmailAddress='mehdi.taj@gmail.com';

COMMIT