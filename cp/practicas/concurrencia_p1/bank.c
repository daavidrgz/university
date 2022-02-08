#include <errno.h>
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/time.h>
#include <stdbool.h>
#include "options.h"

#define MAX_AMOUNT 20
#define GREEN "\033[1;32m"
#define WHITE "\033[0m"
#define CYAN "\033[1;34m"
#define PINK "\033[1;33m"
#define BLUE "\033[0;36m"
#define RED "\033[1;31m"
#define YELLOW "\033[1;35m"

struct bank {
	int num_accounts;        // number of accounts
	int* accounts;           // balance array
	pthread_mutex_t* mutex;  // mutex per account
	pthread_cond_t* cond;    // cond per account
};

struct args {
	pthread_mutex_t mutex;
	int thread_num;       // application defined thread #
	int	delay;			  // delay between operations
	int	iterations;       // number of operations
	int net_total;        // total amount deposited by this thread
	int total_removed;    // total amount removed by this thread
	struct bank* bank;    // pointer to the bank (shared with other threads)
};

struct thread_info {
	pthread_t id;        // id returned by pthread_create()
	struct args* args;   // pointer to the arguments
};

void* deposit(void*);
void* transfer(void*);
void* withdraw(void*);
struct thread_info* start_threads(struct options opt, struct bank* bank);
void print_balances(struct bank*, struct thread_info*, int);
void wait(struct options, struct bank*, struct thread_info*);
void init_accounts(struct bank*, int);

int remaining_threads = true;

// Threads run on this function
void* deposit(void* ptr) {
	struct args* args = ptr;
	int amount, account, balance;

	while ( args->iterations-- ) {
		amount  = rand() % MAX_AMOUNT;
		account = rand() % args->bank->num_accounts;

		// Mutex lock
		pthread_mutex_lock(&args->bank->mutex[account]);

		balance = args->bank->accounts[account];
		if(args->delay) usleep(args->delay); // Force a context switch

		balance += amount;
		if(args->delay) usleep(args->delay);

		args->bank->accounts[account] = balance;
		if(args->delay) usleep(args->delay);

		printf("Thread %3d %s%-11s%s %2d$ on account %d %s(Account %d: %d$)%s\n",
			args->thread_num, GREEN, "depositing", WHITE, amount, account, BLUE,
			account, args->bank->accounts[account], WHITE);

		pthread_cond_broadcast(&args->bank->cond[account]);

		// Mutex unlock
		pthread_mutex_unlock(&args->bank->mutex[account]);

		

		args->net_total += amount;
	}
	return NULL;
}

void* transfer(void* ptr) {
	struct args* args = ptr;
	int fst_account, snd_account, amount;

	while ( args->iterations-- ) {
		fst_account = rand() % args->bank->num_accounts;
		do {
			snd_account = rand() % args->bank->num_accounts;
		} while ( fst_account == snd_account );
		
		// Sorted reservation
		if ( fst_account < snd_account ) {
			pthread_mutex_lock(&args->bank->mutex[fst_account]);
			pthread_mutex_lock(&args->bank->mutex[snd_account]);

		} else {
			pthread_mutex_lock(&args->bank->mutex[snd_account]);
			pthread_mutex_lock(&args->bank->mutex[fst_account]);
		}
		
		if ( args->bank->accounts[fst_account] == 0 )
			amount = 0;
		else
			amount = rand() % (args->bank->accounts[fst_account] + 1);

		args->bank->accounts[fst_account] -= amount;
		if(args->delay) usleep(args->delay);

		args->bank->accounts[snd_account] += amount;
		if(args->delay) usleep(args->delay);

		printf("Thread %3d %stransfering%s %2d$ from account %d to account %d %s(Account %d: %d$, Account %d: %d$)%s\n",
			args->thread_num, CYAN, WHITE, amount, fst_account, snd_account, BLUE, fst_account,
			args->bank->accounts[fst_account], snd_account, args->bank->accounts[snd_account], WHITE);

		pthread_cond_broadcast(&args->bank->cond[snd_account]);

		pthread_mutex_unlock(&args->bank->mutex[fst_account]);
		pthread_mutex_unlock(&args->bank->mutex[snd_account]);
		
	}
	return NULL;
}

void* withdraw(void* ptr) {
	struct args* args = ptr;
	int amount, account;

	account = rand() % args->bank->num_accounts;
	amount = rand() % MAX_AMOUNT;
	
	pthread_mutex_lock(&args->bank->mutex[account]);
	while ( amount > args->bank->accounts[account] ) {
		if ( !remaining_threads ) {
			printf("%s*Thread %3d removing %2d$ on account %d timed out*%s\n", RED, args->thread_num, amount, account, WHITE);
			pthread_mutex_unlock(&args->bank->mutex[account]);
			return NULL;
		}
		pthread_cond_wait(&args->bank->cond[account], &args->bank->mutex[account]);
	}

	args->bank->accounts[account] -= amount;
		if(args->delay) usleep(args->delay);

	printf("Thread %3d %s%-11s%s %2d$ on account %d %s(Account %d: %d$)%s\n",
			args->thread_num, PINK, "removing", WHITE, amount, account, BLUE, account, args->bank->accounts[account], WHITE);

	pthread_mutex_unlock(&args->bank->mutex[account]);

	args->total_removed += amount;
	return NULL;
}

