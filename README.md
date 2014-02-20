Gossip algorithm:

Gossip type algorithms can be used both for group communication and for aggregate computation. The goal of this project is to determine the convergence of such algorithms through a simulator based on actors written in Scala. Since actors in Scala are fully asynchronous, the particular type of Gossip implemented is the so called Asynchronous Gossip. Gossip Algorithm for information propagation The Gossip algorithm involves the following:

1. Starting: A participant(actor) it told/sent a roumor(fact) by the main process
2. Step: Each actor selects a random neighboor and tells it the roumor
3. Termination: Each actor keeps track of rumors and how many times it has heard the rumor. It stops transmitting once it has heard the roumor 10 times (10 is arbitrary, you can play with other numbers).


Push-Sum algorithm for sum computation:
- State: Each actor Ai maintains two quantities: s and w. Initially, s = i (that is actor number i has value i, play with other distribution if you so desire) and w = 1
1. (Starting: Ask one of the actors to start from the main process.
2. Receive: Messages sent and received are pairs of the form (s;w). Upon receive, an actor should add received pair to its own corresponding values. Upon receive, each actor selects a random neighboor and sends it a message.
3. Send: When sending a message to another actor, half of s and w is kept by the sending actor and half is placed in the message.
4. Sum estimate: At any given moment of time, the sum estimate is s/w where s and w are the current values of an actor.
5. Termination: If an actors ratio s/w did not change more than 10^10 in 6 consecutive rounds the actor terminates. WARNING: the values s and w independently never converge, only the ratio does.

Topologies:
The actual network topology plays a critical role in the dissemination speed of Gossip protocols. As part of this project you have to experiment with various topologies. The topology determines who is considered a neighboor in the above algorithms.

1. Full Network Every actor is a neighboor of all other actors. That is, every actor can talk directly to any other actor.
2. 2D Grid: Actors form a 2D grid. The actors can only talk to the grid neigboors.
3. Line: Actors are arranged in a line. Each actor has only 2 neighboors (one left and one right, unless you are the rst or last actor).
4. Imperfect 2D Grid: Grid arrangement but one random other neighboor is selected from the list of all actors (4+1 neighboors).

what is working:
First, we create the List of actors , according to the topology , we initiate the neighbors of each actor and send the message that contains the neighbors information of neighbors and the List that contains all the nodes and actor Listener which determines the termination of the whole system. In terms of gossip algorithm , if an actor has heard from other actors less than 10 times, it send a message to listener so that the actor will be added to another List that keep track of actors that have been informed of the gossip , when most majority of actors have heard the gossip , the whole system shutdown. On the other hand , push-sum algorithm works in anther way. When an actor receives from other actor , it adds both s and w to itself and then send half of its s and w to other actors , when an actor an actors ratio did not change more than 10?10 in 3 consecutive rounds the actor send a message to listener , and the system shutdown. Also , we have tested push-sum algorithm and it really works to calculate the sum of 0 to N-1.





