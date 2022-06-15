# Práctica Programación Lineal
David Rodríguez Bacelar (david.rbacelar@udc.es)  
Lucas Campos Camiña (lucas.campos@udc.es)  
Diego Vázquez Fernández (diego.vazquez.fernandez@udc.es)

## **Problema 1**

* Planteamiento:

  ```
  - Variables:
    • Tomarán el valor 1 si el candidato forma parte de la comisión
    • 0 en caso contrario

  - Objetivo:
  Minimizar     a + b + c + d + e + f + g + h + i + j

  - Restricciones:
  S.A.          a + b + c + d + e >= 1 (al menos una mujer)
                f + g + h + i + j >= 1 (al menos un hombre)
                    a + b + c + j >= 1 (al menos un estudiante)
                            e + f >= 1 (al menos un administrativo)
                    d + g + h + i >= 1 (al menos un profesor)
                    d + g + h + i >= e + f (mayor o igual número de profesores que administrativos)
                a + b + c + d + e = f + g + h + i + j (mismo número de hombres y mujeres)
  ```

&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;   

* Codificación:

  ```python
  from pyomo.environ import *
  from pyomo.opt import SolverFactory
  
  model= ConcreteModel()
  
  model.a=Var(domain=Binary)
  model.b=Var(domain=Binary)
  model.c=Var(domain=Binary)
  model.d=Var(domain=Binary)
  model.e=Var(domain=Binary)
  model.f=Var(domain=Binary)
  model.g=Var(domain=Binary)
  model.h=Var(domain=Binary)
  model.i=Var(domain=Binary)
  model.j=Var(domain=Binary)
  
  model.profit=Objective(expr=model.a+model.b+model.c+model.d+model.e+model.f+model.g+model.h+model.i+model.j, sense=minimize)
  
  model.r1=Constraint(expr=model.a+model.b+model.c+model.d+model.e >= 1)
  model.r2=Constraint(expr=model.f+model.g+model.h+model.i+model.j >= 1)
  model.r3=Constraint(expr=model.a+model.b+model.c+model.j >= 1)
  model.r4=Constraint(expr=model.e+model.f >= 1)
  model.r5=Constraint(expr=model.d+model.g+model.h+model.i >= 1)
  model.r6=Constraint(expr=model.a+model.b+model.c+model.d+model.e == model.f+model.g+model.h+model.i+model.j)
  model.r7=Constraint(expr=model.d+model.g+model.h+model.i >= model.e+model.f)
  
  results=SolverFactory('glpk').solve(model)
  results.write()
  
  if results.solver.status=='ok':
    print('Objetivo=', model.profit)
    for v in model.component_data_objects(Var):
      print(str(v), v.value)
  ```
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  

* Resultados:

  ```
  Objetivo = 4.0
  a 1.0
  b 0.0
  c 0.0
  d 0.0
  e 1.0
  f 0.0
  g 0.0
  h 0.0
  i 1.0
  j 1.0
  ```

* Interpretación:

  ```
  La solución que nos ofrece el solver es óptima (el mínimo número de personas de la comisión tiene que ser 4, dadas las restricciones), pero no es la única, ya que por ejemplo los candidatos C,E,I,J también cumplirían las restricciones.
  ```

&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  

## **Problema 2**

* Planteamiento:

  ```
  - Variables (t_xy):
    • Tomarán el valor 1 si la tarea *x* la realiza el trabajador *y*
    • 0 en caso contrario

  - Objetivo:
  Minimizar     65 * t_11 + 67 * t_12 + 68 * t_13 + 67 * t_14 + 71 * t_15 + 69 * t_16 +
                73 * t_21 + 70 * t_22 + 72 * t_23 + 75 * t_24 + 69 * t_25 + 71 * t_26 +
                63 * t_31 + 65 * t_32 + 69 * t_33 + 70 * t_34 + 75 * t_35 + 66 * t_36 +
                57 * t_41 + 58 * t_42 + 55 * t_43 + 59 * t_44 + 57 * t_45 + 59 * t_46

  - Restricciones:
  S.A.          
                (Cada tarea solo la realiza un trabajador)
                t_11 + t_12 + t_13 + t_14 + t_15 + t_16 = 1
                t_21 + t_22 + t_23 + t_24 + t_25 + t_26 = 1
                t_31 + t_32 + t_33 + t_34 + t_35 + t_36 = 1 
                t_41 + t_42 + t_43 + t_44 + t_45 + t_46 = 1

                (Cada trabajdor solo realiza una tarea)
                t_11 + t_21 + t_31 + t_41 <= 1
                t_12 + t_22 + t_32 + t_42 <= 1
                t_13 + t_23 + t_33 + t_43 <= 1
                t_14 + t_24 + t_34 + t_44 <= 1
                t_15 + t_25 + t_35 + t_45 <= 1
                t_16 + t_26 + t_36 + t_46 <= 1
  ```

&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  
&nbsp;  

* Codificación:

  ```r
  f.obj <- c(65,67,68,67,71,69,73,70,72,75,69,71,63,65,69,70,75,66,57,58,55,59,57,59);

  f.const <- rbind(c(1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
                  c(0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0),
                  c(0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0),
                  c(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1),
                  c(1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0),
                  c(0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0),
                  c(0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0),
                  c(0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0),
                  c(0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0),
                  c(0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1));

  f.cond <- c("=", "=", "=", "=", "<=", "<=", "<=", "<=", "<=", "<=");

  f.rhs <- c(1,1,1,1,1,1,1,1,1,1);

  # Solve
  solution <- lp("min",f.obj, f.const, f.cond, f.rhs)

  solution$objval

  solution$solution
  ```

* Resultados:

  ```r
  > solution$objval
  [1] 254
  > solution$solution
  [1] 1 0 0 0 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 1 0 0 0 
  # (Tarea 1 - Trabajador 1, Tarea 2 - Trabajador 5, Tarea 3 - Trabajador 2, Tarea 4 - Trabajador 3)
  ```

* Interpretación:

  ```
  El mínimo número de horas es 254 con la plantilla de trabajadores anterior.
  ```
