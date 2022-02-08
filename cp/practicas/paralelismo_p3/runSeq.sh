#!/bin/bash
gcc -Wall fractalSeq.c -lm -o aSeq.out
./aSeq.out > resultSeq.txt
python view.py resultSeq.txt
 
