
Multiplayer network card game. 

Program consists with server and client programs.

** Server Description: 

The server code will manage all the clients, ensure that all clients are following the protocol, no client
is cheating, keep track of the score, and deal the cards. Pick your own port number for the server.
You will have four running instances of the client code. The client will connect to the server and
interact with the user and the server. Assume the first client to connect is called Player1, the second
client is called Player2, the third client to connect is Player3, and the last client is Player4. 

** Client Description:

At client startup, ask the user for server name or IP address. Then ask the user for his/her name. Send
the name to the server. Wait for reply from server. Connect to server to obtain 13 cards. The server will create a deck of 52 cards and “deal” 13 cards to each client. Each card is unique; two clients cannot have the same card and one client cannot have two of the same card. Once all cards are dealt, the client will ask the user for a bid. The bid is sent to the server and shown to all the clients. Once all the clients have bid, then the total bid for each team is shown. Then play starts. You must show each client's played card and who won the trick. Make sure to tally the number of tricks won correctly. When a client plays a card, that card is sent to the server. The server then notifies the next client that it is its turn to play. The server needs to keep track of who won the trick to prevent possible cheating. At the end of each round, update the score and show the total number of tricks and bids for each team. Start the next round (if any).

** Game description:

You will write a program for four players to play a card game over the Internet. It is a very simple card
game with a deck of 52 cards. The four players will form two teams of two
players. The goal is to reach 250 points first. At the beginning of every round of the game, each player
is dealt 13 cards. Each player will then bid. The bid indicates how many tricks the player thinks he/she
can take. If you are used to Bridge or Spades or other trick-taking game, this is a simple version
without trumps. Each player must follow suit. For example, if Player1 plays 2 Spade, Player2 has to
play a Spade. If Player2 has no Spade, Player2 can play any card but he/she won't get the trick. 

The highest card takes the trick. For example, if 2 of Spade, 5 of Spade, J of Spade, and K of Heart are
played, since Spade started, the player who played the J of Spade won the trick. Note that this is a team
game, so if Player2 bid 3 tricks and Player4 bid 2 tricks, both Player2 and Player4 have to win 5 tricks
in total (it doesn't matter who wins which trick). The player who won the trick starts next.
At the end of the round, that is, all 13 cards are played, the score for each team is updated. If a team bid
X tricks, and it won Y >= X tricks, its score is (X*10 + Y-X). If the team won fewer than X tricks, then
its score is (-X*10). Note the negative score. For example, if Player2 and Player4 bid 5 tricks total and
they won 7 tricks, then their score for this round is 52 points. However, if they only won 3 tricks, then
their score for this round is -50 points. Their total score (from previous rounds) is added up. Whenever
a team reaches at least 250 points, the game is over. If both teams' scores exceed 250 points, the team
with the higher score wins the game. At the end of each round, the next round starts: 13 cards are dealt,
the players bid, and play their cards.

Play is sequential. For the first round, Player1 starts the bid and plays the first card. For the next round,
Player2 starts the bid and plays the first card. And so on. Within a round, the player who wins the trick,
plays the next card.


### run_server

First you need to run the [script](https://github.com/mcdperera/Cloud-and-Distributed-System/blob/master/Homework1/src/Server.java) (has to be done with source):

```sh
$ java -cp Homework1.jar Server
```

### run_client

First you need to run the [script](https://github.com/mcdperera/Cloud-and-Distributed-System/blob/master/Homework1/src/MainForm.form) (has to be done with source):

![Alt text](images/screens/1.ServerRunning.png?raw=true "Running the server program.")

```sh
$ java -cp HomeWork1.jar MainForm
```
![Alt text](images/screens/2.ClientRunning.png?raw=true "Client running.")

![Alt text](images/screens/2.ClientRunning_Server_message_1.png?raw=true "When client is running server messages.")

![Alt text](images/screens/3.Client_connect_with_duplicate_username_and_connect_again.png?raw=true "When user enters duplicate usernames error messages.")

![Alt text](images/screens/4.After_4_players_connected.png?raw=true "Screen after the 4 players connected.")

![Alt text](images/screens/5.Playerbidding.png?raw=true "When the player put his bid.")

![Alt text](images/screens/6.After_4_players_bid.png?raw=true "Screen after 4 players put their bids.")

![Alt text](images/screens/7.After_4_players_draw_cards.png?raw=true "After 4 players drawn their cards.")

![Alt text](images/screens/9.Player1_won.png?raw=true "Player 1 won.")

![Alt text](images/screens/8.Player1_won_updated_scores.png?raw=true "Player 1 won the trick and updates the scores.")

![Alt text](images/screens/10.Finish _First_round.png?raw=true "Screen after complete the first round.")

![Alt text](images/screens/11.When_player_drawn_a_wrong_card.png?raw=true "Screen with message when player drawn a wrong card.)



