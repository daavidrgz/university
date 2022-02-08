Ejercicio 1.

SELECT ename FROM emp WHERE coalesce(comm, 0)>sal/2;

Ejercicio 2.

SELECT ename FROM emp WHERE comm is null or coalesce(comm,0)<=sal/4;

Ejercicio: 3.

SELECT ename FROM emp WHERE mgr is null;

Ejercicio 4.

SELECT ename, sal/comm FROM emp WHERE comm is not null and comm <> 0 ORDER BY ename;

Ejercicio 5.

SELECT ename FROM emp WHERE ((sal > 1000 and sal < 2000) or deptno=30) and mgr > empno;

Ejercicio 6.

SELECT ename, sal, comm, sal+coalesce(comm, 0) salario_total FROM emp WHERE sal+coalesce(comm, 0)>2300;

Ejercicio 7.

SELECT DISTINCT job, deptno FROM emp GROUP BY job, deptno;

Ejercicio 8.

SELECT MAX(sal), SUM(coalesce(comm,0)), COUNT (*) FROM emp;

Ejercicio 9.

SELECT MAX(ename) FROM emp;

Ejerrcicio 10.

SELECT MAX(sal), MIN(sal), MAX(sal) - MIN(SAL) FROM emp;

Ejercicio 11.

SELECT COUNT(DISTINCT job), COUNT(*), COUNT(DISTINCT sal), SUM(sal) FROM emp WHERE deptno=30;

Ejercicio 12.

SELECT COUNT(*) FROM emp WHERE deptno=20;

Ejercicio 13.

SELECT COUNT(comm) FROM emp;

Ejercicio 14.

SELECT job, COUNT(*) FROM emp GROUP BY job;

Ejercicio 15.

SELECT deptno, SUM(sal) FROM emp GROUP BY deptno;

Ejercicio 16.

SELECT deptno, COUNT(prono) FROM pro GROUP BY deptno;

Ejercicio 17.

SELECT prono, SUM(hours), COUNT(*) FROM emppro GROUP BY prono HAVING COUNT(*)>=3;

Ejercicio 18.

SELECT COUNT(*) FROM pro GROUP BY deptno, loc;

Ejercicio 19.

SELECT deptno, COUNT(*) FROM emp WHERE sal>1500 GROUP BY deptno;

Ejercicio 20.

SELECT deptno, COUNT(*) FROM emp GROUP BY deptno HAVING MIN(sal)>100;

Ejercicio 21.

SELECT deptno, job FROM emp GROUP BY deptno, job HAVING COUNT(*)>=2;

Ejercicio 22.

SELECT ename FROM emp WHERE sal>(SELECT sal FROM emp WHERE empno=7934) ORDER BY sal;

Ejercicio 23.

SELECT ename FROM emp e JOIN dept d ON e.deptno=d.deptno WHERE loc IN ('NEW YORK', 'DALLAS');

Ejercicio 24.

SELECT ename FROM emp WHERE sal>=(SELECT AVG(sal) FROM emp);

Ejercicio 25.

SELECT ename FROM emp WHERE deptno=10 and job IN (SELECT DISTINCT job FROM emp e JOIN dept d ON e.deptno=d.deptno WHERE dname='SALES');

Ejercicio 26.

SELECT DISTINCT ename FROM emp WHERE empno IN (SELECT DISTINCT mgr FROM emp) ORDER BY ename DESC;

Ejercicio 27.

SELECT ename FROM emp WHERE mgr is null;

Ejercicio 28.

SELECT dname FROM emp e LEFT JOIN dept d ON e.deptno=d.deptno WHERE empno is null;

Ejercicio 29.

SELECT empno, prono FROM emppro a WHERE hours>=(SELECT MAX(hours) FROM emmpro WHERE prono=a.prono);

Ejercicio. 30

SELECT ename FROM emp e WHERE sal>(SELECT MAX(sal) FROM emp WHERE deptno=e.deptno and ename<>a.ename);

Ejercicio 32.

SELECT loc, ename FROM emp e JOIN dept d ON e.deptno=d.deptno WHERE loc LIKE '_______%' ORDER BY loc DESC, ename ASC;

Ejercicio 33.

SELECT ename, prono FROM emp e LEFT JOIN emppro p ON e.empno=p.empno;

Ejercicio 34.

SELECT pname, d.deptno FROM dept d LEFT JOIN pro p ON d.deptno=p.deptno;

Ejercicio 35.

SELECT e.ename, e2.ename FROM emp e LEFT JOIN emp e2 ON e.mgr=e2.empno ORDER BY e.empno;

Ejercicio 37.

SELECT empno, dname FROM emmpro e JOIN pro p ON e.prono=p.prono JOIN dept d ON d.deptno=p.deptno; 

Ejercicio 38.

SELECT empno, ename, sal, prono, hours FROM emp e JOIN emppro p ON e.empno=p.empno ORDER BY empno;

Ejercicio 39.

SELECT dname, AVG(sal) Media de Salario, COUNT(*) FROM emp e RIGHT JOIN dept d ON e.deptno=d.deptno GROUP BY dname; 

Ejercicio 40.

SELECT dname, SUM(sal) FROM emp e JOIN dept d ON e.deptno=d.deptno WHERE sal>(SELECT AVG(sal) FROM emp) GROUP BY dname;

Ejercicio 41.

SELECT e.prono, loc, COUNT(*), MAX(hours), MIN(hours), MAX(hours) - MIN(hours) FROM pro p JOIN emppro e ON p.prono=e.prono GROUP BY e.prono, loc ORDER BY e.prono;

Ejercicio 42.

