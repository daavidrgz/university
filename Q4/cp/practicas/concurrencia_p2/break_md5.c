#include <sys/types.h>
#include <openssl/md5.h>
#include <string.h>
#include <errno.h>
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/time.h>
#include <stdbool.h>

#define PASS_LEN 6
#define GREEN "\033[1;32m"
#define WHITE "\033[0m"
#define WHITE_B "\033[1;29m"
#define CYAN "\033[1;34m"
#define YELLOW "\033[1;35m"

struct args {
    long bound;
    long start;
    struct md5s_passwords* md5s_passwords;
    struct progress* progress;
};

struct md5s_passwords {
    char** md5s;
    char** passwords;
    int numPasswords;
    bool*  foundedPasswords;
};

struct progress {
    long progressCounter;
    pthread_mutex_t progressMutex;
};

struct threadInfo {
    pthread_t id;
    struct args* args;
};

bool allFounded = false;

long ipow(long base, int exp) {
    long res = 1;
    for ( ;; ) {
        if ( exp & 1 )
            res *= base;
        exp >>= 1;
        if ( !exp )
            break;
        base *= base;
    }

    return res;
}

long pass_to_long(char *str) {
    long res = 0;

    for ( int i=0; i < PASS_LEN; i++ )
        res = res * 26 + str[i]-'a';

    return res;
};

void long_to_pass(long n, unsigned char *str) {  // str should have size PASS_SIZE+1
    for ( int i = PASS_LEN-1; i >= 0; i-- ) {
        str[i] = n % 26 + 'a';
        n /= 26;
    }
    str[PASS_LEN] = '\0';
}

void to_hex(unsigned char* res, char* hex_res) {
    for ( int i = 0; i < MD5_DIGEST_LENGTH; i++ ) {
        snprintf(&hex_res[i*2], 3, "%.2hhx", res[i]);
    }
    hex_res[MD5_DIGEST_LENGTH * 2] = '\0';
}

bool isFinished(bool* foundedPasswords, int numPasswords) {
    bool sol = true;
    for ( int i = 0; i < numPasswords; i++ )
        sol = sol && foundedPasswords[i];
        
    return sol;
}

void* break_pass(void* ptr) {
    struct args* args = ptr;
    long i = args -> start;
    unsigned char res[MD5_DIGEST_LENGTH];
    char hex_res[MD5_DIGEST_LENGTH * 2 + 1];
    unsigned char* pass = malloc((PASS_LEN + 1) * sizeof(char));
    
    while ( i < args->bound && !allFounded ) {
        long_to_pass(i, pass);
        MD5(pass, PASS_LEN, res);
        to_hex(res, hex_res);

        pthread_mutex_lock(&args->progress->progressMutex);
        args->progress->progressCounter += 1;
        pthread_mutex_unlock(&args->progress->progressMutex);

        //Comparation between all the md5s
        for ( int j = 0; j < args->md5s_passwords->numPasswords; j++ ) {
            if ( !args->md5s_passwords->foundedPasswords[j] && !strcmp(hex_res, args->md5s_passwords->md5s[j]) ) {
                strcpy(args->md5s_passwords->passwords[j], (char*) pass);

                printf("%s%s%s: %s'%s'%s (%s)                  \n\n", CYAN, "Solution", WHITE, YELLOW, 
                        args->md5s_passwords->passwords[j], WHITE, args->md5s_passwords->md5s[j]);
                
                args->md5s_passwords->foundedPasswords[j] = true;
                if ( isFinished(args->md5s_passwords->foundedPasswords, args->md5s_passwords->numPasswords) )
                    allFounded = true;
            }
        }
        i++;
    }

    free(pass);
    return NULL;
}

