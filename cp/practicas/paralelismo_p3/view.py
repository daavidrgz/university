# Script para visualizar el fractal generado por mandel.c
# Uso (secuencial):
# $ gcc -o mandel mandel.c
# $ ./mandel > mandel.txt
# $ python2 view.py mandel.txt
#
# Es necesario tener instalado Python v2.x y los paquetes numpy y matplotlib.
# Las maquinas de docencia no disponen de matplotlib, por lo que este script no funcionara.

import numpy as np
from matplotlib import colors
import matplotlib.pyplot as plt
import math
import sys

# Extract points from specified file
M = np.loadtxt( sys.argv[1] )

# Display
light = colors.LightSource(azdeg=315, altdeg=10)
M = light.shade(M, cmap=plt.cm.hot, vert_exag=1.5,
							norm=colors.PowerNorm(0.3), blend_mode='hsv')
plt.imshow(M, interpolation="bicubic")
# plt.imshow(M,cmap=plt.cm.flag)
plt.show()
