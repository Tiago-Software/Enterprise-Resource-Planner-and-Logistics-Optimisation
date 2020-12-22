# Optimization

- Fleet optimization
    - Capacity
        - How do we determine which packages go where?
        - Can the truck fit the order ?
    - Routes
        - How do we determine what is the shortest route between each distance?
        - Do the deliveries all relate to each other on the same path?(If we delivering bricks, are the orders being delivered all bricks and are they the shortest distance)
- Graph ADT ?

- Algorithms:
    - https://www.geeksforgeeks.org/travelling-salesman-problem-set-1/
    - Genetic algorithm https://www.hindawi.com/journals/cin/2017/7430125/		
    - https://towardsdatascience.com/k-means-clustering-algorithm-applications-evaluation-methods-and-drawbacks-aa03e644b48a
    - https://towardsdatascience.com/introduction-to-genetic-algorithms-including-example-code-e396e98d8bf3
    - https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=5&ved=2ahUKEwjUkcf_jtLoAhXNxYUKHZIHDKEQFjAEegQIARAB&url=https%3A%2F%2Fzenodo.org%2Frecord%2F1059677%2Ffiles%2F4699.pdf%3Fdownload%3D1&usg=AOvVaw06mxo1Ipw_ND2puUIYgCF9

# Solution: 

- Use K-means algorithm to determine clusters of orders close to each other and then apply a genetic algorithm to each cluster of orders to determine optimal route for each cluster()

	- Using the K-means algorithm we can determine the orders that will be carried by each truck. The number of clusters will be the minimum number of trucks needed to carry all orders.
	- After the order clusters have been determined, we can apply a genetic algorithm to determine optimal route for each order cluster as follows:
		- Initial population will be a random number of route possibilities.
		- Fitness function will determine the fitness of each route based on both distance and time. The scoring system will be determined by discussion as a team.
		- Selection will obviously be done based on routes with fitest scores.
		- Crossover will be done on the selected routes.
		- Mutation will be done based on a set probability. 

- Variables 
    - location
    - load volume ?
    - load capacity
    - time ?
    - Truck type