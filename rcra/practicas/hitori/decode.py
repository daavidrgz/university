#!/usr/bin/python3

answer = "cell(0,0,4) cell(0,1,2) cell(0,2,1) cell(0,3,4) cell(0,4,8) cell(0,5,4) cell(0,6,5) cell(0,7,4) cell(1,0,8) cell(1,1,2) cell(1,2,7) cell(1,3,1) cell(1,4,3) cell(1,5,4) cell(1,6,2) cell(1,7,5) cell(2,0,8) cell(2,1,6) cell(2,2,8) cell(2,3,5) cell(2,4,3) cell(2,5,2) cell(2,6,7) cell(2,7,5) cell(3,0,6) cell(3,1,2) cell(3,2,5) cell(3,3,7) cell(3,4,2) cell(3,5,3) cell(3,6,3) cell(3,7,8) cell(4,0,5) cell(4,1,1) cell(4,2,1) cell(4,3,3) cell(4,4,2) cell(4,5,7) cell(4,6,1) cell(4,7,2) cell(5,0,6) cell(5,1,3) cell(5,2,4) cell(5,3,4) cell(5,4,1) cell(5,5,5) cell(5,6,8) cell(5,7,6) cell(6,0,7) cell(6,1,3) cell(6,2,3) cell(6,3,4) cell(6,4,3) cell(6,5,1) cell(6,6,6) cell(6,7,1) cell(7,0,3) cell(7,1,5) cell(7,2,2) cell(7,3,8) cell(7,4,7) cell(7,5,6) cell(7,6,4) cell(7,7,1) black(0,3) black(0,5) black(0,7) black(1,1) black(2,0) black(2,4) black(2,7) black(3,1) black(3,5) black(4,2) black(4,4) black(4,6) black(5,0) black(5,3) black(6,1) black(6,4) black(6,7) black(7,2) black(7,5)"

cells = answer.split(' ')

c_row = 0
for cell in sorted(cells):
	if cell.startswith('black'):
		continue;

	cell_fmt = cell[5:-1]
	[row,col,digit] = cell_fmt.split(',')
	if int(row) > c_row:
		c_row = int(row)
		print()

	if f'black({row},{col})' in cells:
		print('*', end='')
	else:
		print(f'{digit}', end='')
print()