void progressBar(struct progress* progress) {
    char progressBar[52];
    long total = ipow(26, PASS_LEN);
    int frequency;
    int animationPos = 0;
    float percentage = 0.f;

    char animation[4] = {'|', '/', '-', '\\'};
    
    for ( int i = 0; i < 51; i++ ) // Bar initialization
        progressBar[i] = '.';
    progressBar[0] = '|';
    progressBar[51] = '\0';
    
    printf("\n");
    while ( !allFounded ) {
        percentage = (float) progress->progressCounter * 100 / (float) total;
        printf("Progress: [%s%s%s] %s%.2f%%%s\r", WHITE_B, progressBar, WHITE, GREEN, percentage, WHITE);
        fflush(stdout);
        if ( (int)percentage % 2 == 0 )
            progressBar[(int)percentage / 2] = '#';

        if ( frequency == 10 ) { // Animation frquency
            progressBar[(int)percentage / 2 + 1] = animation[animationPos];
            frequency = 0;
            if ( animationPos == 3 )
                animationPos = 0;
            else
                animationPos++;
        }
        frequency++;

        usleep(10000);
    }

    // When completed, the bar should be filled until 100%
    int percentageInt = (int)percentage;
    if ( percentageInt % 2 != 0 )
        percentageInt += 1; // Even number to reach 100% exactly

    while ( percentageInt <= 100 ) {
        progressBar[percentageInt / 2] = '#';
        printf("Progress: [%s%s%s] %s%d%%%s  \r", WHITE_B, progressBar, WHITE, GREEN, percentageInt, WHITE);
        fflush(stdout);
        percentageInt+=2;
        usleep(10000);
    }
    printf("\n\n");
}

struct progress* initBar() {
    struct progress* progress = malloc(sizeof(struct progress));

    progress -> progressCounter = 0;
    pthread_mutex_init(&progress->progressMutex, NULL);
    return progress;
}

struct md5s_passwords* initPasswords(char** hashes, int numPasswords) {
    struct md5s_passwords* md5s_passwords = malloc(sizeof(struct md5s_passwords));

    md5s_passwords->passwords = malloc(sizeof(char*) * numPasswords); // Allocation of two string arrays
    md5s_passwords->md5s = malloc(sizeof(char*) * numPasswords);
    md5s_passwords->foundedPasswords = malloc(sizeof(bool) * numPasswords);
    md5s_passwords->numPasswords = numPasswords;

    for ( int i = 0; i < numPasswords; i++ ) {
        md5s_passwords->foundedPasswords[i] = false;
        md5s_passwords->passwords[i] = malloc(sizeof(char) * (PASS_LEN + 1));
        md5s_passwords->md5s[i] = malloc(sizeof(char) * (MD5_DIGEST_LENGTH * 2 + 1));
        strcpy(md5s_passwords->md5s[i], hashes[i]);
    }
    return md5s_passwords;
}

struct threadInfo* startThreads(char** hashes, int numPasswords, int numThreads) {
    long bound = ipow(26, PASS_LEN);
    struct threadInfo* threads = malloc(sizeof(struct threadInfo) * numThreads);
    struct progress* progress = initBar();
    struct md5s_passwords* md5s_passwords = initPasswords(hashes, numPasswords);
    
    for ( int i = 0; i < numThreads; i++ ) {
        threads[i].args = malloc(sizeof(struct args));

        threads[i].args -> md5s_passwords = md5s_passwords;	
        threads[i].args -> bound = (i+1) * bound / numThreads;
        threads[i].args -> start = i * bound / numThreads;
        threads[i].args -> progress = progress;

        if ( 0 != pthread_create(&threads[i].id, NULL, break_pass, threads[i].args) ) {
            printf("Could not create thread #%d", i);
            exit(1);
        }
    }

    progressBar(progress);
   
    return threads;
}

void wait(struct threadInfo* threads, int numThreads) {
    // Wait for the threads to finish
    for ( int i = 0; i < numThreads; i++ )
        pthread_join(threads[i].id, NULL);

    for ( int i = 0; i < threads->args->md5s_passwords->numPasswords; i++ ) { //Free each string individualy
        free(threads->args->md5s_passwords->md5s[i]);
        free(threads->args->md5s_passwords->passwords[i]);
    }

    free(threads->args->md5s_passwords->md5s);
    free(threads->args->md5s_passwords->passwords);
    free(threads->args->md5s_passwords->foundedPasswords);
    pthread_mutex_destroy(&threads->args->progress->progressMutex);
    free(threads->args->progress);
    free(threads->args->md5s_passwords);

    for ( int i = 0; i < numThreads; i++ )
        free(threads[i].args);

    free(threads);
}

int main(int argc, char* argv[]) {
    if ( argc < 2 ) {
        printf("Use: %s string\n", argv[0]);
        exit(0);
    }
    struct threadInfo* threads;
    int numThreads = 8;

    threads = startThreads(argv+1, argc-1, numThreads);

    wait(threads, numThreads);

    return 0;
}
