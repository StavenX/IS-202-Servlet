/* Use following query to delete everything and allow scripts to be rerun */
DROP DATABASE IF EXISTS mandatory_two;

CREATE DATABASE mandatory_two;
USE mandatory_two;


/* Mandatory one, with edits for mandatory 2*/

/* ~ ~  ~ */
/* PART 1 */
/* ~ ~  ~ */

/* 3 */
CREATE TABLE IF NOT EXISTS customer (
	cust_id VARCHAR(5) PRIMARY KEY,
    cust_name VARCHAR(40),
    cust_phonenumber VARCHAR(20),
    cust_email VARCHAR(50),
    cust_address VARCHAR (50)
);


CREATE TABLE IF NOT EXISTS corporationDetails (
	corporation_id VARCHAR(5),
    cust_id VARCHAR(5),
    CONSTRAINT corp_corporation_id FOREIGN KEY (corporation_id) REFERENCES customer(cust_id),
    CONSTRAINT corp_cust_id FOREIGN KEY (cust_id) REFERENCES customer(cust_id),
    PRIMARY KEY (corporation_id, cust_id)
);

/* 4*/
INSERT INTO customer (cust_id, cust_name, cust_phonenumber, cust_email, cust_address)
	VALUES 	('1', 'Per Person', '12345', 'pp@gmail.com', 'Drammensveien 1'),
			('2', 'Falsk Menneske', '54321', 'falsk@gmail.com', 'UIA 023'),
			('3', 'Ola Nordmann', '65432', 'ola@gmail.com', 'Falskveien 123');
        
UPDATE customer
	SET cust_address = 'Dronningens Gate 1'
	WHERE cust_id = 1 ;

DELETE FROM customer 
	WHERE cust_id = '3';
    
INSERT INTO customer (cust_id, cust_name, cust_phonenumber, cust_email, cust_address)
	VALUES 	('4', 'Frank Espen', '78123', 'anynommail@gmail.com', 'En vei 23'),
			('5', 'Adrian Nesquick', '98765', 'anonym2@gmail.com', 'Veinummerto 3');

/* 5*/            
SELECT cust_name, cust_email FROM customer;

/* 6 */
/* Gives 0 results with our current table values */
SELECT * FROM customer
	WHERE cust_id LIKE 'alb%'
    OR cust_name LIKE 'alb%'
    OR cust_phonenumber LIKE 'alb%'
    OR cust_email LIKE 'alb%'
    OR cust_address LIKE 'alb%';


/* ~ ~  ~ */
/* PART 2 */
/* ~ ~  ~ */

/* 3*/
DESCRIBE customer;

/* 4 */

CREATE TABLE IF NOT EXISTS invoice (
	inv_id VARCHAR(10) PRIMARY KEY,
    inv_dateIssued DATE,
    inv_datePaid DATE,
    inv_cardNumber VARCHAR(16),
    inv_cardHolder VARCHAR(40),
    inv_dateExp DATE
);

CREATE TABLE IF NOT EXISTS orders (
	ord_id VARCHAR(10) PRIMARY KEY,
    cust_id VARCHAR(5),
    CONSTRAINT cust_id FOREIGN KEY (cust_id) REFERENCES customer(cust_id),
    ord_date DATE,
    ord_status VARCHAR(30),
    ord_billing_address VARCHAR(50),
    ord_payment_method VARCHAR(30),
    inv_id VARCHAR(10) DEFAULT NULL,
    CONSTRAINT inv_id FOREIGN KEY (inv_id) REFERENCES invoice(inv_id)    
);

/* 5 */
INSERT INTO orders (ord_id, cust_id, ord_date, ord_status, ord_billing_address)
VALUES 	('001', '1', '2015-08-08', 'Standby', 'Falskveien 56'),
		('002', '1', '2018-07-17', 'Sent', 'Falskveien 56'),
		('003', '2', '2018-01-16', 'Delivered', 'Stålveien 42'),
		('004', '2', '2015-08-21', 'Lost', 'Stålveien 42'),
        ('005', '4', '2018-02-13', 'Standby', 'Gullveien 13'),
		('006', '4', '2018-02-23', 'Delivered', 'Gullveien 15'),
		('007', '5', '2018-05-18', 'Standby', 'Bronseveien 7717'),
		('008', '5', '2018-08-15', 'Sent', 'Sølvgata 3A');

/* Code in next comment will never run, hence why it is commented */
/*
DELETE FROM customer
WHERE cust_id = '4';
*/

/* 6 */
SELECT customer.cust_name, orders.ord_date, orders.ord_status
FROM (customer INNER JOIN orders ON customer.cust_id = orders.cust_id);


/* 7 */
/* Following doesn't work as it's incorrect SQL syntax */
/*
SELECT cust_name, ord_date, status FROM customer, order;
*/
/* The following DOES 'work' but doesn't give the data we want */
SELECT cust_name, ord_date, ord_status FROM customer, orders;


/* ~ ~  ~ */
/* PART 3 */
/* ~ ~  ~ */

