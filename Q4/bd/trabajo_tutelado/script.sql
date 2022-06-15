/*
DROP TABLE TIENE_ASOCIADA;
DROP TABLE ZONA;
DROP TABLE FALTAS;
DROP TABLE PUNTUA;
DROP TABLE VALORA;
DROP TABLE JUEZ;
DROP TABLE ASALTO;
DROP TABLE COMBATE;
DROP TABLE RECINTO;
DROP TABLE INSCRIBE;
DROP TABLE CAMPEONATO;
DROP TABLE HIST_CLUB;
DROP TABLE CLUB;
DROP TABLE BOXEADOR;

PURGE RECYCLEBIN;
*/

CREATE TABLE boxeador (
	dni 	     VARCHAR(9),
	nombre	   VARCHAR(15) NOT NULL,
	apellido1  VARCHAR(15),
	apellido2  VARCHAR(15),
	cat 	     VARCHAR(15) NOT NULL,
	peso	     NUMERIC(5,2) NOT NULL,
	PRIMARY KEY (dni)
);

CREATE TABLE club (
	id_club   NUMERIC(5),
	nombre    VARCHAR(40) NOT NULL,
	PRIMARY KEY (id_club)
);

CREATE TABLE hist_club (
	box     VARCHAR(9) REFERENCES boxeador,
	tvi     DATE,
	tvf     DATE DEFAULT NULL,
	id_club    NUMERIC(5) REFERENCES club NOT NULL,
	PRIMARY KEY (box, tvi)
);

CREATE TABLE campeonato (
	id_camp    NUMERIC(6),
	fecha_ini  DATE,
	nombre     VARCHAR(40),
	PRIMARY KEY (id_camp)
);

CREATE TABLE inscribe (
	id_club   NUMERIC(5) REFERENCES club,
	id_camp	  NUMERIC(6) REFERENCES campeonato,
	PRIMARY KEY (id_club, id_camp)
);

CREATE TABLE recinto (
	nombre     VARCHAR(40),
	direccion  VARCHAR(40),
	PRIMARY KEY (nombre)
);

CREATE TABLE combate (
	id_comb   NUMERIC(10),
	fase      NUMERIC(3) NOT NULL,
	id_camp   NUMERIC(6) REFERENCES campeonato NOT NULL,
	box1      VARCHAR(9) REFERENCES boxeador NOT NULL,
	box2      VARCHAR(9) REFERENCES boxeador NOT NULL,
	ganador   VARCHAR(9) REFERENCES boxeador,
	loc       VARCHAR(40) REFERENCES recinto NOT NULL,
	PRIMARY KEY (id_comb)
);

CREATE TABLE asalto (
	id_comb   NUMERIC(10) REFERENCES combate,
	numero    NUMERIC(2),
	duracion  NUMERIC(4, 2),
	PRIMARY KEY (id_comb, numero)
);

CREATE TABLE juez (
	dni        VARCHAR(9),
	nombre     VARCHAR(15) NOT NULL,
	apellido1  VARCHAR(15),
	apellido2  VARCHAR(15),
	PRIMARY KEY (dni)
);

CREATE TABLE valora (
	id_comb  NUMERIC(10) REFERENCES combate,
	juez     VARCHAR(9) REFERENCES juez,
	tipo     VARCHAR(15) NOT NULL,
	PRIMARY KEY (id_comb, juez)
);

CREATE TABLE puntua (
	id_comb   NUMERIC(10),
	numero    NUMERIC(2),
	juez      VARCHAR(9) REFERENCES juez,
	box       VARCHAR(9) REFERENCES boxeador,
	punt      NUMERIC(2) NOT NULL,
	PRIMARY KEY (id_comb, numero, juez, box),
	FOREIGN KEY (id_comb, numero) REFERENCES asalto
);

CREATE TABLE faltas (
	id_comb     NUMERIC(10),
	numero      NUMERIC(2),
	box         VARCHAR(9) REFERENCES boxeador,
	num_faltas  NUMERIC(2) NOT NULL,
	PRIMARY KEY (id_comb, numero, box),
	FOREIGN KEY (id_comb, numero) REFERENCES asalto
);

