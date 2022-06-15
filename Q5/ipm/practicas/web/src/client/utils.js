const endpoint = "http://ipm-go.hermo.me"

const closeInfoBox = () => {
	const infoBox = document.getElementById("info-box");

	infoBox.setAttribute("aria-hidden", "true");
	infoBox.removeAttribute("aria-errormessage");
	infoBox.removeAttribute("role", "alert");
	infoBox.removeAttribute("aria-live");

	infoBox.classList.add("hidden");
}

const showError = (msg) => {
	const infoBox = document.getElementById("info-box");

	infoBox.setAttribute("role", "alert");
	infoBox.setAttribute("aria-hidden", "false");
	infoBox.setAttribute("aria-errormessage", "true");
	
	infoBox.classList.remove("popupSuccess");
	infoBox.classList.add("popupError");
	infoBox.classList.remove("hidden");
	document.querySelector("#info-box span").innerText = msg;
}

const showSuccess = (msg) => {
	const infoBox = document.getElementById("info-box");

	infoBox.setAttribute("role", "log");
	infoBox.setAttribute("aria-live", "polite");
	infoBox.setAttribute("aria-hidden", "false");

	infoBox.classList.remove("popupError");
	infoBox.classList.add("popupSuccess");
	infoBox.classList.remove("hidden");
	document.querySelector("#info-box span").innerText = msg;
}

const logged = async () => {
	const username = sessionStorage.getItem("username");
	const password = sessionStorage.getItem("password");
	if ( username && password ) {
		try {
			const response = await fetch(`${endpoint}/login`,{
				method: "POST",
				body: JSON.stringify({
					username,
					password
				})
			})
			if ( !response.ok ) return

			const data = await response.json()
			if ( data["users"].length > 0 )
				window.location.href="../info/info.html"
			
		} catch(e) {
			console.error("Can't auto-login")
		}
	}
}

const login = async(username,password) =>{
	try{
		const response = await fetch(`${endpoint}/login`,{
			method:"POST",
			body:JSON.stringify({username,password})
		})

		if ( !response.ok ) {
			showError("Server error");
			return;
		}
		
		const data = await response.json()
		if ( data["users"].length > 0 ) {
			sessionStorage.setItem("username", username)
			sessionStorage.setItem("password", password)
			document.getElementById("cover").classList.add("show-cover")
			setTimeout(() =>window.location.href="../info/info.html",500)	
		} else {
			showError("Error, the user or password is incorrect");
		}

	} catch(e) {
		showError("Server error");
	}
}

export {login, logged, showError, showSuccess, closeInfoBox}
