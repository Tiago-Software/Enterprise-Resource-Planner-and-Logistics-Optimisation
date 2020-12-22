# Introduction

The team at AlgoCodeIT has reached out to Codehesion to assist in a problem with their client. Codehesions's client is a logistics company named Phaki, delivering construction supplies to its customers. We are improving their current system of intensive paper-based delivery notes, causing drivers to plan their routes.

## Company Background
Delivery logistics company Phaki operates from their warehouse in Midlands Office Park, Midstream Estate in the economic hub Gauteng and has a fleet of 15 vehicles (and 15 drivers) but receive numerous manual delivery instructions daily to deliver their limited products from their warehouse location to customers delivery addresses around the city. 

## Current system(s) and/or procedures followed
To remove the current manual process and to grow their fleet efficiently they would like to make use of an automated fleet planning solution. The solution should be able to receive product orders (with delivery instructions) from customers via API. Then, each morning calculate routes for the fleet based on the orders that came through and lastly send a list of daily delivery instructions to the drivers via a mobile app.

 An order has the following attributes: · 
 * Each order has a ‘deliver by’ time limit which could be anytime during the day 

 * Each order has a delivery address where the package must be delivered 

 * Depending on which of the three products they order, the products have a different delivery task duration. A small load takes 5 minutes to deliver at location, a medium load takes 15 minutes to deliver at location, and a heavy load takes 20 minutes to deliver at location. This is only a nice to have, but Phaki would also like to optimize their fleet by using as little petrol and as little drivers as possible to execute daily delivery tasks. The good performance of the optimizer will be preferred.