CREATE TABLE zona (
	loc       VARCHAR(40) REFERENCES recinto,
	numero    NUMERIC(2),
	max_entr  NUMERIC(5) NOT NULL,
	PRIMARY KEY (loc, numero)
);

CREATE TABLE tiene_asociada (
	id_comb    NUMERIC(10) REFERENCES combate,
	loc        VARCHAR(40),
	numero       NUMERIC(2),
	entr_vend  NUMERIC(5) DEFAULT 0 NOT NULL,
	precio     NUMERIC(5, 2) DEFAULT 0.00 NOT NULL,
	PRIMARY KEY (id_comb, loc, numero),
	FOREIGN KEY (loc, numero) REFERENCES zona
);


INSERT INTO BOXEADOR VALUES ('21115568R', 'Pedro', 'Rodríguez', 'González', 'Pesado', 62.5);
INSERT INTO BOXEADOR VALUES ('55566678V', 'Khaled', 'Wheeler', NULL, 'Pesado', 93.0);
INSERT INTO BOXEADOR VALUES ('12399900P', 'Larry', 'Smith', NULL, 'Mosca', 52.8);
INSERT INTO BOXEADOR VALUES ('98766651A', 'James', 'Johnson', NULL, 'Mosca', 53.2);
INSERT INTO BOXEADOR VALUES ('45918233J', 'Paco', 'García', 'García', 'Pesado', 94.0);

INSERT INTO CLUB VALUES (111, 'Los Leones');
INSERT INTO CLUB VALUES (222, 'The Kings');
INSERT INTO CLUB VALUES (333, 'Imparables');

INSERT INTO HIST_CLUB VALUES ('21115568R', to_date('21/2/2020', 'DD/MM/YYYY'), NULL, 111);
INSERT INTO HIST_CLUB VALUES ('55566678V', to_date('15/3/2020', 'DD/MM/YYYY'), NULL, 111);
INSERT INTO HIST_CLUB VALUES ('12399900P', to_date('30/5/2020', 'DD/MM/YYYY'), NULL, 222);
INSERT INTO HIST_CLUB VALUES ('98766651A', to_date('10/2/2020', 'DD/MM/YYYY'), to_date('17/4/2020', 'DD/MM/YYYY'), 333);
INSERT INTO HIST_CLUB VALUES ('98766651A', to_date('10/5/2020', 'DD/MM/YYYY'), NULL, 222);
INSERT INTO HIST_CLUB VALUES ('45918233J', to_date('09/1/2020', 'DD/MM/YYYY'), to_date('22/4/2020', 'DD/MM/YYYY'), 222);
INSERT INTO HIST_CLUB VALUES ('45918233J', to_date('28/4/2020', 'DD/MM/YYYY'), NULL, 333);

INSERT INTO CAMPEONATO VALUES (10, to_date('28/9/2020', 'DD/MM/YYYY'), 'BDB');
INSERT INTO CAMPEONATO VALUES (20, to_date('15/8/2020', 'DD/MM/YYYY'), 'Campeonato Gallego de Boxeo');
INSERT INTO CAMPEONATO VALUES (30, to_date('05/2/2021', 'DD/MM/YYYY'), 'Supercopa de Boxeo');

INSERT INTO INSCRIBE VALUES (111, 10);
INSERT INTO INSCRIBE VALUES (222, 10);
INSERT INTO INSCRIBE VALUES (111, 20);
INSERT INTO INSCRIBE VALUES (222, 20);
INSERT INTO INSCRIBE VALUES (333, 20);

INSERT INTO RECINTO VALUES ('Pabellón 10 Boiro', 'Avd. Inventada, N10');
INSERT INTO RECINTO VALUES ('Pabellón Municipal Vigo', 'Avd. Invent, N15');

INSERT INTO COMBATE VALUES (1, 1, 10, '21115568R', '12399900P', '21115568R', 'Pabellón 10 Boiro');
INSERT INTO COMBATE VALUES (2, 2, 10, '21115568R', '98766651A', NULL, 'Pabellón 10 Boiro');
INSERT INTO COMBATE VALUES (20, 1, 20, '45918233J', '55566678V', NULL, 'Pabellón Municipal Vigo');
INSERT INTO COMBATE VALUES (21, 2, 20, '98766651A', '12399900P', '98766651A', 'Pabellón Municipal Vigo');
INSERT INTO COMBATE VALUES (22, 2, 20, '45918233J', '21115568R', '21115568R', 'Pabellón Municipal Vigo');

