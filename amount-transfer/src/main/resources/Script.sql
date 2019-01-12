DROP TABLE IF EXISTS User;

CREATE TABLE User (UserID LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,UserName VARCHAR(30) NOT NULL,EmailAddress VARCHAR(30) NOT NULL,IsActive BOOLEAN NOT NULL);

CREATE UNIQUE INDEX idx_user_unique on User(UserName,EmailAddress);

INSERT INTO User (UserName, EmailAddress, IsActive) VALUES ('jon saunders','saunders.jon@gmail.com',true);
INSERT INTO User (UserName, EmailAddress, IsActive) VALUES ('david rodilla','rodilla.david@gmail.com',true);
INSERT INTO User (UserName, EmailAddress, IsActive) VALUES ('ogun bilge','bilge.ogun@gmail.com',true);
INSERT INTO User (UserName, EmailAddress, IsActive) VALUES ('mehdi taj','mehdi.taj@gmail.com',true);

DROP TABLE IF EXISTS AccountType;

CREATE TABLE AccountType (AccountTypeID INT PRIMARY KEY AUTO_INCREMENT NOT NULL,AccountTypeName VARCHAR(50));
INSERT INTO AccountType(AccountTypeName) VALUES ('Current');
INSERT INTO AccountType(AccountTypeName) VALUES ('Savings');

DROP TABLE IF EXISTS Account;

CREATE TABLE Account (AccountID LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,AccountTypeID INT ,Balance DECIMAL(20,2),Currency VARCHAR(30),UserID Long,IsActive BOOLEAN NOT NULL,IBAN VARCHAR(50));

ALTER TABLE Account ADD FOREIGN KEY (AccountTypeID ) REFERENCES AccountType (AccountTypeID);

ALTER TABLE Account ADD FOREIGN KEY (UserID ) REFERENCES User(UserID);

CREATE UNIQUE INDEX idx_unique_account on Account(AccountTypeID,UserID);

INSERT INTO Account (AccountTypeID,Balance,Currency,UserID,IsActive,IBAN) SELECT 1,5000.00,'EUR',UserID,1,'CH103211000000000001' FROM User WHERE UserName='jon saunders' AND EmailAddress='saunders.jon@gmail.com';
INSERT INTO Account (AccountTypeID,Balance,Currency,UserID,IsActive,IBAN) SELECT 1,4000.00,'EUR',UserID,1,'CH103211000000000002' FROM User WHERE UserName='david rodilla' AND EmailAddress='rodilla.david@gmail.com';
INSERT INTO Account (AccountTypeID,Balance,Currency,UserID,IsActive,IBAN) SELECT 1,700.00,'EUR',UserID,1,'CH103255000000000003' FROM User WHERE UserName='ogun bilge' AND EmailAddress='bilge.ogun@gmail.com';
INSERT INTO Account (AccountTypeID,Balance,Currency,UserID,IsActive,IBAN) SELECT 2,8000.00,'EUR',UserID,1,'CH103255000000000004' FROM User WHERE UserName='ogun bilge' AND EmailAddress='bilge.ogun@gmail.com';
INSERT INTO Account (AccountTypeID,Balance,Currency,UserID,IsActive,IBAN) SELECT 1,735.00,'EUR',UserID,1,'CH103211000000000005' FROM User WHERE UserName='mehdi taj' AND EmailAddress='mehdi.taj@gmail.com';


DROP TABLE IF EXISTS Country;

CREATE TABLE Country (CountryID INT PRIMARY KEY AUTO_INCREMENT NOT NULL,CountryCode VARCHAR(2),CountryName VARCHAR(30));
INSERT INTO Country(CountryCode,CountryName) VALUES ('CH','Switzerland');
INSERT INTO Country(CountryCode,CountryName) VALUES ('DE','Germany');

DROP TABLE IF EXISTS Branch;

CREATE TABLE Branch (BranchID INT PRIMARY KEY AUTO_INCREMENT NOT NULL,BranchCode VARCHAR(10),Branch VARCHAR(30),CountryID INT);

ALTER TABLE Branch ADD FOREIGN KEY (CountryID) REFERENCES Country(CountryID);

INSERT INTO Branch(BranchCode,Branch,CountryID) SELECT '103211','Zurich',CountryID FROM Country WHERE CountryCode='CH';
INSERT INTO Branch(BranchCode,Branch,CountryID) SELECT '103255','Zug',CountryID FROM Country WHERE CountryCode='CH';
INSERT INTO Branch(BranchCode,Branch,CountryID) SELECT '3001523','Berlin',CountryID FROM Country WHERE CountryCode='DE';
INSERT INTO Branch(BranchCode,Branch,CountryID) SELECT '3031714','Munich',CountryID FROM Country WHERE CountryCode='DE';

COMMIT