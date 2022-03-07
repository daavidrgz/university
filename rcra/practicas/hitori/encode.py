#!/usr/bin/python3
import fileinput

row = 0;
col = 0;
for line in fileinput.input():
	col = 0
	for num in line.strip():
		print(f'cell({row},{col},{num}).')
		col += 1
	print()
	row += 1

print(f'row(0..{row-1}).')
print(f'col(0..{col-1}).')
