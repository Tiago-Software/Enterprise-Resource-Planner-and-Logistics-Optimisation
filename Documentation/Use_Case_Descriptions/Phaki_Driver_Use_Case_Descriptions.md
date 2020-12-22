# Phaki Driver Use Case Descriptions

## General preconditions: 
- Driver is logged into mobile app using two factor authentication.
- During stand-by the home screen displays links to current trip information, this functionality encourages the use of case #1 and #2.
- The main event during stand by is triggered when the service updates the current trip, driver accepts the change (case #3) which leads to case #4.
- Driver needs to be at the store. If not the app searches for current location and routes the driver back to store (while logged into app).

## Use Case #1: Commence route navigation

### Precondition: Driver needs to accept all orders before this event is triggered (Case #3)

### Input: Optimised personal driver route.
### Output: Driver arrives across respective client locations.

1. This event triggered by Case #3 consumes all orders returned from the service. If any order from service is pending, this Case #4 cannot be triggered.
2. Requirements are satisfied for this event, a screen will display a route to the first destination.
3. A Google maps API consumes that location and provides the shortest distance between the driver location and the respective location.
4. This is repeated until the driver returns to Phaki after delivering to each location.

![Commence_route_navigation](Activity_Diagrams/Driver_Subsystem/1-Commence%20route%20navigation.svg)

## Use Case #2: View daily route

### Input: Locations received from service.
### Output: Overview of each location mapped by distance.

1. Event is triggered with a button click, a screen displays all locations to be visited for the current trip
2. Driver has an overview of his current route.
3. All locations are displayed on a list for the Driver to overview.
4. Sort all locations by distance to enhance driver knowledge.
5. An invoice is displayed if a location item on the list is clicked, for the driver to physically verify (security purposes). - see use case #2
6. Each order has a green, orange or red indicator:
    - Green - Order is accepted
    - Orange - Order is pending
    - Red - Order is invalid (Rare event identified by the service, whether supply cannot meet demand or last minute cancelation).

![View_order_per_location](Activity_Diagrams/Driver_Subsystem/2-View%20daily%20route.svg)

## Use Case #3: Accept change to order collection

### Input: Orders received from service respective to location.
### Output: Confirmation the driver has recieved the correct items per order.

1. A notification on the trip update is displayed, driver can view new daily route (Case #1), or accept all orders(Case #3).
2. Driver clicks on a location item on list, triggering this event, a screen displays order information matched to the respective location.
3. Order information records 
    - items in package 
    - client information 
    - address
4. The information displayed is not regarded as a reciept, thats for the client. This invoice is tracked using a unique id and we extract data linked to that order.

![Accept_Change_to_Order_Collection](Activity_Diagrams/Driver_Subsystem/3-Accept%20Change%20to%20Order%20Collection.svg)

## Use Case #4: View order per location

### Input: Invoices recieved from service respective to location.
### Output: Matching invoice to delivery.

1. Based on the list used in use case #1.
2. Driver clicks on a location item on list, triggering this event, a screen displays invoice information matched to the respective location.
3. Invoice information records 
    - items in package 
    - client information 
    - address
4. The information displayed is not regarded as a reciept, thats for the client. This invoice is tracked using a unique id and we extract data linked to that order.

![View_order_per_location](Activity_Diagrams/Driver_Subsystem/4-View%20order%20per%20location.svg)

## Use Case #5: Update client delivery

### Specified pre-condition: Client needs a receipt of purchase

### Input: Client confirmation of received package.
### Output: Phaki manager may view route progress on each drivers route

1. Driver arrives at client location, google maps API closes and driver confirms on the app the correct location.
2. A Barcode scanner is available on the app awaiting for client confirmation
3. Client provides receipt via printout or email.
4. Driver scans receipt and client confirmation is now approved.
5. The service is now aware of the drivers availabilty and assigns the next client location for the Google maps API to consume.

![Update_client_delivery](Activity_Diagrams/Driver_Subsystem/5-Update%20client%20delivery.svg)

## Use Case #6: Report an unforeseen delay(Driver)

### Preconditions: User must be logged in to the mobile app.

### Input: A detailed description of the cause of the delay.

### Output: Delay is logged and reported to the Logistics Manager. An email is also sent to affected clients

1. Driver reports a delay via the app.
2. System relays the report to the Logistics Manager and he/she will be asked to fill out details regarding the delay including estimated delay time and message to affected clients.
3. System records delay information entered by the logistics Manager.
4. System will retrieve the email addresses of the affected clients from the database.
5. System will then construct an email using the report details provided by the logistics manager.
6. System will send the constructed email to all the affected email addresses recieved from the database.

![Report_an_unforeseen_delay](Activity_Diagrams/Driver_Subsystem/6-Report%20an%20unforeseen%20delay(Driver).svg)

