Consulta 1.

	SELECT 'Nombre: ' || ename, sal*0.2
  	FROM emp
  	WHERE deptno=10;

  (primero se ejecuta el FROM, despues el WHERE y finalmente el SELECT)

Consulta 2.

	SELECT ename AS Nombre, sal*0.2 FROM emp; 

	(cambia el nombre de ename a Nombre)

Consulta 3.

	SELECT job, deptno FROM emp ORDER BY deptno, sal; 

	(Primero ordena por deptno y depues, dentro de cada departamento, por salario)

Consulta 4.

	SELECT DISTINCT job, deptno FROM emp ORDER BY deptno, job; 

	(Ignora las filas repetidas)

Consulta 5.

	SELECT job, deptno FROM emp ORDER BY deptno (ASC), job (DESC); 

	(Orden ascendente o descendente)

Consulta 6.

	WHERE comm>1000 

	(Las persdonas com comisiones mayor que 1000 continúan pero en los que 
	hay un nulo el resultado es desconocido)

Consulta 7.

	(null=null ==> DESCONOCIDO); (null + 150 ==> DESCONOCIDO); (comm = null ==> DESCONOCIDO); 
	(comm is (not) Null ==> devuelve las filas que tienen comm null)

Consulta 8.

	WHERE deptno IN (10, 30, 40) 

	(Comprueba si deptno está dentro de ese conjunto, es decir, 
	iguala deptno a todos los valores)

Consulta 9.

	SELECT * FROM emp WHERE ename='ADAMS' 

	(Comprueba toda la info del empleado Adams)

Consulta 10.

	SELECT * FROM emp WHERE ename LIKE 'A_B' (Comprueba toda la info de los empleados que empiecen por A y acaben en B con único caracter en el medio)

	SELECT * FROM emp WHERE ename LIKE 'A%' (Comprueba toda la info de los empleados que empiezan por A)

	SELECT * FROM emp WHERE ename LIKE '____%' (Comprueba toda la info de los empleados que tengan como mínimo 4 letras)

	SELECT * FROM emp WHERE ename NOT LIKE '____%' (Comprueba toda la info de los empleados que tiene como máximo 3 letras en el nombre)

Consulta 11.

SELECT * FROM emp WHERE comm+sal>2500 OR sal>2500


 
 			EJERCICIOS


2) SELECT DISTINCT FROM emp WHERE MGR is not null;

3) SELECT DISTINCT FROM pro WHERE DEPTNO = 30;

4) SELECT ename FROM emp WHERE MGR is null;

5) SELECT ename FROM emp WHERE MGR is not null AND sal+comm>2500;

6) 

Consuta 12. (FUNCIONES)

SELECT empno, ename, sqrt(sal) (Devuelve una tabla con la raíz cuadrada del salario)

-> Otras funciones: 

ABS (Valor absoluto); 

POWER (Potencia); 

SUBSTR (Ej: SUBSTR('Materia',2,4) ==> 'ater'; "A partir del segudno caracter muestra 4")

LENGHT (devuelve la longitud de la cadena); 

COALESCE (Ej: COALESCE(sal + comm, sal) "Evalua primero sal + comm y si es nulo, evalúa 
la siguiente; si esta última no es nula 4, devuelve ese valor") (Ej: sal + COALESCE(comm, 0))

.
.
.

Consulta 13. (FUNCIONES COLECTIVAS)

SELECT SUM(sal) FROM emp; (Solo devuelve una fila con la suma de todos los salarios)

Otras funciones colectivas:

AVG (Media)

MAX, MIN (Máximo y Mínimo)

VAR (Varianza)

COUNT (Cuenta cuantas filas hay)

Ej:

SELECT SUM(comm), COUNT(mgr) FROM emp; ("SUM" solo suma valores no nulos y "COUNT" cuenta solo las filas con valores no nulos)

SELECT SUM(DISTINCT deptno) FROM emp; (Suma solo las filas distintas)

Consulta 15. (GROUP BY (Si aparece algún GROUP BY, en el SELECT solo pueden aparecer los datos de condición del GROUP BY y FUNCIONES COLECTIVAS))

SELECT COUNT(*), SUM(SAL) FROM emp GROUP BY deptno (Agrupa, en este caso, los difernetes empleados según su departamento, 
devolviéndote un conteo y una suma de cada grupo (ya no te devuelve solo una fila utulizando una función colectiva))

SELECT job, deptno, COUNT(*), SUM(sal) FROM emp GROUP BY job, deptno (Se crean grupos por pares de valores, es decir, los empleados que tengan un determinado trbajao Y estén en un determinado grupo)

SELECT comm, COUNT(*) FROM emp GROUP BY comm (En este caso existen nulos por lo que Oracle los agrupa todos juntos)



EJERCICIOS

1) SELECT COUNT(*) AS NUM, COUNT(comm) AS CON_COMISIÓN, COUNT(*)-COUNT(comm) AS SIN_COMISIÓN, SUM(COALESCE(sal+comm,sal))/COUNT(*) AS SALARIO_TOTAL FROM emp GROUP BY deptno;

