#!/bin/bash
mpicc -Wall fractal.c -lm
mpirun -np 8 --oversubscribe a.out > result.txt
python view.py result.txt
