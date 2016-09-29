//to compile  gcc mazeHardware.c -lusb-1.0 -lpthread -I/usr/include/libusb-1.0
//usage sudo ./a.out 192.168.137.1 6212 0x045e 0x00dd  0x81 
//libusb sudo lsusb -d vendor:product -v
//0x045e 0x00dd  0x81 
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h> 
#include <sys/mman.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <semaphore.h>
#include <libusb.h>

#define SEM_NAME "/semaphore"

//This function notifies the user of any errors
void error(const char *msg)
{
    perror(msg);
    exit(0);
}

int main(int argc, char *argv[])
{
	//server variables
	int i, j, k;
	int sockfd, portno, n;
	struct sockaddr_in serv_addr;
	struct hostent *server;

	char buffer[512];
	//ensure that user compiles the program with hostname and port as arguments
	if (argc < 6) {
		fprintf(stderr,"usage sudo %s hostname port vendorID productID endpoint\n", argv[0]);
		exit(0);
	}

	//save the second argument applied to the program as port number
	portno = atoi(argv[2]);

	//create a socket
	sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (sockfd < 0) 
		error("ERROR opening socket");
	server = gethostbyname(argv[1]);
	if (server == NULL) {
		fprintf(stderr,"ERROR, no such host\n");
		exit(0);
	}

	//connect to server
	bzero((char *) &serv_addr, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	bcopy((char *)server->h_addr, 
	 (char *)&serv_addr.sin_addr.s_addr,
	 server->h_length);
	serv_addr.sin_port = htons(portno);
	if (connect(sockfd,(struct sockaddr *) &serv_addr,sizeof(serv_addr)) < 0) 
		error("ERROR connecting");


	//open a semaphore
	sem_t *shm;
	sem_unlink(SEM_NAME);
	if ((shm = sem_open(SEM_NAME, O_CREAT,0666,1)) == SEM_FAILED)
	{
		error("ERROR semaphore initialization");
	}

	//Open a libusb session
	int usb_session = libusb_init(NULL);
	printf ("libusb init returned %d\n", usb_session);

	//Get KeyBoard variables
	uint16_t vendor_id = strtol(argv[3],NULL,0);
	uint16_t product_id = strtol(argv[4],NULL,0);
	unsigned char endpoint = strtol(argv[5],NULL,0);

	//Retrieve device
	libusb_device_handle* handle = libusb_open_device_with_vid_pid(NULL, vendor_id, product_id);

	//Detach Kernel Driver
	int driver_active = libusb_kernel_driver_active(handle,  0);
	printf("is driver active = %d\n",driver_active ); //1 if active, 0 otherwise
	//if driver is active detach kernel driver
	int driver_detached = libusb_detach_kernel_driver (handle, 0);
	printf("is driver detached = %d\n",driver_detached); //0 if success

	//Now that driver is detached, claim an interface
	int claim_success = libusb_claim_interface (handle, 0);
	printf("is claiming of interface successful = %d\n", claim_success); //0 if success

	//fork processes
	pid_t forkvalue;
	forkvalue = fork();

	//this is the reading process
	if (forkvalue == 0)
	{
		//make sure that both processes share the same semaphore
		if ((shm = sem_open(SEM_NAME, O_CREAT,0666,1)) == SEM_FAILED)
		{
			error("ERROR semaphore initialization");
		}


		//Reading from server in an infinite loop
		while (1)
		{
			FILE * instream = fdopen(sockfd,"r");

			while (1)
			{
				char* str = fgets(buffer, 512, instream);
				//take semaphore if asleep
				for(i=0;i<strlen(str)-1;i++)
					if(str[i]=='s' && str[i+1]=='l'&&str[i+2]=='e' && str[i+3]=='e'&&str[i+4]=='p' ) 
					{
						printf("Semaphore taken by reading process, keyboard input disabled\n");
						sem_wait(shm);
					}
				//release semaphore if awake
				for(i=0;i<strlen(str)-1;i++)
					if(str[i]=='a' && str[i+1]=='w'&&str[i+2]=='a' && str[i+3]=='k'&&str[i+4]=='e' ) 
					{
						printf("Semaphore released by reading process, keyboard input enabled\n");
						sem_post(shm);
					}

				//keep reading from buffer until eof
				if (str!=NULL)
					printf("%s",str);
				else
					break;
			}
		}
	}

	//this is the writing process
	else
	{
		//make sure that both processes share the same semaphore
		if ((shm = sem_open(SEM_NAME, O_CREAT,0666,1)) == SEM_FAILED)
		{
			error("ERROR semaphore initialization");
		}

		//this is how I write to server will change with libusb ltr
		int interrupt_success;
		int bytes = 4;
		char data[4];
		int j;
		while (1)
		{
			//read input from keyboard only this process is able to obtain the semaphore token
			sem_wait(shm);
			sem_post(shm);
			interrupt_success = libusb_interrupt_transfer(handle, endpoint, data, 4, &bytes, 0);
			char* str = "";
			for (j = 0 ; j < bytes; j++)
			{
				//printf("%x",data[j]);

				if (j == 2)
				{
					
					if (data[j] == 0x1a)
						str = "w\n";
					else if (data[j] == 0x16)
						str = "s\n";
					else if (data[j] == 0x4)
						str = "a\n";
					else if (data[j] == 0x7)
						str = "d\n";
					else if (data[j] == 0x28)
						str = "enter";
				}

			}
			//printf("\n");
			if (str != "")
			{
				printf("Your vote = %s",str);
				int n = write(sockfd,str,strlen(str));
				if (n < 0) 
				 	error("ERROR writing to socket");
			}

		}		
	}


	//Release interface
	libusb_release_interface(handle, claim_success);
	libusb_close (handle);
	libusb_exit(NULL);
	printf ("exitted normally\n");

	//close socket
	close(sockfd);

	return 0;
}

