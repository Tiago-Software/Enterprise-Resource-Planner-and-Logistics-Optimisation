# Requirements Extraction
## Functional requirements:
- System Core Requirements:
    - API to create delivery instructions.
        - API receives product orders and the quantity of desired resources.
        - API dictates the schedule of drivers.
    - Web application to manage drivers and delivery instructions.
        - Will allow the consultant to view order history.
        - Will allow the consultant to log a new order.
        - Will allow the consultant to cancel pre-existing order. 
        - Will allow the consultant to update undelivered order information.
        - Will allow the consultant to view the location of a particular order.
        - Will allow the Logistics Manager to register and update driver information.
        - Will allow the Logistics Manager to overview the current jobs drivers are executing.
        - Will allow the Logistics Manager to view upcoming delivery schedules as well as drivers that will be working on those days.
        - Will allow the Logistics Manager to access delivery/order information.
        - Will allow the Logistics Manager to Add/Remove number of trucks available.
        - It will allow the Logistics Manager to view the route used by every driver.
        - Will allow the Logistics Manager to access a reporting tool.
        - Web application will make use of two-factor authentication for security.
    - Scheduled Route planning
        - Scheduler that will plan and assign each driver a route for deliveries.
        - Scheduler must automate a planned schedule and ensure driver information is correctly distributed.
        - Scheduled jobs should be communicated one day in advance.
    - Driver mobile app.
        - Drivers can view their scheduled jobs and routes.
        - Mobile app allows drivers to launch jobs.
        - Mobile app notifies drivers of scheduled jobs.
        - If an order is canceled next available job is assigned to current job via API.
        - Drivers can confirm delivery of package by scanning the QR code on a client's invoice.
     Client
        - Receives invoices through email.
- System Optional Requirements:
    - Fleet optimization
        - Coordinating drivers from the shortest to shortest distance to save petrol.
        - Ensure that a Truck is always carrying a maximum load when assigned a job before another truck is utilized.



        
## Non-Functional requirements:
- Performance 
    - It will be measured in how fast we can communicate delivery information to drivers from the initial time the job was logged with the use of a timeline.
    - It will be measured on a timeline basis for how fast we can get drivers to depart to their destinations from the Phaki branch. 
- Security
    - Users must meet the prerequisite to be validated to enter the website.
    - Driver must meet the prerequisite for an order/delivery to be confirmed.
- Reliability 
    - The average time the system runs before failure.
- Availability
    - API up time to communicate with web application and driver application.
- Scalability
    - Measuring how long the API takes to communicate information if a large influx of orders is presented. 
- Capacity
    - It will be measured by how many orders can be accommodated without failure on the system.
- Recoverability
    - How responsive is the system when a rollback needs be called when the system presents itself as unavailable.
- Maintainability
    - How the performance of the system will react to new features after the initial release.