2) SELECT DISTINCT deptno FROM emp WHERE comm is not null;

3) SELECT AVG(coalesce(comm,0)) FROM emp GROUP BY deptno;

4) SELECT COUNT(DISTINCT job) FROM emp GROUP BY deptno;

5) SELECT deptno, job, COUNT(*) FROM emp GROUP BY job, deptno;

6) SELECT deptno, COUNT(*) AS NUM FROM emp WHERE(coalesce(sal+comm,sal)>2500) GROUP BY deptno;  

Consulta 16. (HAVING)

SELECT job, COUNT(*), SUM(sal) FROM emp GROUP BY job HAVING MIN (sal)>1500; (Condición (WHERE) que se aplica sobre grupos; Este grupo cumple esta condición?)

EJERCICIOS

1) SELECT COUNT(*) FROM emp WHERE COALESCE(sal+comm,sal)>2500 GROUP BY deptno;

2) SELECT COUNT(*) FROM emp GROUP BY deptno HAVING AVG(COALESCE(sal+comm,sal));

3) SELECT deptno FROM emp GROUP BY deptno HAVING 

Consulta 14. (JOIN) "Relacionar las tablas"

SELECT * FROM emp JOIN dept ON emp.deptno=dept.deptno; ("Básicamente devuelve las mismas filas pero com más información")

SELECT * FROM emp T1 JOIN dept T2 ON T1.deptno=T2.deptno; ("En este caso renombramos las tablas; cuando hacemos un JOIN de la misma tabla es obligatorio utilizar el alias")

SELECT s.ename subordinado, j.ename jefe FROM emp s JOIN emp j ON s.mgr=j.empno;

Consulta 15. (JOIN EXTERIOR)

SELECT * FROM emp [LEFT | RIGHT | FULL (LEFT y RIGHT al mismo tiempo)] JOIN deptno ON ...

Ej:

SELECT * FROM emp RIGHT JOIN dept ON emp.deptno=dept.deptno;  ("Obliga a utilizar todas las filas de la tabla de la derecha")

Consulta 16. (Encadenaminto de dos o más tablas)

SELECT * FROM emp t1 JOIN emp t2 ON cond1 JOIN emp t3 ON cond2;


EJERCICIOS

1) SELECT p.pname, d.dname FROM pro p JOIN dept d ON p.deptno=d.deptno;

2) SELECT e.ename, p.prono FROM emp e JOIN emppro p ON e.empno=p.empno;

3) SELECT e.ename, p.prono FROM emp e LEFT JOIN emppro p ON e.empno=p.empno;

4) SELECT e.ename, j.ename FROM emp e LEFT JOIN emp j ON e.mgr=j.empno;

5) SELECT e.ename, j.ename, j.deptno FROM emp e JOIN emp j ON e.mgr=j.empno;

6) SELECT e.ename FROM emp e JOIN emp j ON e.mgr=j.empno WHERE e.sal>j.sal;

Consulta 17. (SUBCONSULTAS)

SELECT * FROM emp WHERE deptno=(SELECT deptno FROM dept WHERE dname='RESEARCH')

Consulta 18. (SUBCONSULTAS CORRELACIONADAS)

SELECT * FROM emppro a WHERE hours = (SELEECT ())

Consulta 19. (PREDICADO EXISTS)

SELECT dname FROM dept d WHERE EXISTS (SELECT * FROM emo WHERE deptno=d.deptno)

EJERCICIOS

1) SELECT ename FROM emp WHERE sal > (SELECT AVG(sal) FROM emp);

2) SELECT COUNT(*), dname FROM emp JOIN dept ON emp.deptno=dept.deptno WHERE sal > (SELECT AVG(sal) FROM emp) GROUP BY emp.deptno, dname;

6) SELECT SUM(sal), dname FROM emp e JOIN dept d ON e.deptno=d.deptno GROUP BY e.deptno, dname HAVING SUM(sal) >= ALL (SELECT SUM(sal) FROM emp GROUP BY deptno);