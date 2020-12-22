# Optimization Use Case Descriptions


## Use Case: Plan routes for the day and assign drivers.

###Preconditions: This use case is triggered automatically at a specified time during the day provided there is Order data in the database.

###Input: Order data stored in the database.

###Output: Planned routes for each driver needed for the day.

1. System retrieves data of orders due for the next day.
2. System will request time and distance data for all locations from Google Maps API.
3. System will store returned data possibly in a graph data structure.
4. System will determine the number of trucks needed to deliver the orders efficiently based on the order sizes and the maximum capacity of a truck.
5. System will then subdivide the graph into subgraphs depending on the number of trucks needed.
6. System will then run a genetic algorithm on each subgraph to determine an optimal route.
7. System will then prompt the logistics manager to choose the drivers that will make these deliveries after calculating the routes.
8. See Use Case: Inform assigned drivers one day in advance.

![Optimization_Subsystem](Activity_Diagrams/Optimization_Subsystem/Plan%20routes%20for%20the%20next%20day%20and%20assign%20a%20driver%20to%20each%20route.svg)


## Use Case: Inform assigned drivers one day in advance.

### Preconditions: Routes have been calculated and drivers for the job have been chosen.

### Input: Drivers that have been assigned to the job.

### Output: Selected drivers informed of the job via the app.

1. System will update the database and notify the assigned drivers of the available job.
2. Driver may see the route and start navigation on the morning of the job.

![Optimization_Subsystem](Activity_Diagrams/Optimization_Subsystem/Inform%20assigned%20drivers%20via%20email.svg)

## Use Case: Request Location of Driver

### Preconditions: Order tracking request must be received from the Logistics Manager subsystem

### Input: Order ID or Driver ID to be tracked. 

### Output: Location of the specified order or driver.

1. Phaki consultant/logistics manager clicks on "Track an order" button.
2. System will respond by displaying a list of orders that are being delivered as well as information about the truck and the driver making the delivery. Also, each order displayed will contain a link that displays the most recent location of the driver delivering the order. 
3. Phaki consultant/logistics manager can now click on a particular order to view the driver’s location.
4. See use case: Request Location of the driver.
6. System then receives the location information and displays the location using the Google Maps API.

### Extensions:
1. An error occurs when trying to retrieve the Drivers location.
	A. Phaki consultant/logistics manager is notified of the failed attempt and is asked if he/she would like to retry.
	B. System attempts the location request again.
	C. If the attempt fails then step A is repeated, otherwise step 6 above is executed.

### Extensions:
1. An error occurs when trying to retrieve the Drivers location.
	A. Phaki consultant/logistics manager is notified of the failed attempt and is asked if he/she would like to retry.
	B. System attempts the location request again.
	C. If the attempt fails then step A is repeated, otherwise step 6 above is executed.


![Optimization_Subsystem](Activity_Diagrams/Optimization_Subsystem/Request%20Location%20of%20driver.svg)


## Use Case: Log undelivered orders.

### Preconditions: Driver has reported undelivered orders through driver subsystem.

### Input: ID of driver that reported the undelivered orders. 

### Output: Flip dates of undelivered orders so that the system can consider them when planning the next day's routes.

1. Driver subsystem reports undelivered orders.
2. System uses Driver ID to identify all orders not marked as delivered.
3. System logs the delayed orders in the database.
4. System flips dates of undelivered orders by 1 day.


![Optimization_Subsystem](Activity_Diagrams/Optimization_Subsystem/Log%20undelivered%20orders.svg)