INSERT INTO ASALTO VALUES (1, 1, 4.1);
INSERT INTO ASALTO VALUES (1, 2, 3);
INSERT INTO ASALTO VALUES (21, 1, 2.1);
INSERT INTO ASALTO VALUES (21, 2, 1);
INSERT INTO ASALTO VALUES (22, 1, 6);

INSERT INTO JUEZ VALUES ('10101099L', 'Juanjo', 'Álvarez', 'Pérez');
INSERT INTO JUEZ VALUES ('20202099M', 'Cecilia', 'Enteneza', 'Fernández');
INSERT INTO JUEZ VALUES ('30303099B', 'Federico', 'Jiménez', NULL);

INSERT INTO VALORA VALUES (1, '10101099L', 'Titular');
INSERT INTO VALORA VALUES (1, '20202099M', 'Titular');
INSERT INTO VALORA VALUES (1, '30303099B', 'Suplente');
INSERT INTO VALORA VALUES (21, '30303099B', 'Titular');
INSERT INTO VALORA VALUES (21, '10101099L', 'Suplente');
INSERT INTO VALORA VALUES (22, '20202099M', 'Titular');

INSERT INTO PUNTUA VALUES (1, 1, '10101099L', '21115568R', 10);
INSERT INTO PUNTUA VALUES (1, 1, '10101099L', '12399900P', 9);
INSERT INTO PUNTUA VALUES (1, 1, '20202099M', '21115568R', 10);
INSERT INTO PUNTUA VALUES (1, 1, '20202099M', '12399900P', 9);

INSERT INTO PUNTUA VALUES (1, 2, '10101099L', '21115568R', 10);
INSERT INTO PUNTUA VALUES (1, 2, '10101099L', '12399900P', 10);
INSERT INTO PUNTUA VALUES (1, 2, '20202099M', '21115568R', 10);
INSERT INTO PUNTUA VALUES (1, 2, '20202099M', '12399900P', 10);

INSERT INTO PUNTUA VALUES (21, 1, '30303099B', '98766651A', 10);
INSERT INTO PUNTUA VALUES (21, 1, '30303099B', '12399900P', 9);
INSERT INTO PUNTUA VALUES (21, 2, '30303099B', '98766651A', 10);
INSERT INTO PUNTUA VALUES (21, 2, '30303099B', '12399900P', 9);

INSERT INTO PUNTUA VALUES (22, 1, '20202099M', '45918233J', 9);
INSERT INTO PUNTUA VALUES (22, 1, '20202099M', '21115568R', 10);

INSERT INTO FALTAS VALUES (1, 1, '21115568R', 1);
INSERT INTO FALTAS VALUES (1, 1, '12399900P', 0);
INSERT INTO FALTAS VALUES (1, 2, '21115568R', 0);
INSERT INTO FALTAS VALUES (1, 2, '12399900P', 0);

INSERT INTO FALTAS VALUES (21, 1, '98766651A', 0);
INSERT INTO FALTAS VALUES (21, 1, '12399900P', 0);
INSERT INTO FALTAS VALUES (21, 2, '98766651A', 1);
INSERT INTO FALTAS VALUES (21, 2, '12399900P', 1);

INSERT INTO FALTAS VALUES (22, 1, '45918233J', 2);
INSERT INTO FALTAS VALUES (22, 1, '21115568R', 0);

INSERT INTO ZONA VALUES ('Pabellón 10 Boiro', 1, 100);
INSERT INTO ZONA VALUES ('Pabellón 10 Boiro', 2, 150);
INSERT INTO ZONA VALUES ('Pabellón 10 Boiro', 3, 50);
INSERT INTO ZONA VALUES ('Pabellón Municipal Vigo', 1, 200);
INSERT INTO ZONA VALUES ('Pabellón Municipal Vigo', 2, 500);

