# Vending Machine API
An API for a vending machine that limits functionalities to certain user roles.

Users with a **“seller”** role can
 add, update or remove products

Users with a **“buyer”** role can 
 deposit money into the machine and make purchases.

It only accepts amounts in 50, 100, 200, 500, 1000 denominations.

Some features as below:

- CRUD endpoints for users
- CRUD endpoints for products
- Deposit money
- Buy product
- Reset balance
- Email notification
- Terminate all active sessions

## Tech/framework used

- Springboot 3.3.4
- Java 17
- maven 3.9.8
- Lombok 1.18.34
- H2 Database

## Step-By-Step guide to setup project on local system

- Clone project from the git repository using this link [Git Repo](https://github.com/WisdomEssien/vending-machine-api.git).

- Open command prompt and navigate to the desired directory. Copy, paste and execute the git command below on the command prompt.
  _**You must git installed on your system**._

```
	git clone -b main https://github.com/WisdomEssien/vending-machine-api.git
```

- Once the project is completely downloaded, switch to the project directory, build and run the application.
  _**You must have maven and Java17 installed on your system**._

```
	cd vending-machine-api
```

```
	mvn clean package
```

```
	cd target
```

```
	java -jar vending-machine-api-1.0.jar
```

- Import the Postman Collection shared in the email. There are saved request/responses to test with.
    - You can start from ``product`` and ``user`` creation endpoints for a seamless test.
- Launch the database console using this link: [H2 Database Console](http://localhost:7789/h2-console/).
  Copy and paste link directly on the browser if you are experiencing any difficulty redirecting.
- Use the credentials below when prompted to login

```
   - User Name: sa
   - Password:
   - JDBC URL:	jdbc:h2:mem:mydb
   - Driver Class: org.h2.Driver
```

Hope this was helpful to get you started

[wisdom essien](https://github.com/WisdomEssien/vending-machine-api.git) © 2024 
