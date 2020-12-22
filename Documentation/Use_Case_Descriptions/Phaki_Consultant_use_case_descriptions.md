# Phaki Consultant Use Case Descriptions

###General precondition: The use case is activitated instantaneously when the consultant clicked the "Check Identity" button

##Usecase: Identitfy Client

###Input: The consultant insert the client 's phone number
###Output: The consultant gets information whether the person calling is a new client or an old client.

1. Phaki 's consultant receives a client 's call
2. The consultant inserts the client 's phone number into the system
3. The system checks if the client exists in the system.
4. If the client exits in the system, a new order is made based on using basic information like the name, location, etc from the past orders.
5. If the client does not exist in the system, a new order is made based on new information provided by the client and a new client profile'is saved.

![Activity Diagram](Activity_Diagrams/Consultant_Subsystem/IdentifyCient.svg)

##Usecase: View Order History

###Input: The consultant clicks the viewOrder History button
###Output: The consultant gains access to the past orders along with their details.

1. Phaki 's consultant logs into his or her profile
2. The consultant clicks on the Order History button
3. The syetem displays all the past orders in brief details if past orders exits
4. The system notifies the user that there are no past order if the order history is empty

![Activity Diagram](Activity_Diagrams/Consultant_Subsystem/ViewOrder%20History.svg)

##Use case: Log a new order

###Input: The consultant inserts customers ‘s name, the weight of the load, the time of delivery, the delivery location. the list of items part of the order.
###Output: The system notifies the user of the successful entry of a new order

1. Phaki 's consultant receives a call from a client
2. The consultant inserts the new order ‘s details ffrom the client
2. The consultant clicks the “Ädd New Order” button.
3. If the order is successfully captured, The system displays the message “Success entry”. 
4. if the information entered is not entirely valid, the system displays the message "Incorrect data".

![Activity Diagram](Activity_Diagrams/Consultant_Subsystem/Log%20a%20new%20order.svg)

##Usecase: Cancel a pre-exisiting order

###Input: The consultant inserts the order number
###Output: a confirmation of order deletion

1. Phaki 's consultant receives a client 's phone call
2. The consultant inserts the order 's number
2. The consultant then clicks the “Cancel Order” button.
3. The system prompts the consultant to confirm the cancellation of the order
4. If the consultant clicks on the yes icon, the order is cancelled
5. If the consultant clicks on the no icon, the order remains unchanged

![Activity Diagram](Activity_Diagrams/Consultant_Subsystem/CancelOrder.svg)

##Usecase: Update undelivered order information

###Input: The consultant inserts the order number
###Output: The confirmation of order updated

1. Phaki 's consultant receives a notice on an undelivered order
2. The consultant inserts the order 's number
3. The consultant requests the order
4. The consultant retrieves and inserts the order 's latest details
2. The consultant clicks the “Update Order” button.
3. The system verifies the order 
4. If the order exists, the system updates the order and sends the message "Update Successful"
5. If the order does not exit, the system notifies the user that "the order does not exist

![Activity Diagram](Activity_Diagrams/Consultant_Subsystem/Update%20the%20undelivered%20order%20information.svg)

##Usecase: View Location of a particular order

###Input: The consultant inserts the order number
###Output: The location of the order

1. Phaki 's consultant inserts the order number
2. The consultant clicks the button "locate Order".
3. The system verifies the order number.
4. If the order 's number are valid, the system  provides the order 's location to the consultant.
5. If the order 's number are invalid, the system prompts the user to insert accurate information.

![Activity Diagram](Activity_Diagrams/Consultant_Subsystem/View%20Location%20of%20a%20particular%20order.svg)