/* 3 */
CREATE TABLE IF NOT EXISTS product (
	prod_id VARCHAR(20) PRIMARY KEY,
	prod_name VARCHAR (50),
    prod_price INT,
    prod_qis INT
    );

INSERT INTO product 
VALUES 	('0001', 'iPhone excess', 50000, 5),
		('0002', 'Samsung 4k TV', 20000, 10),
		('00AB5', 'Fortnite gift card', 500, 100000),
        ('0120C', 'Staren Grandiosa', 30, 500);
        
CREATE TABLE IF NOT EXISTS orderitems (
	ord_id VARCHAR(10),
    prod_id VARCHAR(20),
    CONSTRAINT ord_id FOREIGN KEY (ord_id) REFERENCES orders(ord_id),
    CONSTRAINT prod_id FOREIGN KEY (prod_id) REFERENCES product(prod_id),
    PRIMARY KEY (ord_id, prod_id),
    oi_prod_qt INT
    );

INSERT INTO orderitems
	VALUES	('001', '0001', 1),
			('002', '0001', 2),
            ('003', '0002', 1),
            ('004', '0120C', 4);


/* 4a */
SELECT orders.ord_date, SUBSTRING(product.prod_name, 1, 10) AS prod_shortname, product.prod_price
FROM (orders INNER JOIN orderitems ON orders.ord_id = orderitems.ord_id)
INNER JOIN product ON product.prod_id = orderitems.prod_id WHERE ord_date LIKE '2015-08-%';

/* 4b */
SELECT orders.ord_date, SUBSTRING(product.prod_name, 1, 10) AS prod_shortname, product.prod_price
FROM (orders INNER JOIN orderitems ON orders.ord_id = orderitems.ord_id)
INNER JOIN product ON product.prod_id = orderitems.prod_id WHERE ord_date LIKE '2015-08-%' AND prod_name LIKE '%STAR%';

/* 4c */
SELECT customer.cust_name, orderitems.oi_prod_qt, product.prod_name
FROM (customer INNER JOIN orders ON customer.cust_id = orders.cust_id)
INNER JOIN orderitems ON orderitems.ord_id = orders.ord_id
INNER JOIN product ON product.prod_id = orderitems.prod_id;

/* 4d */
SELECT customer.cust_name, orderitems.oi_prod_qt, product.prod_name
FROM (customer INNER JOIN orders ON customer.cust_id = orders.cust_id)
INNER JOIN orderitems ON orderitems.ord_id = orders.ord_id
INNER JOIN product ON product.prod_id = orderitems.prod_id
ORDER BY cust_name, prod_name;

/* 4e */
ALTER TABLE product
ADD COLUMN prod_reorderlvl INT;

/* ~~~~ */
/* ~~~~ */

/* Mandatory 2 queries starts here */

/* ~~~~ */
/* ~~~~ */

/* 3 */

INSERT INTO invoice 
VALUES	('01', '2018-07-07', '2018-07-08', '1234567812341234', 'Bob Bobsen', '2018-08-08'),
		('02', '2018-07-05', '2018-07-09', '1234567812344321', 'Falsk Menneske', '2018-09-05'),
        ('03', '2018-09-12', '2018-09-17', '1234567812345678', 'Ikke ekte', '2018-10-12');
        
UPDATE orders 
SET inv_id = '01' WHERE ord_id = '001';

UPDATE orders
SET inv_id = '02' WHERE ord_id = '002';

UPDATE orders
SET inv_id = '03' WHERE ord_id = '003';

        
/* 4a */

SELECT customer.cust_name, orderitems.oi_prod_qt, product.prod_name
FROM (customer INNER JOIN orders ON customer.cust_id = orders.cust_id)
INNER JOIN orderitems ON orderitems.ord_id = orders.ord_id
INNER JOIN product ON product.prod_id = orderitems.prod_id;

/* Note for 4a: Per Person has bought iphones in 2 seperate orders, so it will show up twice in the results */


/* 4b */
SELECT product.prod_name, SUM(orderitems.oi_prod_qt) AS quantity, SUM(orderitems.oi_prod_qt) * product.prod_price AS total_paid
FROM (product INNER JOIN orderitems ON product.prod_id = orderitems.prod_id)
GROUP BY prod_name
ORDER BY quantity DESC
LIMIT 3;

/* 4c */
CREATE VIEW FireC AS
SELECT customer.cust_name, orders.ord_id, SUM(orderitems.oi_prod_qt) * product.prod_price AS total_paid
FROM (customer INNER JOIN orders ON customer.cust_id = orders.cust_id)
INNER JOIN orderitems ON orders.ord_id = orderitems.ord_id
INNER JOIN product ON orderitems.prod_id = product.prod_id
GROUP BY ord_id, prod_name;

/* 4d */

/* prod_reorderlvl was null for all rows before this */
UPDATE product
SET prod_reorderlvl = 1;


SELECT SUBSTRING(prod_name,1,10) AS prod_name_short, prod_qis - prod_reorderlvl AS prod_quantity_before_reorder FROM product
WHERE SUBSTRING(prod_name,1,10) LIKE '%EN%';

/* 4e */
SELECT * FROM FireC;