INSERT INTO TIENE_ASOCIADA VALUES (1, 'Pabellón 10 Boiro', 1, 15, 10.50);
INSERT INTO TIENE_ASOCIADA VALUES (1, 'Pabellón 10 Boiro', 2, 10, 20.00);
INSERT INTO TIENE_ASOCIADA VALUES (1, 'Pabellón 10 Boiro', 3, 5, 6);

INSERT INTO TIENE_ASOCIADA VALUES (21, 'Pabellón Municipal Vigo', 1, 30, 10);
INSERT INTO TIENE_ASOCIADA VALUES (21, 'Pabellón Municipal Vigo', 2, 50, 20);

INSERT INTO TIENE_ASOCIADA VALUES (22, 'Pabellón Municipal Vigo', 1, 100, 15);
INSERT INTO TIENE_ASOCIADA VALUES (22, 'Pabellón Municipal Vigo', 2, 80, 25);

/* CONSULTAS */
/*
a) --------

SELECT id_club, box, nombre
FROM HIST_CLUB h JOIN boxeador ON h.box = boxeador.dni
WHERE tvf IS NULL
GROUP BY id_club, box, nombre

b) --------

SELECT COUNT(*) AS Victorias
FROM combate c
WHERE (c.box1 = '21115568R' OR c.box2 = '21115568R') AND c.ganador = '21115568R'

SELECT COUNT(*) AS Derrotas
FROM combate c
WHERE (c.box1 = '12399900P' OR c.box2 = '12399900P') AND c.ganador != '12399900P'

c) --------

SELECT c.id_camp, c.nombre, i.id_club, h.box
FROM inscribe i JOIN CAMPEONATO c ON i.id_camp = c.id_camp 
				JOIN hist_club h ON i.id_club = h.id_club
WHERE c.id_camp = 20 AND h.tvi < c.fecha AND (h.tvf IS NULL OR h.tvf > c.fecha)

d) --------

SELECT id_camp, id_comb, box1, b1.nombre, b1.apellido1, box2, b2.nombre, b2.apellido1, ganador, loc
FROM COMBATE c JOIN BOXEADOR b1 ON c.box1 = b1.dni
			   JOIN BOXEADOR b2 ON c.box2 = b2.dni
WHERE id_camp = 10

e) --------

SELECT c.id_comb, a.numero AS num_asalto, a.duracion, p.box, p.punt, p.juez
FROM combate c JOIN asalto a ON c.ID_COMB = a.ID_COMB AND (c.box1 = '21115568R' OR c.box1 = '12399900P') AND (c.box2 = '21115568R' OR c.box2 = '12399900P')
			   JOIN puntua p ON p.ID_COMB = c.id_comb AND p.NUMERO = a.NUMERO
			   
SELECT c.id_comb, a.numero AS num_asalto, a.duracion, f.BOX, f.NUM_FALTAS 
FROM combate c JOIN asalto a ON c.ID_COMB = a.ID_COMB AND (c.box1 = '21115568R' OR c.box1 = '12399900P') AND (c.box2 = '21115568R' OR c.box2 = '12399900P')
			   JOIN faltas f ON f.id_comb = c.id_comb AND f.numero = a.numero

f) --------

SELECT c.id_comb, j.NOMBRE, j.APELLIDO1, v.tipo
FROM combate c JOIN valora v ON c.ID_COMB = v.ID_COMB
			   JOIN juez j ON v.JUEZ = j.DNI
WHERE c.id_comb = 1

SELECT DISTINCT p.id_comb, j.nombre, j.APELLIDO1 
FROM puntua p JOIN juez j ON p.JUEZ = j.DNI
WHERE p.id_comb = 1

g) --------

SELECT p.id_comb, p.NUMERO, b.nombre AS BOXEADOR, j.nombre AS juez, p.punt
FROM puntua p JOIN boxeador b ON p.box = b.dni
			  JOIN juez j ON p.juez = j.dni
WHERE p.id_comb = 21

h) --------

SELECT c.id_comb, c.loc, z.NUMERO AS zona, z.MAX_ENTR, ta.ENTR_VEND, ta.PRECIO
FROM combate c JOIN TIENE_ASOCIADA ta ON c.ID_COMB = ta.ID_COMB
			   JOIN zona z ON z.LOC = ta.LOC AND z.numero = ta.ZONA 
WHERE c.id_comb = 1
*/