// start opt.num_threads threads running on deposit.
struct thread_info* start_threads(struct options opt, struct bank* bank) {
	int i = 0;
	struct thread_info* threads;
	void* (*thread_fun)(void*) = deposit;

	printf("Creating %d threads\n", opt.num_threads * 3);
	threads = malloc(sizeof(struct thread_info) * opt.num_threads * 3);

	if ( threads == NULL ) {
		printf("Not enough memory\n");
		exit(1);
	}

	// Create num_thread threads running swap()
	while ( i < opt.num_threads * 3 ) {
		threads[i].args = malloc(sizeof(struct args));

		threads[i].args -> thread_num = i;
		threads[i].args -> net_total = 0;
		threads[i].args -> total_removed = 0;
		threads[i].args -> bank = bank;
		threads[i].args -> delay = opt.delay;
		threads[i].args -> iterations = opt.iterations;

		if ( 0 != pthread_create(&threads[i].id, NULL, thread_fun, threads[i].args) ) {
			printf("Could not create thread #%d", i);
			exit(1);
		}
		
		i++;

		if ( i == opt.num_threads )
			thread_fun = transfer;
		if ( i == opt.num_threads * 2 )
			thread_fun = withdraw;
	}
	return threads;
}

// Print the final balances of accounts and threads
void print_balances(struct bank* bank, struct thread_info* thrs, int num_threads) {
	int total_deposits=0, bank_total=0, total_removed=0;
	printf("\n\nNet deposits by thread\n");

	for ( int i=0; i < num_threads; i++ ) {
		printf("%d: %d\n", i, thrs[i].args->net_total);
		total_deposits += thrs[i].args->net_total;
	}
	printf("Total: %d\n", total_deposits);
	
	printf("\nNet withdraws by thread\n");
	for ( int i=num_threads*2; i < num_threads*3; i++ ) {
		printf("%d: %d\n", i, thrs[i].args->total_removed);
		total_removed += thrs[i].args->total_removed;
	}
	printf("Total: %d\n", total_removed);

    printf("\nAccount balance\n");
	for ( int i=0; i < bank->num_accounts; i++ ) {
		printf("%d: %d\n", i, bank->accounts[i]);
		bank_total += bank->accounts[i];
	}
	printf("Total: %d (%d - %d = %s%d%s)\n\n", bank_total,
			total_deposits, total_removed, GREEN, total_deposits - total_removed, WHITE);
}

// wait for all threads to finish, print totals, and free memory
void wait(struct options opt, struct bank* bank, struct thread_info* threads) {
	// Wait for the threads to finish
	for ( int i = 0; i < opt.num_threads * 3; i++ ) {
		if ( i == opt.num_threads * 2 ) {
			remaining_threads = false;
			for ( int j = 0; j < opt.num_accounts; j++ )
				pthread_cond_broadcast(&bank->cond[j]);
		}
		pthread_join(threads[i].id, NULL);
	}
		
	print_balances(bank, threads, opt.num_threads);

	for ( int i = 0; i < opt.num_threads * 3; i++ )
		free(threads[i].args);

	free(threads);
	free(bank->accounts);
	free(bank->mutex);
	free(bank->cond);
}

// allocate memory, and set all accounts to 0
void init_accounts(struct bank* bank, int num_accounts) {
	bank->num_accounts = num_accounts;
	bank->accounts = malloc(bank->num_accounts * sizeof(int));
	bank->mutex = malloc(bank->num_accounts * sizeof(pthread_mutex_t));
	bank->cond = malloc(bank->num_accounts * sizeof(pthread_cond_t));

	for ( int i=0; i < bank->num_accounts; i++ ) {
		bank->accounts[i] = 0;
		pthread_mutex_init(&bank->mutex[i], NULL);
		pthread_cond_init(&bank->cond[i], NULL);
	}	
}

int main(int argc, char** argv) {
	struct options opt;
	struct bank bank;
	struct thread_info* thrs;

	srand(time(NULL));

	// Default values for the options
	opt.num_threads = 5;
	opt.num_accounts = 10;
	opt.iterations = 100;
	opt.delay = 10;

	read_options(argc, argv, &opt);

	init_accounts(&bank, opt.num_accounts);

	thrs = start_threads(opt, &bank);
    wait(opt, &bank, thrs);

	return 0;
}
