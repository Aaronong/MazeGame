//to compile  gcc mazeHardware.c -lusb-1.0 -lpthread -I/usr/include/libusb-1.0
//usage sudo ./a.out 192.168.137.1 6212 0x045e 0x00dd  0x81 

General Instructions
- port number used is 6212

Android Usage Instructions
- Android Client will be released in next patch

Telnet Usage Instructions
- telnet hostname port

Hardware Client Compilation Instructions
- gcc mazeHardware.c -lusb-1.0 -lpthread -I/usr/include/libusb-1.0


Hardware Client Usage Instructions
- sudo ./a.out hostname port vendorID productID endpoint
	

Game Instructions

Commands
- On Maze: Up, Down, Left, Right
- On Battle: Scissors, Paper, Stone

Input
- Accepted Keyboard Input: w, a, s, d
- A legend will be printed to specify which keyboard input maps to which command

Mapping
- eg.    vote   = up down left right
	 legend = w  s    a    d

- eg.    vote   = scissors paper stone
	 legend = a        s     d

Maze Events

- Drawing of Maze
	maze = 
	#######################
	# #       #           #
	# # ##### # ##### #####
	#   #   # #     # #   #
	# ###h# # ##### # # # #
	# #...# #       #   # #
	# #.### ### ######### #
	# #.#     #   #     # #
	# #.##### ### # ### # #
	# #.  # #   # #   #   #
	# ### # ### ### # ### #
	#   # #   #   # # # # #
	##### ### ### ### # # #
	#   # #     #     # # #
	# ### # ########### # #
	#     #        x      #
	#######################

	walls = "#"
	unvisited tiles = " "
	visited tiles = "."
	hero position = "h"
	boss position = "x"

- Status
	status = awake
	status = sleep

- Lives 
	- specify how many clients are connected to the server
		total lives = 2
	- specify how many connected clients are awake
		current lives = 1

- Voting - Calls players to vote on the available options
	vote   = up down left right
	vote   = scissors paper stone

- Voting results
	- Individual client votes
		-eg. client no. 0 voted for left
		     client no. 1 voted for right
	- Voting outcome
		voted = left

- Events - Informs players of potion events
	event = rez potion
	event = death potion

- States - Informs players of the current game state
	state = maze
	state = battle
	state = boss

Battle Events

- Health
	my health = 2
	enemy health = 2
