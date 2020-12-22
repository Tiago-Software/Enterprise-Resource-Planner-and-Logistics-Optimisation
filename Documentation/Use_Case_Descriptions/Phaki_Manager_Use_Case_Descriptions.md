# Phaki Manager Use Case Descriptions

## General precondition: User must be logged in as a Logistics Manager on the website.

## Use Case: Register a new driver

### Input: Phaki Logistics Manager enters all required driver information

### Output: New driver being registered on system

1. The system captures and inputs information to the driver database.
2. Once a driver is successfully registered, the system returns a confirmation message.
3. If registration is unsuccessful the system will be prompt the manager to correct any errors in the required fields.

![Activity Diagram](Activity_Diagrams/Logistics_Manager_Subsystem/Register%20New%20Driver.svg)

## Use Case: Update driver information

### Input: Phaki Logistics Manager navigates to drivers.

### Output: Updated driver information being registered on system

1. The system retrieves a list of Phaki's drivers.
2. Phaki Logistics Manager selects the targeted driver.
3. The system will retrieve information from the database of the targeted driver.
4. Phaki Logistics Manager will have the ability to edit a drivers information by field or add to the driver's information.
5. The system will update targeted driver information.
6. The system will display targeted driver information that was just updated.
7. If an error occurs in updating a drivers information the manager will be prompted to correct the error.

![Activity Diagram](Activity_Diagrams/Logistics_Manager_Subsystem/Update%20Driver%20Information.svg)

## Use Case: View route used by every driver

### Input: Phaki Logistics Manager navigates to active jobs

### Output: Routes used by every driver 

1. The system will retrieve a list of jobs.
2. Phaki Logistics Manager navigates to a driver currently executing a job.
3. The system will return the route the driver is executing, with all relevant details of the job and driver.

![Activity Diagram](Activity_Diagrams/Logistics_Manager_Subsystem/View%20Route%20Used%20By%20Each%20Driver.svg)

## Use Case: Track each job being executed by each driver

### Input: Phaki Logistics Manager navigates to active jobs.

### Output: Route and Job information of a targeted driver

1. The system will retrieve a list of current active live jobs. 
2. Phaki Logistics Manager navigates to a driver currently executing a job.
3. Phaki Logistics Manager can view job details of the route being executed along with all relevant details of the job and driver.

![Activity Diagram](Activity_Diagrams/Logistics_Manager_Subsystem/Track%20Each%20Job%20Being%20Executed%20By%20Each%20Driver.svg)

## Use Case: View order or delivery information

### Input: Phaki Logistics Manager navigates to order/delivery list.

### Output: Targeted order or delivery information 

1. The system retrieves a list of orders or deliveries.
2. Phaki Logistics Manager selects the targeted order or delivery.
3. System returns order details.

![Activity Diagram](Activity_Diagrams/Logistics_Manager_Subsystem/View%20Order%20or%20delivery%20Information.svg)

## Use Case: View current stock levels

### Input: Phaki Logistics Manager navigates to stock levels list.

### Output: Stock levels

1. The system retrieves a list of Phaki stock levels.
2. Phaki Logistics Manager views the current stock list.

![Activity Diagram](Activity_Diagrams/Logistics_Manager_Subsystem/View%20stock%20levels.svg)

## Use Case: Update order or delivery information

### Input: Phaki Logistics Manager navigates to order/delivery list.

### Output: Updated order or delivery information registered on system 

1. The system retrieves a list of orders or deliveries.
2. Phaki Logistics Manager selects the targeted order or delivery.
3. Phaki Logistics Manager updates orders or delivery details.
4. System returns order details.
5. If an error occurs in the updating of order or delivery information the manager will be prompted to correct the error.

![Activity Diagram](Activity_Diagrams/Logistics_Manager_Subsystem/Update%20order%20or%20delivery%20information.svg)

## Use Case: View report on delivery history

### Input: Phaki Logistics Manager navigates to report portal

### Output: Reports

1. The reporting tool will display current reports available.
2. Phaki logistics manager will have the ability to filter reports.
3. The reporting tool will display filtered reports.

![Activity Diagram](Activity_Diagrams/Logistics_Manager_Subsystem/View%20Reports.svg)

## Use Case: Add/Remove the number of trucks available 

### Input: Phaki logistics manager navigates to Phaki's truck list

### Output: List of trucks available

1. The system will return a list of trucks with a status of either available or unavailable.
2. Phaki Logistics Manager will assign what trucks are available or unavailable to the current list of trucks.
3. Phaki Logistics Manager will assign a driver to a truck.
4. Phaki Logistics Manager can remove a truck off the available truck list.
5. The system will update the list of available trucks.

![Activity Diagram](Activity_Diagrams/Logistics_Manager_Subsystem/Add%20or%20remove%20trucks.svg)

## Use Case: View delivery schedules and their assigned drivers

### Input: Phaki Logistics Manager navigates to delivery schedules.

### Output: Delivery schedules and the assigned drivers to those schedules

1. The system retrieves a list of delivery schedules.
2. Phaki Logistics Manager selects a targeted driver from the delivery schedule list.
3. The system will return driver information and a full delivery description.

![Activity Diagram](Activity_Diagrams/Logistics_Manager_Subsystem/View%20Delivery%20Schedule%20And%20Their%20Assigned%20Driver.svg)

## Use Case: Report an unforeseen delay
 
### Precondition: Driver must have reported a delay via the mobile app.

### Input: A detailed description of the cause of the delay, the estimated time delay and a message to the affected clients.

### Output: Delay is logged and an email is sent to affected clients.

1. The system receives delay information from the driver mobile app.
2. Logistics Manager reports a delay via the website.
3. System records delay information entered by the logistics Manager.
4. The system will retrieve the email addresses of the affected clients from the database.
5. The system will then construct an email using the report details provided by the logistics manager.
6. The system will send the constructed email to all the affected email addresses received from the database.

![Activity Diagram](Activity_Diagrams/Logistics_Manager_Subsystem/Report%20an%20unforseen%20delay.svg)