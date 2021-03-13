# pizza

How to Run and test this :

1. Open command prompt and go to folder containing 'WAR' file.
2. Type the command below and press enter to run the server.
3. java -jar [warname].war -Dserver.port=[X] --mode='server' --ovenBakingTime=[Y]

Example : java -jar GANPizza.war -Dserver.port=8080 --mode='server' --ovenBakingTime=30

Note: Client and server are in same war

4. Open another command prompt and go to folder containing 'WAR' file.
5. Type the command below and press enter to run the client.
6. java -jar [warname].war --mode='client'

Example : java -jar GANPizza.war --mode='client'

7.Choose the options you see on screen like below :

1. 5 mushrooms, 10 cheese, 2 sausages
2. 1 + 3 pineapples, 10 cheese, 2 sausages
3. EXTRA HOT - 10 pepperoni, 20 jalapeno, 2 sausages
4. 2 x pineapple from option 2 + 2 hams, 5 jalapenos, 2 sausages
5. Check order status using above ids
6. Exit app
What do you want to do: 

NOTE : Run as many clients as u want upto 100 by opening new command prompts and repeat steps 5 and 6.


Design Notes :

1. Project created with Spring Boot starters.

2. Design Patterns used are as below :

3. ApplicationStateHolder.java follows Singleton Design pattern to maintain single instance for client requests. This is to maintain requirement of "Immediately notify users when ingredients are no longer available"

4. OrderController.java is the java class that is exposed as a REST Service end point. This handles the order request from client and first checks for ingredients availability and upon which it calls method "bakePizzas". This method calls "ApplicationStateHolder" which is a single instance that connect to "Kitchen" class which handles pizza order and returns a list. This is a Factory design Pattern.

5. OvenController.java is used to add Ovens only. Selection of Oven and its status is taken care by using Model object "Oven.java" in an appropriate way.

6. Business Objects are under package "com.example.pizza.bom"

7. JavaMethod names are created in a way to be self explanatory to its purpose.

8. Client is also inbuilt in this project and can be confusing. ApplicationStateCreator.java and ClientConsoleRunner.java drive the client part.
