-- USERS

INSERT INTO users(id, name, phone, password)
VALUES
(1,
'Prashant',
'9876543210',
'$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6G8K30YIOCwRvDDDeWGPAHDq6cNru');



-- DRIVERS

INSERT INTO driver(
id,
name,
phone,
password,
vehicle_type,
available,
price,
current_location
)
VALUES
(
1,
'Rajesh',
'9999999991',
'$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6G8K30YIOCwRvDDDeWGPAHDq6cNru',
'TEMPO',
true,
5000,
'Mumbai'
),

(
2,
'Amit',
'9999999992',
'$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6G8K30YIOCwRvDDDeWGPAHDq6cNru',
'TRUCK',
false,
7000,
'Mumbai'
),

(
3,
'Suresh',
'9999999993',
'$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6G8K30YIOCwRvDDDeWGPAHDq6cNru',
'TEMPO',
true,
4500,
'Mumbai'
),

(
4,
'Mahesh',
'9999999994',
'$2a$10$7EqJtq98hPqEX7fNZaFWoOHi6G8K30YIOCwRvDDDeWGPAHDq6cNru',
'MINI_TRUCK',
true,
5500,
'Mumbai'
);



-- RIDES

INSERT INTO ride(
id,
pickup_location,
drop_location,
goods_type,
amount,
status,
user_id,
driver_id
)
VALUES
(
1,
'Mumbai',
'Nashik',
'Paper Bundles',
5000,
'PENDING',
1,
1
);