# Accesibilidad

## Título

* Se modificó el título de la página, añadiendo una pequeña descripción:
	```html
	<head>
		<title>Covid Radar for covid-related tracing</title>
	</head>
	```

## Texto alternativo

* Implementado en la única imagen que utilizamos:
	```html
	<img id="qrcode" src="../assets/no-pictures.png" alt="user qrcode"/>
	```

## Errores descriptivos

* A pesar de utilizar el rojo como color de error y el verde como éxito, tanto los formularios como los accesos a la BBDD proporcionan códigos de error descriptivos:
	```html
	<input title="Password's length must be greater than 8 and must only contains a letter and a number">
	<input title="Name must only contains letters">
	```

	```javascript
	const data = await response.json()
	if ( data["users"].length > 0 ) {
		...
	} else {
		showError("Error, the user or password is incorrect");
	}
	```

## Tags HTML

* Se intentó evitar utilizar tags html genéricos en todo momento, empleando en cambio tags autodescriptivos:   
	* En el contenedor principal de cada página:
		```html
		<div id="root">
			...
		</div>

		<!-- Replaced by -->

		<main>
			...
		</main>
		```
	* En la lisa de datos del usuario:
		```html
		<div class="info-container">
			<div class="info-element">
				<span class="info-element-type">Email:&nbsp;</span>
				<span id="email"></span>
			</div>
		</div>

		<!-- Replaced by -->

		<ul class="info-container" >
			<li class="info-element">
				<label>Email:&nbsp;</label>
				<span id="email"></span>
			</li>
		</ul>
		```

## Roles

* Se añadieron roles en los siguientes elementos:
	* Como la pantalla de carga que cubre la página es meramente decorativa, el lector de pantalla debería ignorarla:
		```html
		<div id="cover" class="show-cover" role="presentation">
			...
		</div>
		```
	* Al elemento separador de la página principal también se le añadió el rol **separator**:
		```html
		<div class="separator" role="separator">
			...
		</div>
		```
	* A los botones que realmente redirigen a otra página también se les añadió el rol **link**:
		```html
		<button class="action-btn" id="login" role="link">Login</button>
		<button class="action-btn" id="register" role="link">Register</button>
		```

## Atributos de widgets y de relación

* Se añadió por defecto el atributo **aria-hidden="true"** a los popups de la página y, cada vez que se muestran, se modifican varios atributos
utilizando js:
	```javascript
	// Si el popup es de error
	infoBox.setAttribute("role", "alert");
	infoBox.setAttribute("aria-errormessage", "true");
	infoBox.setAttribute("aria-hidden", "false");

	// Si el popup es informativo
	infoBox.setAttribute("role", "log");
	infoBox.setAttribute("aria-live", "polite");
	infoBox.setAttribute("aria-hidden", "false");
	```

* También añadimos el atributo **aria-busy** en la página de informacion mientras se accede a la BBDD:
	```javascript
	document.getElementById("access-list").setAttribute("aria-busy", "true")

	// Se obtienen los datos
	await fetch(`${endpoint}/login`,{
		method: "POST",
		body: JSON.stringify({
			username,
			password
		})
	})
	...

	// Tras actualizar la lista
	document.getElementById("access-list").setAttribute("aria-busy", "false")
	```
